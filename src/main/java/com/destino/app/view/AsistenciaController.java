package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Asistencia;
import com.destino.app.model.Persona;
import com.destino.app.model.SesionAsistencia;
import com.destino.app.service.AsistenciaService;
import com.destino.app.service.PersonaService;
import com.destino.app.service.SesionAsistenciaService;
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
import java.time.format.DateTimeFormatter;

public class AsistenciaController {

    @FXML private ComboBox<SesionAsistencia> comboSesion;
    @FXML private Label etiquetaConteo;
    @FXML private TableView<Asistencia> tablaAsistencia;
    @FXML private TableColumn<Asistencia, String> colPersona;
    @FXML private TableColumn<Asistencia, String> colMetodo;
    @FXML private TableColumn<Asistencia, OffsetDateTime> colHora;

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private Label etiquetaMensaje;

    private final AsistenciaService service = new AsistenciaService();
    private final SesionAsistenciaService sesionService = new SesionAsistenciaService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Asistencia> datos = FXCollections.observableArrayList();

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    @FXML
    public void initialize() {
        colPersona.setCellValueFactory(new PropertyValueFactory<>("nombrePersona"));
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodo"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaRegistro"));
        colHora.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(OffsetDateTime t, boolean empty) {
                super.updateItem(t, empty);
                setText((empty || t == null) ? null : HORA.format(t));
            }
        });

        comboSesion.setConverter(new StringConverter<>() {
            @Override public String toString(SesionAsistencia s) {
                return (s == null) ? "" : s.getFecha() + "  —  " + s.getReferenciaMostrar()
                                          + " (" + s.getContexto() + ")";
            }
            @Override public SesionAsistencia fromString(String s) { return null; }
        });
        comboSesion.setItems(FXCollections.observableArrayList(sesionService.listar()));
        comboSesion.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> recargar());

        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });
        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));

        tablaAsistencia.setItems(datos);
    }

    private void recargar() {
        SesionAsistencia s = comboSesion.getValue();
        if (s == null) return;
        datos.setAll(service.listarPorSesion(s.getId()));
        etiquetaConteo.setText("Asistentes: " + datos.size());
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onRegistrar() {
        try {
            SesionAsistencia s = comboSesion.getValue();
            Persona p = comboPersona.getValue();
            if (s == null) { mensaje("Elige una sesión.", Color.RED); return; }
            service.registrarManual(s.getId(), p != null ? p.getId() : null);
            mensaje("Asistencia registrada.", Color.GREEN);
            comboPersona.setValue(null);
            recargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al registrar.", Color.RED);
        }
    }

    @FXML
    private void onQuitar() {
        Asistencia sel = tablaAsistencia.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona un registro.", Color.RED); return; }
        service.eliminar(sel.getId());
        recargar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaAsistencia.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}