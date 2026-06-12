package com.destino.app.model;

public class Configuracion {
    private String clave;
    private String valor;
    private String descripcion;

    public Configuracion() {
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(final String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(final String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "clave='" + clave + '\'' +
                ", valor='" + valor + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}