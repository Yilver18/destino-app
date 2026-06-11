package com.destino.app.model;

import java.time.OffsetDateTime;

public class NotaSeguimiento {
    private Long id;
    private Long personaId;     // a quién se le hace seguimiento
    private Long autorId;       // quién la escribió (líder)
    private String tipo;        // LLAMADA | VISITA | SITUACION | OTRO
    private String descripcion;
    private OffsetDateTime fecha;

    private String nombreAutor; // transitorio (del JOIN)

    public NotaSeguimiento() {
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

    public Long getAutorId() {
        return this.autorId;
    }

    public void setAutorId(final Long autorId) {
        this.autorId = autorId;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
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

    public String getNombreAutor() {
        return this.nombreAutor;
    }

    public void setNombreAutor(final String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    @Override
    public String toString() {
        return "NotaSeguimiento{" +
                "id=" + id +
                ", personaId=" + personaId +
                ", autorId=" + autorId +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", nombreAutor='" + nombreAutor + '\'' +
                '}';
    }
}