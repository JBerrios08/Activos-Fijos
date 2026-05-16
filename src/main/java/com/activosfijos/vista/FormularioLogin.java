package com.activosfijos.vista;

import com.activosfijos.componentes.ComponentesFabrica;
import com.activosfijos.servicio.AutenticacionServicio;
import com.activosfijos.util.TemaVisual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FormularioLogin extends JFrame {
    private final JTextField campoUsuario;
    private final JPasswordField campoClave;
    private final AutenticacionServicio autenticacionServicio = new AutenticacionServicio();
    private final JLabel etiquetaLogo = new JLabel();

    public FormularioLogin() {
        setTitle("Activos Fijos - Inicio de Sesión");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        campoUsuario = ComponentesFabrica.crearCampoTexto(20);
        campoClave = ComponentesFabrica.crearCampoClave(20);
        setContentPane(crearContenedorPrincipal());
        actualizarLogoPorTema();
    }

    private JPanel crearContenedorPrincipal() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(crearBarraLateral(), BorderLayout.WEST);
        contenedor.add(crearPanelLogin(), BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearBarraLateral() {
        JPanel barraLateral = new JPanel();
        barraLateral.setBackground(TemaVisual.AZUL_INSTITUCIONAL);
        barraLateral.setPreferredSize(new Dimension(280, 650));
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBorder(new EmptyBorder(50, 24, 24, 24));
        etiquetaLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraLateral.add(etiquetaLogo);
        barraLateral.add(Box.createVerticalStrut(24));
        barraLateral.add(ComponentesFabrica.crearTituloSeccion("ACTIVOS-FIJOS", 30, TemaVisual.GRIS_CLARO));
        barraLateral.add(Box.createVerticalStrut(10));
        barraLateral.add(ComponentesFabrica.crearEtiquetaFormulario("Control moderno de inventario"));
        barraLateral.getComponent(4).setForeground(TemaVisual.GRIS_CLARO);
        return barraLateral;
    }

    private JPanel crearPanelLogin() {
        JPanel panelLogin = new JPanel(new GridBagLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton botonIngresar = ComponentesFabrica.crearBotonPrimario("Ingresar");
        botonIngresar.addActionListener(e -> iniciarSesion());
        JButton botonCrearCuenta = ComponentesFabrica.crearBotonCrearCuenta();
        botonCrearCuenta.addActionListener(e -> { new FormularioRegistro(this).setVisible(true); setVisible(false); });

        gbc.gridx = 0; gbc.gridy = 0; formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Usuario"), gbc);
        gbc.gridy = 1; formulario.add(campoUsuario, gbc);
        gbc.gridy = 2; formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Clave"), gbc);
        gbc.gridy = 3; formulario.add(campoClave, gbc);
        gbc.gridy = 4; formulario.add(botonIngresar, gbc);
        gbc.gridy = 5; formulario.add(botonCrearCuenta, gbc);

        panelLogin.add(formulario);
        return panelLogin;
    }

    private void actualizarLogoPorTema() {
        ImageIcon icono = new ImageIcon(getClass().getResource(TemaVisual.rutaLogoSegunTema()));
        Image escalada = icono.getImage().getScaledInstance(210, 70, Image.SCALE_SMOOTH);
        etiquetaLogo.setIcon(new ImageIcon(escalada));
    }

    private void iniciarSesion() {
        String nombreUsuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        if (nombreUsuario.isEmpty() || clave.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete usuario y clave.", "Validación", JOptionPane.WARNING_MESSAGE); return; }
        try {
            String rol = autenticacionServicio.autenticar(nombreUsuario, clave);
            if (rol == null) { JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE); return; }
            String codigo = JOptionPane.showInputDialog(this, "Ingrese código de verificación", "Segundo paso", JOptionPane.QUESTION_MESSAGE);
            if ("1234".equals(codigo)) { SwingUtilities.invokeLater(() -> { new MenuPrincipal(nombreUsuario, rol).setVisible(true); dispose(); }); }
            else { JOptionPane.showMessageDialog(this, "Código de verificación incorrecto.", "MFA", JOptionPane.ERROR_MESSAGE); }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        TemaVisual.aplicarTema(false);
        SwingUtilities.invokeLater(() -> new FormularioLogin().setVisible(true));
    }
}
