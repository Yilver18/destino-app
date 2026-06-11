package com.destino.app.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Predica {
    private Long id;
    private String titulo;
    private String youtubeUrl;
    private LocalDate fechaPublicacion;
    private Long creativoId;
    private String resumenIa;            // Hito 5 (IA)
    private OffsetDateTime fechaResumen; // Hito 5
    private boolean esActual;

    private String nombreCreativo;       // transitorio (del LEFT JOIN)

    public Predica() {
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

    public String getYoutubeUrl() {
        return this.youtubeUrl;
    }

    public void setYoutubeUrl(final String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public LocalDate getFechaPublicacion() {
        return this.fechaPublicacion;
    }

    public void setFechaPublicacion(final LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Long getCreativoId() {
        return this.creativoId;
    }

    public void setCreativoId(final Long creativoId) {
        this.creativoId = creativoId;
    }

    public String getResumenIa() {
        return this.resumenIa;
    }

    public void setResumenIa(final String resumenIa) {
        this.resumenIa = resumenIa;
    }

    public OffsetDateTime getFechaResumen() {
        return this.fechaResumen;
    }

    public void setFechaResumen(final OffsetDateTime fechaResumen) {
        this.fechaResumen = fechaResumen;
    }

    public boolean isEsActual() {
        return this.esActual;
    }

    public void setEsActual(final boolean esActual) {
        this.esActual = esActual;
    }

    public String getNombreCreativo() {
        return this.nombreCreativo;
    }

    public void setNombreCreativo(final String nombreCreativo) {
        this.nombreCreativo = nombreCreativo;
    }

    @Override
    public String toString() {
        return "Predica{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", creativoId=" + creativoId +
                ", resumenIa='" + resumenIa + '\'' +
                ", fechaResumen=" + fechaResumen +
                ", esActual=" + esActual +
                ", nombreCreativo='" + nombreCreativo + '\'' +
                '}';
    }
}