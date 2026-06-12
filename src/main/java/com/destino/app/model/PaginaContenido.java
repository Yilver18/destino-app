package com.destino.app.model;

import java.time.OffsetDateTime;

public class PaginaContenido {
    private Long id;
    private String clave;
    private String titulo;
    private String cuerpo;
    private String imagenUrl;
    private Long actualizadoPor;
    private OffsetDateTime fechaActualizacion;

    public PaginaContenido() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(final String clave) {
        this.clave = clave;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return this.cuerpo;
    }

    public void setCuerpo(final String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Long getActualizadoPor() {
        return this.actualizadoPor;
    }

    public void setActualizadoPor(final Long actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public OffsetDateTime getFechaActualizacion() {
        return this.fechaActualizacion;
    }

    public void setFechaActualizacion(final OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "PaginaContenido{" +
                "id=" + id +
                ", clave='" + clave + '\'' +
                ", titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", actualizadoPor=" + actualizadoPor +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}