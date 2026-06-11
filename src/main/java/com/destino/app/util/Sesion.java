package com.destino.app.util;

import com.destino.app.model.Rol;
import com.destino.app.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public final class Sesion {

    private static Usuario usuarioActual;
    private static List<Rol> rolesActuales = new ArrayList<>();

    private Sesion() {
    }

    public static void iniciar(Usuario usuario, List<Rol> roles) {
        usuarioActual = usuario;
        rolesActuales = (roles != null) ? roles : new ArrayList<>();
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static List<Rol> getRoles() {
        return rolesActuales;
    }

    // ¿El usuario actual tiene este rol? (case-insensitive)
    public static boolean tieneRol(String nombreRol) {
        return rolesActuales.stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase(nombreRol));
    }

    public static void cerrar() {
        usuarioActual = null;
        rolesActuales = new ArrayList<>();
    }
}