package com.destino.app.util;

import com.destino.app.exceptions.DestinoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class ColaPendientes {

    // Archivo local en la carpeta del usuario. Formato por línea: tipo,ref,sesionId,metodo,hora
    private static final Path ARCHIVO =
            Paths.get(System.getProperty("user.home"), "destinoapp_pendientes.csv");

    private ColaPendientes() {
    }

    public static void agregarPersona(long sesionId, long personaId, String metodo, OffsetDateTime hora) {
        escribir("PERSONA," + personaId + "," + sesionId + "," + metodo + "," + hora);
    }

    public static void agregarQr(long sesionId, UUID uuid, OffsetDateTime hora) {
        escribir("QR," + uuid + "," + sesionId + ",QR," + hora);
    }

    private static void escribir(String linea) {
        try {
            Files.writeString(ARCHIVO, linea + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DestinoException("No se pudo guardar el pendiente local", e);
        }
    }

    public static int contar() {
        if (!Files.exists(ARCHIVO)) return 0;
        try (Stream<String> s = Files.lines(ARCHIVO)) {
            return (int) s.filter(l -> !l.isBlank()).count();
        } catch (IOException e) {
            return 0;
        }
    }

    public static List<String[]> leerTodos() {
        if (!Files.exists(ARCHIVO)) return List.of();
        try (Stream<String> s = Files.lines(ARCHIVO)) {
            return s.filter(l -> !l.isBlank()).map(l -> l.split(",")).toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    public static void limpiar() {
        try { Files.deleteIfExists(ARCHIVO); } catch (IOException ignore) {}
    }
}