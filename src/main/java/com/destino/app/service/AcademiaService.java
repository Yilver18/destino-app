package com.destino.app.service;

import com.destino.app.dao.AcademiaDao;
import com.destino.app.dao.AcademiaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Academia;

import java.math.BigDecimal;
import java.util.List;

public class AcademiaService {

    private final AcademiaDao dao;

    public AcademiaService() {
        this.dao = new AcademiaDaoImpl();
    }

    public List<Academia> listar() {
        return dao.listar();
    }

    public Academia guardar(Academia a) {
        if (a.getNombre() == null || a.getNombre().isBlank()) {
            throw new ValidacionException("El nombre de la academia es obligatorio.");
        }
        if (a.getPrecioInscripcion() != null
                && a.getPrecioInscripcion().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacionException("El precio no puede ser negativo.");
        }
        if (a.getId() == null) {
            return dao.guardar(a);
        }
        dao.actualizar(a);
        return a;
    }

    public boolean inactivar(Long id) {
        return dao.inactivar(id);
    }
}