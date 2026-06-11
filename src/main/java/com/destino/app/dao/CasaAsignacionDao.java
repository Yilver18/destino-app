package com.destino.app.dao;

import com.destino.app.model.Persona;
import java.util.List;

public interface CasaAsignacionDao {
    List<Persona> listarLideres(Long casaId);
    void agregarLider(Long casaId, Long personaId);
    void quitarLider(Long casaId, Long personaId);

    List<Persona> listarMiembros(Long casaId);
    void agregarMiembro(Long casaId, Long personaId);
    void quitarMiembro(Long casaId, Long personaId);
}