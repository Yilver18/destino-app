package com.destino.app.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {

    private static final int COSTO = 12;   // factor de trabajo (2^12 iteraciones)

    private PasswordUtil() {
    }

    /** Genera el hash BCrypt de una contraseña en texto plano. */
    public static String hashear(String passwordPlano) {
        return BCrypt.withDefaults().hashToString(COSTO, passwordPlano.toCharArray());
    }

    /** Verifica una contraseña en texto plano contra un hash almacenado. */
    public static boolean verificar(String passwordPlano, String hash) {
        BCrypt.Result resultado =
                BCrypt.verifyer().verify(passwordPlano.toCharArray(), hash);
        return resultado.verified;
    }
}