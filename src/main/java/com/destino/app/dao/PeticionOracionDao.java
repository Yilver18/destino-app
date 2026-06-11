package com.destino.app.dao;

import com.destino.app.model.PeticionOracion;
import java.util.List;

public interface PeticionOracionDao {
    List<PeticionOracion> listar();
    PeticionOracion guardar(PeticionOracion p);
    boolean marcarRespondida(Long id);
}