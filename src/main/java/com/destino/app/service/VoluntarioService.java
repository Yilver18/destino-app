package com.destino.app.service;

import com.destino.app.dao.AsignacionVoluntarioDao;
import com.destino.app.dao.AsignacionVoluntarioDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.AsignacionVoluntario;

import java.util.List;

public class VoluntarioService {

    private final AsignacionVoluntarioDao dao;

    public VoluntarioService() {
        this.dao = new AsignacionVoluntarioDaoImpl();
    }

    public List<AsignacionVoluntario> listar() {
        return dao.listar();
    }

    public void asignar(AsignacionVoluntario a) {
        if (a.getPersonaId() == null || a.getMinisterioId() == null
                || a.getServicioId() == null || a.getFechaServicio() == null) {
            throw new ValidacionException("Completa persona, ministerio, servicio y fecha.");
        }
        boolean creado = dao.asignar(a);
        if (!creado) {
            throw new ValidacionException("Esa persona ya está asignada a ese ministerio en ese servicio y fecha.");
        }
    }

    public void confirmar(Long id) { dao.actualizarEstado(id, "CONFIRMADO"); }
    public void cancelar(Long id)  { dao.actualizarEstado(id, "CANCELADO"); }
    public void eliminar(Long id)  { dao.eliminar(id); }
}