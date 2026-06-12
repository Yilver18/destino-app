package com.destino.app.service;

import com.destino.app.dao.*;
import com.destino.app.util.GroqApiClient;

public class ReporteIaService {

    private final EstadisticasDao estadisticasDao;
    private final ReporteIaDao reporteIaDao;

    public ReporteIaService() {
        this.estadisticasDao = new EstadisticasDaoImpl();
        this.reporteIaDao = new ReporteIaDaoImpl();
    }

    public String analizarAsistencia(Long generadoPor) {
        StringBuilder datos = new StringBuilder("Datos de asistencia de la iglesia:\n\n");
        datos.append("Asistencia total por servicio:\n");
        for (String[] r : estadisticasDao.asistenciaPorServicio()) {
            datos.append("- ").append(r[0]).append(": ").append(r[1]).append(" asistencias\n");
        }
        datos.append("\nÚltimas sesiones (más reciente primero):\n");
        for (String[] r : estadisticasDao.asistenciaPorSesion(10)) {
            datos.append("- ").append(r[0]).append(": ").append(r[1]).append(" asistentes\n");
        }

        String sistema = "Eres un analista que ayuda a un equipo pastoral cristiano a entender la " +
                "asistencia de su iglesia. Respondes en español, claro, breve y práctico.";
        String prompt = "Analiza estos datos de asistencia y entrega un reporte con: 1) tendencias " +
                "que observas, 2) puntos de atención, 3) dos o tres recomendaciones para los líderes.\n\n"
                + datos;

        String reporte = GroqApiClient.preguntar(sistema, prompt);
        reporteIaDao.guardar("ASISTENCIA", reporte, generadoPor);
        return reporte;
    }
}