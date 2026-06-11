package com.destino.app.service;

import com.destino.app.dao.PastorDao;
import com.destino.app.dao.PastorDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Pastor;

import java.util.List;

public class PastorService {

    private final PastorDao dao;

    public PastorService() {
        this.dao = new PastorDaoImpl();
    }

    public List<Pastor> listar() {
        return dao.listar();
    }

    public Pastor guardar(Pastor p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new ValidacionException("El nombre es obligatorio.");
        }
        if (p.getId() == null) {
            return dao.guardar(p);
        }
        dao.actualizar(p);
        return p;
    }

    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}