package com.destino.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Punto único de acceso al pool de conexiones PostgreSQL.
 * Patrón singleton: existe UN solo pool para toda la aplicación.
 */
public class Conexion {

    private static Conexion instancia;
    private final HikariDataSource dataSource;

    // Constructor privado: nadie crea Conexion desde fuera.
    private Conexion() {
        Properties props = cargarPropiedades();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.usuario"));
        config.setPassword(props.getProperty("db.password"));
        config.setMaximumPoolSize(
                Integer.parseInt(props.getProperty("db.poolMaximo", "10")));
        config.setPoolName("DestinoPool");

        this.dataSource = new HikariDataSource(config);
    }

    // synchronized: evita que dos hilos creen dos pools a la vez.
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Entrega una conexión LIBRE del pool.
     * Quien la pide DEBE cerrarla (try-with-resources): al cerrarla,
     * no se destruye, vuelve al pool para reutilizarse.
     */
    public Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }

    // Cierra el pool completo. Se llama una vez, al salir de la app.
    public void cerrar() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró db.properties en resources");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar db.properties", e);
        }
        return props;
    }
}