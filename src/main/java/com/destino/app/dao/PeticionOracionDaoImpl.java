package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.PeticionOracion;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PeticionOracionDaoImpl implements PeticionOracionDao {

    @Override
    public List<PeticionOracion> listar() {
        String sql = "SELECT po.id, po.solicitante_id, po.descripcion, po.fecha, po.estado, " +
                "po.fecha_respondida, po.privada, " +
                "p.nombres || ' ' || p.apellidos AS nombre_solicitante " +
                "FROM peticion_oracion po " +
                "JOIN persona p ON p.id = po.solicitante_id " +
                "ORDER BY po.fecha DESC";
        List<PeticionOracion> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar peticiones", e);
        }
        return lista;
    }

    @Override
    public PeticionOracion guardar(PeticionOracion p) {
        String sql = "INSERT INTO peticion_oracion (solicitante_id, descripcion, privada) " +
                "VALUES (?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getSolicitanteId());
            ps.setString(2, p.getDescripcion());
            ps.setBoolean(3, p.isPrivada());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar petición", e);
        }
        return p;
    }

    @Override
    public boolean marcarRespondida(Long id) {
        String sql = "UPDATE peticion_oracion SET estado = 'RESPONDIDA', fecha_respondida = now() " +
                "WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al marcar respondida id=" + id, e);
        }
    }

    private PeticionOracion mapear(ResultSet rs) throws SQLException {
        PeticionOracion p = new PeticionOracion();
        p.setId(rs.getLong("id"));
        p.setSolicitanteId(rs.getLong("solicitante_id"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setFecha(rs.getObject("fecha", OffsetDateTime.class));
        p.setEstado(rs.getString("estado"));
        p.setFechaRespondida(rs.getObject("fecha_respondida", OffsetDateTime.class));
        p.setPrivada(rs.getBoolean("privada"));
        p.setNombreSolicitante(rs.getString("nombre_solicitante"));
        return p;
    }
}