package com.destino.app.dao;

import com.destino.app.model.Persona;
import java.util.List;

public interface PersonaDao {
    List<Persona> listar();
    Persona buscarPorId(Long id);
    Persona buscarPorDocumento(String numeroDocumento);
    Persona buscarPorQr(java.util.UUID qr);
    Persona guardar(Persona persona);
    boolean actualizar(Persona persona);
    boolean inactivar(Long id);   // borrado lógico
}