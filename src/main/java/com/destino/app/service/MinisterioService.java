package com.destino.app.service;

import com.destino.app.dao.MinisterioDao;
import com.destino.app.dao.MinisterioDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Ministerio;

import java.util.List;

public class MinisterioService {

    private final MinisterioDao dao;

    public MinisterioService() {
        this.dao = new MinisterioDaoImpl();
    }

    public List<Ministerio> listar() {
        return dao.listar();
    }

    public Ministerio guardar(Ministerio m) {
        if (m.getNombre() == null || m.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del ministerio es obligatorio.");
        }
        if (m.getId() == null) {
            return dao.guardar(m);
        }
        dao.actualizar(m);
        return m;
    }

    public boolean inactivar(Long id) {
        return dao.inactivar(id);
    }
}