package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasaAsignacionDaoImpl implements CasaAsignacionDao {

    @Override
    public List<Persona> listarLideres(Long casaId) {
        return listarPersonas(casaId, "casa_lider");
    }

    @Override
    public List<Persona> listarMiembros(Long casaId) {
        return listarPersonas(casaId, "casa_miembro");
    }

    // Reutiliza la misma consulta para las dos tablas puente.
    private List<Persona> listarPersonas(Long casaId, String tablaPuente) {
        String sql = "SELECT p.id, p.nombres, p.apellidos " +
                "FROM persona p JOIN " + tablaPuente + " tp ON tp.persona_id = p.id " +
                "WHERE tp.casa_id = ? ORDER BY p.apellidos, p.nombres";
        List<Persona> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, casaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Persona p = new Persona();
                    p.setId(rs.getLong("id"));
                    p.setNombres(rs.getString("nombres"));
                    p.setApellidos(rs.getString("apellidos"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar de " + tablaPuente, e);
        }
        return lista;
    }

    @Override
    public void agregarLider(Long casaId, Long personaId) {
        ejecutarInsert("INSERT INTO casa_lider (casa_id, persona_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                casaId, personaId, "asignar líder");
    }

    @Override
    public void agregarMiembro(Long casaId, Long personaId) {
        ejecutarInsert("INSERT INTO casa_miembro (casa_id, persona_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                casaId, personaId, "asignar miembro");
    }

    @Override
    public void quitarLider(Long casaId, Long personaId) {
        ejecutarDelete("DELETE FROM casa_lider WHERE casa_id = ? AND persona_id = ?",
                casaId, personaId, "quitar líder");
    }

    @Override
    public void quitarMiembro(Long casaId, Long personaId) {
        ejecutarDelete("DELETE FROM casa_miembro WHERE casa_id = ? AND persona_id = ?",
                casaId, personaId, "quitar miembro");
    }

    private void ejecutarInsert(String sql, Long casaId, Long personaId, String accion) {
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, casaId);
            ps.setLong(2, personaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al " + accion, e);
        }
    }

    private void ejecutarDelete(String sql, Long casaId, Long personaId, String accion) {
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, casaId);
            ps.setLong(2, personaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al " + accion, e);
        }
    }
}