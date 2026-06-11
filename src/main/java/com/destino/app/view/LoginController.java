package com.destino.app.view;

import com.destino.app.exceptions.AutenticacionException;
import com.destino.app.exceptions.DestinoException;
import com.destino.app.model.Usuario;
import com.destino.app.service.AutenticacionService;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoPassword;
    @FXML private Label etiquetaError;

    private final AutenticacionService autenticacionService = new AutenticacionService();

    @FXML
    private void onEntrar() {
        etiquetaError.setText("");
        String usuario = campoUsuario.getText().trim();
        String password = campoPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            etiquetaError.setTextFill(Color.RED);
            etiquetaError.setText("Ingresa usuario y contraseña.");
            return;
        }

        try {
            Usuario u = autenticacionService.autenticar(usuario, password);
            Sesion.iniciar(u, autenticacionService.rolesDe(u));
            Stage stage = (Stage) campoUsuario.getScene().getWindow();
            Navegador.cambiarEscena(stage,
                    "/com/destino/app/view/principal.fxml",
                    "Destino App — Panel principal");
        } catch (AutenticacionException e) {
            etiquetaError.setTextFill(Color.RED);
            etiquetaError.setText(e.getMessage());
        } catch (DestinoException e) {
            etiquetaError.setTextFill(Color.RED);
            etiquetaError.setText("Error de sistema. Intenta de nuevo.");
        }
    }
}