package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Donacion;
import com.destino.app.model.Persona;
import com.destino.app.service.DonacionService;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class AgapeController {

    @FXML private TableView<Donacion> tablaDonaciones;
    @FXML private TableColumn<Donacion, String> colDonante;
    @FXML private TableColumn<Donacion, String> colTipo;
    @FXML private TableColumn<Donacion, BigDecimal> colMonto;
    @FXML private TableColumn<Donacion, String> colDescripcion;
    @FXML private TableColumn<Donacion, OffsetDateTime> colFecha;

    @FXML private ComboBox<Persona> comboDonante;
    @FXML private TextField campoNombreDonante;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoMonto;
    @FXML private TextArea campoDescripcion;
    @FXML private Label etiquetaMensaje;

    private final DonacionService service = new DonacionService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<Donacion> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDonante.setCellValueFactory(new PropertyValueFactory<>("donanteMostrar"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(OffsetDateTime t, boolean empty) {
                super.updateItem(t, empty);
                setText((empty || t == null) ? null : t.toLocalDate().toString());
            }
        });

        comboDonante.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) {
                return (p == null) ? "" : p.getNombres() + " " + p.getApellidos();
            }
            @Override public Persona fromString(String s) { return null; }
        });
        comboDonante.setItems(FXCollections.observableArrayList(personaService.listar()));
        comboTipo.getItems().addAll("FISICA", "DIGITAL");

        tablaDonaciones.setItems(datos);
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onRegistrar() {
        try {
            Donacion d = new Donacion();
            Persona donante = comboDonante.getValue();
            if (donante != null) {
                d.setDonanteId(donante.getId());        // persona registrada
            } else {
                d.setNombreDonante(vacioANull(campoNombreDonante.getText()));  // externo
            }
            d.setTipo(comboTipo.getValue());
            d.setMonto(parsearMonto(campoMonto.getText()));
            d.setDescripcion(vacioANull(campoDescripcion.getText()));
            if (Sesion.getUsuario() != null) {
                d.setRegistradoPor(Sesion.getUsuario().getPersonaId());
            }

            service.registrar(d);
            mensaje("Donación registrada.", Color.GREEN);
            limpiar();
            cargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al registrar.", Color.RED);
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaDonaciones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private BigDecimal parsearMonto(String texto) {
        if (texto == null || texto.isBlank()) return null;   // físicas pueden no tener monto
        try {
            return new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Monto inválido. Escribe solo números.");
        }
    }

    private void limpiar() {
        comboDonante.setValue(null);
        campoNombreDonante.clear();
        comboTipo.setValue(null);
        campoMonto.clear();
        campoDescripcion.clear();
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}