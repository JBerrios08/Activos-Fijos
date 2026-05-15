package com.activosfijos.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formulario base para administración de activos.
 */
public class GestionActivos extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    public GestionActivos() {
        setTitle("Activos Fijos - Gestión de Activos");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BLANCO_NIEVE);

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

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            formulario.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.8;
            formulario.add(txt, gbc);
        }

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        acciones.setBackground(BLANCO_NIEVE);

        JButton botonGuardar = crearBotonAccion("Guardar", UIManager.getIcon("FileView.floppyDriveIcon"));
        JButton botonEliminar = crearBotonAccion("Eliminar", UIManager.getIcon("OptionPane.errorIcon"));
        JButton botonBuscar = crearBotonAccion("Buscar", UIManager.getIcon("FileView.directoryIcon"));

        // Opción recomendada: reemplazar los iconos estándar con PNG personalizados.
        // Guardar iconos en: src/main/resources/iconos/guardar.png, eliminar.png, buscar.png

        acciones.add(botonGuardar);
        acciones.add(botonEliminar);
        acciones.add(botonBuscar);

        principal.add(titulo, BorderLayout.NORTH);
        principal.add(formulario, BorderLayout.CENTER);
        principal.add(acciones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private JButton crearBotonAccion(String texto, Icon icono) {
        JButton boton = new JButton(texto, icono);
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return boton;
    }
}
