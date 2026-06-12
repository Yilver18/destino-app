package com.destino.app.service;

import com.destino.app.dao.ConfiguracionDao;
import com.destino.app.dao.ConfiguracionDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Configuracion;

import java.util.List;

public class ConfiguracionService {

    private final ConfiguracionDao dao;

    public ConfiguracionService() {
        this.dao = new ConfiguracionDaoImpl();
    }

    public List<Configuracion> listar() {
        return dao.listar();
    }

    public void guardar(Configuracion c) {
        if (c.getClave() == null || c.getClave().isBlank()) {
            throw new ValidacionException("La clave es obligatoria.");
        }
        dao.guardar(c);
    }

    public void eliminar(String clave) {
        dao.eliminar(clave);
    }
}