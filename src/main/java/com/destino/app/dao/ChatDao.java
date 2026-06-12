package com.destino.app.dao;

import com.destino.app.model.MensajeChat;
import java.util.List;

public interface ChatDao {
    List<String[]> conversacionesDe(Long personaId);   // {id, etiqueta}
    Long obtenerOCrearDirecta(Long personaA, Long personaB);
    List<MensajeChat> mensajesDe(Long conversacionId);
    void enviar(Long conversacionId, Long remitenteId, String contenido);
}