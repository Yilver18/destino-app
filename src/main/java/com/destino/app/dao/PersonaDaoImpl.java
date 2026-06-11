package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Persona;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonaDaoImpl implements PersonaDao {

    // Lista de columnas en un solo sitio (DRY): se reutiliza en los SELECT.
    private static final String COLUMNAS =
            "id, nombres, apellidos, tipo_documento, numero_documento, fecha_nacimiento, " +
                    "sexo, telefono, email, direccion, barrio, foto_url, qr_codigo, estado, " +
                    "consentimiento_datos, fecha_consentimiento, fecha_registro";

    @Override
    public List<Persona> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM persona ORDER BY apellidos, nombres";
        List<Persona> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar personas", e);
        }
        return lista;
    }

    @Override
    public Persona buscarPorId(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM persona WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar persona id=" + id, e);
        }
        return null;
    }

    @Override
    public Persona buscarPorDocumento(String numeroDocumento) {
        String sql = "SELECT " + COLUMNAS + " FROM persona WHERE numero_documento = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar persona documento=" + numeroDocumento, e);
        }
        return null;
    }

    @Override
    public Persona guardar(Persona p) {
        String sql = "INSERT INTO persona " +
                "(nombres, apellidos, tipo_documento, numero_documento, fecha_nacimiento, sexo, " +
                " telefono, email, direccion, barrio, foto_url, estado, consentimiento_datos, fecha_consentimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getTipoDocumento());
            ps.setString(4, p.getNumeroDocumento());
            ps.setObject(5, p.getFechaNacimiento());        // LocalDate o null
            ps.setString(6, p.getSexo());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getDireccion());
            ps.setString(10, p.getBarrio());
            ps.setString(11, p.getFotoUrl());
            ps.setString(12, p.getEstado() != null ? p.getEstado() : "nueva");
            ps.setBoolean(13, p.isConsentimientoDatos());
            ps.setObject(14, p.getFechaConsentimiento());   // LocalDateTime o null
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getLong("id"));
                    p.setQrCodigo(rs.getObject("qr_codigo", java.util.UUID.class));
                    p.setFechaRegistro(rs.getObject("fecha_registro", OffsetDateTime.class));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar persona", e);
        }
        return p;
    }

    @Override
    public boolean actualizar(Persona p) {
        String sql = "UPDATE persona SET " +
                "nombres = ?, apellidos = ?, tipo_documento = ?, numero_documento = ?, " +
                "fecha_nacimiento = ?, sexo = ?, telefono = ?, email = ?, direccion = ?, " +
                "barrio = ?, foto_url = ?, estado = ?, consentimiento_datos = ?, fecha_consentimiento = ? " +
                "WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getTipoDocumento());
            ps.setString(4, p.getNumeroDocumento());
            ps.setObject(5, p.getFechaNacimiento());
            ps.setString(6, p.getSexo());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getDireccion());
            ps.setString(10, p.getBarrio());
            ps.setString(11, p.getFotoUrl());
            ps.setString(12, p.getEstado());
            ps.setBoolean(13, p.isConsentimientoDatos());
            ps.setObject(14, p.getFechaConsentimiento());
            ps.setLong(15, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar persona id=" + p.getId(), e);
        }
    }

    @Override
    public boolean inactivar(Long id) {
        String sql = "UPDATE persona SET estado = 'inactiva' WHERE id = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al inactivar persona id=" + id, e);
        }
    }

    private Persona mapear(ResultSet rs) throws SQLException {
        Persona p = new Persona();
        p.setId(rs.getLong("id"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setTipoDocumento(rs.getString("tipo_documento"));
        p.setNumeroDocumento(rs.getString("numero_documento"));
        p.setFechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class));
        p.setSexo(rs.getString("sexo"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        p.setBarrio(rs.getString("barrio"));
        p.setFotoUrl(rs.getString("foto_url"));
        p.setQrCodigo(rs.getObject("qr_codigo", UUID.class));
        p.setEstado(rs.getString("estado"));
        p.setConsentimientoDatos(rs.getBoolean("consentimiento_datos"));
        p.setFechaConsentimiento(rs.getObject("fecha_consentimiento", OffsetDateTime.class));
        p.setFechaRegistro(rs.getObject("fecha_registro", OffsetDateTime.class));
        return p;
    }
}