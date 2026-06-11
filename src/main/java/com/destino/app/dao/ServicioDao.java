package com.destino.app.dao;

import com.destino.app.model.Servicio;
import java.util.List;

public interface ServicioDao {
    List<Servicio> listarActivos();
}