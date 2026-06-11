package com.destino.app.model;

import java.math.BigDecimal;

public class Devocional {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer mes;
    private Integer anio;
    private BigDecimal precio;
    private String imagenUrl;
    private boolean destacado;
    private boolean activo;

    public Devocional() {
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

    public Integer getMes() {
        return this.mes;
    }

    public void setMes(final Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return this.anio;
    }

    public void setAnio(final Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public void setPrecio(final BigDecimal precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isDestacado() {
        return this.destacado;
    }

    public void setDestacado(final boolean destacado) {
        this.destacado = destacado;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Devocional{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", mes=" + mes +
                ", anio=" + anio +
                ", precio=" + precio +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", destacado=" + destacado +
                ", activo=" + activo +
                '}';
    }
}