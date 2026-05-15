package com.activosfijos.vista;

import com.activosfijos.util.ConexionBaseDatos;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Módulo para creación, edición y eliminación de usuarios.
 */
public class GestionUsuarios extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private JTextField campoUsuario;
    private JPasswordField campoClave;
    private JComboBox<String> comboRol;

    public GestionUsuarios() {
        setTitle("Activos Fijos - Gestión de Usuarios");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titulo = new JLabel("Administración de Usuarios");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(AZUL_MEDIANOCHE);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO_NIEVE);
        formulario.setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoUsuario = new JTextField();
        campoClave = new JPasswordField();
        comboRol = new JComboBox<>(new String[]{"Administrador", "Operador"});

        agregarCampo(formulario, gbc, 0, "Usuario", campoUsuario);
        agregarCampo(formulario, gbc, 1, "Clave", campoClave);
        agregarCampo(formulario, gbc, 2, "Rol", comboRol);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setBackground(BLANCO_NIEVE);

        JButton botonCrear = crearBotonAccion("Crear", cargarIcono("guardar"));
        JButton botonEditar = crearBotonAccion("Editar", cargarIcono("editar"));
        JButton botonEliminar = crearBotonAccion("Eliminar", cargarIcono("eliminar"));

        botonCrear.addActionListener(e -> crearUsuario());
        botonEditar.addActionListener(e -> editarUsuario());
        botonEliminar.addActionListener(e -> eliminarUsuario());

        acciones.add(botonCrear);
        acciones.add(botonEditar);
        acciones.add(botonEliminar);

        principal.add(titulo, BorderLayout.NORTH);
        principal.add(formulario, BorderLayout.CENTER);
        principal.add(acciones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.25;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        panel.add(componente, gbc);
    }

    private JButton crearBotonAccion(String texto, Icon icono) {
        JButton boton = new JButton(texto, icono);
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    public static Icon cargarIcono(String nombre) {
        String ruta = "/iconos/" + nombre + ".png";
        URL recurso = GestionUsuarios.class.getResource(ruta);
        if (recurso != null) {
            return new ImageIcon(recurso);
        }
        return UIManager.getIcon("OptionPane.informationIcon");
    }

    private void crearUsuario() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y clave son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO usuarios (nombre_usuario, clave, rol) VALUES (?, ?, ?)";
        try (Connection cn = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ps.setString(3, mapearRol(rol));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique el usuario a editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE usuarios SET clave = ?, rol = ? WHERE nombre_usuario = ?";
        try (Connection cn = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.setString(2, mapearRol(rol));
            ps.setString(3, usuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        String usuario = campoUsuario.getText().trim();
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique el usuario a eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "DELETE FROM usuarios WHERE nombre_usuario = ?";
        try (Connection cn = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String mapearRol(String rol) {
        return "Administrador".equalsIgnoreCase(rol) ? "admin" : "operador";
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        private RoundedBorder(int radius) {
            this.radius = radius;
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
            g.setColor(AZUL_MEDIANOCHE);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
