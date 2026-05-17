package com.activosfijos.vista;

import com.activosfijos.componentes.ComponentesFabrica;
import com.activosfijos.servicio.AutenticacionServicio;
import com.activosfijos.util.TemaVisual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

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

        JPanel campoUsuarioUI = ComponentesFabrica.crearCampoUsuario();
        JPanel campoClaveUI = ComponentesFabrica.crearCampoContrasena();
        campoUsuario = ComponentesFabrica.extraerCampoTexto(campoUsuarioUI);
        campoClave = ComponentesFabrica.extraerCampoClave(campoClaveUI);
        setContentPane(crearContenedorPrincipal(campoUsuarioUI, campoClaveUI));
        actualizarLogoPorTema();
    }

    private JPanel crearContenedorPrincipal(JPanel campoUsuarioUI, JPanel campoClaveUI) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(crearBarraLateral(), BorderLayout.WEST);
        contenedor.add(crearPanelLogin(campoUsuarioUI, campoClaveUI), BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearBarraLateral() {
        JPanel barraLateral = new JPanel();
        barraLateral.setBackground(TemaVisual.AZUL_INSTITUCIONAL);
        barraLateral.setPreferredSize(new Dimension(280, 650));
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBorder(new EmptyBorder(50, 24, 24, 24));
        etiquetaLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraLateral.add(etiquetaLogo);
        barraLateral.add(Box.createVerticalStrut(24));
        JLabel titulo = ComponentesFabrica.crearTituloSeccion("ACTIVOS-FIJOS", 30, TemaVisual.GRIS_CLARO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraLateral.add(titulo);
        barraLateral.add(Box.createVerticalStrut(10));
        JLabel subtitulo = ComponentesFabrica.crearEtiquetaFormulario("Control de inventario UNAB");
        subtitulo.setForeground(TemaVisual.GRIS_CLARO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraLateral.add(subtitulo);
        return barraLateral;
    }

    private JPanel crearPanelLogin(JPanel campoUsuarioUI, JPanel campoClaveUI) {
        JPanel panelLogin = new JPanel(new GridBagLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(24, 40, 24, 40));
        formulario.setPreferredSize(new Dimension(440, 330));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);

        JButton botonIngresar = ComponentesFabrica.crearBotonIngresar();
        botonIngresar.addActionListener(e -> iniciarSesion());
        JButton botonCrearCuenta = ComponentesFabrica.crearBotonRegistrarse();
        botonCrearCuenta.addActionListener(e -> {
            new FormularioRegistro(this).setVisible(true);
            setVisible(false);
        });

        gbc.gridy = 0;
        formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Usuario"), gbc);
        gbc.gridy = 1;
        formulario.add(campoUsuarioUI, gbc);
        gbc.gridy = 2;
        formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Clave"), gbc);
        gbc.gridy = 3;
        formulario.add(campoClaveUI, gbc);
        gbc.gridy = 4;
        formulario.add(botonIngresar, gbc);
        gbc.gridy = 5;
        formulario.add(botonCrearCuenta, gbc);

        panelLogin.add(formulario);
        return panelLogin;
    }

    private void actualizarLogoPorTema() {
        URL rutaLogo = getClass().getResource(TemaVisual.esModoOscuroActivo() ? "/imagenes/logo_unab_oscuro.png" : "/imagenes/logo_unab_claro.png");
        if (rutaLogo == null) {
            etiquetaLogo.setIcon(null);
            etiquetaLogo.setText("UNAB Activos");
            etiquetaLogo.setForeground(TemaVisual.GRIS_CLARO);
            return;
        }
        etiquetaLogo.setText("");
        ImageIcon icono = new ImageIcon(rutaLogo);
        Image escalada = icono.getImage().getScaledInstance(210, 70, Image.SCALE_SMOOTH);
        etiquetaLogo.setIcon(new ImageIcon(escalada));
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
