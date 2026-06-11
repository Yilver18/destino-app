package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Asistencia;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDaoImpl implements AsistenciaDao {

    @Override
    public List<Asistencia> listarPorSesion(Long sesionId) {
        String sql = "SELECT a.id, a.sesion_id, a.persona_id, a.hora_registro, a.metodo, a.sincronizado, " +
                "(p.nombres || ' ' || p.apellidos) AS nombre_persona " +
                "FROM asistencia a JOIN persona p ON p.id = a.persona_id " +
                "WHERE a.sesion_id = ? ORDER BY a.hora_registro DESC";
        List<Asistencia> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, sesionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar asistencia de sesión id=" + sesionId, e);
        }
        return lista;
    }

    @Override
    public boolean registrar(Long sesionId, Long personaId, String metodo) {
        String sql = "INSERT INTO asistencia (sesion_id, persona_id, metodo) VALUES (?, ?, ?) " +
                "ON CONFLICT DO NOTHING";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, sesionId);
            ps.setLong(2, personaId);
            ps.setString(3, metodo);
            return ps.executeUpdate() > 0;   // 0 => ya estaba registrada (UNIQUE sesion+persona)
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al registrar asistencia", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM asistencia WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar asistencia id=" + id, e);
        }
    }

    private Asistencia mapear(ResultSet rs) throws SQLException {
        Asistencia a = new Asistencia();
        a.setId(rs.getLong("id"));
        a.setSesionId(rs.getLong("sesion_id"));
        a.setPersonaId(rs.getLong("persona_id"));
        a.setHoraRegistro(rs.getObject("hora_registro", OffsetDateTime.class));
        a.setMetodo(rs.getString("metodo"));
        a.setSincronizado(rs.getBoolean("sincronizado"));
        a.setNombrePersona(rs.getString("nombre_persona"));
        return a;
    }
}