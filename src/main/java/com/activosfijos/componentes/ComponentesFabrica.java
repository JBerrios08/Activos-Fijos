package com.activosfijos.componentes;

import com.activosfijos.util.TemaVisual;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class ComponentesFabrica {
    private ComponentesFabrica() {
    }

    public static JButton crearBotonConIcono(String texto, Ikon icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setIconTextGap(8);
        boton.setFocusPainted(false);
        boton.setBackground(UIManager.getColor("Button.background"));
        boton.setForeground(UIManager.getColor("Button.foreground"));
        boton.setBorder(new BordeRedondeado(12));
        return boton;
    }

    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(UIManager.getColor("Button.default.background"));
        boton.setForeground(UIManager.getColor("Button.default.foreground"));
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBorder(new BordeRedondeado(12));
        return boton;
    }

    public static JButton crearBotonEnlaceConIcono(String texto, Ikon icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, TemaVisual.AZUL_INSTITUCIONAL));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setForeground(TemaVisual.AZUL_INSTITUCIONAL);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static JTextField crearCampoTexto(int tamano) {
        JTextField campo = new JTextField(tamano);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return campo;
    }

    public static JPasswordField crearCampoClave(int tamano) {
        JPasswordField campo = new JPasswordField(tamano);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return campo;
    }

    public static JLabel crearEtiquetaFormulario(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return etiqueta;
    }

    public static JLabel crearTituloSeccion(String texto, int tamano, Color color) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, tamano));
        titulo.setForeground(color);
        return titulo;
    }

    public static JComboBox<String> crearComboOpciones(String[] opciones) {
        return new JComboBox<>(opciones);
    }

    private static class BordeRedondeado implements Border {
        private final int radio;

        private BordeRedondeado(int radio) {
            this.radio = radio;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 16, 8, 16);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(TemaVisual.AZUL_INSTITUCIONAL);
            g.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
        }
    }
}
