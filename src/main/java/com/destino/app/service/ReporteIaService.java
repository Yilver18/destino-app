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
    public String analizarCrecimiento(Long generadoPor) {
        StringBuilder datos = new StringBuilder("Datos de crecimiento de la iglesia:\n\n");
        datos.append("Personas registradas por mes:\n");
        for (String[] r : estadisticasDao.personasPorMes()) {
            datos.append("- ").append(r[0]).append(": ").append(r[1]).append(" personas\n");
        }
        datos.append("\nPersonas por estado:\n");
        for (String[] r : estadisticasDao.personasPorEstado()) {
            datos.append("- ").append(r[0]).append(": ").append(r[1]).append("\n");
        }

        String sistema = "Eres un analista que ayuda a un pastor a entender el crecimiento de su " +
                "iglesia. Respondes en español, claro, breve y alentador pero realista.";
        String prompt = "Interpreta estos datos de crecimiento y entrega un resumen para el pastor con: " +
                "1) cómo viene el crecimiento, 2) qué dice el estado de las personas (nuevas vs activas), " +
                "3) dos recomendaciones para consolidar y retener.\n\n" + datos;

        String reporte = GroqApiClient.preguntar(sistema, prompt);
        reporteIaDao.guardar("CRECIMIENTO", reporte, generadoPor);
        return reporte;
    }
    public String analizarAgape(Long generadoPor) {
        StringBuilder datos = new StringBuilder("Datos del ministerio de ayuda social (Agapé):\n\n");
        datos.append("Donaciones por tipo:\n");
        for (String[] r : estadisticasDao.donacionesPorTipo()) {
            datos.append("- ").append(r[0]).append(": ").append(r[1])
                    .append(" donaciones, monto total ").append(r[2]).append("\n");
        }
        String[] dist = estadisticasDao.distribucionTotales();
        datos.append("\nDistribución de mercados: ").append(dist[0]).append(" entregas, ")
                .append(dist[1]).append(" mercados entregados.\n");

        String sistema = "Eres un analista que ayuda al ministerio de ayuda social (Agapé) de una " +
                "iglesia cristiana a entender su impacto. Respondes en español, claro, breve y agradecido.";
        String prompt = "Analiza estos datos de donaciones y distribución de Agapé y entrega: " +
                "1) un resumen del impacto, 2) observaciones, 3) dos sugerencias de mejora.\n\n" + datos;

        String reporte = GroqApiClient.preguntar(sistema, prompt);
        reporteIaDao.guardar("AGAPE", reporte, generadoPor);
        return reporte;
    }
}