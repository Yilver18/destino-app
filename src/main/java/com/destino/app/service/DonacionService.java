package com.destino.app.service;

import com.destino.app.dao.DonacionDao;
import com.destino.app.dao.DonacionDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Donacion;

import java.math.BigDecimal;
import java.util.List;

public class DonacionService {

    private final DonacionDao dao;

    public DonacionService() {
        this.dao = new DonacionDaoImpl();
    }

    public List<Donacion> listar() {
        return dao.listar();
    }

    public Donacion registrar(Donacion d) {
        if (d.getTipo() == null || d.getTipo().isBlank()) {
            throw new ValidacionException("Elige el tipo de donación (física o digital).");
        }
        // Debe haber donante: persona registrada o nombre externo.
        boolean sinDonante = d.getDonanteId() == null
                && (d.getNombreDonante() == null || d.getNombreDonante().isBlank());
        if (sinDonante) {
            throw new ValidacionException("Indica el donante: elige una persona o escribe un nombre.");
        }
        // En digitales el monto es obligatorio y positivo.
        if ("DIGITAL".equals(d.getTipo())) {
            if (d.getMonto() == null || d.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacionException("Una donación digital requiere un monto mayor a cero.");
            }
        }
        return dao.guardar(d);
    }
}