package com.destino.app.dao;

import com.destino.app.model.CasaDestino;
import java.util.List;

public interface CasaDestinoDao {
    List<CasaDestino> listar();
    CasaDestino buscarPorId(Long id);
    CasaDestino guardar(CasaDestino casa);
    boolean actualizar(CasaDestino casa);
    boolean inactivar(Long id);
}