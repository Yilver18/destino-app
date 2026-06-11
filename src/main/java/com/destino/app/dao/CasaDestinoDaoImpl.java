package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.CasaDestino;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CasaDestinoDaoImpl implements CasaDestinoDao {

    private static final String COLUMNAS =
            "id, nombre, direccion, sector, dia_reunion, hora_reunion, activa";

    @Override
    public List<CasaDestino> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM casa_destino ORDER BY nombre";
        List<CasaDestino> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar casas", e);
        }
        return lista;
    }

    @Override
    public CasaDestino buscarPorId(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM casa_destino WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar casa id=" + id, e);
        }
        return null;
    }

    @Override
    public CasaDestino guardar(CasaDestino c) {
        String sql = "INSERT INTO casa_destino (nombre, direccion, sector, dia_reunion, hora_reunion, activa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDireccion());
            ps.setString(3, c.getSector());
            ps.setString(4, c.getDiaReunion());
            ps.setObject(5, c.getHoraReunion());   // LocalTime o null
            ps.setBoolean(6, c.isActiva());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar casa", e);
        }
        return c;
    }

    @Override
    public boolean actualizar(CasaDestino c) {
        String sql = "UPDATE casa_destino SET nombre = ?, direccion = ?, sector = ?, " +
                "dia_reunion = ?, hora_reunion = ?, activa = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDireccion());
            ps.setString(3, c.getSector());
            ps.setString(4, c.getDiaReunion());
            ps.setObject(5, c.getHoraReunion());
            ps.setBoolean(6, c.isActiva());
            ps.setLong(7, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar casa id=" + c.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE casa_destino SET activa = false WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar casa id=" + id, e);
        }
    }

    private CasaDestino mapear(ResultSet rs) throws SQLException {
        CasaDestino c = new CasaDestino();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDireccion(rs.getString("direccion"));
        c.setSector(rs.getString("sector"));
        c.setDiaReunion(rs.getString("dia_reunion"));
        c.setHoraReunion(rs.getObject("hora_reunion", LocalTime.class));
        c.setActiva(rs.getBoolean("activa"));
        return c;
    }
}