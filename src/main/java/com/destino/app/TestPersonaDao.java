package com.destino.app;

import com.destino.app.config.Conexion;
import com.destino.app.dao.PersonaDao;
import com.destino.app.dao.PersonaDaoImpl;
import com.destino.app.model.Persona;

public class TestPersonaDao {
    public static void main(String[] args) {
        PersonaDao dao = new PersonaDaoImpl();

        Persona nueva = new Persona();
        nueva.setNombres("María");
        nueva.setApellidos("Gómez");
        nueva.setTipoDocumento("CC");
        nueva.setNumeroDocumento("1000000002");
        nueva.setEstado("nueva");
        nueva.setConsentimientoDatos(true);
        dao.guardar(nueva);
        System.out.println("Guardada con id=" + nueva.getId() + ", QR=" + nueva.getQrCodigo());

        System.out.println("\nTodas las personas:");
        for (Persona p : dao.listar()) {
            System.out.println("  " + p.getId() + " - " + p.getNombres() + " " + p.getApellidos()
                    + " [" + p.getEstado() + "]");
        }

        Conexion.getInstancia().cerrar();
    }
}