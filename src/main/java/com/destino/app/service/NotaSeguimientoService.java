package com.destino.app.service;

import com.destino.app.dao.NotaSeguimientoDao;
import com.destino.app.dao.NotaSeguimientoDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.NotaSeguimiento;

import java.util.List;

public class NotaSeguimientoService {

    private final NotaSeguimientoDao dao;

    public NotaSeguimientoService() {
        this.dao = new NotaSeguimientoDaoImpl();
    }

    public List<NotaSeguimiento> listarPorPersona(Long personaId) {
        return dao.listarPorPersona(personaId);
    }

    public NotaSeguimiento registrar(NotaSeguimiento n) {
        if (n.getPersonaId() == null) {
            throw new ValidacionException("Elige la persona a la que pertenece la nota.");
        }
        if (n.getAutorId() == null) {
            throw new ValidacionException("No hay un autor en sesión.");
        }
        if (n.getTipo() == null || n.getTipo().isBlank()) {
            throw new ValidacionException("Elige el tipo de nota.");
        }
        if (n.getDescripcion() == null || n.getDescripcion().isBlank()) {
            throw new ValidacionException("La nota no puede estar vacía.");
        }
        return dao.guardar(n);
    }
}