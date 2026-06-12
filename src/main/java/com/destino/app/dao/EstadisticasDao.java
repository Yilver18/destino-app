package com.destino.app.dao;

import java.util.List;

public interface EstadisticasDao {
    List<String[]> asistenciaPorServicio();      // {nombreServicio, total}
    List<String[]> asistenciaPorSesion(int limite); // {fecha+referencia, total}
}