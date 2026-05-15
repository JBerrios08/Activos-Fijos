package com.activosfijos.vista;

import com.activosfijos.util.ConexionBaseDatos;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Pantalla de autenticación principal.
 */
public class FormularioLogin extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private final JTextField campoUsuario;
    private final JPasswordField campoClave;

    public FormularioLogin() {
        setTitle("Activos Fijos - Inicio de Sesión");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(BLANCO_NIEVE);

        JPanel barraLateral = new JPanel();
        barraLateral.setBackground(AZUL_MEDIANOCHE);
        barraLateral.setPreferredSize(new Dimension(280, 650));
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBorder(new EmptyBorder(80, 24, 24, 24));

        JLabel etiquetaSistema = new JLabel("ACTIVOS-FIJOS");
        etiquetaSistema.setFont(new Font("Segoe UI", Font.BOLD, 30));
        etiquetaSistema.setForeground(BLANCO_NIEVE);
        JLabel etiquetaSub = new JLabel("Control moderno de inventario");
        etiquetaSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        etiquetaSub.setForeground(BLANCO_NIEVE);

        barraLateral.add(etiquetaSistema);
        barraLateral.add(Box.createVerticalStrut(10));
        barraLateral.add(etiquetaSub);

        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(BLANCO_NIEVE);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO_NIEVE);
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
        botonIngresar.setBackground(AZUL_ELECTRICO);
        botonIngresar.setForeground(Color.WHITE);
        botonIngresar.setFocusPainted(false);
        botonIngresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonIngresar.addActionListener(e -> iniciarSesion());

        JButton botonRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        botonRegistro.setBorderPainted(false);
        botonRegistro.setContentAreaFilled(false);
        botonRegistro.setFocusPainted(false);
        botonRegistro.setForeground(AZUL_ELECTRICO);
        botonRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonRegistro.addActionListener(e -> new RegistroUsuario().setVisible(true));

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
        formulario.add(botonRegistro, gbc);

        panelLogin.add(formulario);

        contenedor.add(barraLateral, BorderLayout.WEST);
        contenedor.add(panelLogin, BorderLayout.CENTER);

        setContentPane(contenedor);
    }

    private void iniciarSesion() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete usuario y clave.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT rol FROM usuarios WHERE nombre_usuario = ? AND clave = ?";

        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    SwingUtilities.invokeLater(() -> {
                        new MenuPrincipal(usuario, rol).setVisible(true);
                        dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new FormularioLogin().setVisible(true));
    }
}
