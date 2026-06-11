package com.destino.app.dao;

import com.destino.app.model.SesionAsistencia;
import java.util.List;

public interface SesionAsistenciaDao {
    List<SesionAsistencia> listar();
    SesionAsistencia guardar(SesionAsistencia s);
}