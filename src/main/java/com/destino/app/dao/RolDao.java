package com.destino.app.dao;

import com.destino.app.model.Rol;
import java.util.List;

public interface RolDao {
    List<Rol> listar();List<Rol>
    listarPorPersona(Long personaId);
    Rol buscarPorId(Long id);
    Rol guardar(Rol rol);        // devuelve el Rol ya con su id generado
    boolean actualizar(Rol rol);
    boolean eliminar(Long id);
}