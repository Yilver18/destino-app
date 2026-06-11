package com.destino.app.model;

import java.time.LocalDate;

public class DistribucionMercado {
    private Long id;
    private Long beneficiarioId;
    private String nombreBeneficiario;
    private LocalDate fecha;
    private int cantidad;
    private String descripcion;
    private Long responsableId;

    private String beneficiarioMostrar;  // transitorio (del LEFT JOIN + COALESCE)

    public DistribucionMercado() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getBeneficiarioId() {
        return this.beneficiarioId;
    }

    public void setBeneficiarioId(final Long beneficiarioId) {
        this.beneficiarioId = beneficiarioId;
    }

    public String getNombreBeneficiario() {
        return this.nombreBeneficiario;
    }

    public void setNombreBeneficiario(final String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(final int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getResponsableId() {
        return this.responsableId;
    }

    public void setResponsableId(final Long responsableId) {
        this.responsableId = responsableId;
    }

    public String getBeneficiarioMostrar() {
        return this.beneficiarioMostrar;
    }

    public void setBeneficiarioMostrar(final String beneficiarioMostrar) {
        this.beneficiarioMostrar = beneficiarioMostrar;
    }

    @Override
    public String toString() {
        return "DistribucionMercado{" +
                "id=" + id +
                ", beneficiarioId=" + beneficiarioId +
                ", nombreBeneficiario='" + nombreBeneficiario + '\'' +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", descripcion='" + descripcion + '\'' +
                ", responsableId=" + responsableId +
                ", beneficiarioMostrar='" + beneficiarioMostrar + '\'' +
                '}';
    }
}