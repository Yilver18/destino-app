package com.destino.app.model;

import java.time.OffsetDateTime;

public class Asistencia {
    private Long id;
    private Long sesionId;
    private Long personaId;
    private OffsetDateTime horaRegistro;
    private String metodo;          // QR | MANUAL
    private boolean sincronizado;

    private String nombrePersona;   // transitorio (del JOIN)

    public Asistencia() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getSesionId() {
        return this.sesionId;
    }

    public void setSesionId(final Long sesionId) {
        this.sesionId = sesionId;
    }

    public Long getPersonaId() {
        return this.personaId;
    }

    public void setPersonaId(final Long personaId) {
        this.personaId = personaId;
    }


    public OffsetDateTime getHoraRegistro() {
        return this.horaRegistro;
    }

    public void setHoraRegistro(final OffsetDateTime horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getMetodo() {
        return this.metodo;
    }

    public void setMetodo(final String metodo) {
        this.metodo = metodo;
    }

    public boolean isSincronizado() {
        return this.sincronizado;
    }

    public void setSincronizado(final boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public void setNombrePersona(final String nombrePersona) {
        this.nombrePersona = nombrePersona;
    } @Override
    public String toString() {
        return "Asistencia{" +
                "id=" + id +
                ", sesionId=" + sesionId +
                ", personaId=" + personaId +
                ", horaRegistro=" + horaRegistro +
                ", metodo='" + metodo + '\'' +
                ", sincronizado=" + sincronizado +
                ", nombrePersona='" + nombrePersona + '\'' +
                '}';
    }

}