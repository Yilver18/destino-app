package com.destino.app.model;

import java.time.OffsetDateTime;

public class MensajeChat {
    private Long id;
    private Long conversacionId;
    private Long remitenteId;
    private String contenido;
    private OffsetDateTime fechaEnvio;
    private String nombreRemitente;   // transitorio (del JOIN)

    public MensajeChat() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getConversacionId() {
        return this.conversacionId;
    }

    public void setConversacionId(final Long conversacionId) {
        this.conversacionId = conversacionId;
    }

    public Long getRemitenteId() {
        return this.remitenteId;
    }

    public void setRemitenteId(final Long remitenteId) {
        this.remitenteId = remitenteId;
    }

    public String getContenido() {
        return this.contenido;
    }

    public void setContenido(final String contenido) {
        this.contenido = contenido;
    }

    public OffsetDateTime getFechaEnvio() {
        return this.fechaEnvio;
    }

    public void setFechaEnvio(final OffsetDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getNombreRemitente() {
        return this.nombreRemitente;
    }

    public void setNombreRemitente(final String nombreRemitente) {
        this.nombreRemitente = nombreRemitente;
    }

    @Override
    public String toString() {
        return "MensajeChat{" +
                "id=" + id +
                ", conversacionId=" + conversacionId +
                ", remitenteId=" + remitenteId +
                ", contenido='" + contenido + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", nombreRemitente='" + nombreRemitente + '\'' +
                '}';
    }
}