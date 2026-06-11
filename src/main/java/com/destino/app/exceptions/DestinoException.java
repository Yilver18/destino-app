package com.destino.app.exceptions;

/**
 * Excepción base de Destino App. Todas las excepciones propias
 * heredan de aquí. Es unchecked (extends RuntimeException) a propósito.
 */
public class DestinoException extends RuntimeException {

    public DestinoException(String mensaje) {
        super(mensaje);
    }

    public DestinoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}