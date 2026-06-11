package com.destino.app.view;

import com.destino.app.model.Persona;
import com.destino.app.model.SesionAsistencia;
import com.destino.app.service.AsistenciaService;
import com.destino.app.service.PersonaService;
import com.destino.app.service.SesionAsistenciaService;
import com.destino.app.util.Navegador;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class EscaneoController {

    @FXML private ComboBox<SesionAsistencia> comboSesion;
    @FXML private ImageView imagenCamara;
    @FXML private Button btnIniciar;
    @FXML private Button btnDetener;
    @FXML private TextField campoCodigo;
    @FXML private Label etiquetaMensaje;

    private final SesionAsistenciaService sesionService = new SesionAsistenciaService();
    private final PersonaService personaService = new PersonaService();
    private final AsistenciaService asistenciaService = new AsistenciaService();

    private Webcam webcam;
    private Thread hilo;
    private volatile boolean corriendo = false;

    @FXML
    public void initialize() {
        comboSesion.setConverter(new StringConverter<>() {
            @Override public String toString(SesionAsistencia s) {
                return (s == null) ? "" : s.getFecha() + "  —  " + s.getReferenciaMostrar();
            }
            @Override public SesionAsistencia fromString(String s) { return null; }
        });
        comboSesion.setItems(FXCollections.observableArrayList(sesionService.listar()));
    }

    @FXML
    private void onIniciar() {
        try {
            webcam = Webcam.getDefault();
            if (webcam == null) {
                mensaje("No se detectó ninguna cámara. Usa el campo de abajo.", Color.RED);
                return;
            }
            webcam.setViewSize(WebcamResolution.QVGA.getSize());
            webcam.open();
            corriendo = true;
            btnIniciar.setDisable(true);
            btnDetener.setDisable(false);
            hilo = new Thread(this::bucleCaptura);
            hilo.setDaemon(true);
            hilo.start();
            mensaje("Cámara activa. Acerca un QR.", Color.BLACK);
        } catch (Throwable t) {
            // webcam-capture puede fallar en JDK nuevos: avisamos y seguimos con el campo.
            mensaje("No se pudo abrir la cámara en este equipo. Usa el campo de abajo o un lector USB.", Color.RED);
            corriendo = false;
        }
    }

    private void bucleCaptura() {
        MultiFormatReader reader = new MultiFormatReader();
        while (corriendo && webcam != null && webcam.isOpen()) {
            BufferedImage img = webcam.getImage();
            if (img == null) continue;
            javafx.scene.image.Image fx = SwingFXUtils.toFXImage(img, null);
            Platform.runLater(() -> imagenCamara.setImage(fx));
            try {
                LuminanceSource source = new BufferedImageLuminanceSource(img);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result = reader.decode(bitmap);
                String texto = result.getText();
                Platform.runLater(() -> procesarCodigo(texto));
                Thread.sleep(1500);   // pausa para no registrar el mismo QR mil veces
            } catch (NotFoundException ignore) {
                // no hay QR en el frame: normal
            } catch (InterruptedException e) {
                break;
            } catch (Exception ignore) {
            }
            try { Thread.sleep(80); } catch (InterruptedException e) { break; }
        }
    }

    @FXML
    private void onCodigoManual() {
        String texto = campoCodigo.getText();
        if (texto != null && !texto.isBlank()) procesarCodigo(texto.trim());
        campoCodigo.clear();
    }

    // Lógica común: cámara y campo terminan aquí.
    private void procesarCodigo(String texto) {
        SesionAsistencia s = comboSesion.getValue();
        if (s == null) { mensaje("Elige una sesión primero.", Color.RED); return; }
        UUID uuid;
        try {
            uuid = UUID.fromString(texto);
        } catch (IllegalArgumentException e) {
            mensaje("Código no válido.", Color.RED);
            return;
        }
        try {
            Persona p = personaService.buscarPorQr(uuid);
            if (p == null) { mensaje("Ese QR no corresponde a ninguna persona.", Color.RED); return; }
            String nombre = p.getNombres() + " " + p.getApellidos();
            com.destino.app.service.ResultadoAsistencia r =
                    asistenciaService.registrar(s.getId(), p.getId(), "QR");
            switch (r) {
                case REGISTRADO -> mensaje("✓ Registrado: " + nombre, Color.GREEN);
                case DUPLICADO  -> mensaje(nombre + " ya estaba registrado.", Color.ORANGE);
                case OFFLINE    -> mensaje("Sin conexión: " + nombre + " guardado localmente.", Color.ORANGE);
            }
        } catch (com.destino.app.exceptions.AccesoDatosException e) {
            // La BD está caída y ni se pudo resolver el QR: guardamos el código crudo.
            asistenciaService.encolarQr(s.getId(), uuid);
            mensaje("Sin conexión: QR guardado localmente (se resolverá al sincronizar).", Color.ORANGE);
        }
    }

    @FXML
    private void onDetener() {
        detener();
        mensaje("Cámara detenida.", Color.BLACK);
    }

    private void detener() {
        corriendo = false;
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        try {
            if (webcam != null && webcam.isOpen()) webcam.close();
        } catch (Throwable ignore) {
        }
    }

    @FXML
    private void onVolver() {
        detener();   // ¡importante! liberar la cámara al salir
        Stage stage = (Stage) comboSesion.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }

    private void mensaje(String texto, Color color) {
        etiquetaMensaje.setTextFill(color);
        etiquetaMensaje.setText(texto);
    }
}