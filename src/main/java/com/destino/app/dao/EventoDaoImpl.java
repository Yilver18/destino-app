package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Evento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDaoImpl implements EventoDao {

    private static final String COLUMNAS =
            "id, titulo, descripcion, fecha, hora, lugar, imagen_url, publicado, creado_por";

    @Override
    public List<Evento> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM evento ORDER BY fecha DESC";
        List<Evento> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar eventos", e);
        }
        return lista;
    }

    @Override
    public Evento guardar(Evento e) {
        String sql = "INSERT INTO evento (titulo, descripcion, fecha, hora, lugar, imagen_url, publicado, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescripcion());
            ps.setObject(3, e.getFecha());
            ps.setObject(4, e.getHora());
            ps.setString(5, e.getLugar());
            ps.setString(6, e.getImagenUrl());
            ps.setBoolean(7, e.isPublicado());
            ps.setObject(8, e.getCreadoPor());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getLong("id"));
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al guardar evento", ex);
        }
        return e;
    }

    @Override
    public boolean actualizar(Evento e) {
        String sql = "UPDATE evento SET titulo = ?, descripcion = ?, fecha = ?, hora = ?, " +
                "lugar = ?, imagen_url = ?, publicado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescripcion());
            ps.setObject(3, e.getFecha());
            ps.setObject(4, e.getHora());
            ps.setString(5, e.getLugar());
            ps.setString(6, e.getImagenUrl());
            ps.setBoolean(7, e.isPublicado());
            ps.setLong(8, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al actualizar evento id=" + e.getId(), ex);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM evento WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al eliminar evento id=" + id, ex);
        }
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setId(rs.getLong("id"));
        e.setTitulo(rs.getString("titulo"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setFecha(rs.getObject("fecha", LocalDate.class));
        e.setHora(rs.getObject("hora", LocalTime.class));
        e.setLugar(rs.getString("lugar"));
        e.setImagenUrl(rs.getString("imagen_url"));
        e.setPublicado(rs.getBoolean("publicado"));
        e.setCreadoPor(rs.getObject("creado_por", Long.class));
        return e;
    }
}