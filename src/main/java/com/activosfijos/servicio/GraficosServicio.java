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
        if (datosEstados == null || datosEstados.isEmpty()) {
            return crearPanelSinDatos();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datosEstados.entrySet()) {
            dataset.addValue(entry.getValue(), "Activos", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart("Activos por Estado", "Estado", "Cantidad", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(360, 240));

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(chartPanel, BorderLayout.CENTER);
        return contenedor;
    }

    public JPanel crearPanelSinDatos() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        JLabel mensaje = new JLabel("No hay activos para mostrar");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contenedor.add(mensaje);
        return contenedor;
    }
}
