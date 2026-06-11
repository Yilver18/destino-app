package com.destino.app.service;

import com.destino.app.dao.EventoDao;
import com.destino.app.dao.EventoDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Evento;

import java.util.List;

public class EventoService {

    private final EventoDao dao;

    public EventoService() {
        this.dao = new EventoDaoImpl();
    }

    public List<Evento> listar() {
        return dao.listar();
    }

    public Evento guardar(Evento e) {
        if (e.getTitulo() == null || e.getTitulo().isBlank()) {
            throw new ValidacionException("El título es obligatorio.");
        }
        if (e.getFecha() == null) {
            throw new ValidacionException("La fecha es obligatoria.");
        }
        if (e.getId() == null) {
            return dao.guardar(e);
        }
        dao.actualizar(e);
        return e;
    }

    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}