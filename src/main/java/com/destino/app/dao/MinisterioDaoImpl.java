package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Ministerio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MinisterioDaoImpl implements MinisterioDao {

    private static final String COLUMNAS =
            "id, nombre, descripcion, coordinador_id, activo";

    @Override
    public List<Ministerio> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM ministerio ORDER BY nombre";
        List<Ministerio> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar ministerios", e);
        }
        return lista;
    }

    @Override
    public Ministerio buscarPorId(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM ministerio WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar ministerio id=" + id, e);
        }
        return null;
    }

    @Override
    public Ministerio guardar(Ministerio m) {
        String sql = "INSERT INTO ministerio (nombre, descripcion, coordinador_id, activo) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getDescripcion());
            ps.setObject(3, m.getCoordinadorId());   // Long o null
            ps.setBoolean(4, m.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar ministerio", e);
        }
        return m;
    }

    @Override
    public boolean actualizar(Ministerio m) {
        String sql = "UPDATE ministerio SET nombre = ?, descripcion = ?, " +
                "coordinador_id = ?, activo = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getDescripcion());
            ps.setObject(3, m.getCoordinadorId());
            ps.setBoolean(4, m.isActivo());
            ps.setLong(5, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar ministerio id=" + m.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE ministerio SET activo = false WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar ministerio id=" + id, e);
        }
    }

    private Ministerio mapear(ResultSet rs) throws SQLException {
        Ministerio m = new Ministerio();
        m.setId(rs.getLong("id"));
        m.setNombre(rs.getString("nombre"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setCoordinadorId(rs.getObject("coordinador_id", Long.class));  // null-safe
        m.setActivo(rs.getBoolean("activo"));
        return m;
    }
}