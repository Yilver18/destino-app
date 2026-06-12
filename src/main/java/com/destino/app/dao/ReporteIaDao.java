package com.destino.app.dao;

public interface ReporteIaDao {
    void guardar(String tipo, String contenido, Long generadoPor);
}