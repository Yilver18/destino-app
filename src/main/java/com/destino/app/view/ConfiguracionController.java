package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Configuracion;
import com.destino.app.service.ConfiguracionService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConfiguracionController {

    @FXML private TableView<Configuracion> tablaConfig;
    @FXML private TableColumn<Configuracion, String> colClave;
    @FXML private TableColumn<Configuracion, String> colValor;
    @FXML private TableColumn<Configuracion, String> colDescripcion;

    @FXML private TextField campoClave;
    @FXML private TextField campoValor;
    @FXML private TextField campoDescripcion;
    @FXML private Label etiquetaMensaje;

    private final ConfiguracionService service = new ConfiguracionService();
    private final ObservableList<Configuracion> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tablaConfig.setItems(datos);
        tablaConfig.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Configuracion c) {
        if (c == null) return;
        campoClave.setText(c.getClave());
        campoValor.setText(c.getValor());
        campoDescripcion.setText(c.getDescripcion());
    }

    @FXML
    private void onNuevo() {
        tablaConfig.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Configuracion c = new Configuracion();
            c.setClave(campoClave.getText() == null ? null : campoClave.getText().trim());
            c.setValor(campoValor.getText());
            c.setDescripcion(vacioANull(campoDescripcion.getText()));

            service.guardar(c);
            mensaje("Configuración guardada.", Color.GREEN);
            cargar();
            limpiar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al guardar.", Color.RED);
        }
    }

    @FXML
    private void onEliminar() {
        Configuracion sel = tablaConfig.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una clave.", Color.RED); return; }
        service.eliminar(sel.getClave());
        mensaje("Configuración eliminada.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaConfig.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        campoClave.clear();
        campoValor.clear();
        campoDescripcion.clear();
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}