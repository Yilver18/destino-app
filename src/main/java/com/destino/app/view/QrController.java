package com.destino.app.view;

import com.destino.app.model.Persona;
import com.destino.app.service.PersonaService;
import com.destino.app.util.Navegador;
import com.destino.app.util.QrUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class QrController {

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private ImageView imagenQr;
    @FXML private Label etiquetaCodigo;
    @FXML private Button btnGuardar;
    @FXML private Label etiquetaMensaje;

    private final PersonaService personaService = new PersonaService();
    private byte[] qrActual;          // bytes del QR mostrado, para guardarlo
    private Persona personaActual;

    @FXML
    public void initialize() {
        comboPersona.setConverter(new StringConverter<>() {
            @Override public String toString(Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
            @Override public Persona fromString(String s) { return null; }
        });
        comboPersona.setItems(FXCollections.observableArrayList(personaService.listar()));
        comboPersona.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrarQr(sel));
    }

    private void mostrarQr(Persona p) {
        personaActual = p;
        if (p == null || p.getQrCodigo() == null) {
            imagenQr.setImage(null);
            etiquetaCodigo.setText("");
            btnGuardar.setDisable(true);
            return;
        }
        String codigo = p.getQrCodigo().toString();
        qrActual = QrUtil.generarPng(codigo, 300);
        imagenQr.setImage(new Image(new ByteArrayInputStream(qrActual)));
        etiquetaCodigo.setText(codigo);
        btnGuardar.setDisable(false);
        mensaje("", Color.BLACK);
    }

    @FXML
    private void onGuardar() {
        if (qrActual == null || personaActual == null) return;
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar QR");
            fc.setInitialFileName("qr_" + personaActual.getNombres() + "_" + personaActual.getApellidos() + ".png");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen PNG", "*.png"));
            File destino = fc.showSaveDialog(comboPersona.getScene().getWindow());
            if (destino != null) {
                Files.write(destino.toPath(), qrActual);
                mensaje("QR guardado en: " + destino.getName(), Color.GREEN);
            }
        } catch (Exception e) {
            mensaje("No se pudo guardar el archivo.", Color.RED);
        }
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