package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Pastor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PastorDaoImpl implements PastorDao {

    private static final String COLUMNAS = "id, nombre, cargo, foto_url, biografia, orden";

    @Override
    public List<Pastor> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM pastor ORDER BY orden, nombre";
        List<Pastor> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar pastores", e);
        }
        return lista;
    }

    @Override
    public Pastor guardar(Pastor p) {
        String sql = "INSERT INTO pastor (nombre, cargo, foto_url, biografia, orden) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCargo());
            ps.setString(3, p.getFotoUrl());
            ps.setString(4, p.getBiografia());
            ps.setInt(5, p.getOrden());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar pastor", e);
        }
        return p;
    }

    @Override
    public boolean actualizar(Pastor p) {
        String sql = "UPDATE pastor SET nombre = ?, cargo = ?, foto_url = ?, biografia = ?, orden = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCargo());
            ps.setString(3, p.getFotoUrl());
            ps.setString(4, p.getBiografia());
            ps.setInt(5, p.getOrden());
            ps.setLong(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar pastor id=" + p.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM pastor WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar pastor id=" + id, e);
        }
    }

    private Pastor mapear(ResultSet rs) throws SQLException {
        Pastor p = new Pastor();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setCargo(rs.getString("cargo"));
        p.setFotoUrl(rs.getString("foto_url"));
        p.setBiografia(rs.getString("biografia"));
        p.setOrden(rs.getInt("orden"));
        return p;
    }
}