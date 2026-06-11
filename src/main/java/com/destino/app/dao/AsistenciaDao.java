package com.destino.app.dao;

import com.destino.app.model.Asistencia;
import java.util.List;

public interface AsistenciaDao {
    List<Asistencia> listarPorSesion(Long sesionId);
    boolean registrar(Long sesionId, Long personaId, String metodo);  // false si ya estaba
    void eliminar(Long id);
}