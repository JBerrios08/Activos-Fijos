package com.activosfijos.vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana principal luego de autenticación.
 */
public class MenuPrincipal extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    public MenuPrincipal(String usuario, String rol) {
        setTitle("Activos Fijos - Menú Principal");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL_MEDIANOCHE);
        cabecera.setPreferredSize(new Dimension(950, 70));
        cabecera.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel bienvenida = new JLabel(" Usuario: " + usuario + "  |  Rol: " + rol);
        bienvenida.setForeground(Color.WHITE);
        bienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cabecera.add(bienvenida, BorderLayout.WEST);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centro.setBackground(BLANCO_NIEVE);

        JButton botonGestion = crearBoton("Ir a Gestión de Activos");
        botonGestion.addActionListener(e -> new GestionActivos().setVisible(true));
        centro.add(botonGestion);

        if ("admin".equalsIgnoreCase(rol)) {
            JButton botonUsuarios = crearBoton("Administrar Usuarios");
            botonUsuarios.addActionListener(e -> new GestionUsuarios().setVisible(true));
            centro.add(botonUsuarios);
        }

        principal.add(cabecera, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);

        setContentPane(principal);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        private RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 16, 10, 16);
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
}
