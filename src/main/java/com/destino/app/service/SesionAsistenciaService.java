package com.destino.app.service;

import com.destino.app.dao.SesionAsistenciaDao;
import com.destino.app.dao.SesionAsistenciaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.SesionAsistencia;

import java.util.List;

public class SesionAsistenciaService {

    private final SesionAsistenciaDao dao;

    public SesionAsistenciaService() {
        this.dao = new SesionAsistenciaDaoImpl();
    }

    public List<SesionAsistencia> listar() {
        return dao.listar();
    }

    public SesionAsistencia crear(SesionAsistencia s) {
        if (s.getFecha() == null) {
            throw new ValidacionException("Elige la fecha de la sesión.");
        }
        if (s.getContexto() == null) {
            throw new ValidacionException("Elige el contexto (servicio, casa o academia).");
        }
        // Debe venir exactamente la referencia del contexto elegido.
        boolean ok = ("SERVICIO".equals(s.getContexto()) && s.getServicioId() != null)
                || ("CASA".equals(s.getContexto()) && s.getCasaId() != null)
                || ("ACADEMIA".equals(s.getContexto()) && s.getAcademiaId() != null);
        if (!ok) {
            throw new ValidacionException("Elige el " + s.getContexto().toLowerCase() + " de la sesión.");
        }
        return dao.guardar(s);
    }
}