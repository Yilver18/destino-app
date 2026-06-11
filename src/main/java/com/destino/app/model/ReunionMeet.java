package com.destino.app.model;

import java.time.LocalTime;

public class ReunionMeet {
    private Long id;
    private String nombre;
    private String url;
    private String dia;
    private LocalTime hora;
    private String descripcion;
    private boolean activa;

    public ReunionMeet() {
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDia() {
        return this.dia;
    }

    public void setDia(final String dia) {
        this.dia = dia;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public void setHora(final LocalTime hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void setActiva(final boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "ReunionMeet{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", url='" + url + '\'' +
                ", dia='" + dia + '\'' +
                ", hora=" + hora +
                ", descripcion='" + descripcion + '\'' +
                ", activa=" + activa +
                '}';
    }
}