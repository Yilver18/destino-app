package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Pastor;
import com.destino.app.service.PastorService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PastoresController {

    @FXML private TableView<Pastor> tablaPastores;
    @FXML private TableColumn<Pastor, Integer> colOrden;
    @FXML private TableColumn<Pastor, String> colNombre;
    @FXML private TableColumn<Pastor, String> colCargo;

    @FXML private TextField campoNombre;
    @FXML private TextField campoCargo;
    @FXML private TextField campoFotoUrl;
    @FXML private TextArea campoBiografia;
    @FXML private TextField campoOrden;
    @FXML private Label etiquetaMensaje;

    private final PastorService service = new PastorService();
    private final ObservableList<Pastor> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        tablaPastores.setItems(datos);
        tablaPastores.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Pastor p) {
        if (p == null) return;
        campoNombre.setText(p.getNombre());
        campoCargo.setText(p.getCargo());
        campoFotoUrl.setText(p.getFotoUrl());
        campoBiografia.setText(p.getBiografia());
        campoOrden.setText(String.valueOf(p.getOrden()));
    }

    @FXML
    private void onNuevo() {
        tablaPastores.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Pastor sel = tablaPastores.getSelectionModel().getSelectedItem();
            Pastor p = (sel != null) ? sel : new Pastor();

            p.setNombre(campoNombre.getText().trim());
            p.setCargo(vacioANull(campoCargo.getText()));
            p.setFotoUrl(vacioANull(campoFotoUrl.getText()));
            p.setBiografia(vacioANull(campoBiografia.getText()));
            p.setOrden(parsearOrden(campoOrden.getText()));

            service.guardar(p);
            mensaje("Pastor guardado.", Color.GREEN);
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
        Pastor sel = tablaPastores.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona un pastor.", Color.RED); return; }
        service.eliminar(sel.getId());
        mensaje("Pastor eliminado.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaPastores.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private int parsearOrden(String texto) {
        if (texto == null || texto.isBlank()) return 0;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("El orden debe ser un número entero.");
        }
    }

    private void limpiar() {
        campoNombre.clear();
        campoCargo.clear();
        campoFotoUrl.clear();
        campoBiografia.clear();
        campoOrden.clear();
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}