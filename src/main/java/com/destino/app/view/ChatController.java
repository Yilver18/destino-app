package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.MensajeChat;
import com.destino.app.model.Persona;
import com.destino.app.service.ChatService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ChatController {

    // Item de la lista: muestra la etiqueta, guarda el id de conversación.
    private record ConvItem(Long id, String etiqueta) {
        @Override public String toString() { return etiqueta; }
    }

    @FXML private ListView<ConvItem> listaConversaciones;
    @FXML private ComboBox<Persona> comboPersona;
    @FXML private TextArea areaMensajes;
    @FXML private TextField campoMensaje;

    private final ChatService service = new ChatService();
    private final PersonaService personaService = new PersonaService();
    private Long yo;

    @FXML
    public void initialize() {
        yo = (Sesion.getUsuario() != null) ? Sesion.getUsuario().getPersonaId() : null;

        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });
        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));

        listaConversaciones.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarMensajes(sel));

        cargarConversaciones();
    }

    private void cargarConversaciones() {
        listaConversaciones.getItems().clear();
        for (String[] r : service.conversacionesDe(yo)) {
            listaConversaciones.getItems().add(new ConvItem(Long.parseLong(r[0]), r[1]));
        }
    }

    private void cargarMensajes(ConvItem conv) {
        areaMensajes.clear();
        if (conv == null) return;
        for (MensajeChat m : service.mensajes(conv.id())) {
            areaMensajes.appendText(m.getNombreRemitente() + ": " + m.getContenido() + "\n");
        }
    }

    @FXML
    private void onIniciar() {
        try {
            Persona p = comboPersona.getValue();
            Long convId = service.iniciarConversacion(yo, p != null ? p.getId() : null);
            cargarConversaciones();
            // seleccionar la conversación recién abierta
            for (ConvItem c : listaConversaciones.getItems()) {
                if (c.id().equals(convId)) { listaConversaciones.getSelectionModel().select(c); break; }
            }
        } catch (ValidacionException e) {
            areaMensajes.setText(e.getMessage());
        } catch (DestinoException e) {
            areaMensajes.setText("Error al iniciar la conversación.");
        }
    }

    @FXML
    private void onEnviar() {
        ConvItem conv = listaConversaciones.getSelectionModel().getSelectedItem();
        if (conv == null) { areaMensajes.setText("Elige o inicia una conversación primero."); return; }
        String texto = campoMensaje.getText();
        if (texto == null || texto.isBlank()) return;
        service.enviar(conv.id(), yo, texto);
        campoMensaje.clear();
        cargarMensajes(conv);
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) areaMensajes.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }
}