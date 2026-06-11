package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;

import java.sql.*;

public class PersonaRolDaoImpl implements PersonaRolDao {

    @Override
    public void asignar(Long personaId, Long rolId) {
        // ministerio_id queda null (rol sin acotar a un área).
        String sql = "INSERT INTO persona_rol (persona_id, rol_id) VALUES (?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            ps.setLong(2, rolId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al asignar rol", e);
        }
    }

    @Override
    public void quitar(Long personaId, Long rolId) {
        String sql = "DELETE FROM persona_rol WHERE persona_id = ? AND rol_id = ? AND ministerio_id IS NULL";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            ps.setLong(2, rolId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al quitar rol", e);
        }
    }

    @Override
    public boolean existe(Long personaId, Long rolId) {
        String sql = "SELECT 1 FROM persona_rol WHERE persona_id = ? AND rol_id = ? AND ministerio_id IS NULL";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            ps.setLong(2, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();   // hay fila => ya existe
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al verificar rol", e);
        }
    }
}