package com.activosfijos.vista;

import com.activosfijos.util.ConexionBaseDatos;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormularioRegistro extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private final JTextField campoNombre;
    private final JPasswordField campoClave;
    private final JComboBox<String> comboRol;
    private final JFrame loginFrame;

    public FormularioRegistro(JFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Registro de Usuario");
        setSize(500, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO_NIEVE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Crear nueva cuenta");
        titulo.setForeground(AZUL_MEDIANOCHE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        campoNombre = new JTextField(20);
        campoClave = new JPasswordField(20);
        comboRol = new JComboBox<>(new String[]{"operador", "administrador"});

        JButton botonRegistrar = new JButton("Registrar", FontIcon.of(FontAwesomeSolid.USER_PLUS, 14, Color.WHITE));
        botonRegistrar.setBackground(AZUL_ELECTRICO);
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.addActionListener(e -> registrarUsuario());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre"), gbc);
        gbc.gridx = 1;
        panel.add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Clave"), gbc);
        gbc.gridx = 1;
        panel.add(campoClave, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Rol"), gbc);
        gbc.gridx = 1;
        panel.add(comboRol, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(botonRegistrar, gbc);

        setContentPane(panel);
    }

    private void registrarUsuario() {
        String nombre = campoNombre.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();

        if (nombre.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y clave son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO usuarios (nombre, clave, rol) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, clave);
            ps.setString(3, rol);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (loginFrame != null) {
                loginFrame.setVisible(true);
            }
        } catch (SQLException ex) {
            System.err.println("Error SQL al registrar usuario: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No fue posible registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
