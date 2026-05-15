package com.activosfijos.vista;

import com.activosfijos.servicio.UsuariosServicio;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
public class GestionUsuarios extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private JTextField campoUsuario;
    private JPasswordField campoClave;
    private JComboBox<String> comboRol;
    private final UsuariosServicio usuariosServicio = new UsuariosServicio();

    public GestionUsuarios() {
        setTitle("Activos Fijos - Gestión de Usuarios");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JButton botonRegresar = crearBotonAccion("Regresar", FontAwesomeSolid.ARROW_LEFT);
        botonRegresar.addActionListener(e -> regresarAlMenuPrincipal());
        JLabel titulo = new JLabel("Administración de Usuarios");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(AZUL_MEDIANOCHE);
        encabezado.add(botonRegresar, BorderLayout.WEST);
        encabezado.add(titulo, BorderLayout.CENTER);

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

        JButton botonCrear = crearBotonAccion("Crear", FontAwesomeSolid.USER_PLUS);
        JButton botonEditar = crearBotonAccion("Editar", FontAwesomeSolid.SEARCH);
        JButton botonEliminar = crearBotonAccion("Eliminar", FontAwesomeSolid.TRASH_ALT);

        botonCrear.addActionListener(e -> crearUsuario());
        botonEditar.addActionListener(e -> editarUsuario());
        botonEliminar.addActionListener(e -> eliminarUsuario());

        acciones.add(botonCrear);
        acciones.add(botonEditar);
        acciones.add(botonEliminar);

        principal.add(encabezado, BorderLayout.NORTH);
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

    private JButton crearBotonAccion(String texto, FontAwesomeSolid icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, Color.WHITE));
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    private void crearUsuario() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();
        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y clave son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            usuariosServicio.insertar(usuario, clave, mapearRol(rol));
            JOptionPane.showMessageDialog(this, "Usuario creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        try {
            int filas = usuariosServicio.actualizar(usuario, clave, mapearRol(rol));
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        String usuario = campoUsuario.getText().trim();
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique el usuario a eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int filas = usuariosServicio.eliminar(usuario);
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void regresarAlMenuPrincipal() {
        Window menu = buscarMenuPrincipal();
        dispose();
        if (menu != null) {
            menu.setVisible(true);
            menu.toFront();
            menu.requestFocus();
        }
    }

    private Window buscarMenuPrincipal() {
        for (Window ventana : Window.getWindows()) {
            if (ventana instanceof MenuPrincipal) {
                return ventana;
            }
        }
        return null;
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
