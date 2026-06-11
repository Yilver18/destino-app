package com.destino.app.service;

import com.destino.app.dao.AsistenciaDao;
import com.destino.app.dao.AsistenciaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Asistencia;

import java.util.List;

public class AsistenciaService {

    private final AsistenciaDao dao;

    public AsistenciaService() {
        this.dao = new AsistenciaDaoImpl();
    }

    public List<Asistencia> listarPorSesion(Long sesionId) {
        return dao.listarPorSesion(sesionId);
    }

    public void registrarManual(Long sesionId, Long personaId) {
        if (sesionId == null) throw new ValidacionException("Elige una sesión.");
        if (personaId == null) throw new ValidacionException("Elige una persona.");
        boolean creado = dao.registrar(sesionId, personaId, "MANUAL");
        if (!creado) {
            throw new ValidacionException("Esa persona ya tiene asistencia en esta sesión.");
        }
    }

    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}