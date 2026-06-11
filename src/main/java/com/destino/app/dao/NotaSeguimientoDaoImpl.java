package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.NotaSeguimiento;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotaSeguimientoDaoImpl implements NotaSeguimientoDao {

    @Override
    public List<NotaSeguimiento> listarPorPersona(Long personaId) {
        String sql = "SELECT ns.id, ns.persona_id, ns.autor_id, ns.tipo, ns.descripcion, ns.fecha, " +
                "(a.nombres || ' ' || a.apellidos) AS nombre_autor " +
                "FROM nota_seguimiento ns " +
                "JOIN persona a ON a.id = ns.autor_id " +
                "WHERE ns.persona_id = ? " +
                "ORDER BY ns.fecha DESC";
        List<NotaSeguimiento> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar notas de persona id=" + personaId, e);
        }
        return lista;
    }

    @Override
    public NotaSeguimiento guardar(NotaSeguimiento n) {
        String sql = "INSERT INTO nota_seguimiento (persona_id, autor_id, tipo, descripcion) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, n.getPersonaId());
            ps.setLong(2, n.getAutorId());
            ps.setString(3, n.getTipo());
            ps.setString(4, n.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) n.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar nota", e);
        }
        return n;
    }

    private NotaSeguimiento mapear(ResultSet rs) throws SQLException {
        NotaSeguimiento n = new NotaSeguimiento();
        n.setId(rs.getLong("id"));
        n.setPersonaId(rs.getLong("persona_id"));
        n.setAutorId(rs.getLong("autor_id"));
        n.setTipo(rs.getString("tipo"));
        n.setDescripcion(rs.getString("descripcion"));
        n.setFecha(rs.getObject("fecha", OffsetDateTime.class));
        n.setNombreAutor(rs.getString("nombre_autor"));
        return n;
    }
}