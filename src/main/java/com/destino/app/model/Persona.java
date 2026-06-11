package com.destino.app.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Persona {
    private Long id;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String email;
    private String direccion;
    private String barrio;
    private String fotoUrl;
    private UUID qrCodigo;
    private String estado;
    private boolean consentimientoDatos;
    private OffsetDateTime fechaConsentimiento;
    private OffsetDateTime fechaRegistro;

    public Persona() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public void setNombres(final String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(final String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(final String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public void setNumeroDocumento(final String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(final LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return this.sexo;
    }

    public void setSexo(final String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(final String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(final String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return this.barrio;
    }

    public void setBarrio(final String barrio) {
        this.barrio = barrio;
    }

    public String getFotoUrl() {
        return this.fotoUrl;
    }

    public void setFotoUrl(final String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public UUID getQrCodigo() {
        return this.qrCodigo;
    }

    public void setQrCodigo(final UUID qrCodigo) {
        this.qrCodigo = qrCodigo;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public boolean isConsentimientoDatos() {
        return this.consentimientoDatos;
    }

    public void setConsentimientoDatos(final boolean consentimientoDatos) {
        this.consentimientoDatos = consentimientoDatos;
    }

    public OffsetDateTime getFechaConsentimiento() {
        return this.fechaConsentimiento;
    }

    public void setFechaConsentimiento(final OffsetDateTime fechaConsentimiento) {
        this.fechaConsentimiento = fechaConsentimiento;
    }

    public OffsetDateTime getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(final OffsetDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", sexo='" + sexo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", barrio='" + barrio + '\'' +
                ", fotoUrl='" + fotoUrl + '\'' +
                ", qrCodigo=" + qrCodigo +
                ", estado='" + estado + '\'' +
                ", consentimientoDatos=" + consentimientoDatos +
                ", fechaConsentimiento=" + fechaConsentimiento +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
