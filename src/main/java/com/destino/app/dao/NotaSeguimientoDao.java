package com.destino.app.dao;

import com.destino.app.model.NotaSeguimiento;
import java.util.List;

public interface NotaSeguimientoDao {
    List<NotaSeguimiento> listarPorPersona(Long personaId);
    NotaSeguimiento guardar(NotaSeguimiento n);
}