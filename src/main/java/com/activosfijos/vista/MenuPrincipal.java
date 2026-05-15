package com.activosfijos.vista;

import com.activosfijos.servicio.GraficosServicio;
import com.activosfijos.servicio.EstadosServicio;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class MenuPrincipal extends JFrame {
    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");
    private boolean modoOscuro = false;
    private final EstadosServicio estadosServicio = new EstadosServicio();

    public MenuPrincipal(String usuario, String rol) {
        setTitle("Activos Fijos - Menú Principal");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL_MEDIANOCHE);
        cabecera.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel bienvenida = new JLabel(" Usuario: " + usuario + "  |  Rol: " + rol);
        bienvenida.setForeground(Color.WHITE);

        JButton botonTema = crearBoton("Modo Claro/Oscuro", FontAwesomeSolid.ADJUST);
        botonTema.addActionListener(e -> alternarTema());

        JButton botonBackup = crearBoton("Generar Backup", FontAwesomeSolid.SAVE);
        botonBackup.addActionListener(e -> ejecutarBackup());

        JPanel panelDerechaCabecera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerechaCabecera.setOpaque(false);
        panelDerechaCabecera.add(botonTema);
        panelDerechaCabecera.add(botonBackup);

        cabecera.add(bienvenida, BorderLayout.WEST);
        cabecera.add(panelDerechaCabecera, BorderLayout.EAST);

        JPanel centro = new JPanel(new BorderLayout(20, 20));
        centro.setBackground(BLANCO_NIEVE);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        acciones.setBackground(BLANCO_NIEVE);
        JButton botonGestion = crearBoton("Ir a Gestión de Activos", FontAwesomeSolid.SEARCH);
        botonGestion.addActionListener(e -> new GestionActivos().setVisible(true));
        acciones.add(botonGestion);

        if ("admin".equalsIgnoreCase(rol)) {
            JButton botonUsuarios = crearBoton("Administrar Usuarios", FontAwesomeSolid.USER_PLUS);
            botonUsuarios.addActionListener(e -> new GestionUsuarios().setVisible(true));
            acciones.add(botonUsuarios);
        }

        JPanel graficoPanel = new GraficosServicio().crearGraficoEstados(estadosServicio.obtenerEstados());
        graficoPanel.setBorder(BorderFactory.createTitledBorder("Resumen de estados"));

        centro.add(acciones, BorderLayout.NORTH);
        centro.add(graficoPanel, BorderLayout.CENTER);

        principal.add(cabecera, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);
        setContentPane(principal);
    }


    private void ejecutarBackup() {
        try {
            String destino = new File("backup_activos.sql").getAbsolutePath();
            ProcessBuilder pb = new ProcessBuilder("mysqldump", "-h", "localhost", "-P", "3306", "-u", "root", "-proot", "activos_fijos", "--result-file=" + destino);
            Process p = pb.start();
            int code = p.waitFor();
            JOptionPane.showMessageDialog(this, code == 0 ? "Backup creado: " + destino : "Falló el backup. Código: " + code);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error ejecutando backup: " + ex.getMessage());
        }
    }

    private void alternarTema() {
        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            modoOscuro = !modoOscuro;
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No fue posible cambiar el tema: " + ex.getMessage());
        }
    }

    private JButton crearBoton(String texto, FontAwesomeSolid icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, Color.WHITE));
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    private static class RoundedBorder implements Border {
        private final int radius;
        private RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(10, 16, 10, 16); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { g.setColor(AZUL_MEDIANOCHE); g.drawRoundRect(x, y, width - 1, height - 1, radius, radius); }
    }
}
