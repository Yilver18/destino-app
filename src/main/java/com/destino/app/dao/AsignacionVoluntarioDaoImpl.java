package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.AsignacionVoluntario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsignacionVoluntarioDaoImpl implements AsignacionVoluntarioDao {

    @Override
    public List<AsignacionVoluntario> listar() {
        String sql = "SELECT av.id, av.persona_id, av.ministerio_id, av.servicio_id, av.fecha_servicio, " +
                "av.estado, av.notificado, " +
                "(p.nombres || ' ' || p.apellidos) AS nombre_persona, " +
                "m.nombre AS nombre_ministerio, s.nombre AS nombre_servicio " +
                "FROM asignacion_voluntario av " +
                "JOIN persona p ON p.id = av.persona_id " +
                "JOIN ministerio m ON m.id = av.ministerio_id " +
                "JOIN servicio s ON s.id = av.servicio_id " +
                "ORDER BY av.fecha_servicio DESC";
        List<AsignacionVoluntario> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar asignaciones", e);
        }
        return lista;
    }

    @Override
    public boolean asignar(AsignacionVoluntario a) {
        String sql = "INSERT INTO asignacion_voluntario (persona_id, ministerio_id, servicio_id, fecha_servicio) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, a.getPersonaId());
            ps.setLong(2, a.getMinisterioId());
            ps.setLong(3, a.getServicioId());
            ps.setObject(4, a.getFechaServicio());
            return ps.executeUpdate() > 0;   // 0 => ya existía (UNIQUE)
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al asignar voluntario", e);
        }
    }

    @Override
    public void actualizarEstado(Long id, String estado) {
        String sql = "UPDATE asignacion_voluntario SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar estado id=" + id, e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM asignacion_voluntario WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar asignación id=" + id, e);
        }
    }

    private AsignacionVoluntario mapear(ResultSet rs) throws SQLException {
        AsignacionVoluntario a = new AsignacionVoluntario();
        a.setId(rs.getLong("id"));
        a.setPersonaId(rs.getLong("persona_id"));
        a.setMinisterioId(rs.getLong("ministerio_id"));
        a.setServicioId(rs.getLong("servicio_id"));
        a.setFechaServicio(rs.getObject("fecha_servicio", LocalDate.class));
        a.setEstado(rs.getString("estado"));
        a.setNotificado(rs.getBoolean("notificado"));
        a.setNombrePersona(rs.getString("nombre_persona"));
        a.setNombreMinisterio(rs.getString("nombre_ministerio"));
        a.setNombreServicio(rs.getString("nombre_servicio"));
        return a;
    }
}