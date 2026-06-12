package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.PaginaContenido;
import com.destino.app.service.PaginaContenidoService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PaginasController {

    @FXML private TableView<PaginaContenido> tablaPaginas;
    @FXML private TableColumn<PaginaContenido, String> colClave;
    @FXML private TableColumn<PaginaContenido, String> colTitulo;

    @FXML private TextField campoClave;
    @FXML private TextField campoTitulo;
    @FXML private TextArea campoCuerpo;
    @FXML private TextField campoImagenUrl;
    @FXML private Label etiquetaMensaje;

    private final PaginaContenidoService service = new PaginaContenidoService();
    private final ObservableList<PaginaContenido> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        tablaPaginas.setItems(datos);
        tablaPaginas.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(PaginaContenido p) {
        if (p == null) return;
        campoClave.setText(p.getClave());
        campoTitulo.setText(p.getTitulo());
        campoCuerpo.setText(p.getCuerpo());
        campoImagenUrl.setText(p.getImagenUrl());
    }

    @FXML
    private void onNuevo() {
        tablaPaginas.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            PaginaContenido p = new PaginaContenido();
            p.setClave(campoClave.getText() == null ? null : campoClave.getText().trim());
            p.setTitulo(vacioANull(campoTitulo.getText()));
            p.setCuerpo(campoCuerpo.getText());
            p.setImagenUrl(vacioANull(campoImagenUrl.getText()));
            if (Sesion.getUsuario() != null) {
                p.setActualizadoPor(Sesion.getUsuario().getPersonaId());
            }

            service.guardar(p);
            mensaje("Contenido guardado.", Color.GREEN);
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
        PaginaContenido sel = tablaPaginas.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una página.", Color.RED); return; }
        service.eliminar(sel.getId());
        mensaje("Página eliminada.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaPaginas.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        campoClave.clear();
        campoTitulo.clear();
        campoCuerpo.clear();
        campoImagenUrl.clear();
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}