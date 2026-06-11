package com.destino.app.dao;

import com.destino.app.model.Evento;
import java.util.List;

public interface EventoDao {
    List<Evento> listar();
    Evento guardar(Evento e);
    boolean actualizar(Evento e);
    void eliminar(Long id);
}