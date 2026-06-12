package com.destino.app.dao;

import com.destino.app.model.Configuracion;
import java.util.List;

public interface ConfiguracionDao {
    List<Configuracion> listar();
    void guardar(Configuracion c);   // crea o actualiza (upsert)
    void eliminar(String clave);
    String buscarValor(String clave);
}