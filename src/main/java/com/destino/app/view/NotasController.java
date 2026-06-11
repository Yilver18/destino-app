package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.NotaSeguimiento;
import com.destino.app.model.Persona;
import com.destino.app.service.NotaSeguimientoService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.OffsetDateTime;

public class NotasController {

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private TableView<NotaSeguimiento> tablaNotas;
    @FXML private TableColumn<NotaSeguimiento, OffsetDateTime> colFecha;
    @FXML private TableColumn<NotaSeguimiento, String> colTipo;
    @FXML private TableColumn<NotaSeguimiento, String> colDescripcion;
    @FXML private TableColumn<NotaSeguimiento, String> colAutor;

    @FXML private ComboBox<String> comboTipo;
    @FXML private TextArea campoDescripcion;
    @FXML private Label etiquetaMensaje;

    private final NotaSeguimientoService service = new NotaSeguimientoService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<NotaSeguimiento> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(OffsetDateTime t, boolean empty) {
                super.updateItem(t, empty);
                setText((empty || t == null) ? null : t.toLocalDate().toString());
            }
        });
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("nombreAutor"));

        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });
        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));
        comboPersona.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> recargar());

        comboTipo.getItems().addAll("LLAMADA", "VISITA", "SITUACION", "OTRO");

        tablaNotas.setItems(datos);
    }

    private void recargar() {
        Persona p = comboPersona.getValue();
        if (p == null) return;
        datos.setAll(service.listarPorPersona(p.getId()));
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onRegistrar() {
        try {
            Persona p = comboPersona.getValue();
            if (p == null) { mensaje("Elige primero una persona.", Color.RED); return; }

            NotaSeguimiento n = new NotaSeguimiento();
            n.setPersonaId(p.getId());
            n.setAutorId(Sesion.getUsuario() != null ? Sesion.getUsuario().getPersonaId() : null);
            n.setTipo(comboTipo.getValue());
            n.setDescripcion(campoDescripcion.getText());

            service.registrar(n);
            mensaje("Nota registrada.", Color.GREEN);
            comboTipo.setValue(null);
            campoDescripcion.clear();
            recargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al registrar.", Color.RED);
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaNotas.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}