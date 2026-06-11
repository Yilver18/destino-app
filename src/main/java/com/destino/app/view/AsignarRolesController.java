package com.destino.app.view;

import com.destino.app.model.Persona;
import com.destino.app.model.Rol;
import com.destino.app.service.AsignacionRolService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AsignarRolesController {

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private ListView<Rol> listaDisponibles;
    @FXML private ListView<Rol> listaAsignados;
    @FXML private Label etiquetaMensaje;

    private final PersonaService personaService = new PersonaService();
    private final AsignacionRolService service = new AsignacionRolService();

    @FXML
    public void initialize() {
        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });
        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));

        // Las dos listas muestran el nombre del rol (celda personalizada).
        listaDisponibles.setCellFactory(lv -> celdaRol());
        listaAsignados.setCellFactory(lv -> celdaRol());

        // Al elegir persona, recargar sus listas.
        comboPersona.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> recargar());
    }

    private ListCell<Rol> celdaRol() {
        return new ListCell<>() {
            @Override protected void updateItem(Rol r, boolean empty) {
                super.updateItem(r, empty);
                setText((empty || r == null) ? null : r.getNombre());
            }
        };
    }

    private void recargar() {
        Persona p = comboPersona.getValue();
        if (p == null) return;
        listaAsignados.setItems(FXCollections.observableArrayList(service.rolesAsignados(p.getId())));
        listaDisponibles.setItems(FXCollections.observableArrayList(service.rolesDisponibles(p.getId())));
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onAsignar() {
        Persona p = comboPersona.getValue();
        Rol r = listaDisponibles.getSelectionModel().getSelectedItem();
        if (p == null || r == null) { mensaje("Elige una persona y un rol disponible.", Color.RED); return; }
        service.asignar(p.getId(), r.getId());
        recargar();
        mensaje("Rol asignado.", Color.GREEN);
    }

    @FXML
    private void onQuitar() {
        Persona p = comboPersona.getValue();
        Rol r = listaAsignados.getSelectionModel().getSelectedItem();
        if (p == null || r == null) { mensaje("Elige un rol asignado para quitar.", Color.RED); return; }
        service.quitar(p.getId(), r.getId());
        recargar();
        mensaje("Rol quitado.", Color.GREEN);
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) comboPersona.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}