package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.CasaDestino;
import com.destino.app.service.CasaDestinoService;
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

public class CasasController {

    @FXML private TableView<CasaDestino> tablaCasas;
    @FXML private TableColumn<CasaDestino, String> colNombre;
    @FXML private TableColumn<CasaDestino, String> colSector;
    @FXML private TableColumn<CasaDestino, String> colDia;
    @FXML private TableColumn<CasaDestino, LocalTime> colHora;
    @FXML private TableColumn<CasaDestino, Boolean> colActiva;

    @FXML private TextField campoNombre;
    @FXML private TextField campoDireccion;
    @FXML private TextField campoSector;
    @FXML private ComboBox<String> comboDia;
    @FXML private TextField campoHora;
    @FXML private CheckBox checkActiva;
    @FXML private Label etiquetaMensaje;

    private final CasaDestinoService service = new CasaDestinoService();
    private final ObservableList<CasaDestino> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        colDia.setCellValueFactory(new PropertyValueFactory<>("diaReunion"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaReunion"));
        colActiva.setCellValueFactory(new PropertyValueFactory<>("activa"));

        comboDia.getItems().addAll("LUNES","MARTES","MIERCOLES","JUEVES","VIERNES","SABADO","DOMINGO");

        tablaCasas.setItems(datos);
        tablaCasas.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(CasaDestino c) {
        if (c == null) return;
        campoNombre.setText(c.getNombre());
        campoDireccion.setText(c.getDireccion());
        campoSector.setText(c.getSector());
        comboDia.setValue(c.getDiaReunion());
        campoHora.setText(c.getHoraReunion() != null ? c.getHoraReunion().toString() : "");
        checkActiva.setSelected(c.isActiva());
    }

    @FXML
    private void onNuevo() {
        tablaCasas.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            CasaDestino sel = tablaCasas.getSelectionModel().getSelectedItem();
            CasaDestino c = (sel != null) ? sel : new CasaDestino();

            c.setNombre(campoNombre.getText().trim());
            c.setDireccion(vacioANull(campoDireccion.getText()));
            c.setSector(vacioANull(campoSector.getText()));
            c.setDiaReunion(comboDia.getValue());
            c.setHoraReunion(parsearHora(campoHora.getText()));
            c.setActiva(checkActiva.isSelected());

            service.guardar(c);
            mensaje("Casa guardada.", Color.GREEN);
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
        CasaDestino sel = tablaCasas.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una casa.", Color.RED); return; }
        service.inactivar(sel.getId());
        mensaje("Casa inactivada.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaCasas.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    // Convierte "19:00" en LocalTime; vacío = null; formato inválido = error claro.
    private LocalTime parsearHora(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return LocalTime.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new ValidacionException("Hora inválida. Usa el formato HH:mm (ej. 19:00).");
        }
    }

    private void limpiar() {
        campoNombre.clear();
        campoDireccion.clear();
        campoSector.clear();
        comboDia.setValue(null);
        campoHora.clear();
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