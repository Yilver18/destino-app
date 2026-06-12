package com.destino.app.dao;

import com.destino.app.model.PaginaContenido;
import java.util.List;

public interface PaginaContenidoDao {
    List<PaginaContenido> listar();
    void guardar(PaginaContenido p);   // upsert por clave
    void eliminar(Long id);
}