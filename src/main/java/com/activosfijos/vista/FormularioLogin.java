package com.activosfijos.vista;

import com.activosfijos.servicio.AutenticacionServicio;
import com.activosfijos.util.TemaVisual;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FormularioLogin extends JFrame {


    private final JTextField campoUsuario;
    private final JPasswordField campoClave;
    private final AutenticacionServicio autenticacionServicio = new AutenticacionServicio();

    public FormularioLogin() {
        setTitle("Activos Fijos - Inicio de Sesión");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel contenedor = new JPanel(new BorderLayout());

        JPanel barraLateral = new JPanel();
        barraLateral.setBackground(TemaVisual.AZUL_INSTITUCIONAL);
        barraLateral.setPreferredSize(new Dimension(280, 650));
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBorder(new EmptyBorder(80, 24, 24, 24));

        JLabel etiquetaSistema = new JLabel("ACTIVOS-FIJOS");
        etiquetaSistema.setFont(new Font("Segoe UI", Font.BOLD, 30));
        etiquetaSistema.setForeground(TemaVisual.GRIS_CLARO);
        JLabel etiquetaSub = new JLabel("Control moderno de inventario");
        etiquetaSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        etiquetaSub.setForeground(TemaVisual.GRIS_CLARO);

        barraLateral.add(etiquetaSistema);
        barraLateral.add(Box.createVerticalStrut(10));
        barraLateral.add(etiquetaSub);

        JPanel panelLogin = new JPanel(new GridBagLayout());

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoUsuario = new JTextField(20);
        campoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel etiquetaClave = new JLabel("Clave");
        etiquetaClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoClave = new JPasswordField(20);
        campoClave.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton botonIngresar = new JButton("Ingresar");
        botonIngresar.setBackground(UIManager.getColor("Button.default.background"));
        botonIngresar.setForeground(UIManager.getColor("Button.default.foreground"));
        botonIngresar.setFocusPainted(false);
        botonIngresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonIngresar.addActionListener(e -> iniciarSesion());

        JButton botonCrearCuenta = new JButton("Crear Cuenta", FontIcon.of(FontAwesomeSolid.USER_PLUS, 14, TemaVisual.AZUL_INSTITUCIONAL));
        botonCrearCuenta.setBorderPainted(false);
        botonCrearCuenta.setContentAreaFilled(false);
        botonCrearCuenta.setFocusPainted(false);
        botonCrearCuenta.setForeground(TemaVisual.AZUL_INSTITUCIONAL);
        botonCrearCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonCrearCuenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCrearCuenta.addActionListener(e -> {
            new FormularioRegistro(this).setVisible(true);
            setVisible(false);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(etiquetaUsuario, gbc);

        gbc.gridy = 1;
        formulario.add(campoUsuario, gbc);

        gbc.gridy = 2;
        formulario.add(etiquetaClave, gbc);

        gbc.gridy = 3;
        formulario.add(campoClave, gbc);

        gbc.gridy = 4;
        formulario.add(botonIngresar, gbc);

        gbc.gridy = 5;
        formulario.add(botonCrearCuenta, gbc);

        panelLogin.add(formulario);

        contenedor.add(barraLateral, BorderLayout.WEST);
        contenedor.add(panelLogin, BorderLayout.CENTER);

        setContentPane(contenedor);
    }

    private void iniciarSesion() {
        String nombreUsuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();

        if (nombreUsuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete usuario y clave.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String rol = autenticacionServicio.autenticar(nombreUsuario, clave);
            if (rol == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String codigo = JOptionPane.showInputDialog(this, "Ingrese código de verificación", "Segundo paso", JOptionPane.QUESTION_MESSAGE);
            if ("1234".equals(codigo)) {
                SwingUtilities.invokeLater(() -> {
                    new MenuPrincipal(nombreUsuario, rol).setVisible(true);
                    dispose();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Código de verificación incorrecto.", "MFA", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        TemaVisual.aplicarTema(false);
        SwingUtilities.invokeLater(() -> new FormularioLogin().setVisible(true));
    }
}
