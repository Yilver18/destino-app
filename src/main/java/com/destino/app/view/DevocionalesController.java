package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Devocional;
import com.destino.app.service.DevocionalService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class DevocionalesController {

    @FXML private TableView<Devocional> tablaDevocionales;
    @FXML private TableColumn<Devocional, String> colNombre;
    @FXML private TableColumn<Devocional, Integer> colMes;
    @FXML private TableColumn<Devocional, Integer> colAnio;
    @FXML private TableColumn<Devocional, BigDecimal> colPrecio;
    @FXML private TableColumn<Devocional, Boolean> colDestacado;

    @FXML private TextField campoNombre;
    @FXML private TextArea campoDescripcion;
    @FXML private ComboBox<Integer> comboMes;
    @FXML private TextField campoAnio;
    @FXML private TextField campoPrecio;
    @FXML private TextField campoImagenUrl;
    @FXML private CheckBox checkDestacado;
    @FXML private CheckBox checkActivo;
    @FXML private Label etiquetaMensaje;

    private final DevocionalService service = new DevocionalService();
    private final ObservableList<Devocional> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDestacado.setCellValueFactory(new PropertyValueFactory<>("destacado"));

        for (int m = 1; m <= 12; m++) comboMes.getItems().add(m);

        tablaDevocionales.setItems(datos);
        tablaDevocionales.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Devocional d) {
        if (d == null) return;
        campoNombre.setText(d.getNombre());
        campoDescripcion.setText(d.getDescripcion());
        comboMes.setValue(d.getMes());
        campoAnio.setText(d.getAnio() != null ? d.getAnio().toString() : "");
        campoPrecio.setText(d.getPrecio() != null ? d.getPrecio().toPlainString() : "");
        campoImagenUrl.setText(d.getImagenUrl());
        checkDestacado.setSelected(d.isDestacado());
        checkActivo.setSelected(d.isActivo());
    }

    @FXML
    private void onNuevo() {
        tablaDevocionales.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Devocional sel = tablaDevocionales.getSelectionModel().getSelectedItem();
            Devocional d = (sel != null) ? sel : new Devocional();

            d.setNombre(campoNombre.getText().trim());
            d.setDescripcion(vacioANull(campoDescripcion.getText()));
            d.setMes(comboMes.getValue());
            d.setAnio(parsearEntero(campoAnio.getText(), "año"));
            d.setPrecio(parsearPrecio(campoPrecio.getText()));
            d.setImagenUrl(vacioANull(campoImagenUrl.getText()));
            d.setDestacado(checkDestacado.isSelected());
            d.setActivo(checkActivo.isSelected());

            service.guardar(d);
            mensaje("Devocional guardado.", Color.GREEN);
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
        Devocional sel = tablaDevocionales.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona un devocional.", Color.RED); return; }
        service.inactivar(sel.getId());
        mensaje("Devocional inactivado.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaDevocionales.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private Integer parsearEntero(String texto, String campo) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("El " + campo + " debe ser un número.");
        }
    }

    private BigDecimal parsearPrecio(String texto) {
        if (texto == null || texto.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Precio inválido. Escribe solo números.");
        }
    }

    private void limpiar() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboMes.setValue(null);
        campoAnio.clear();
        campoPrecio.clear();
        campoImagenUrl.clear();
        checkDestacado.setSelected(false);
        checkActivo.setSelected(true);
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}