package com.destino.app.dao;

import com.destino.app.config.Conexion;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.model.Configuracion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionDaoImpl implements ConfiguracionDao {

    @Override
    public List<Configuracion> listar() {
        String sql = "SELECT clave, valor, descripcion FROM configuracion ORDER BY clave";
        List<Configuracion> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Configuracion c = new Configuracion();
                c.setClave(rs.getString("clave"));
                c.setValor(rs.getString("valor"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar configuración", e);
        }
        return lista;
    }

    @Override
    public void guardar(Configuracion c) {
        // UPSERT: si la clave ya existe, actualiza; si no, inserta.
        String sql = "INSERT INTO configuracion (clave, valor, descripcion) VALUES (?, ?, ?) " +
                "ON CONFLICT (clave) DO UPDATE SET valor = EXCLUDED.valor, descripcion = EXCLUDED.descripcion";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getClave());
            ps.setString(2, c.getValor());
            ps.setString(3, c.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar configuración", e);
        }
    }

    @Override
    public void eliminar(String clave) {
        String sql = "DELETE FROM configuracion WHERE clave = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar configuración", e);
        }
    }

    @Override
    public String buscarValor(String clave) {
        String sql = "SELECT valor FROM configuracion WHERE clave = ?";
        try (Connection con = Conexion.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("valor");
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar configuración " + clave, e);
        }
        return null;
    }
}