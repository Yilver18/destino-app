package com.destino.app.model;

import java.time.LocalTime;

public class CasaDestino {
    private Long id;
    private String nombre;
    private String direccion;
    private String sector;
    private String diaReunion;
    private LocalTime horaReunion;
    private boolean activa;

    public CasaDestino() {
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

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(final String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return this.sector;
    }

    public void setSector(final String sector) {
        this.sector = sector;
    }

    public String getDiaReunion() {
        return this.diaReunion;
    }

    public void setDiaReunion(final String diaReunion) {
        this.diaReunion = diaReunion;
    }

    public LocalTime getHoraReunion() {
        return this.horaReunion;
    }

    public void setHoraReunion(final LocalTime horaReunion) {
        this.horaReunion = horaReunion;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void setActiva(final boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "CasaDestino{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", sector='" + sector + '\'' +
                ", diaReunion='" + diaReunion + '\'' +
                ", horaReunion=" + horaReunion +
                ", activa=" + activa +
                '}';
    }
}