package com.destino.app.service;

import com.destino.app.dao.PersonaRolDao;
import com.destino.app.dao.PersonaRolDaoImpl;
import com.destino.app.dao.RolDao;
import com.destino.app.dao.RolDaoImpl;
import com.destino.app.model.Rol;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AsignacionRolService {

    private final PersonaRolDao personaRolDao;
    private final RolDao rolDao;

    public AsignacionRolService() {
        this.personaRolDao = new PersonaRolDaoImpl();
        this.rolDao = new RolDaoImpl();
    }

    public List<Rol> rolesAsignados(Long personaId) {
        return rolDao.listarPorPersona(personaId);
    }

    // Roles que la persona NO tiene aún (todos menos los asignados).
    public List<Rol> rolesDisponibles(Long personaId) {
        Set<Long> yaTiene = rolesAsignados(personaId).stream()
                .map(Rol::getId)
                .collect(Collectors.toSet());
        return rolDao.listar().stream()
                .filter(r -> !yaTiene.contains(r.getId()))
                .toList();
    }

    public void asignar(Long personaId, Long rolId) {
        if (!personaRolDao.existe(personaId, rolId)) {
            personaRolDao.asignar(personaId, rolId);
        }
    }

    public void quitar(Long personaId, Long rolId) {
        personaRolDao.quitar(personaId, rolId);
    }
}