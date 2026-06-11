package com.destino.app.model;

import java.time.LocalDate;

public class SesionAsistencia {
    private Long id;
    private LocalDate fecha;
    private String contexto;       // SERVICIO | CASA | ACADEMIA
    private Long servicioId;
    private Long casaId;
    private Long academiaId;
    private String descripcion;

    private String referenciaMostrar;  // transitorio: nombre del servicio/casa/academia

    public SesionAsistencia() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getContexto() {
        return this.contexto;
    }

    public void setContexto(final String contexto) {
        this.contexto = contexto;
    }

    public Long getServicioId() {
        return this.servicioId;
    }

    public void setServicioId(final Long servicioId) {
        this.servicioId = servicioId;
    }

    public Long getCasaId() {
        return this.casaId;
    }

    public void setCasaId(final Long casaId) {
        this.casaId = casaId;
    }

    public Long getAcademiaId() {
        return this.academiaId;
    }

    public void setAcademiaId(final Long academiaId) {
        this.academiaId = academiaId;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferenciaMostrar() {
        return this.referenciaMostrar;
    }

    public void setReferenciaMostrar(final String referenciaMostrar) {
        this.referenciaMostrar = referenciaMostrar;
    }

    @Override
    public String toString() {
        return "SesionAsistencia{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", contexto='" + contexto + '\'' +
                ", servicioId=" + servicioId +
                ", casaId=" + casaId +
                ", academiaId=" + academiaId +
                ", descripcion='" + descripcion + '\'' +
                ", referenciaMostrar='" + referenciaMostrar + '\'' +
                '}';
    }
}