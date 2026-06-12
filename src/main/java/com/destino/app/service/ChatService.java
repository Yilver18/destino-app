package com.destino.app.service;

import com.destino.app.dao.ChatDao;
import com.destino.app.dao.ChatDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.MensajeChat;

import java.util.List;

public class ChatService {

    private final ChatDao dao;

    public ChatService() {
        this.dao = new ChatDaoImpl();
    }

    public List<String[]> conversacionesDe(Long personaId) {
        return dao.conversacionesDe(personaId);
    }

    public Long iniciarConversacion(Long yo, Long otro) {
        if (otro == null) throw new ValidacionException("Elige una persona.");
        if (otro.equals(yo)) throw new ValidacionException("No puedes chatear contigo mismo.");
        return dao.obtenerOCrearDirecta(yo, otro);
    }

    public List<MensajeChat> mensajes(Long conversacionId) {
        return dao.mensajesDe(conversacionId);
    }

    public void enviar(Long conversacionId, Long remitenteId, String contenido) {
        if (contenido == null || contenido.isBlank()) return;
        dao.enviar(conversacionId, remitenteId, contenido.trim());
    }
}