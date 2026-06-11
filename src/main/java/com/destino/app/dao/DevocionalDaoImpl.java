package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Devocional;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DevocionalDaoImpl implements DevocionalDao {

    private static final String COLUMNAS =
            "id, nombre, descripcion, mes, anio, precio, imagen_url, destacado, activo";

    @Override
    public List<Devocional> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM devocional ORDER BY anio DESC, mes DESC, nombre";
        List<Devocional> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar devocionales", e);
        }
        return lista;
    }

    @Override
    public Devocional guardar(Devocional d) {
        String sql = "INSERT INTO devocional (nombre, descripcion, mes, anio, precio, imagen_url, destacado, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getDescripcion());
            ps.setObject(3, d.getMes());
            ps.setObject(4, d.getAnio());
            ps.setBigDecimal(5, d.getPrecio() != null ? d.getPrecio() : BigDecimal.ZERO);
            ps.setString(6, d.getImagenUrl());
            ps.setBoolean(7, d.isDestacado());
            ps.setBoolean(8, d.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar devocional", e);
        }
        return d;
    }

    @Override
    public boolean actualizar(Devocional d) {
        String sql = "UPDATE devocional SET nombre = ?, descripcion = ?, mes = ?, anio = ?, " +
                "precio = ?, imagen_url = ?, destacado = ?, activo = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getDescripcion());
            ps.setObject(3, d.getMes());
            ps.setObject(4, d.getAnio());
            ps.setBigDecimal(5, d.getPrecio() != null ? d.getPrecio() : BigDecimal.ZERO);
            ps.setString(6, d.getImagenUrl());
            ps.setBoolean(7, d.isDestacado());
            ps.setBoolean(8, d.isActivo());
            ps.setLong(9, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar devocional id=" + d.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE devocional SET activo = false WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar devocional id=" + id, e);
        }
    }

    private Devocional mapear(ResultSet rs) throws SQLException {
        Devocional d = new Devocional();
        d.setId(rs.getLong("id"));
        d.setNombre(rs.getString("nombre"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setMes(rs.getObject("mes", Integer.class));
        d.setAnio(rs.getObject("anio", Integer.class));
        d.setPrecio(rs.getBigDecimal("precio"));
        d.setImagenUrl(rs.getString("imagen_url"));
        d.setDestacado(rs.getBoolean("destacado"));
        d.setActivo(rs.getBoolean("activo"));
        return d;
    }
}