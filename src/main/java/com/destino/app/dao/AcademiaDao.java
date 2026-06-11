package com.destino.app.dao;

import com.destino.app.model.Academia;
import java.util.List;

public interface AcademiaDao {
    List<Academia> listar();
    Academia buscarPorId(Long id);
    Academia guardar(Academia a);
    boolean actualizar(Academia a);
    boolean inactivar(Long id);
}