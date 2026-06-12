package com.destino.app.dao;

import java.util.List;

public interface EstadisticasDao {
    List<String[]> asistenciaPorServicio();      // {nombreServicio, total}
    List<String[]> asistenciaPorSesion(int limite); // {fecha+referencia, total}
    List<String[]> personasPorMes();     // {mes YYYY-MM, total}
    List<String[]> personasPorEstado();  // {estado, total}
    List<String[]> donacionesPorTipo();   // {tipo, cantidad, montoTotal}
    String[] distribucionTotales();        // {entregas, mercados}
}