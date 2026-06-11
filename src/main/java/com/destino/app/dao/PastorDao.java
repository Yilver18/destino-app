package com.destino.app.dao;

import com.destino.app.model.Pastor;
import java.util.List;

public interface PastorDao {
    List<Pastor> listar();
    Pastor guardar(Pastor p);
    boolean actualizar(Pastor p);
    void eliminar(Long id);
}