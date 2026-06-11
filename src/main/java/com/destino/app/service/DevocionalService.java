package com.destino.app.service;

import com.destino.app.dao.DevocionalDao;
import com.destino.app.dao.DevocionalDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Devocional;

import java.math.BigDecimal;
import java.util.List;

public class DevocionalService {

    private final DevocionalDao dao;

    public DevocionalService() {
        this.dao = new DevocionalDaoImpl();
    }

    public List<Devocional> listar() {
        return dao.listar();
    }

    public Devocional guardar(Devocional d) {
        if (d.getNombre() == null || d.getNombre().isBlank()) {
            throw new ValidacionException("El nombre es obligatorio.");
        }
        if (d.getMes() != null && (d.getMes() < 1 || d.getMes() > 12)) {
            throw new ValidacionException("El mes debe estar entre 1 y 12.");
        }
        if (d.getPrecio() != null && d.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacionException("El precio no puede ser negativo.");
        }
        if (d.getId() == null) {
            return dao.guardar(d);
        }
        dao.actualizar(d);
        return d;
    }

    public void inactivar(Long id) {
        dao.inactivar(id);
    }
}