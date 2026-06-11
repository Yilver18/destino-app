package com.destino.app.exceptions;

/**
 * Error en la capa de acceso a datos (DAO / JDBC).
 * Envuelve las SQLException para que las capas superiores
 * (service, view) no dependan de detalles de JDBC.
 */
public class AccesoDatosException extends DestinoException {

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}