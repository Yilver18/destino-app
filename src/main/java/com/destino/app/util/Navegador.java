package com.destino.app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class Navegador {

    private Navegador() {
    }

    public static void cambiarEscena(Stage stage, String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));
            Parent raiz = loader.load();
            stage.setScene(new Scene(raiz));
            stage.setTitle(titulo);
            stage.centerOnScreen();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFxml, e);
        }
    }
}
