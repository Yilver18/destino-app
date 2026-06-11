package com.destino.app.model;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private Long personaId;
    private String nombreUsuario;
    private String hashPassword;
    private boolean activo;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
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

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public void setNombreUsuario(final String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getHashPassword() {
        return this.hashPassword;
    }

    public void setHashPassword(final String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getUltimoAcceso() {
        return this.ultimoAcceso;
    }

    public void setUltimoAcceso(final LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", personaId=" + personaId +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", activo=" + activo +
                ", ultimoAcceso=" + ultimoAcceso +
                '}';
    }
}
