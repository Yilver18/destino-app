package com.destino.app.service;

import com.destino.app.dao.PersonaDao;
import com.destino.app.dao.PersonaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Persona;

import java.util.List;

public class PersonaService {

    private final PersonaDao personaDao;

    public PersonaService() {
        this.personaDao = new PersonaDaoImpl();
    }

    // Para inyectar un DAO en pruebas.
    public PersonaService(PersonaDao personaDao) {
        this.personaDao = personaDao;
    }

    public List<Persona> listar() {
        return personaDao.listar();
    }

    public Persona buscarPorId(Long id) {
        return personaDao.buscarPorId(id);
    }
    public Persona buscarPorQr(java.util.UUID qr) {
        return personaDao.buscarPorQr(qr);
    }

    public Persona registrar(Persona p) {
        validarDatos(p);

        // Consentimiento obligatorio (Ley 1581 de 2012).
        if (!p.isConsentimientoDatos()) {
            throw new ValidacionException(
                    "Debe aceptar el consentimiento de datos para registrar la persona.");
        }

        // Documento único: mensaje amable antes de chocar con la BD.
        if (personaDao.buscarPorDocumento(p.getNumeroDocumento()) != null) {
            throw new ValidacionException(
                    "Ya existe una persona con el documento " + p.getNumeroDocumento() + ".");
        }

        if (p.getEstado() == null || p.getEstado().isBlank()) {
            p.setEstado("nueva");
        }
        return personaDao.guardar(p);
    }

    public boolean actualizar(Persona p) {
        if (p.getId() == null) {
            throw new ValidacionException("La persona no tiene id; no se puede actualizar.");
        }
        validarDatos(p);

        // Si ese documento ya pertenece a OTRA persona, error.
        Persona existente = personaDao.buscarPorDocumento(p.getNumeroDocumento());
        if (existente != null && !existente.getId().equals(p.getId())) {
            throw new ValidacionException(
                    "El documento " + p.getNumeroDocumento() + " ya pertenece a otra persona.");
        }
        return personaDao.actualizar(p);
    }

    public boolean inactivar(Long id) {
        return personaDao.inactivar(id);
    }

    // Validaciones de campos obligatorios.
    private void validarDatos(Persona p) {
        if (esVacio(p.getNombres()))         throw new ValidacionException("El nombre es obligatorio.");
        if (esVacio(p.getApellidos()))       throw new ValidacionException("El apellido es obligatorio.");
        if (esVacio(p.getTipoDocumento()))   throw new ValidacionException("El tipo de documento es obligatorio.");
        if (esVacio(p.getNumeroDocumento())) throw new ValidacionException("El número de documento es obligatorio.");
    }

    private boolean esVacio(String s) {
        return s == null || s.isBlank();
    }
}