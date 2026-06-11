package com.destino.app.dao;

import com.destino.app.model.Ministerio;
import java.util.List;

public interface MinisterioDao {
    List<Ministerio> listar();
    Ministerio buscarPorId(Long id);
    Ministerio guardar(Ministerio m);
    boolean actualizar(Ministerio m);
    boolean inactivar(Long id);
}