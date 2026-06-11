package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.ReunionMeet;
import com.destino.app.service.ReunionMeetService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReunionesController {

    @FXML private TableView<ReunionMeet> tablaReuniones;
    @FXML private TableColumn<ReunionMeet, String> colNombre;
    @FXML private TableColumn<ReunionMeet, String> colDia;
    @FXML private TableColumn<ReunionMeet, LocalTime> colHora;
    @FXML private TableColumn<ReunionMeet, Boolean> colActiva;

    @FXML private TextField campoNombre;
    @FXML private TextField campoUrl;
    @FXML private ComboBox<String> comboDia;
    @FXML private TextField campoHora;
    @FXML private TextArea campoDescripcion;
    @FXML private CheckBox checkActiva;
    @FXML private Label etiquetaMensaje;

    private final ReunionMeetService service = new ReunionMeetService();
    private final ObservableList<ReunionMeet> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colActiva.setCellValueFactory(new PropertyValueFactory<>("activa"));

        comboDia.getItems().addAll("LUNES","MARTES","MIERCOLES","JUEVES","VIERNES","SABADO","DOMINGO");

        tablaReuniones.setItems(datos);
        tablaReuniones.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(ReunionMeet r) {
        if (r == null) return;
        campoNombre.setText(r.getNombre());
        campoUrl.setText(r.getUrl());
        comboDia.setValue(r.getDia());
        campoHora.setText(r.getHora() != null ? r.getHora().toString() : "");
        campoDescripcion.setText(r.getDescripcion());
        checkActiva.setSelected(r.isActiva());
    }

    @FXML
    private void onNuevo() {
        tablaReuniones.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            ReunionMeet sel = tablaReuniones.getSelectionModel().getSelectedItem();
            ReunionMeet r = (sel != null) ? sel : new ReunionMeet();

            r.setNombre(campoNombre.getText().trim());
            r.setUrl(campoUrl.getText().trim());
            r.setDia(comboDia.getValue());
            r.setHora(parsearHora(campoHora.getText()));
            r.setDescripcion(vacioANull(campoDescripcion.getText()));
            r.setActiva(checkActiva.isSelected());

            service.guardar(r);
            mensaje("Reunión guardada.", Color.GREEN);
            cargar();
            limpiar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al guardar.", Color.RED);
        }
    }

    @FXML
    private void onInactivar() {
        ReunionMeet sel = tablaReuniones.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una reunión.", Color.RED); return; }
        service.inactivar(sel.getId());
        mensaje("Reunión inactivada.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaReuniones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private LocalTime parsearHora(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return LocalTime.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new ValidacionException("Hora inválida. Usa el formato HH:mm.");
        }
    }

    private void limpiar() {
        campoNombre.clear();
        campoUrl.clear();
        comboDia.setValue(null);
        campoHora.clear();
        campoDescripcion.clear();
        checkActiva.setSelected(true);
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}