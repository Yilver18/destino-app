package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Predica;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PredicaDaoImpl implements PredicaDao {

    @Override
    public List<Predica> listar() {
        String sql = "SELECT pr.id, pr.titulo, pr.youtube_url, pr.fecha_publicacion, pr.creativo_id, " +
                "pr.resumen_ia, pr.fecha_resumen, pr.es_actual, " +
                "(p.nombres || ' ' || p.apellidos) AS nombre_creativo " +
                "FROM predica pr LEFT JOIN persona p ON p.id = pr.creativo_id " +
                "ORDER BY pr.es_actual DESC, pr.fecha_publicacion DESC";
        List<Predica> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar prédicas", e);
        }
        return lista;
    }

    @Override
    public Predica guardar(Predica p) {
        String sql = "INSERT INTO predica (titulo, youtube_url, fecha_publicacion, creativo_id) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getYoutubeUrl());
            ps.setObject(3, p.getFechaPublicacion() != null ? p.getFechaPublicacion() : LocalDate.now());
            ps.setObject(4, p.getCreativoId());   // Long o null
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar prédica", e);
        }
        return p;
    }

    // Marca una prédica como la actual: apaga la anterior y prende la nueva,
    // todo en una TRANSACCIÓN (o pasa todo, o no pasa nada).
    @Override
    public void marcarActual(Long id) {
        Connection con = null;
        try {
            con = Conexion.getInstancia().obtenerConexion();
            con.setAutoCommit(false);   // inicia transacción manual

            try (PreparedStatement apagar =
                         con.prepareStatement("UPDATE predica SET es_actual = false WHERE es_actual = true")) {
                apagar.executeUpdate();
            }
            try (PreparedStatement prender =
                         con.prepareStatement("UPDATE predica SET es_actual = true WHERE id = ?")) {
                prender.setLong(1, id);
                prender.executeUpdate();
            }
            con.commit();              // confirma ambos cambios juntos
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignore) {}  // deshace si algo falla
            }
            throw new AccesoDatosException("Error al marcar prédica como actual", e);
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private Predica mapear(ResultSet rs) throws SQLException {
        Predica p = new Predica();
        p.setId(rs.getLong("id"));
        p.setTitulo(rs.getString("titulo"));
        p.setYoutubeUrl(rs.getString("youtube_url"));
        p.setFechaPublicacion(rs.getObject("fecha_publicacion", LocalDate.class));
        p.setCreativoId(rs.getObject("creativo_id", Long.class));
        p.setResumenIa(rs.getString("resumen_ia"));
        p.setFechaResumen(rs.getObject("fecha_resumen", OffsetDateTime.class));
        p.setEsActual(rs.getBoolean("es_actual"));
        p.setNombreCreativo(rs.getString("nombre_creativo"));
        return p;
    }
}