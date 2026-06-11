package com.destino.app.dao;

public interface PersonaRolDao {
    void asignar(Long personaId, Long rolId);
    void quitar(Long personaId, Long rolId);
    boolean existe(Long personaId, Long rolId);
}