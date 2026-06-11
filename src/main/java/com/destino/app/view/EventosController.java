package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Evento;
import com.destino.app.service.EventoService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EventosController {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colTitulo;
    @FXML private TableColumn<Evento, LocalDate> colFecha;
    @FXML private TableColumn<Evento, String> colLugar;
    @FXML private TableColumn<Evento, Boolean> colPublicado;

    @FXML private TextField campoTitulo;
    @FXML private TextArea campoDescripcion;
    @FXML private DatePicker fecha;
    @FXML private TextField campoHora;
    @FXML private TextField campoLugar;
    @FXML private TextField campoImagenUrl;
    @FXML private CheckBox checkPublicado;
    @FXML private Label etiquetaMensaje;

    private final EventoService service = new EventoService();
    private final ObservableList<Evento> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        colPublicado.setCellValueFactory(new PropertyValueFactory<>("publicado"));

        tablaEventos.setItems(datos);
        tablaEventos.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Evento e) {
        if (e == null) return;
        campoTitulo.setText(e.getTitulo());
        campoDescripcion.setText(e.getDescripcion());
        fecha.setValue(e.getFecha());
        campoHora.setText(e.getHora() != null ? e.getHora().toString() : "");
        campoLugar.setText(e.getLugar());
        campoImagenUrl.setText(e.getImagenUrl());
        checkPublicado.setSelected(e.isPublicado());
    }

    @FXML
    private void onNuevo() {
        tablaEventos.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
            Evento e = (sel != null) ? sel : new Evento();

            e.setTitulo(campoTitulo.getText().trim());
            e.setDescripcion(vacioANull(campoDescripcion.getText()));
            e.setFecha(fecha.getValue());
            e.setHora(parsearHora(campoHora.getText()));
            e.setLugar(vacioANull(campoLugar.getText()));
            e.setImagenUrl(vacioANull(campoImagenUrl.getText()));
            e.setPublicado(checkPublicado.isSelected());
            if (e.getId() == null && Sesion.getUsuario() != null) {
                e.setCreadoPor(Sesion.getUsuario().getPersonaId());
            }

            service.guardar(e);
            mensaje("Evento guardado.", Color.GREEN);
            cargar();
            limpiar();
        } catch (ValidacionException ex) {
            mensaje(ex.getMessage(), Color.RED);
        } catch (DestinoException ex) {
            mensaje("Error al guardar.", Color.RED);
        }
    }

    @FXML
    private void onEliminar() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona un evento.", Color.RED); return; }
        service.eliminar(sel.getId());
        mensaje("Evento eliminado.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaEventos.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private LocalTime parsearHora(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return LocalTime.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new ValidacionException("Hora inválida. Usa el formato HH:mm (ej. 19:00).");
        }
    }

    private void limpiar() {
        campoTitulo.clear();
        campoDescripcion.clear();
        fecha.setValue(null);
        campoHora.clear();
        campoLugar.clear();
        campoImagenUrl.clear();
        checkPublicado.setSelected(false);
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}