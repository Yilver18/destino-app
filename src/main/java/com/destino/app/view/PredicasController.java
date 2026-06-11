package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Persona;
import com.destino.app.model.Predica;
import com.destino.app.service.PersonaService;
import com.destino.app.service.PredicaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class PredicasController {

    @FXML private TableView<Predica> tablaPredicas;
    @FXML private TableColumn<Predica, Boolean> colActual;
    @FXML private TableColumn<Predica, String> colTitulo;
    @FXML private TableColumn<Predica, String> colCreativo;
    @FXML private TableColumn<Predica, LocalDate> colFecha;

    @FXML private TextField campoTitulo;
    @FXML private TextField campoYoutube;
    @FXML private ComboBox<Persona> comboCreativo;
    @FXML private DatePicker fechaPublicacion;
    @FXML private Label etiquetaMensaje;

    private final PredicaService service = new PredicaService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Predica> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colActual.setCellValueFactory(new PropertyValueFactory<>("esActual"));
        // Muestra ★ en la actual, vacío en las demás.
        colActual.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Boolean actual, boolean empty) {
                super.updateItem(actual, empty);
                setText((empty || actual == null || !actual) ? "" : "★");
            }
        });
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCreativo.setCellValueFactory(new PropertyValueFactory<>("nombreCreativo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));

        comboCreativo.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });
        comboCreativo.setItems(FXCollections.observableArrayList(personaService.listar()));

        tablaPredicas.setItems(datos);
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onRegistrar() {
        try {
            Predica p = new Predica();
            p.setTitulo(campoTitulo.getText().trim());
            p.setYoutubeUrl(campoYoutube.getText().trim());
            Persona creativo = comboCreativo.getValue();
            p.setCreativoId(creativo != null ? creativo.getId() : null);
            p.setFechaPublicacion(fechaPublicacion.getValue());

            service.registrar(p);
            mensaje("Prédica registrada.", Color.GREEN);
            limpiar();
            cargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al registrar.", Color.RED);
        }
    }

    @FXML
    private void onMarcarActual() {
        Predica sel = tablaPredicas.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una prédica.", Color.RED); return; }
        service.marcarActual(sel.getId());
        mensaje("Marcada como actual. La anterior pasó al historial.", Color.GREEN);
        cargar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaPredicas.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        campoTitulo.clear();
        campoYoutube.clear();
        comboCreativo.setValue(null);
        fechaPublicacion.setValue(null);
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}