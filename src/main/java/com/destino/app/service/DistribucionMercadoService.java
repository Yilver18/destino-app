package com.destino.app.service;

import com.destino.app.dao.DistribucionMercadoDao;
import com.destino.app.dao.DistribucionMercadoDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.DistribucionMercado;

import java.util.List;

public class DistribucionMercadoService {

    private final DistribucionMercadoDao dao;

    public DistribucionMercadoService() {
        this.dao = new DistribucionMercadoDaoImpl();
    }

    public List<DistribucionMercado> listar() {
        return dao.listar();
    }

    public DistribucionMercado registrar(DistribucionMercado d) {
        boolean sinBeneficiario = d.getBeneficiarioId() == null
                && (d.getNombreBeneficiario() == null || d.getNombreBeneficiario().isBlank());
        if (sinBeneficiario) {
            throw new ValidacionException("Indica el beneficiario: elige una persona o escribe un nombre.");
        }
        if (d.getCantidad() < 1) {
            throw new ValidacionException("La cantidad debe ser al menos 1.");
        }
        return dao.guardar(d);
    }
}