package com.destino.app.model;

public class Ministerio {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long coordinadorId;
    private boolean activo;

    public Ministerio() {
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

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCoordinadorId() {
        return this.coordinadorId;
    }

    public void setCoordinadorId(final Long coordinadorId) {
        this.coordinadorId = coordinadorId;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Ministerio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", coordinadorId=" + coordinadorId +
                ", activo=" + activo +
                '}';
    }
}