package com.activosfijos.vista;

import com.activosfijos.componentes.ComponentesFabrica;
import com.activosfijos.util.ConexionBaseDatos;
import com.activosfijos.util.TemaVisual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormularioRegistro extends JFrame {

    private final JTextField campoNombreUsuario;
    private final JPasswordField campoClave;
    private final JComboBox<String> comboRol;
    private final JFrame loginFrame;
    private final JLabel etiquetaLogo = new JLabel();

    public FormularioRegistro(JFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Registro de Usuario");
        setSize(560, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel campoUsuarioUI = ComponentesFabrica.crearCampoUsuario();
        JPanel campoClaveUI = ComponentesFabrica.crearCampoContrasena();
        JPanel campoRolUI = ComponentesFabrica.crearCampoRol(new String[]{"operador", "administrador"});
        campoNombreUsuario = ComponentesFabrica.extraerCampoTexto(campoUsuarioUI);
        campoClave = ComponentesFabrica.extraerCampoClave(campoClaveUI);
        comboRol = ComponentesFabrica.extraerCampoRol(campoRolUI);

        setContentPane(crearPanelPrincipal(campoUsuarioUI, campoClaveUI, campoRolUI));
        actualizarLogoPorTema();
    }

    private JPanel crearPanelPrincipal(JPanel campoUsuarioUI, JPanel campoClaveUI, JPanel campoRolUI) {
        JPanel panelBase = new JPanel(new GridBagLayout());
        panelBase.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(10, 24, 10, 24));
        formulario.setPreferredSize(new Dimension(430, 420));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);

        etiquetaLogo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel titulo = new JLabel("Crear nueva cuenta");
        titulo.setForeground(TemaVisual.AZUL_INSTITUCIONAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton botonRegistrar = ComponentesFabrica.crearBotonRegistrarse();
        botonRegistrar.addActionListener(e -> registrarUsuario());

        gbc.gridy = 0;
        formulario.add(etiquetaLogo, gbc);
        gbc.gridy = 1;
        formulario.add(titulo, gbc);
        gbc.gridy = 2;
        formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Nombre de usuario"), gbc);
        gbc.gridy = 3;
        formulario.add(campoUsuarioUI, gbc);
        gbc.gridy = 4;
        formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Clave"), gbc);
        gbc.gridy = 5;
        formulario.add(campoClaveUI, gbc);
        gbc.gridy = 6;
        formulario.add(ComponentesFabrica.crearEtiquetaFormulario("Rol"), gbc);
        gbc.gridy = 7;
        formulario.add(campoRolUI, gbc);
        gbc.gridy = 8;
        formulario.add(botonRegistrar, gbc);

        panelBase.add(formulario);
        return panelBase;
    }

    private void actualizarLogoPorTema() {
        URL rutaLogo = getClass().getResource(TemaVisual.esModoOscuroActivo() ? "/imagenes/logo_unab_oscuro.png" : "/imagenes/logo_unab_claro.png");
        if (rutaLogo == null) {
            etiquetaLogo.setText("UNAB");
            etiquetaLogo.setForeground(TemaVisual.AZUL_INSTITUCIONAL);
            etiquetaLogo.setIcon(null);
            return;
        }
        etiquetaLogo.setText("");
        ImageIcon icono = new ImageIcon(rutaLogo);
        Image escalada = icono.getImage().getScaledInstance(220, 72, Image.SCALE_SMOOTH);
        etiquetaLogo.setIcon(new ImageIcon(escalada));
    }

    private void registrarUsuario() {
        String nombreUsuario = campoNombreUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();

        if (nombreUsuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de usuario y clave son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO usuarios (nombre_usuario, clave, rol) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, clave);
            ps.setString(3, rol);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (loginFrame != null) {
                loginFrame.setVisible(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No fue posible registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
