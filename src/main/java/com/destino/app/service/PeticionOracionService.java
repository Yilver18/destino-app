package com.destino.app.service;

import com.destino.app.dao.PeticionOracionDao;
import com.destino.app.dao.PeticionOracionDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.PeticionOracion;

import java.util.List;

public class PeticionOracionService {

    private final PeticionOracionDao dao;

    public PeticionOracionService() {
        this.dao = new PeticionOracionDaoImpl();
    }

    public List<PeticionOracion> listar() {
        return dao.listar();
    }

    public PeticionOracion registrar(PeticionOracion p) {
        if (p.getSolicitanteId() == null) {
            throw new ValidacionException("Debes elegir el solicitante.");
        }
        if (p.getDescripcion() == null || p.getDescripcion().isBlank()) {
            throw new ValidacionException("La petición no puede estar vacía.");
        }
        return dao.guardar(p);
    }

    public boolean marcarRespondida(Long id) {
        return dao.marcarRespondida(id);
    }
}