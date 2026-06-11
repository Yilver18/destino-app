package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.DistribucionMercado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DistribucionMercadoDaoImpl implements DistribucionMercadoDao {

    @Override
    public List<DistribucionMercado> listar() {
        String sql = "SELECT dm.id, dm.beneficiario_id, dm.nombre_beneficiario, dm.fecha, dm.cantidad, " +
                "dm.descripcion, dm.responsable_id, " +
                "COALESCE(p.nombres || ' ' || p.apellidos, dm.nombre_beneficiario, 'Sin nombre') AS beneficiario_mostrar " +
                "FROM distribucion_mercado dm " +
                "LEFT JOIN persona p ON p.id = dm.beneficiario_id " +
                "ORDER BY dm.fecha DESC";
        List<DistribucionMercado> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar distribuciones", e);
        }
        return lista;
    }

    @Override
    public DistribucionMercado guardar(DistribucionMercado d) {
        String sql = "INSERT INTO distribucion_mercado " +
                "(beneficiario_id, nombre_beneficiario, fecha, cantidad, descripcion, responsable_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, d.getBeneficiarioId());
            ps.setString(2, d.getNombreBeneficiario());
            ps.setObject(3, d.getFecha() != null ? d.getFecha() : LocalDate.now());
            ps.setInt(4, d.getCantidad());
            ps.setString(5, d.getDescripcion());
            ps.setObject(6, d.getResponsableId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar distribución", e);
        }
        return d;
    }

    private DistribucionMercado mapear(ResultSet rs) throws SQLException {
        DistribucionMercado d = new DistribucionMercado();
        d.setId(rs.getLong("id"));
        d.setBeneficiarioId(rs.getObject("beneficiario_id", Long.class));
        d.setNombreBeneficiario(rs.getString("nombre_beneficiario"));
        d.setFecha(rs.getObject("fecha", LocalDate.class));
        d.setCantidad(rs.getInt("cantidad"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setResponsableId(rs.getObject("responsable_id", Long.class));
        d.setBeneficiarioMostrar(rs.getString("beneficiario_mostrar"));
        return d;
    }
}