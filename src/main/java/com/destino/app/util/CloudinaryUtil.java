package com.destino.app.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.destino.app.exceptions.DestinoException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public final class CloudinaryUtil {

    private static Cloudinary cloudinary;

    private CloudinaryUtil() {
    }

    // Singleton perezoso: crea el cliente una sola vez con tus credenciales.
    private static synchronized Cloudinary obtener() {
        if (cloudinary == null) {
            Properties p = cargar();
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", p.getProperty("cloudinary.cloud_name"),
                    "api_key", p.getProperty("cloudinary.api_key"),
                    "api_secret", p.getProperty("cloudinary.api_secret"),
                    "secure", true));
        }
        return cloudinary;
    }

    /** Sube un archivo de imagen y devuelve su URL pública (https). */
    public static String subir(File archivo) {
        try {
            Map<?, ?> resultado = obtener().uploader()
                    .upload(archivo, ObjectUtils.asMap("folder", "destinoapp"));
            return (String) resultado.get("secure_url");
        } catch (IOException e) {
            throw new DestinoException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    private static Properties cargar() {
        Properties props = new Properties();
        try (InputStream in = CloudinaryUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new DestinoException("No se encontró db.properties");
            props.load(in);
        } catch (IOException e) {
            throw new DestinoException("Error al cargar credenciales de Cloudinary", e);
        }
        return props;
    }
}