package com.destino.app.view;

import com.destino.app.service.ReporteIaService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AgapeReporteController {

    @FXML private TextArea areaReporte;
    @FXML private Button btnGenerar;
    @FXML private Label etiquetaMensaje;

    private final ReporteIaService service = new ReporteIaService();

    @FXML
    private void onGenerar() {
        btnGenerar.setDisable(true);
        btnGenerar.setText("Generando...");
        mensaje("La IA está analizando...", Color.BLACK);

        Long generadoPor = (Sesion.getUsuario() != null) ? Sesion.getUsuario().getPersonaId() : null;
        Task<String> tarea = new Task<>() {
            @Override protected String call() {
                return service.analizarAgape(generadoPor);
            }
        };
        tarea.setOnSucceeded(e -> {
            areaReporte.setText(tarea.getValue());
            mensaje("Reporte generado y guardado.", Color.GREEN);
            restaurar();
        });
        tarea.setOnFailed(e -> {
            Throwable ex = tarea.getException();
            mensaje("Error IA: " + (ex != null ? ex.getMessage() : "desconocido"), Color.RED);
            restaurar();
        });
        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void restaurar() {
        btnGenerar.setDisable(false);
        btnGenerar.setText("Generar con IA");
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) areaReporte.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}