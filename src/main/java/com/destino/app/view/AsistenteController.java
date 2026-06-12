package com.destino.app.view;

import com.destino.app.util.GroqApiClient;
import com.destino.app.util.GroqApiClient.Mensaje;
import com.destino.app.util.Navegador;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AsistenteController {

    @FXML private TextArea areaChat;
    @FXML private TextField campoMensaje;
    @FXML private Button btnEnviar;

    private static final String SISTEMA =
            "Eres un asistente bíblico para líderes, ministros y servidores del Centro Cristiano " +
                    "Destino (una iglesia cristiana en Valledupar, Colombia). Ayudas con búsqueda de " +
                    "versículos, reflexiones y preparación de estudios bíblicos. Responde en español, de " +
                    "forma clara, pastoral y respetuosa. Si citas versículos, indica la referencia.";

    private final List<Mensaje> historial = new ArrayList<>();

    @FXML
    public void initialize() {
        areaChat.setText("Asistente: ¡Hola! Soy tu asistente bíblico. ¿En qué te puedo ayudar hoy?\n\n");
    }

    @FXML
    private void onEnviar() {
        String texto = campoMensaje.getText();
        if (texto == null || texto.isBlank()) return;

        areaChat.appendText("Tú: " + texto.trim() + "\n");
        historial.add(new Mensaje("user", texto.trim()));
        campoMensaje.clear();
        bloquear(true);

        Task<String> tarea = new Task<>() {
            @Override protected String call() {
                return GroqApiClient.conversar(SISTEMA, historial);
            }
        };
        tarea.setOnSucceeded(e -> {
            String respuesta = tarea.getValue();
            historial.add(new Mensaje("assistant", respuesta));
            areaChat.appendText("Asistente: " + respuesta + "\n\n");
            bloquear(false);
        });
        tarea.setOnFailed(e -> {
            Throwable ex = tarea.getException();
            areaChat.appendText("[Error: " + (ex != null ? ex.getMessage() : "desconocido") + "]\n\n");
            bloquear(false);
        });
        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void bloquear(boolean b) {
        btnEnviar.setDisable(b);
        campoMensaje.setDisable(b);
        btnEnviar.setText(b ? "Pensando..." : "Enviar");
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) areaChat.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }
}