package com.destino.app.service;

import com.destino.app.dao.PaginaContenidoDao;
import com.destino.app.dao.PaginaContenidoDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.PaginaContenido;

import java.util.List;

public class PaginaContenidoService {

    private final PaginaContenidoDao dao;

    public PaginaContenidoService() {
        this.dao = new PaginaContenidoDaoImpl();
    }

    public List<PaginaContenido> listar() {
        return dao.listar();
    }

    public void guardar(PaginaContenido p) {
        if (p.getClave() == null || p.getClave().isBlank()) {
            throw new ValidacionException("La clave es obligatoria (ej. NOSOTROS_HISTORIA).");
        }
        if (p.getCuerpo() == null || p.getCuerpo().isBlank()) {
            throw new ValidacionException("El cuerpo del contenido no puede estar vacío.");
        }
        dao.guardar(p);
    }

    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}