package com.destino.app.view;

import com.destino.app.dao.ServicioDao;
import com.destino.app.dao.ServicioDaoImpl;
import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.AsignacionVoluntario;
import com.destino.app.model.Ministerio;
import com.destino.app.model.Persona;
import com.destino.app.model.Servicio;
import com.destino.app.service.MinisterioService;
import com.destino.app.service.PersonaService;
import com.destino.app.service.VoluntarioService;
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

public class VoluntariosController {

    @FXML private TableView<AsignacionVoluntario> tablaAsignaciones;
    @FXML private TableColumn<AsignacionVoluntario, String> colPersona;
    @FXML private TableColumn<AsignacionVoluntario, String> colMinisterio;
    @FXML private TableColumn<AsignacionVoluntario, String> colServicio;
    @FXML private TableColumn<AsignacionVoluntario, LocalDate> colFecha;
    @FXML private TableColumn<AsignacionVoluntario, String> colEstado;

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private ComboBox<Ministerio> comboMinisterio;
    @FXML private ComboBox<Servicio> comboServicio;
    @FXML private DatePicker fechaServicio;
    @FXML private Label etiquetaMensaje;

    private final VoluntarioService service = new VoluntarioService();
    private final PersonaService personaService = new PersonaService();
    private final MinisterioService ministerioService = new MinisterioService();
    private final ServicioDao servicioDao = new ServicioDaoImpl();
    private final ObservableList<AsignacionVoluntario> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colPersona.setCellValueFactory(new PropertyValueFactory<>("nombrePersona"));
        colMinisterio.setCellValueFactory(new PropertyValueFactory<>("nombreMinisterio"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("nombreServicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaServicio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));
        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });

        comboMinisterio.setItems(FXCollections.observableArrayList(ministerioService.listar()));
        comboMinisterio.setConverter(new StringConverter<>() {
            @Override public String toString(Ministerio m) { return m == null ? "" : m.getNombre(); }
            @Override public Ministerio fromString(String s) { return null; }
        });

        comboServicio.setItems(FXCollections.observableArrayList(servicioDao.listarActivos()));
        comboServicio.setConverter(new StringConverter<>() {
            @Override public String toString(Servicio s) { return s == null ? "" : s.getNombre(); }
            @Override public Servicio fromString(String s) { return null; }
        });

        tablaAsignaciones.setItems(datos);
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onAsignar() {
        try {
            AsignacionVoluntario a = new AsignacionVoluntario();
            Persona p = comboPersona.getValue();
            Ministerio m = comboMinisterio.getValue();
            Servicio s = comboServicio.getValue();
            a.setPersonaId(p != null ? p.getId() : null);
            a.setMinisterioId(m != null ? m.getId() : null);
            a.setServicioId(s != null ? s.getId() : null);
            a.setFechaServicio(fechaServicio.getValue());

            service.asignar(a);
            mensaje("Voluntario asignado.", Color.GREEN);
            cargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al asignar.", Color.RED);
        }
    }

    @FXML private void onConfirmar() { conEstado(true); }
    @FXML private void onCancelar()  { conEstado(false); }

    private void conEstado(boolean confirmar) {
        AsignacionVoluntario sel = tablaAsignaciones.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una asignación.", Color.RED); return; }
        if (confirmar) service.confirmar(sel.getId());
        else service.cancelar(sel.getId());
        mensaje(confirmar ? "Confirmada." : "Cancelada.", Color.GREEN);
        cargar();
    }

    @FXML
    private void onEliminar() {
        AsignacionVoluntario sel = tablaAsignaciones.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una asignación.", Color.RED); return; }
        service.eliminar(sel.getId());
        mensaje("Asignación eliminada.", Color.GREEN);
        cargar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaAsignaciones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}