package com.destino.app.model;

import java.time.LocalDate;

public class AsignacionVoluntario {
    private Long id;
    private Long personaId;
    private Long ministerioId;
    private Long servicioId;
    private LocalDate fechaServicio;
    private String estado;
    private boolean notificado;

    private String nombrePersona;     // transitorios (del JOIN)
    private String nombreMinisterio;
    private String nombreServicio;

    public AsignacionVoluntario() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getPersonaId() {
        return this.personaId;
    }

    public void setPersonaId(final Long personaId) {
        this.personaId = personaId;
    }

    public Long getMinisterioId() {
        return this.ministerioId;
    }

    public void setMinisterioId(final Long ministerioId) {
        this.ministerioId = ministerioId;
    }

    public Long getServicioId() {
        return this.servicioId;
    }

    public void setServicioId(final Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDate getFechaServicio() {
        return this.fechaServicio;
    }

    public void setFechaServicio(final LocalDate fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public boolean isNotificado() {
        return this.notificado;
    }

    public void setNotificado(final boolean notificado) {
        this.notificado = notificado;
    }

    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public void setNombrePersona(final String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombreMinisterio() {
        return this.nombreMinisterio;
    }

    public void setNombreMinisterio(final String nombreMinisterio) {
        this.nombreMinisterio = nombreMinisterio;
    }

    public String getNombreServicio() {
        return this.nombreServicio;
    }

    public void setNombreServicio(final String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    @Override
    public String toString() {
        return "AsignacionVoluntario{" +
                "id=" + id +
                ", personaId=" + personaId +
                ", ministerioId=" + ministerioId +
                ", servicioId=" + servicioId +
                ", fechaServicio=" + fechaServicio +
                ", estado='" + estado + '\'' +
                ", notificado=" + notificado +
                ", nombrePersona='" + nombrePersona + '\'' +
                ", nombreMinisterio='" + nombreMinisterio + '\'' +
                ", nombreServicio='" + nombreServicio + '\'' +
                '}';
    }
}