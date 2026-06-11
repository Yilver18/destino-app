package com.destino.app.service;

import com.destino.app.dao.ReunionMeetDao;
import com.destino.app.dao.ReunionMeetDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.ReunionMeet;

import java.util.List;

public class ReunionMeetService {

    private final ReunionMeetDao dao;

    public ReunionMeetService() {
        this.dao = new ReunionMeetDaoImpl();
    }

    public List<ReunionMeet> listar() {
        return dao.listar();
    }

    public ReunionMeet guardar(ReunionMeet r) {
        if (r.getNombre() == null || r.getNombre().isBlank()) {
            throw new ValidacionException("El nombre es obligatorio.");
        }
        if (r.getUrl() == null || r.getUrl().isBlank()) {
            throw new ValidacionException("La URL de la reunión es obligatoria.");
        }
        if (r.getId() == null) {
            return dao.guardar(r);
        }
        dao.actualizar(r);
        return r;
    }

    public void inactivar(Long id) {
        dao.inactivar(id);
    }
}