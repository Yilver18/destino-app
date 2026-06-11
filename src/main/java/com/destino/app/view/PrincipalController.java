package com.destino.app.view;

import com.destino.app.model.Usuario;
import com.destino.app.util.Navegador;
import com.destino.app.util.Sesion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrincipalController {

    @FXML private Label etiquetaBienvenida;
    @FXML private Button btnPersonas;   // agrega este campo (import javafx.scene.control.Button)
    @FXML private Button btnCasas;
    @FXML private Button btnMinisterios;
    @FXML private Button btnRoles;
    // initialize() lo llama JavaFX automáticamente al cargar el FXML.
    @FXML
    public void initialize() {
        Usuario u = Sesion.getUsuario();
        String roles = Sesion.getRoles().stream()
                .map(com.destino.app.model.Rol::getNombre)
                .collect(java.util.stream.Collectors.joining(", "));
        if (u != null) {
            etiquetaBienvenida.setText("Usuario id " + u.getId() + " — Roles: "
                    + (roles.isBlank() ? "(sin roles)" : roles));
        }

        // Solo Pastor/Admin ve la gestión de personas.
        boolean puedeGestionar = Sesion.tieneRol("Pastor/Admin");
        btnPersonas.setVisible(puedeGestionar);
        btnPersonas.setManaged(puedeGestionar);
        btnCasas.setVisible(puedeGestionar);
        btnCasas.setManaged(puedeGestionar);
        btnMinisterios.setVisible(puedeGestionar);
        btnMinisterios.setManaged(puedeGestionar);
        btnRoles.setVisible(puedeGestionar);
        btnRoles.setManaged(puedeGestionar);
    }

    @FXML
    private void onCerrarSesion() {
        Sesion.cerrar();
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage,
                "/com/destino/app/view/login.fxml",
                "Destino App — Iniciar sesión");
    }

    @FXML
    private void onPersonas() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage,
                "/com/destino/app/view/personas.fxml",
                "Destino App — Gestión de personas");
    }
    @FXML
    private void onCasas() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/casas.fxml",
                "Destino App — Casas Destino");
    }
    @FXML
    private void onMinisterios() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/ministerios.fxml",
                "Destino App — Ministerios");
    }
    @FXML
    private void onRoles() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/asignar_roles.fxml",
                "Destino App — Asignar roles");
    }
}