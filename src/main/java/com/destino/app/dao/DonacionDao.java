package com.destino.app.dao;

import com.destino.app.model.Donacion;
import java.util.List;

public interface DonacionDao {
    List<Donacion> listar();
    Donacion guardar(Donacion d);
}