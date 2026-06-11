package com.destino.app.dao;

import com.destino.app.model.ReunionMeet;
import java.util.List;

public interface ReunionMeetDao {
    List<ReunionMeet> listar();
    ReunionMeet guardar(ReunionMeet r);
    boolean actualizar(ReunionMeet r);
    boolean inactivar(Long id);
}