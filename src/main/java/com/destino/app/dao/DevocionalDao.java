package com.destino.app.dao;

import com.destino.app.model.Devocional;
import java.util.List;

public interface DevocionalDao {
    List<Devocional> listar();
    Devocional guardar(Devocional d);
    boolean actualizar(Devocional d);
    boolean inactivar(Long id);
}