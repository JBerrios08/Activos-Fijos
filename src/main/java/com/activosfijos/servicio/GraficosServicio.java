package com.activosfijos.servicio;

import com.activosfijos.util.TemaVisual;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraficosServicio {


    public JPanel crearGraficoEstados(Map<String, Integer> datosEstados, Color colorFondoTema) {
        if (datosEstados == null || datosEstados.isEmpty()) {
            return crearPanelSinDatos();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datosEstados.entrySet()) {
            dataset.addValue(entry.getValue(), "Activos", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart("Activos por Estado", "Estado", "Cantidad", dataset);
        estilizarGrafico(chart, colorFondoTema);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(360, 240));
        chartPanel.setBackground(colorFondoTema);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(colorFondoTema);
        contenedor.add(chartPanel, BorderLayout.CENTER);
        return contenedor;
    }

    private void estilizarGrafico(JFreeChart chart, Color colorFondoTema) {
        chart.setBackgroundPaint(colorFondoTema);
        chart.getTitle().setPaint(UIManager.getColor("Label.foreground"));
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(colorFondoTema);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(UIManager.getColor("Separator.foreground"));
        plot.getDomainAxis().setTickLabelPaint(UIManager.getColor("Label.foreground"));
        plot.getDomainAxis().setLabelPaint(UIManager.getColor("Label.foreground"));
        plot.getRangeAxis().setTickLabelPaint(UIManager.getColor("Label.foreground"));
        plot.getRangeAxis().setLabelPaint(UIManager.getColor("Label.foreground"));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, esTemaOscuro() ? TemaVisual.AZUL_SUAVE : TemaVisual.AZUL_INSTITUCIONAL);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
    }

    private boolean esTemaOscuro() {
        Color fondo = UIManager.getColor("Panel.background");
        return fondo != null && brillo(fondo) < 140;
    }

    private int brillo(Color color) {
        return (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000;
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
