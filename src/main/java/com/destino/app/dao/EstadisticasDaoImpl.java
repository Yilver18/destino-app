package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasDaoImpl implements EstadisticasDao {

    @Override
    public List<String[]> asistenciaPorServicio() {
        String sql = "SELECT s.nombre, COUNT(a.id) AS total " +
                "FROM servicio s " +
                "LEFT JOIN sesion se ON se.servicio_id = s.id " +
                "LEFT JOIN asistencia a ON a.sesion_id = se.id " +
                "GROUP BY s.nombre ORDER BY total DESC";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{ rs.getString("nombre"), String.valueOf(rs.getInt("total")) });
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en estadística por servicio", e);
        }
        return filas;
    }

    @Override
    public List<String[]> asistenciaPorSesion(int limite) {
        String sql = "SELECT se.fecha, se.contexto, " +
                "COALESCE(sv.nombre, c.nombre, ac.nombre) AS ref, COUNT(a.id) AS total " +
                "FROM sesion se " +
                "LEFT JOIN servicio sv ON sv.id = se.servicio_id " +
                "LEFT JOIN casa_destino c ON c.id = se.casa_id " +
                "LEFT JOIN academia ac ON ac.id = se.academia_id " +
                "LEFT JOIN asistencia a ON a.sesion_id = se.id " +
                "GROUP BY se.id, se.fecha, se.contexto, ref " +
                "ORDER BY se.fecha DESC LIMIT ?";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String etiqueta = rs.getString("fecha") + " (" + rs.getString("contexto") + " " +
                            rs.getString("ref") + ")";
                    filas.add(new String[]{ etiqueta, String.valueOf(rs.getInt("total")) });
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en estadística por sesión", e);
        }
        return filas;
    }
    @Override
    public List<String[]> personasPorMes() {
        String sql = "SELECT to_char(fecha_registro, 'YYYY-MM') AS mes, COUNT(*) AS total " +
                "FROM persona GROUP BY mes ORDER BY mes";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{ rs.getString("mes"), String.valueOf(rs.getInt("total")) });
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en personas por mes", e);
        }
        return filas;
    }

    @Override
    public List<String[]> personasPorEstado() {
        String sql = "SELECT estado, COUNT(*) AS total FROM persona GROUP BY estado ORDER BY estado";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{ rs.getString("estado"), String.valueOf(rs.getInt("total")) });
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en personas por estado", e);
        }
        return filas;
    }
    @Override
    public List<String[]> donacionesPorTipo() {
        String sql = "SELECT tipo, COUNT(*) AS cantidad, COALESCE(SUM(monto), 0) AS total " +
                "FROM donacion GROUP BY tipo ORDER BY tipo";
        List<String[]> filas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{
                        rs.getString("tipo"),
                        String.valueOf(rs.getInt("cantidad")),
                        rs.getBigDecimal("total").toPlainString()
                });
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en donaciones por tipo", e);
        }
        return filas;
    }

    @Override
    public String[] distribucionTotales() {
        String sql = "SELECT COUNT(*) AS entregas, COALESCE(SUM(cantidad), 0) AS mercados " +
                "FROM distribucion_mercado";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new String[]{ String.valueOf(rs.getInt("entregas")),
                        String.valueOf(rs.getInt("mercados")) };
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error en totales de distribución", e);
        }
        return new String[]{ "0", "0" };
    }
}