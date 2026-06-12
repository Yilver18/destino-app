package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;

import java.sql.*;

public class ReporteIaDaoImpl implements ReporteIaDao {
    @Override
    public void guardar(String tipo, String contenido, Long generadoPor) {
        String sql = "INSERT INTO reporte_ia (tipo, contenido, generado_por) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setString(2, contenido);
            ps.setObject(3, generadoPor);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar reporte IA", e);
        }
    }
}