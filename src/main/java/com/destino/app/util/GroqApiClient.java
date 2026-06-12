package com.destino.app.util;

import com.destino.app.exceptions.DestinoException;
import com.google.gson.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public final class GroqApiClient {

    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";
    // Si diera error de modelo, prueba "llama-3.1-8b-instant".
    private static final String MODELO = "llama-3.3-70b-versatile";

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static String apiKey;

    private GroqApiClient() {
    }

    public record Mensaje(String rol, String contenido) {   // rol: "user" | "assistant"
    }

    public static String preguntar(String sistema, String mensajeUsuario) {
        return conversar(sistema, List.of(new Mensaje("user", mensajeUsuario)));
    }

    public static String conversar(String sistema, List<Mensaje> historial) {
        JsonObject body = new JsonObject();
        body.addProperty("model", MODELO);

        JsonArray mensajes = new JsonArray();
        if (sistema != null && !sistema.isBlank()) {
            mensajes.add(mensaje("system", sistema));   // el sistema es solo otro mensaje
        }
        for (Mensaje m : historial) {
            mensajes.add(mensaje(m.rol(), m.contenido()));   // user/assistant tal cual
        }
        body.add("messages", mensajes);

        HttpRequest req = HttpRequest.newBuilder(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + clave())
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(body)))
                .build();
        try {
            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                throw new DestinoException("Groq API (" + resp.statusCode() + "): " + resp.body());
            }
            JsonObject json = JsonParser.parseString(resp.body()).getAsJsonObject();
            return json.getAsJsonArray("choices").get(0).getAsJsonObject()
                    .getAsJsonObject("message").get("content").getAsString();
        } catch (DestinoException e) {
            throw e;
        } catch (Exception e) {
            throw new DestinoException("Error al llamar a Groq API", e);
        }
    }

    private static JsonObject mensaje(String rol, String contenido) {
        JsonObject o = new JsonObject();
        o.addProperty("role", rol);
        o.addProperty("content", contenido);
        return o;
    }

    private static synchronized String clave() {
        if (apiKey == null) {
            Properties p = new Properties();
            try (InputStream in = GroqApiClient.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (in == null) throw new DestinoException("No se encontró db.properties");
                p.load(in);
                apiKey = p.getProperty("groq.api_key");
            } catch (Exception e) {
                throw new DestinoException("No se pudo cargar la API key de Groq", e);
            }
            if (apiKey == null || apiKey.isBlank()) {
                throw new DestinoException("Falta 'groq.api_key' en db.properties");
            }
        }
        return apiKey;
    }
}