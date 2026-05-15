package com.activosfijos.vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL;

/**
 * Formulario base para administración de activos.
 */
public class GestionActivos extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private JTextField campoCodigoBarra;
    private JTextField campoNombre;
    private Border bordeCodigoOriginal;
    private Border bordeNombreOriginal;

    public GestionActivos() {
        setTitle("Activos Fijos - Gestión de Activos");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(AZUL_MEDIANOCHE);
        titulo.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel etiquetaTitulo = new JLabel("Módulo de Gestión de Activos");
        etiquetaTitulo.setForeground(Color.WHITE);
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.add(etiquetaTitulo, BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO_NIEVE);
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Código de Barra", "Nombre", "Descripción", "Serie", "Valor de Compra", "Estado"};

        for (int i = 0; i < etiquetas.length; i++) {
            JLabel lbl = new JLabel(etiquetas[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JTextField txt = new JTextField();
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            if ("Código de Barra".equals(etiquetas[i])) {
                campoCodigoBarra = txt;
                bordeCodigoOriginal = txt.getBorder();
            }
            if ("Nombre".equals(etiquetas[i])) {
                campoNombre = txt;
                bordeNombreOriginal = txt.getBorder();
            }

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            formulario.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.8;
            formulario.add(txt, gbc);
        }

        JTable tablaActivos = crearTablaModerna();
        JScrollPane scroll = new JScrollPane(tablaActivos);
        scroll.setBorder(new EmptyBorder(0, 8, 0, 8));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        acciones.setBackground(BLANCO_NIEVE);
        acciones.setBorder(new EmptyBorder(0, 0, 8, 8));

        JButton botonGuardar = crearBotonAccion("Guardar", cargarIcono("guardar"));
        JButton botonEliminar = crearBotonAccion("Eliminar", cargarIcono("eliminar"));
        JButton botonBuscar = crearBotonAccion("Buscar", cargarIcono("buscar"));

        botonGuardar.addActionListener(e -> guardarActivo());

        acciones.add(botonGuardar);
        acciones.add(botonEliminar);
        acciones.add(botonBuscar);

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setBackground(BLANCO_NIEVE);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        principal.add(titulo, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);
        principal.add(acciones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private JTable crearTablaModerna() {
        String[] columnas = {"Código", "Nombre", "Serie", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(AZUL_MEDIANOCHE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        return tabla;
    }

    private JButton crearBotonAccion(String texto, Icon icono) {
        JButton boton = new JButton(texto, icono);
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    public static Icon cargarIcono(String nombre) {
        String ruta = "/iconos/" + nombre + ".png";
        URL recurso = GestionActivos.class.getResource(ruta);
        if (recurso != null) {
            return new ImageIcon(recurso);
        }
        return new TextIcon("•", 14, AZUL_MEDIANOCHE);
    }

    private void guardarActivo() {
        boolean invalido = false;
        campoNombre.setBorder(bordeNombreOriginal);
        campoCodigoBarra.setBorder(bordeCodigoOriginal);

        if (campoNombre.getText().trim().isEmpty()) {
            campoNombre.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            invalido = true;
        }
        if (campoCodigoBarra.getText().trim().isEmpty()) {
            campoCodigoBarra.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            invalido = true;
        }

        if (invalido) {
            JOptionPane.showMessageDialog(this, "Nombre y Código de Barra son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Activo guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        private RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 14, 8, 14);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(AZUL_MEDIANOCHE);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private static class TextIcon implements Icon {
        private final String text;
        private final int size;
        private final Color color;

        private TextIcon(String text, int size, Color color) {
            this.text = text;
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.setFont(new Font("Segoe UI Symbol", Font.BOLD, size));
            g.drawString(text, x + 3, y + size);
        }

        @Override
        public int getIconWidth() { return 16; }

        @Override
        public int getIconHeight() { return 16; }
    }
}
