package com.destino.app.model;

public class Pastor {
    private Long id;
    private String nombre;
    private String cargo;
    private String fotoUrl;
    private String biografia;
    private int orden;

    public Pastor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(final String cargo) {
        this.cargo = cargo;
    }

    public String getFotoUrl() {
        return this.fotoUrl;
    }

    public void setFotoUrl(final String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getBiografia() {
        return this.biografia;
    }

    public void setBiografia(final String biografia) {
        this.biografia = biografia;
    }

    public int getOrden() {
        return this.orden;
    }

    public void setOrden(final int orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "Pastor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                ", fotoUrl='" + fotoUrl + '\'' +
                ", biografia='" + biografia + '\'' +
                ", orden=" + orden +
                '}';
    }
}