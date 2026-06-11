package com.destino.app.service;

import com.destino.app.dao.CasaDestinoDao;
import com.destino.app.dao.CasaDestinoDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.CasaDestino;

import java.util.List;

public class CasaDestinoService {

    private final CasaDestinoDao dao;

    public CasaDestinoService() {
        this.dao = new CasaDestinoDaoImpl();
    }

    public CasaDestinoService(CasaDestinoDao dao) {
        this.dao = dao;
    }

    public List<CasaDestino> listar() {
        return dao.listar();
    }

    public CasaDestino guardar(CasaDestino c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new ValidacionException("El nombre de la casa es obligatorio.");
        }
        if (c.getId() == null) {
            return dao.guardar(c);
        }
        dao.actualizar(c);
        return c;
    }

    public boolean inactivar(Long id) {
        return dao.inactivar(id);
    }
}