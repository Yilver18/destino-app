package com.destino.app.service;

import com.destino.app.dao.PredicaDao;
import com.destino.app.dao.PredicaDaoImpl;
import com.destino.app.exceptions.ValidacionException;
import com.destino.app.model.Predica;
import com.destino.app.util.GroqApiClient;
import java.util.List;

public class PredicaService {

    private final PredicaDao dao;

    public PredicaService() {
        this.dao = new PredicaDaoImpl();
    }

    public List<Predica> listar() {
        return dao.listar();
    }

    public Predica registrar(Predica p) {
        if (p.getTitulo() == null || p.getTitulo().isBlank()) {
            throw new ValidacionException("El título es obligatorio.");
        }
        if (p.getYoutubeUrl() == null || p.getYoutubeUrl().isBlank()) {
            throw new ValidacionException("El link de YouTube es obligatorio.");
        }
        return dao.guardar(p);
    }
    private static final String SISTEMA_RESUMEN =
            "Eres un asistente que ayuda a un equipo pastoral cristiano a preparar resúmenes de " +
                    "prédicas, claros y fieles a la Biblia. Respondes en español.";

    public String generarResumen(com.destino.app.model.Predica p, String notas) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Prepara un resumen de la prédica titulada: \"").append(p.getTitulo()).append("\".");
        if (notas != null && !notas.isBlank()) {
            prompt.append("\nBasándote en estas notas o transcripción:\n").append(notas);
        }
        prompt.append("\n\nIncluye, en formato claro:\n")
                .append("1) 3 a 5 puntos clave.\n")
                .append("2) Versículos relacionados con su referencia bíblica.\n")
                .append("3) Una aplicación práctica breve para la vida diaria.");

        String resumen = GroqApiClient.preguntar(SISTEMA_RESUMEN, prompt.toString());
        dao.guardarResumen(p.getId(), resumen);
        return resumen;
    }

    public void guardarResumen(Long id, String texto) {
        dao.guardarResumen(id, texto);
    }
    public void marcarActual(Long id) {
        dao.marcarActual(id);
    }
}