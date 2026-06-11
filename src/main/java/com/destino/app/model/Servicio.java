package com.destino.app.model;

import java.time.LocalTime;

public class Servicio {
    private Long id;
    private String nombre;
    private String diaSemana;
    private LocalTime hora;
    private boolean activo;

    public Servicio() {
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

    public String getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(final String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public void setHora(final LocalTime hora) {
        this.hora = hora;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", diaSemana='" + diaSemana + '\'' +
                ", hora=" + hora +
                ", activo=" + activo +
                '}';
    }
}