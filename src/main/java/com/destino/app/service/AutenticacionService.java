package com.destino.app.service;

import com.destino.app.dao.UsuarioDao;
import com.destino.app.dao.UsuarioDaoImpl;
import com.destino.app.exceptions.AutenticacionException;
import com.destino.app.model.Usuario;
import com.destino.app.util.PasswordUtil;
import com.destino.app.dao.RolDao;
import com.destino.app.dao.RolDaoImpl;
import com.destino.app.model.Rol;
import java.util.List;
public class AutenticacionService {

    // Hash dummy generado al inicio, para igualar tiempos (ver nota abajo).
    private static final String HASH_DUMMY =
            PasswordUtil.hashear("hash_dummy_anti_timing");

    private final UsuarioDao usuarioDao;
    private final RolDao rolDao;

    public AutenticacionService() {
        this.usuarioDao = new UsuarioDaoImpl();   // <-- UsuarioDaoImpl, no RolDaoImpl
        this.rolDao = new RolDaoImpl();
    }

    // Constructor para inyectar un DAO (útil para pruebas).
    public AutenticacionService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
        this.rolDao = new RolDaoImpl();           // <-- faltaba inicializar rolDao
    }
    public List<Rol> rolesDe(Usuario usuario) {
        return rolDao.listarPorPersona(usuario.getPersonaId());
    }
    public Usuario autenticar(String nombreUsuario, String passwordPlano) {
        Usuario usuario = usuarioDao.buscarPorNombreUsuario(nombreUsuario);

        // Siempre ejecuta BCrypt (con hash real o dummy) para que el tiempo
        // de respuesta sea igual exista o no el usuario.
        String hash = (usuario != null) ? usuario.getHashPassword() : HASH_DUMMY;
        boolean passwordOk = PasswordUtil.verificar(passwordPlano, hash);

        // Anti-enumeración: mismo mensaje si no existe o si la clave falla.
        if (usuario == null || !passwordOk) {
            throw new AutenticacionException("Usuario o contraseña incorrectos");
        }

        // La clave ya es correcta: ahora sí es seguro revelar si está inactiva.
        if (!usuario.isActivo()) {
            throw new AutenticacionException("La cuenta está inactiva. Contacta al administrador.");
        }

        usuarioDao.actualizarUltimoAcceso(usuario.getId());
        return usuario;
    }
}