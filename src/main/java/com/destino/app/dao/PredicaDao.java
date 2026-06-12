package com.destino.app.dao;

import com.destino.app.model.Predica;
import java.util.List;

public interface PredicaDao {
    List<Predica> listar();
    Predica guardar(Predica p);
    void guardarResumen(Long id, String resumen);
    void marcarActual(Long id);
}