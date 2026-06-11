package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Usuario;

import java.sql.*;

public class UsuarioDaoImpl implements UsuarioDao {

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id, persona_id, nombre_usuario, hash_password, activo, ultimo_acceso "
                + "FROM usuario WHERE nombre_usuario = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar usuario: " + nombreUsuario, e);
        }
        return null;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (persona_id, nombre_usuario, hash_password, activo) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, usuario.getPersonaId());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setString(3, usuario.getHashPassword());
            ps.setBoolean(4, usuario.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar usuario", e);
        }
        return usuario;
    }

    @Override
    public void actualizarUltimoAcceso(Long id) {
        String sql = "UPDATE usuario SET ultimo_acceso = now() WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar último acceso id=" + id, e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setPersonaId(rs.getLong("persona_id"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setHashPassword(rs.getString("hash_password"));
        u.setActivo(rs.getBoolean("activo"));
        Timestamp ts = rs.getTimestamp("ultimo_acceso");
        u.setUltimoAcceso(ts != null ? ts.toLocalDateTime() : null);
        return u;
    }
}