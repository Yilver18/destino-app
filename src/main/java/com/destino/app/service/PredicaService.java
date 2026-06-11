package com.destino.app.service;

import com.destino.app.dao.PredicaDao;
import com.destino.app.dao.PredicaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Predica;

import java.util.List;

public class PredicaService {

    private final PredicaDao dao;

    public PredicaService() {
        this.dao = new PredicaDaoImpl();
    }

    public List<Predica> listar() {
        return dao.listar();
    }

    public Predica registrar(Predica p) {
        if (p.getTitulo() == null || p.getTitulo().isBlank()) {
            throw new ValidacionException("El título es obligatorio.");
        }
        if (p.getYoutubeUrl() == null || p.getYoutubeUrl().isBlank()) {
            throw new ValidacionException("El link de YouTube es obligatorio.");
        }
        return dao.guardar(p);
    }

    public void marcarActual(Long id) {
        dao.marcarActual(id);
    }
}