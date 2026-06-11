package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Persona;
import com.destino.app.model.PeticionOracion;
import com.destino.app.service.PersonaService;
import com.destino.app.service.PeticionOracionService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.OffsetDateTime;

public class OracionController {

    @FXML private TableView<PeticionOracion> tablaPeticiones;
    @FXML private TableColumn<PeticionOracion, String> colSolicitante;
    @FXML private TableColumn<PeticionOracion, String> colDescripcion;
    @FXML private TableColumn<PeticionOracion, String> colEstado;
    @FXML private TableColumn<PeticionOracion, OffsetDateTime> colFecha;

    @FXML private ComboBox<Persona> comboSolicitante;
    @FXML private TextArea campoDescripcion;
    @FXML private CheckBox checkPrivada;
    @FXML private Label etiquetaMensaje;

    private final PeticionOracionService service = new PeticionOracionService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<PeticionOracion> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colSolicitante.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        // Mostrar solo la fecha (sin la hora ni el offset).
        colFecha.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(OffsetDateTime t, boolean empty) {
                super.updateItem(t, empty);
                setText((empty || t == null) ? null : t.toLocalDate().toString());
            }
        });

        comboSolicitante.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });
        comboSolicitante.setItems(FXCollections.observableArrayList(personaService.listar()));

        tablaPeticiones.setItems(datos);
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onRegistrar() {
        try {
            Persona p = comboSolicitante.getValue();
            PeticionOracion pet = new PeticionOracion();
            pet.setSolicitanteId(p != null ? p.getId() : null);
            pet.setDescripcion(campoDescripcion.getText());
            pet.setPrivada(checkPrivada.isSelected());

            service.registrar(pet);
            mensaje("Petición registrada.", Color.GREEN);
            limpiar();
            cargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al registrar.", Color.RED);
        }
    }

    @FXML
    private void onResponder() {
        PeticionOracion sel = tablaPeticiones.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una petición.", Color.RED); return; }
        service.marcarRespondida(sel.getId());
        mensaje("Marcada como respondida.", Color.GREEN);
        cargar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaPeticiones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        comboSolicitante.setValue(null);
        campoDescripcion.clear();
        checkPrivada.setSelected(false);
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}