package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.SesionAsistencia;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SesionAsistenciaDaoImpl implements SesionAsistenciaDao {

    @Override
    public List<SesionAsistencia> listar() {
        String sql = "SELECT s.id, s.fecha, s.contexto, s.servicio_id, s.casa_id, s.academia_id, s.descripcion, " +
                "COALESCE(sv.nombre, c.nombre, ac.nombre) AS referencia " +
                "FROM sesion s " +
                "LEFT JOIN servicio sv ON sv.id = s.servicio_id " +
                "LEFT JOIN casa_destino c ON c.id = s.casa_id " +
                "LEFT JOIN academia ac ON ac.id = s.academia_id " +
                "ORDER BY s.fecha DESC, s.id DESC";
        List<SesionAsistencia> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar sesiones", e);
        }
        return lista;
    }

    @Override
    public SesionAsistencia guardar(SesionAsistencia s) {
        String sql = "INSERT INTO sesion (fecha, contexto, servicio_id, casa_id, academia_id, descripcion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, s.getFecha());
            ps.setString(2, s.getContexto());
            ps.setObject(3, s.getServicioId());
            ps.setObject(4, s.getCasaId());
            ps.setObject(5, s.getAcademiaId());
            ps.setString(6, s.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar sesión", e);
        }
        return s;
    }

    private SesionAsistencia mapear(ResultSet rs) throws SQLException {
        SesionAsistencia s = new SesionAsistencia();
        s.setId(rs.getLong("id"));
        s.setFecha(rs.getObject("fecha", LocalDate.class));
        s.setContexto(rs.getString("contexto"));
        s.setServicioId(rs.getObject("servicio_id", Long.class));
        s.setCasaId(rs.getObject("casa_id", Long.class));
        s.setAcademiaId(rs.getObject("academia_id", Long.class));
        s.setDescripcion(rs.getString("descripcion"));
        s.setReferenciaMostrar(rs.getString("referencia"));
        return s;
    }
}