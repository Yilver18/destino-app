package com.destino.app.dao;

import com.destino.app.model.DistribucionMercado;
import java.util.List;

public interface DistribucionMercadoDao {
    List<DistribucionMercado> listar();
    DistribucionMercado guardar(DistribucionMercado d);
}