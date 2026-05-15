package com.activosfijos.vista;

import com.activosfijos.servicio.EstadosServicio;
import com.activosfijos.servicio.GraficosServicio;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class MenuPrincipal extends JFrame {
    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");
    private boolean modoOscuro;
    private final EstadosServicio estadosServicio = new EstadosServicio();
    private final GraficosServicio graficosServicio = new GraficosServicio();

    public MenuPrincipal(String usuario, String rol) {
        setTitle("Activos Fijos - Menú Principal");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL_MEDIANOCHE);
        cabecera.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel bienvenida = new JLabel(" Usuario: " + usuario + "  |  Rol: " + rol);
        bienvenida.setForeground(UIManager.getColor("Label.foreground"));

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
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JButton botonGestion = crearBoton("Ir a Gestión de Activos", FontAwesomeSolid.TOOLS);
        botonGestion.addActionListener(e -> new GestionActivos().setVisible(true));
        acciones.add(botonGestion);

        if ("admin".equalsIgnoreCase(rol)) {
            JButton botonUsuarios = crearBoton("Administrar Usuarios", FontAwesomeSolid.USER_PLUS);
            botonUsuarios.addActionListener(e -> new GestionUsuarios().setVisible(true));
            acciones.add(botonUsuarios);
        }

        JPanel graficoPanel = estadosServicio.noHayActivosRegistrados() ? graficosServicio.crearPanelSinDatos() : graficosServicio.crearGraficoEstados(estadosServicio.obtenerEstados());
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
            for (Window ventana : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(ventana);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No fue posible cambiar el tema: " + ex.getMessage());
        }
    }

    private JButton crearBoton(String texto, FontAwesomeSolid icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setIconTextGap(8);
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(UIManager.getColor("Button.foreground"));
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
