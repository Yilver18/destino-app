package com.destino.app.dao;

import com.destino.app.model.AsignacionVoluntario;
import java.util.List;

public interface AsignacionVoluntarioDao {
    List<AsignacionVoluntario> listar();
    boolean asignar(AsignacionVoluntario a);   // false si ya existía
    void actualizarEstado(Long id, String estado);
    void eliminar(Long id);
}