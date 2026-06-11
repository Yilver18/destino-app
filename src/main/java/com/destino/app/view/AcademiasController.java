package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Academia;
import com.destino.app.model.Persona;
import com.destino.app.service.AcademiaService;
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

import java.math.BigDecimal;

public class AcademiasController {

    @FXML private TableView<Academia> tablaAcademias;
    @FXML private TableColumn<Academia, String> colNombre;
    @FXML private TableColumn<Academia, BigDecimal> colPrecio;
    @FXML private TableColumn<Academia, Boolean> colActiva;

    @FXML private TextField campoNombre;
    @FXML private TextArea campoDescripcion;
    @FXML private TextField campoPrecio;
    @FXML private ComboBox<Persona> comboCoordinador;
    @FXML private CheckBox checkActiva;
    @FXML private Label etiquetaMensaje;

    private final AcademiaService service = new AcademiaService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Academia> datos = FXCollections.observableArrayList();
    private final ObservableList<Persona> personas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioInscripcion"));
        colActiva.setCellValueFactory(new PropertyValueFactory<>("activa"));

        comboCoordinador.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });
        personas.setAll(personaService.listar());
        comboCoordinador.setItems(personas);

        tablaAcademias.setItems(datos);
        tablaAcademias.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrar(sel));
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    private void mostrar(Academia a) {
        if (a == null) return;
        campoNombre.setText(a.getNombre());
        campoDescripcion.setText(a.getDescripcion());
        campoPrecio.setText(a.getPrecioInscripcion() != null ? a.getPrecioInscripcion().toPlainString() : "");
        checkActiva.setSelected(a.isActiva());
        Persona coord = personas.stream()
                .filter(p -> p.getId().equals(a.getCoordinadorId()))
                .findFirst().orElse(null);
        comboCoordinador.setValue(coord);
    }

    @FXML
    private void onNuevo() {
        tablaAcademias.getSelectionModel().clearSelection();
        limpiar();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Academia sel = tablaAcademias.getSelectionModel().getSelectedItem();
            Academia a = (sel != null) ? sel : new Academia();

            a.setNombre(campoNombre.getText().trim());
            a.setDescripcion(vacioANull(campoDescripcion.getText()));
            a.setPrecioInscripcion(parsearPrecio(campoPrecio.getText()));
            Persona coord = comboCoordinador.getValue();
            a.setCoordinadorId(coord != null ? coord.getId() : null);
            a.setActiva(checkActiva.isSelected());

            service.guardar(a);
            mensaje("Academia guardada.", Color.GREEN);
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
        Academia sel = tablaAcademias.getSelectionModel().getSelectedItem();
        if (sel == null) { mensaje("Selecciona una academia.", Color.RED); return; }
        service.inactivar(sel.getId());
        mensaje("Academia inactivada.", Color.GREEN);
        cargar();
        limpiar();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaAcademias.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    // "50000" -> BigDecimal; vacío -> 0; inválido -> error claro.
    private BigDecimal parsearPrecio(String texto) {
        if (texto == null || texto.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Precio inválido. Escribe solo números (ej. 50000).");
        }
    }

    private void limpiar() {
        campoNombre.clear();
        campoDescripcion.clear();
        campoPrecio.clear();
        comboCoordinador.setValue(null);
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