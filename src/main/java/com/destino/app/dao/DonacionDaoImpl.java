package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Donacion;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonacionDaoImpl implements DonacionDao {

    @Override
    public List<Donacion> listar() {
        String sql = "SELECT d.id, d.donante_id, d.nombre_donante, d.tipo, d.monto, d.descripcion, " +
                "d.fecha, d.pago_id, d.registrado_por, " +
                "COALESCE(p.nombres || ' ' || p.apellidos, d.nombre_donante, 'Anónimo') AS donante_mostrar " +
                "FROM donacion d " +
                "LEFT JOIN persona p ON p.id = d.donante_id " +
                "ORDER BY d.fecha DESC";
        List<Donacion> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar donaciones", e);
        }
        return lista;
    }

    @Override
    public Donacion guardar(Donacion d) {
        String sql = "INSERT INTO donacion (donante_id, nombre_donante, tipo, monto, descripcion, registrado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, d.getDonanteId());        // Long o null
            ps.setString(2, d.getNombreDonante());
            ps.setString(3, d.getTipo());
            ps.setBigDecimal(4, d.getMonto());        // BigDecimal o null
            ps.setString(5, d.getDescripcion());
            ps.setObject(6, d.getRegistradoPor());    // Long o null
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar donación", e);
        }
        return d;
    }

    private Donacion mapear(ResultSet rs) throws SQLException {
        Donacion d = new Donacion();
        d.setId(rs.getLong("id"));
        d.setDonanteId(rs.getObject("donante_id", Long.class));
        d.setNombreDonante(rs.getString("nombre_donante"));
        d.setTipo(rs.getString("tipo"));
        d.setMonto(rs.getBigDecimal("monto"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setFecha(rs.getObject("fecha", OffsetDateTime.class));
        d.setPagoId(rs.getObject("pago_id", Long.class));
        d.setRegistradoPor(rs.getObject("registrado_por", Long.class));
        d.setDonanteMostrar(rs.getString("donante_mostrar"));
        return d;
    }
}