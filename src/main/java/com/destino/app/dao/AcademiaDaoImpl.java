package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Academia;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademiaDaoImpl implements AcademiaDao {

    private static final String COLUMNAS =
            "id, nombre, descripcion, mision, vision, logros, imagen_url, " +
                    "precio_inscripcion, coordinador_id, activa";

    @Override
    public List<Academia> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM academia ORDER BY nombre";
        List<Academia> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar academias", e);
        }
        return lista;
    }

    @Override
    public Academia buscarPorId(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM academia WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar academia id=" + id, e);
        }
        return null;
    }

    @Override
    public Academia guardar(Academia a) {
        String sql = "INSERT INTO academia (nombre, descripcion, mision, vision, logros, " +
                "imagen_url, precio_inscripcion, coordinador_id, activa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setString(3, a.getMision());
            ps.setString(4, a.getVision());
            ps.setString(5, a.getLogros());
            ps.setString(6, a.getImagenUrl());
            ps.setBigDecimal(7, a.getPrecioInscripcion() != null ? a.getPrecioInscripcion() : BigDecimal.ZERO);
            ps.setObject(8, a.getCoordinadorId());   // Long o null
            ps.setBoolean(9, a.isActiva());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar academia", e);
        }
        return a;
    }

    @Override
    public boolean actualizar(Academia a) {
        String sql = "UPDATE academia SET nombre = ?, descripcion = ?, mision = ?, vision = ?, " +
                "logros = ?, imagen_url = ?, precio_inscripcion = ?, coordinador_id = ?, activa = ? " +
                "WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setString(3, a.getMision());
            ps.setString(4, a.getVision());
            ps.setString(5, a.getLogros());
            ps.setString(6, a.getImagenUrl());
            ps.setBigDecimal(7, a.getPrecioInscripcion() != null ? a.getPrecioInscripcion() : BigDecimal.ZERO);
            ps.setObject(8, a.getCoordinadorId());
            ps.setBoolean(9, a.isActiva());
            ps.setLong(10, a.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar academia id=" + a.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE academia SET activa = false WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar academia id=" + id, e);
        }
    }

    private Academia mapear(ResultSet rs) throws SQLException {
        Academia a = new Academia();
        a.setId(rs.getLong("id"));
        a.setNombre(rs.getString("nombre"));
        a.setDescripcion(rs.getString("descripcion"));
        a.setMision(rs.getString("mision"));
        a.setVision(rs.getString("vision"));
        a.setLogros(rs.getString("logros"));
        a.setImagenUrl(rs.getString("imagen_url"));
        a.setPrecioInscripcion(rs.getBigDecimal("precio_inscripcion"));
        a.setCoordinadorId(rs.getObject("coordinador_id", Long.class));
        a.setActiva(rs.getBoolean("activa"));
        return a;
    }
}