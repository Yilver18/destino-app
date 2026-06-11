package com.destino.app.view;

import com.destino.app.dao.ServicioDao;
import com.destino.app.dao.ServicioDaoImpl;
import com.destino.app.exceptions.DestinoException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.SesionAsistencia;
import com.destino.app.service.AcademiaService;
import com.destino.app.service.CasaDestinoService;
import com.destino.app.service.SesionAsistenciaService;
import com.destino.app.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SesionesController {

    // Pequeño holder para el combo de referencia: muestra el nombre, guarda el id.
    private record ItemRef(Long id, String nombre) {
        @Override public String toString() { return nombre; }
    }

    @FXML private TableView<SesionAsistencia> tablaSesiones;
    @FXML private TableColumn<SesionAsistencia, LocalDate> colFecha;
    @FXML private TableColumn<SesionAsistencia, String> colContexto;
    @FXML private TableColumn<SesionAsistencia, String> colReferencia;
    @FXML private TableColumn<SesionAsistencia, String> colDescripcion;

    @FXML private DatePicker fecha;
    @FXML private ComboBox<String> comboContexto;
    @FXML private ComboBox<ItemRef> comboReferencia;
    @FXML private TextField campoDescripcion;
    @FXML private Label etiquetaMensaje;

    private final SesionAsistenciaService service = new SesionAsistenciaService();
    private final ServicioDao servicioDao = new ServicioDaoImpl();
    private final CasaDestinoService casaService = new CasaDestinoService();
    private final AcademiaService academiaService = new AcademiaService();
    private final ObservableList<SesionAsistencia> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colContexto.setCellValueFactory(new PropertyValueFactory<>("contexto"));
        colReferencia.setCellValueFactory(new PropertyValueFactory<>("referenciaMostrar"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        comboContexto.getItems().addAll("SERVICIO", "CASA", "ACADEMIA");
        // Al cambiar el contexto, cargar la lista correspondiente en el segundo combo.
        comboContexto.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, ctx) -> cargarReferencias(ctx));

        tablaSesiones.setItems(datos);
        cargar();
    }

    private void cargarReferencias(String contexto) {
        comboReferencia.getItems().clear();
        if (contexto == null) return;
        switch (contexto) {
            case "SERVICIO" -> servicioDao.listarActivos()
                    .forEach(s -> comboReferencia.getItems().add(new ItemRef(s.getId(), s.getNombre())));
            case "CASA" -> casaService.listar()
                    .forEach(c -> comboReferencia.getItems().add(new ItemRef(c.getId(), c.getNombre())));
            case "ACADEMIA" -> academiaService.listar()
                    .forEach(a -> comboReferencia.getItems().add(new ItemRef(a.getId(), a.getNombre())));
        }
    }

    private void cargar() {
        datos.setAll(service.listar());
    }

    @FXML
    private void onCrear() {
        try {
            SesionAsistencia s = new SesionAsistencia();
            s.setFecha(fecha.getValue());
            String ctx = comboContexto.getValue();
            s.setContexto(ctx);
            ItemRef ref = comboReferencia.getValue();
            Long refId = (ref != null) ? ref.id() : null;
            if ("SERVICIO".equals(ctx)) s.setServicioId(refId);
            else if ("CASA".equals(ctx)) s.setCasaId(refId);
            else if ("ACADEMIA".equals(ctx)) s.setAcademiaId(refId);
            s.setDescripcion(campoDescripcion.getText() == null || campoDescripcion.getText().isBlank()
                    ? null : campoDescripcion.getText().trim());

            service.crear(s);
            mensaje("Sesión creada.", Color.GREEN);
            limpiar();
            cargar();
        } catch (ValidacionException e) {
            mensaje(e.getMessage(), Color.RED);
        } catch (DestinoException e) {
            mensaje("Error al crear la sesión.", Color.RED);
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) tablaSesiones.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void limpiar() {
        fecha.setValue(null);
        comboContexto.setValue(null);
        comboReferencia.getItems().clear();
        campoDescripcion.clear();
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}