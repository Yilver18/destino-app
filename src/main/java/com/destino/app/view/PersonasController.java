package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Persona;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PersonasController {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombres;
    @FXML private TableColumn<Persona, String> colApellidos;
    @FXML private TableColumn<Persona, String> colTipoDoc;
    @FXML private TableColumn<Persona, String> colDocumento;
    @FXML private TableColumn<Persona, String> colTelefono;
    @FXML private TableColumn<Persona, String> colEstado;

    @FXML private TextField campoNombres;
    @FXML private TextField campoApellidos;
    @FXML private ComboBox<String> comboTipoDoc;
    @FXML private TextField campoDocumento;
    @FXML private TextField campoTelefono;
    @FXML private TextField campoEmail;
    @FXML private ComboBox<String> comboSexo;
    @FXML private DatePicker fechaNacimiento;
    @FXML private CheckBox checkConsentimiento;
    @FXML private Label etiquetaMensaje;

    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Persona> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // PropertyValueFactory usa los getters de Persona (getNombres, etc.).
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTipoDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        comboTipoDoc.getItems().addAll("CC", "TI", "CE", "PAS");
        comboSexo.getItems().addAll("M", "F");

        tablaPersonas.setItems(datos);
        tablaPersonas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccion) -> mostrarEnFormulario(seleccion));

        cargarPersonas();
    }

    private void cargarPersonas() {
        datos.setAll(personaService.listar());
    }

    private void mostrarEnFormulario(Persona p) {
        if (p == null) return;
        campoNombres.setText(p.getNombres());
        campoApellidos.setText(p.getApellidos());
        comboTipoDoc.setValue(p.getTipoDocumento());
        campoDocumento.setText(p.getNumeroDocumento());
        campoTelefono.setText(p.getTelefono());
        campoEmail.setText(p.getEmail());
        comboSexo.setValue(p.getSexo());
        fechaNacimiento.setValue(p.getFechaNacimiento());
        checkConsentimiento.setSelected(p.isConsentimientoDatos());
    }

    @FXML
    private void onNuevo() {
        tablaPersonas.getSelectionModel().clearSelection();
        limpiarFormulario();
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        try {
            Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
            Persona p = (seleccionada != null) ? seleccionada : new Persona();

            p.setNombres(campoNombres.getText().trim());
            p.setApellidos(campoApellidos.getText().trim());
            p.setTipoDocumento(comboTipoDoc.getValue());
            p.setNumeroDocumento(campoDocumento.getText().trim());
            p.setTelefono(vacioANull(campoTelefono.getText()));
            p.setEmail(vacioANull(campoEmail.getText()));
            p.setSexo(comboSexo.getValue());
            p.setFechaNacimiento(fechaNacimiento.getValue());
            p.setConsentimientoDatos(checkConsentimiento.isSelected());

            if (p.getId() == null) {
                personaService.registrar(p);
                mensaje("Persona registrada.", Color.GREEN);
            } else {
                personaService.actualizar(p);
                mensaje("Persona actualizada.", Color.GREEN);
            }
            cargarPersonas();
            limpiarFormulario();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al guardar. Intenta de nuevo.", Color.RED);
        }
    }

    @FXML
    private void onInactivar() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensaje("Selecciona una persona primero.", Color.RED);
            return;
        }
        try {
            personaService.inactivar(seleccionada.getId());
            mensaje("Persona inactivada.", Color.GREEN);
            cargarPersonas();
            limpiarFormulario();
        } catch (DestinoException e) {
            mensaje("Error al inactivar.", Color.RED);
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaPersonas.getScene().getWindow();
        Navegador.cambiarEscena(stage,
                "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiarFormulario() {
        campoNombres.clear();
        campoApellidos.clear();
        comboTipoDoc.setValue(null);
        campoDocumento.clear();
        campoTelefono.clear();
        campoEmail.clear();
        comboSexo.setValue(null);
        fechaNacimiento.setValue(null);
        checkConsentimiento.setSelected(false);
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}