package com.destino.app.model;

import java.math.BigDecimal;

public class Academia {
    private Long id;
    private String nombre;
    private String descripcion;
    private String mision;
    private String vision;
    private String logros;
    private String imagenUrl;
    private BigDecimal precioInscripcion;
    private Long coordinadorId;
    private boolean activa;

    public Academia() {
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

    public String getMision() {
        return this.mision;
    }

    public void setMision(final String mision) {
        this.mision = mision;
    }

    public String getVision() {
        return this.vision;
    }

    public void setVision(final String vision) {
        this.vision = vision;
    }

    public String getLogros() {
        return this.logros;
    }

    public void setLogros(final String logros) {
        this.logros = logros;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public BigDecimal getPrecioInscripcion() {
        return this.precioInscripcion;
    }

    public void setPrecioInscripcion(final BigDecimal precioInscripcion) {
        this.precioInscripcion = precioInscripcion;
    }

    public Long getCoordinadorId() {
        return this.coordinadorId;
    }

    public void setCoordinadorId(final Long coordinadorId) {
        this.coordinadorId = coordinadorId;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void setActiva(final boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Academia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", mision='" + mision + '\'' +
                ", vision='" + vision + '\'' +
                ", logros='" + logros + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", precioInscripcion=" + precioInscripcion +
                ", coordinadorId=" + coordinadorId +
                ", activa=" + activa +
                '}';
    }
}