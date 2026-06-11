package com.destino.app.view;

import com.destino.app.model.CasaDestino;
import com.destino.app.model.Persona;
import com.destino.app.service.CasaAsignacionService;
import com.destino.app.service.CasaDestinoService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class CasaIntegrantesController {

    @FXML private ComboBox<CasaDestino> comboCasa;
    @FXML private ListView<Persona> listaLideres;
    @FXML private ListView<Persona> listaMiembros;
    @FXML private ComboBox<Persona> comboNuevoLider;
    @FXML private ComboBox<Persona> comboNuevoMiembro;
    @FXML private Label etiquetaMensaje;

    private final CasaDestinoService casaService = new CasaDestinoService();
    private final PersonaService personaService = new PersonaService();
    private final CasaAsignacionService service = new CasaAsignacionService();

    @FXML
    public void initialize() {
        comboCasa.setConverter(new StringConverter<>() {
            @Override public String toString(CasaDestino c) { return c == null ? "" : c.getNombre(); }
            @Override public CasaDestino fromString(String s) { return null; }
        });
        comboCasa.setItems(FXCollections.observableArrayList(casaService.listar()));

        // Las personas para los combos de agregar.
        var personas = FXCollections.observableArrayList(personaService.listar());
        comboNuevoLider.setItems(personas);
        comboNuevoMiembro.setItems(personas);
        comboNuevoLider.setConverter(convPersona());
        comboNuevoMiembro.setConverter(convPersona());

        listaLideres.setCellFactory(lv -> celdaPersona());
        listaMiembros.setCellFactory(lv -> celdaPersona());

        comboCasa.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> recargar());
    }

    private StringConverter<Persona> convPersona() {
        return new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        };
    }

    private ListCell<Persona> celdaPersona() {
        return new ListCell<>() {
            @Override protected void updateItem(Persona p, boolean empty) {
                super.updateItem(p, empty);
                setText((empty || p == null) ? null : p.getNombres() + " " + p.getApellidos());
            }
        };
    }

    private void recargar() {
        CasaDestino c = comboCasa.getValue();
        if (c == null) return;
        listaLideres.setItems(FXCollections.observableArrayList(service.lideres(c.getId())));
        listaMiembros.setItems(FXCollections.observableArrayList(service.miembros(c.getId())));
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onAgregarLider() {
        CasaDestino c = comboCasa.getValue();
        Persona p = comboNuevoLider.getValue();
        if (c == null || p == null) { mensaje("Elige una casa y una persona.", Color.RED); return; }
        service.agregarLider(c.getId(), p.getId());
        recargar();
        mensaje("Líder agregado.", Color.GREEN);
    }

    @FXML
    private void onQuitarLider() {
        CasaDestino c = comboCasa.getValue();
        Persona p = listaLideres.getSelectionModel().getSelectedItem();
        if (c == null || p == null) { mensaje("Selecciona un líder de la lista.", Color.RED); return; }
        service.quitarLider(c.getId(), p.getId());
        recargar();
        mensaje("Líder quitado.", Color.GREEN);
    }

    @FXML
    private void onAgregarMiembro() {
        CasaDestino c = comboCasa.getValue();
        Persona p = comboNuevoMiembro.getValue();
        if (c == null || p == null) { mensaje("Elige una casa y una persona.", Color.RED); return; }
        service.agregarMiembro(c.getId(), p.getId());
        recargar();
        mensaje("Miembro agregado.", Color.GREEN);
    }

    @FXML
    private void onQuitarMiembro() {
        CasaDestino c = comboCasa.getValue();
        Persona p = listaMiembros.getSelectionModel().getSelectedItem();
        if (c == null || p == null) { mensaje("Selecciona un miembro de la lista.", Color.RED); return; }
        service.quitarMiembro(c.getId(), p.getId());
        recargar();
        mensaje("Miembro quitado.", Color.GREEN);
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) comboCasa.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}