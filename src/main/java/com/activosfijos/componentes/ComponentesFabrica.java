package com.activosfijos.componentes;

import com.activosfijos.servicio.AudioServicio;
import com.activosfijos.util.TemaVisual;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
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
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setBackground(UIManager.getColor("Button.default.background") != null ? UIManager.getColor("Button.default.background") : TemaVisual.AZUL_INSTITUCIONAL);
        boton.setForeground(UIManager.getColor("Button.default.foreground") != null ? UIManager.getColor("Button.default.foreground") : TemaVisual.GRIS_CLARO);
        boton.setBorder(new BordeRedondeado(12));
        boton.addActionListener(e -> AudioServicio.reproducirClick());
        return boton;
    }

    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setBackground(UIManager.getColor("Button.background") != null ? UIManager.getColor("Button.background") : TemaVisual.AZUL_INSTITUCIONAL);
        boton.setForeground(UIManager.getColor("Button.foreground") != null ? UIManager.getColor("Button.foreground") : TemaVisual.GRIS_CLARO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBorder(new BordeRedondeado(12));
        boton.addActionListener(e -> AudioServicio.reproducirClick());
        return boton;
    }

    public static JButton crearBotonRegresar() { return crearBotonConIcono("Regresar", FontAwesomeSolid.ARROW_LEFT); }
    public static JButton crearBotonGuardar() { return crearBotonConIcono("Guardar", FontAwesomeSolid.SAVE); }
    public static JButton crearBotonModificar() { return crearBotonConIcono("Modificar", FontAwesomeSolid.EDIT); }
    public static JButton crearBotonEliminar() { return crearBotonConIcono("Eliminar", FontAwesomeSolid.TRASH_ALT); }
    public static JButton crearBotonPdf() { return crearBotonConIcono("PDF", FontAwesomeSolid.FILE_PDF); }
    public static JButton crearBotonLimpiar() { return crearBotonConIcono("Limpiar", FontAwesomeSolid.ERASER); }
    public static JButton crearBotonCsv() { return crearBotonConIcono("CSV", FontAwesomeSolid.FILE_CSV); }
    public static JButton crearBotonDepreciacion() { return crearBotonConIcono("Calcular Depreciación", FontAwesomeSolid.CALCULATOR); }
    public static JButton crearBotonCrearUsuario() { return crearBotonConIcono("Crear", FontAwesomeSolid.USER_PLUS); }
    public static JButton crearBotonEditarUsuario() { return crearBotonConIcono("Editar", FontAwesomeSolid.SEARCH); }
    public static JButton crearBotonCrearCuenta() { return crearBotonEnlaceConIcono("Crear Cuenta", FontAwesomeSolid.USER_PLUS); }

    public static JButton crearBotonEnlaceConIcono(String texto, Ikon icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, TemaVisual.AZUL_INSTITUCIONAL));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setForeground(TemaVisual.AZUL_INSTITUCIONAL);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(e -> AudioServicio.reproducirClick());
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
