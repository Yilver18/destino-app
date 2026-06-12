package com.destino.app.view;

import com.destino.app.dao.EstadisticasDao;
import com.destino.app.dao.EstadisticasDaoImpl;
import com.destino.app.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private VBox contenedor;

    private final EstadisticasDao dao = new EstadisticasDaoImpl();

    @FXML
    public void initialize() {
        contenedor.getChildren().addAll(
                graficoAsistenciaPorServicio(),
                graficoPersonasPorEstado(),
                graficoPersonasPorMes()
        );
    }

    private BarChart<String, Number> graficoAsistenciaPorServicio() {
        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();
        ejeX.setLabel("Servicio");
        ejeY.setLabel("Asistencias");
        BarChart<String, Number> chart = new BarChart<>(ejeX, ejeY);
        chart.setTitle("Asistencia por servicio");
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (String[] r : dao.asistenciaPorServicio()) {
            serie.getData().add(new XYChart.Data<>(r[0], Integer.parseInt(r[1])));
        }
        chart.getData().add(serie);
        return chart;
    }

    private PieChart graficoPersonasPorEstado() {
        PieChart chart = new PieChart();
        chart.setTitle("Personas por estado");
        chart.setPrefHeight(300);
        for (String[] r : dao.personasPorEstado()) {
            chart.getData().add(new PieChart.Data(r[0] + " (" + r[1] + ")", Integer.parseInt(r[1])));
        }
        return chart;
    }

    private BarChart<String, Number> graficoPersonasPorMes() {
        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();
        ejeX.setLabel("Mes");
        ejeY.setLabel("Personas nuevas");
        BarChart<String, Number> chart = new BarChart<>(ejeX, ejeY);
        chart.setTitle("Personas nuevas por mes");
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (String[] r : dao.personasPorMes()) {
            serie.getData().add(new XYChart.Data<>(r[0], Integer.parseInt(r[1])));
        }
        chart.getData().add(serie);
        return chart;
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) contenedor.getScene().getWindow();
        Navegador.cambiarEscena(stage, "/com/destino/app/view/principal.fxml",
                "Destino App — Panel principal");
    }
}