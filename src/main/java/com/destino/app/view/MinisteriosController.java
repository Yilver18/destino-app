package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Ministerio;
import com.destino.app.model.Persona;
import com.destino.app.service.MinisterioService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class MinisteriosController {

    @FXML private TableView<Ministerio> tablaMinisterios;
    @FXML private TableColumn<Ministerio, String> colNombre;
    @FXML private TableColumn<Ministerio, String> colDescripcion;
    @FXML private TableColumn<Ministerio, Boolean> colActivo;

    @FXML private TextField campoNombre;
    @FXML private TextField campoDescripcion;
    @FXML private ComboBox<Persona> comboCoordinador;
    @FXML private CheckBox checkActivo;
    @FXML private Label etiquetaMensaje;

    private final MinisterioService service = new MinisterioService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Ministerio> datos = FXCollections.observableArrayList();
    private final ObservableList<Persona> personas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // El combo muestra "Nombres Apellidos" pero guarda el objeto Persona.
        comboCoordinador.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });

        personas.setAll(personaService.listar());
        comboCoordinador.setItems(personas);

        tablaMinisterios.setItems(datos);
        tablaMinisterios.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Ministerio m) {
        if (m == null) return;
        campoNombre.setText(m.getNombre());
        campoDescripcion.setText(m.getDescripcion());
        checkActivo.setSelected(m.isActivo());
        // Buscar la persona coordinadora por id entre las cargadas.
        Persona coord = personas.stream()
                .filter(p -> p.getId().equals(m.getCoordinadorId()))
                .findFirst().orElse(null);
        comboCoordinador.setValue(coord);
    }

    @FXML
    private void onNuevo() {
        tablaMinisterios.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Ministerio sel = tablaMinisterios.getSelectionModel().getSelectedItem();
            Ministerio m = (sel != null) ? sel : new Ministerio();

            m.setNombre(campoNombre.getText().trim());
            m.setDescripcion(vacioANull(campoDescripcion.getText()));
            Persona coord = comboCoordinador.getValue();
            m.setCoordinadorId(coord != null ? coord.getId() : null);
            m.setActivo(checkActivo.isSelected());

            service.guardar(m);
            mensaje("Ministerio guardado.", Color.GREEN);
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
        Ministerio sel = tablaMinisterios.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona un ministerio.", Color.RED); return; }
        service.inactivar(sel.getId());
        mensaje("Ministerio inactivado.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaMinisterios.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboCoordinador.setValue(null);
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