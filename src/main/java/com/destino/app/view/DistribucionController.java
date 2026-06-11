package com.destino.app.view;

import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.DistribucionMercado;
import com.destino.app.model.Persona;
import com.destino.app.service.DistribucionMercadoService;
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

import java.time.LocalDate;

public class DistribucionController {

    @FXML private TableView<DistribucionMercado> tablaDistribuciones;
    @FXML private TableColumn<DistribucionMercado, String> colBeneficiario;
    @FXML private TableColumn<DistribucionMercado, LocalDate> colFecha;
    @FXML private TableColumn<DistribucionMercado, Integer> colCantidad;
    @FXML private TableColumn<DistribucionMercado, String> colDescripcion;

    @FXML private ComboBox<Persona> comboBeneficiario;
    @FXML private TextField campoNombreBeneficiario;
    @FXML private DatePicker fecha;
    @FXML private TextField campoCantidad;
    @FXML private TextArea campoDescripcion;
    @FXML private Label etiquetaMensaje;

    private final DistribucionMercadoService service = new DistribucionMercadoService();
    private final PersonaService personaService = new PersonaService();
    private final ObservableList<DistribucionMercado> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colBeneficiario.setCellValueFactory(new PropertyValueFactory<>("beneficiarioMostrar"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        comboBeneficiario.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });
        comboBeneficiario.setItems(FXCollections.observableArrayList(personaService.listar()));

        tablaDistribuciones.setItems(datos);
        cargar();
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onRegistrar() {
        try {
            DistribucionMercado d = new DistribucionMercado();
            Persona ben = comboBeneficiario.getValue();
            if (ben != null) {
                d.setBeneficiarioId(ben.getId());
            } else {
                d.setNombreBeneficiario(vacioANull(campoNombreBeneficiario.getText()));
            }
            d.setFecha(fecha.getValue());
            d.setCantidad(parsearCantidad(campoCantidad.getText()));
            d.setDescripcion(vacioANull(campoDescripcion.getText()));
            if (Sesion.getUsuario() != null) {
                d.setResponsableId(Sesion.getUsuario().getPersonaId());
            }

            service.registrar(d);
            mensaje("Entrega registrada.", Color.GREEN);
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
        Stage stage = (Stage) tablaDistribuciones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private int parsearCantidad(String texto) {
        if (texto == null || texto.isBlank()) return 1;   // por defecto 1
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Cantidad inválida. Escribe un número entero.");
        }
    }

    private void limpiar() {
        comboBeneficiario.setValue(null);
        campoNombreBeneficiario.clear();
        fecha.setValue(null);
        campoCantidad.clear();
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