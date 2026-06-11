package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDaoImpl implements RolDao {

    @Override
    public List<Rol> listar() {
        String sql = "SELECT id, nombre, descripcion FROM rol ORDER BY id";
        List<Rol> roles = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles", e);
        }
        return roles;
    }
    @Override
    public List<Rol> listarPorPersona(Long personaId) {
        String sql = "SELECT r.id, r.nombre, r.descripcion " +
                "FROM rol r " +
                "JOIN persona_rol pr ON pr.rol_id = r.id " +
                "WHERE pr.persona_id = ? " +
                "ORDER BY r.nombre";
        List<Rol> roles = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) roles.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles de persona id=" + personaId, e);
        }
        return roles;
    }
    @Override
    public Rol buscarPorId(Long id) {
        String sql = "SELECT id, nombre, descripcion FROM rol WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles", e);
        }
        return null;
    }

    @Override
    public Rol guardar(Rol rol) {
        String sql = "INSERT INTO rol (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rol.setId(rs.getLong(1));   // PostgreSQL devuelve el id IDENTITY
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles", e);
        }
        return rol;
    }

    @Override
    public boolean actualizar(Rol rol) {
        String sql = "UPDATE rol SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setLong(3, rol.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles", e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM rol WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar roles", e);
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Rol (evita repetir el mapeo).
    private Rol mapear(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setId(rs.getLong("id"));
        rol.setNombre(rs.getString("nombre"));
        rol.setDescripcion(rs.getString("descripcion"));
        return rol;
    }
}