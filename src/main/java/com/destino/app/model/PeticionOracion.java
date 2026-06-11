package com.destino.app.model;

import java.time.OffsetDateTime;

public class PeticionOracion {
    private Long id;
    private Long solicitanteId;
    private String descripcion;
    private OffsetDateTime fecha;
    private String estado;
    private OffsetDateTime fechaRespondida;
    private boolean privada;

    private String nombreSolicitante;   // transitorio: solo para mostrar (viene del JOIN)

    public PeticionOracion() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getSolicitanteId() {
        return this.solicitanteId;
    }

    public void setSolicitanteId(final Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public OffsetDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(final OffsetDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public OffsetDateTime getFechaRespondida() {
        return this.fechaRespondida;
    }

    public void setFechaRespondida(final OffsetDateTime fechaRespondida) {
        this.fechaRespondida = fechaRespondida;
    }

    public boolean isPrivada() {
        return this.privada;
    }

    public void setPrivada(final boolean privada) {
        this.privada = privada;
    }

    public String getNombreSolicitante() {
        return this.nombreSolicitante;
    }

    public void setNombreSolicitante(final String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    @Override
    public String toString() {
        return "PeticionOracion{" +
                "id=" + id +
                ", solicitanteId=" + solicitanteId +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", fechaRespondida=" + fechaRespondida +
                ", privada=" + privada +
                ", nombreSolicitante='" + nombreSolicitante + '\'' +
                '}';
    }
}