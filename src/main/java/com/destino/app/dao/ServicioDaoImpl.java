package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Servicio;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioDaoImpl implements ServicioDao {
    @Override
    public List<Servicio> listarActivos() {
        String sql = "SELECT id, nombre, dia_semana, hora, activo FROM servicio WHERE activo = true ORDER BY id";
        List<Servicio> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setId(rs.getLong("id"));
                s.setNombre(rs.getString("nombre"));
                s.setDiaSemana(rs.getString("dia_semana"));
                s.setHora(rs.getObject("hora", LocalTime.class));
                s.setActivo(rs.getBoolean("activo"));
                lista.add(s);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar servicios", e);
        }
        return lista;
    }
}