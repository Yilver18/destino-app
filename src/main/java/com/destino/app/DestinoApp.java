package com.destino.app;

import com.destino.app.config.Conexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DestinoApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/destino/app/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Destino App — Iniciar sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        // Al cerrar la app, cerramos el pool de conexiones.
        Conexion.getInstancia().cerrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}