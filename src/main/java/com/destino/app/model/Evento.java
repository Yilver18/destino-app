package com.destino.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String imagenUrl;
    private boolean publicado;
    private Long creadoPor;

    public Evento() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public void setHora(final LocalTime hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return this.lugar;
    }

    public void setLugar(final String lugar) {
        this.lugar = lugar;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isPublicado() {
        return this.publicado;
    }

    public void setPublicado(final boolean publicado) {
        this.publicado = publicado;
    }

    public Long getCreadoPor() {
        return this.creadoPor;
    }

    public void setCreadoPor(final Long creadoPor) {
        this.creadoPor = creadoPor;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", lugar='" + lugar + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", publicado=" + publicado +
                ", creadoPor=" + creadoPor +
                '}';
    }
}