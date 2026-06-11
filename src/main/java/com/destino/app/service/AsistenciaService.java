package com.destino.app.service;

import com.destino.app.dao.*;
import com.destino.app.exceptions.AccesoDatosException;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Asistencia;
import com.destino.app.model.Persona;
import com.destino.app.util.ColaPendientes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class AsistenciaService {

    private final AsistenciaDao dao;
    private final PersonaDao personaDao;

    public AsistenciaService() {
        this.dao = new AsistenciaDaoImpl();
        this.personaDao = new PersonaDaoImpl();
    }

    public List<Asistencia> listarPorSesion(Long sesionId) {
        return dao.listarPorSesion(sesionId);
    }

    // Registro normal. Si la BD está caída, encola localmente y devuelve OFFLINE.
    public ResultadoAsistencia registrar(Long sesionId, Long personaId, String metodo) {
        if (sesionId == null) throw new ValidacionException("Elige una sesión.");
        if (personaId == null) throw new ValidacionException("Elige una persona.");
        try {
            boolean nuevo = dao.registrar(sesionId, personaId, metodo);
            return nuevo ? ResultadoAsistencia.REGISTRADO : ResultadoAsistencia.DUPLICADO;
        } catch (AccesoDatosException e) {
            ColaPendientes.agregarPersona(sesionId, personaId, metodo, OffsetDateTime.now());
            return ResultadoAsistencia.OFFLINE;
        }
    }

    // Cuando ni siquiera se pudo resolver el QR (BD caída): se guarda el UUID.
    public void encolarQr(Long sesionId, UUID uuid) {
        ColaPendientes.agregarQr(sesionId, uuid, OffsetDateTime.now());
    }

    public void eliminar(Long id) {
        dao.eliminar(id);
    }

    // Sube todos los pendientes locales. Lanza si la BD sigue caída (no borra el archivo).
    public int sincronizarPendientes() {
        List<String[]> pendientes = ColaPendientes.leerTodos();
        if (pendientes.isEmpty()) return 0;
        for (String[] r : pendientes) {
            String tipo = r[0].trim();
            Long sesionId = Long.parseLong(r[2].trim());
            String metodo = r[3].trim();
            OffsetDateTime hora = OffsetDateTime.parse(r[4].trim());
            Long personaId;
            if ("QR".equals(tipo)) {
                Persona p = personaDao.buscarPorQr(UUID.fromString(r[1].trim()));
                if (p == null) continue;   // QR sin persona: lo saltamos
                personaId = p.getId();
            } else {
                personaId = Long.parseLong(r[1].trim());
            }
            dao.registrarConHora(sesionId, personaId, metodo, hora);
        }
        ColaPendientes.limpiar();
        return pendientes.size();
    }
}