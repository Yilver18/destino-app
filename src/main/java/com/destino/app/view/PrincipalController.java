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
    @FXML private Button btnAcademias;
    @FXML private Button btnOracion;
    @FXML private Button btnAgape;
    @FXML private Button btnCasaIntegrantes;
    @FXML private Button btnPredicas;
    @FXML private Button btnVoluntarios;
    @FXML private Button btnNotas;
    @FXML private Button btnDistribucion;
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
        btnAcademias.setVisible(puedeGestionar);
        btnAcademias.setManaged(puedeGestionar);
        btnOracion.setVisible(puedeGestionar);
        btnOracion.setManaged(puedeGestionar);
        btnAgape.setVisible(puedeGestionar);
        btnAgape.setManaged(puedeGestionar);
        btnPredicas.setVisible(puedeGestionar);
        btnPredicas.setManaged(puedeGestionar);
        btnVoluntarios.setVisible(puedeGestionar);
        btnVoluntarios.setManaged(puedeGestionar);
        btnNotas.setVisible(puedeGestionar);
        btnNotas.setManaged(puedeGestionar);
        btnDistribucion.setVisible(puedeGestionar);
        btnDistribucion.setManaged(puedeGestionar);
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

    @FXML
    private void onAcademias() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/academias.fxml",
                "Destino App — Academias");
    }
    @FXML
    private void onOracion() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/oracion.fxml",
                "Destino App — Peticiones de oración");
    }
    @FXML
    private void onAgape() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/agape.fxml",
                "Destino App — Agapé");
    }
    @FXML
    private void onCasaIntegrantes() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/casa_integrantes.fxml",
                "Destino App — Líderes y miembros");
    }
    @FXML
    private void onPredicas() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/predicas.fxml",
                "Destino App — Prédicas");
    }
    @FXML
    private void onVoluntarios() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/voluntarios.fxml",
                "Destino App — Voluntarios");
    }
    @FXML
    private void onNotas() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/notas.fxml",
                "Destino App — Notas de seguimiento");
    }

    @FXML
    private void onDistribucion() {
        Stage stage = (Stage) etiquetaBienvenida.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/distribucion.fxml",
                "Destino App — Distribución de mercados");
    }
}