package com.destino.app.dao;

import com.destino.app.model.Usuario;

public interface UsuarioDao {
    Usuario buscarPorNombreUsuario(String nombreUsuario);
    Usuario guardar(Usuario usuario);
    void actualizarUltimoAcceso(Long id);
}