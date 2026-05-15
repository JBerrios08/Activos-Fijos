package com.activosfijos.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal luego de autenticación.
 */
public class MenuPrincipal extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");

    public MenuPrincipal(String usuario, String rol) {
        setTitle("Activos Fijos - Menú Principal");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BLANCO_NIEVE);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL_MEDIANOCHE);
        cabecera.setPreferredSize(new Dimension(950, 70));

        JLabel bienvenida = new JLabel(" Usuario: " + usuario + "  |  Rol: " + rol);
        bienvenida.setForeground(Color.WHITE);
        bienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cabecera.add(bienvenida, BorderLayout.WEST);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(BLANCO_NIEVE);

        JButton botonGestion = new JButton("Ir a Gestión de Activos");
        botonGestion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonGestion.addActionListener(e -> new GestionActivos().setVisible(true));

        centro.add(botonGestion);

        principal.add(cabecera, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);

        setContentPane(principal);
    }
}
