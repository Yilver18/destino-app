package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.PaginaContenido;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaginaContenidoDaoImpl implements PaginaContenidoDao {

    private static final String COLUMNAS =
            "id, clave, titulo, cuerpo, imagen_url, actualizado_por, fecha_actualizacion";

    @Override
    public List<PaginaContenido> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM pagina_contenido ORDER BY clave";
        List<PaginaContenido> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar páginas", e);
        }
        return lista;
    }

    @Override
    public void guardar(PaginaContenido p) {
        // Upsert por la clave única; actualiza también quién y cuándo editó.
        String sql = "INSERT INTO pagina_contenido (clave, titulo, cuerpo, imagen_url, actualizado_por, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?, ?, now()) " +
                "ON CONFLICT (clave) DO UPDATE SET " +
                "titulo = EXCLUDED.titulo, cuerpo = EXCLUDED.cuerpo, imagen_url = EXCLUDED.imagen_url, " +
                "actualizado_por = EXCLUDED.actualizado_por, fecha_actualizacion = now()";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getClave());
            ps.setString(2, p.getTitulo());
            ps.setString(3, p.getCuerpo());
            ps.setString(4, p.getImagenUrl());
            ps.setObject(5, p.getActualizadoPor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar página", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM pagina_contenido WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar página id=" + id, e);
        }
    }

    private PaginaContenido mapear(ResultSet rs) throws SQLException {
        PaginaContenido p = new PaginaContenido();
        p.setId(rs.getLong("id"));
        p.setClave(rs.getString("clave"));
        p.setTitulo(rs.getString("titulo"));
        p.setCuerpo(rs.getString("cuerpo"));
        p.setImagenUrl(rs.getString("imagen_url"));
        p.setActualizadoPor(rs.getObject("actualizado_por", Long.class));
        p.setFechaActualizacion(rs.getObject("fecha_actualizacion", OffsetDateTime.class));
        return p;
    }
}