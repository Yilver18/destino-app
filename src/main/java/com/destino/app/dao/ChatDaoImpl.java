package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.MensajeChat;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatDaoImpl implements ChatDao {

    @Override
    public List<String[]> conversacionesDe(Long personaId) {
        // Para conversaciones DIRECTAS, la etiqueta es el nombre del OTRO participante.
        String sql = "SELECT c.id, " +
                "CASE WHEN c.tipo = 'DIRECTA' THEN (" +
                "   SELECT (p2.nombres || ' ' || p2.apellidos) " +
                "   FROM conversacion_participante cp2 JOIN persona p2 ON p2.id = cp2.persona_id " +
                "   WHERE cp2.conversacion_id = c.id AND cp2.persona_id <> ? LIMIT 1" +
                ") ELSE c.nombre END AS etiqueta " +
                "FROM conversacion c " +
                "JOIN conversacion_participante cp ON cp.conversacion_id = c.id " +
                "WHERE cp.persona_id = ? " +
                "ORDER BY c.fecha_creacion DESC";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            ps.setLong(2, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new String[]{ String.valueOf(rs.getLong("id")), rs.getString("etiqueta") });
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar conversaciones", e);
        }
        return filas;
    }

    @Override
    public Long obtenerOCrearDirecta(Long a, Long b) {
        String buscar = "SELECT c.id FROM conversacion c WHERE c.tipo = 'DIRECTA' " +
                "AND EXISTS (SELECT 1 FROM conversacion_participante WHERE conversacion_id = c.id AND persona_id = ?) " +
                "AND EXISTS (SELECT 1 FROM conversacion_participante WHERE conversacion_id = c.id AND persona_id = ?) LIMIT 1";
        Connection con = null;
        try {
            con = Conexion.getInstancia().obtenerConexion();
            // 1) ¿ya existe?
            try (PreparedStatement ps = con.prepareStatement(buscar)) {
                ps.setLong(1, a);
                ps.setLong(2, b);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                }
            }
            // 2) crear conversación + dos participantes, en una transacción
            con.setAutoCommit(false);
            Long convId;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO conversacion (tipo) VALUES ('DIRECTA')", Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    convId = rs.getLong(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO conversacion_participante (conversacion_id, persona_id) VALUES (?, ?)")) {
                ps.setLong(1, convId); ps.setLong(2, a); ps.addBatch();
                ps.setLong(1, convId); ps.setLong(2, b); ps.addBatch();
                ps.executeBatch();
            }
            con.commit();
            return convId;
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ig) {}
            throw new AccesoDatosException("Error al abrir conversación", e);
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ig) {}
        }
    }

    @Override
    public List<MensajeChat> mensajesDe(Long conversacionId) {
        String sql = "SELECT m.id, m.conversacion_id, m.remitente_id, m.contenido, m.fecha_envio, " +
                "(p.nombres || ' ' || p.apellidos) AS remitente " +
                "FROM mensaje_chat m JOIN persona p ON p.id = m.remitente_id " +
                "WHERE m.conversacion_id = ? ORDER BY m.fecha_envio";
        List<MensajeChat> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, conversacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MensajeChat m = new MensajeChat();
                    m.setId(rs.getLong("id"));
                    m.setConversacionId(rs.getLong("conversacion_id"));
                    m.setRemitenteId(rs.getLong("remitente_id"));
                    m.setContenido(rs.getString("contenido"));
                    m.setFechaEnvio(rs.getObject("fecha_envio", OffsetDateTime.class));
                    m.setNombreRemitente(rs.getString("remitente"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar mensajes", e);
        }
        return lista;
    }

    @Override
    public void enviar(Long conversacionId, Long remitenteId, String contenido) {
        String sql = "INSERT INTO mensaje_chat (conversacion_id, remitente_id, contenido) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, conversacionId);
            ps.setLong(2, remitenteId);
            ps.setString(3, contenido);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al enviar mensaje", e);
        }
    }
}