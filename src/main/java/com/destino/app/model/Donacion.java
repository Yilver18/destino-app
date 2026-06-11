package com.destino.app.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Donacion {
    private Long id;
    private Long donanteId;
    private String nombreDonante;
    private String tipo;            // FISICA | DIGITAL
    private BigDecimal monto;       // puede ser null en físicas (en especie)
    private String descripcion;
    private OffsetDateTime fecha;
    private Long pagoId;            // solo digitales (Hito 4)
    private Long registradoPor;

    private String donanteMostrar;  // transitorio: del JOIN (persona, externo o "Anónimo")

    public Donacion() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getDonanteId() {
        return this.donanteId;
    }

    public void setDonanteId(final Long donanteId) {
        this.donanteId = donanteId;
    }

    public String getNombreDonante() {
        return this.nombreDonante;
    }

    public void setNombreDonante(final String nombreDonante) {
        this.nombreDonante = nombreDonante;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(final BigDecimal monto) {
        this.monto = monto;
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

    public Long getPagoId() {
        return this.pagoId;
    }

    public void setPagoId(final Long pagoId) {
        this.pagoId = pagoId;
    }

    public Long getRegistradoPor() {
        return this.registradoPor;
    }

    public void setRegistradoPor(final Long registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getDonanteMostrar() {
        return this.donanteMostrar;
    }

    public void setDonanteMostrar(final String donanteMostrar) {
        this.donanteMostrar = donanteMostrar;
    }

    @Override
    public String toString() {
        return "Donacion{" +
                "id=" + id +
                ", donanteId=" + donanteId +
                ", nombreDonante='" + nombreDonante + '\'' +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", pagoId=" + pagoId +
                ", registradoPor=" + registradoPor +
                ", donanteMostrar='" + donanteMostrar + '\'' +
                '}';
    }
}