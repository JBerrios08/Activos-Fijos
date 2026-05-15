package com.activosfijos.servicio;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraficosServicio {

    public JPanel crearGraficoEstados(Map<String, Integer> datosEstados) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datosEstados.entrySet()) {
            dataset.addValue(entry.getValue(), "Activos", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Activos por Estado",
                "Estado",
                "Cantidad",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(360, 240));

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(chartPanel, BorderLayout.CENTER);
        return contenedor;
    }
}
