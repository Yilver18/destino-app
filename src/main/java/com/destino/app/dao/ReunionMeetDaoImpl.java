package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.ReunionMeet;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReunionMeetDaoImpl implements ReunionMeetDao {

    private static final String COLUMNAS = "id, nombre, url, dia, hora, descripcion, activa";

    @Override
    public List<ReunionMeet> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM reunion_meet ORDER BY nombre";
        List<ReunionMeet> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar reuniones", e);
        }
        return lista;
    }

    @Override
    public ReunionMeet guardar(ReunionMeet r) {
        String sql = "INSERT INTO reunion_meet (nombre, url, dia, hora, descripcion, activa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getUrl());
            ps.setString(3, r.getDia());
            ps.setObject(4, r.getHora());
            ps.setString(5, r.getDescripcion());
            ps.setBoolean(6, r.isActiva());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar reunión", e);
        }
        return r;
    }

    @Override
    public boolean actualizar(ReunionMeet r) {
        String sql = "UPDATE reunion_meet SET nombre = ?, url = ?, dia = ?, hora = ?, " +
                "descripcion = ?, activa = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getUrl());
            ps.setString(3, r.getDia());
            ps.setObject(4, r.getHora());
            ps.setString(5, r.getDescripcion());
            ps.setBoolean(6, r.isActiva());
            ps.setLong(7, r.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar reunión id=" + r.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE reunion_meet SET activa = false WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar reunión id=" + id, e);
        }
    }

    private ReunionMeet mapear(ResultSet rs) throws SQLException {
        ReunionMeet r = new ReunionMeet();
        r.setId(rs.getLong("id"));
        r.setNombre(rs.getString("nombre"));
        r.setUrl(rs.getString("url"));
        r.setDia(rs.getString("dia"));
        r.setHora(rs.getObject("hora", LocalTime.class));
        r.setDescripcion(rs.getString("descripcion"));
        r.setActiva(rs.getBoolean("activa"));
        return r;
    }
}