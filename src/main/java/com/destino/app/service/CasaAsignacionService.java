package com.destino.app.service;

import com.destino.app.dao.CasaAsignacionDao;
import com.destino.app.dao.CasaAsignacionDaoImpl;
import com.destino.app.model.Persona;

import java.util.List;

public class CasaAsignacionService {

    private final CasaAsignacionDao dao;

    public CasaAsignacionService() {
        this.dao = new CasaAsignacionDaoImpl();
    }

    public List<Persona> lideres(Long casaId)  { return dao.listarLideres(casaId); }
    public List<Persona> miembros(Long casaId) { return dao.listarMiembros(casaId); }

    public void agregarLider(Long casaId, Long personaId)  { dao.agregarLider(casaId, personaId); }
    public void quitarLider(Long casaId, Long personaId)   { dao.quitarLider(casaId, personaId); }
    public void agregarMiembro(Long casaId, Long personaId){ dao.agregarMiembro(casaId, personaId); }
    public void quitarMiembro(Long casaId, Long personaId) { dao.quitarMiembro(casaId, personaId); }
}