package com.activosfijos.vista;

import com.activosfijos.componentes.ComponentesFabrica;
import com.activosfijos.servicio.UsuariosServicio;
import com.activosfijos.util.TemaVisual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GestionUsuarios extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoClave;
    private JComboBox<String> comboRol;
    private final UsuariosServicio usuariosServicio = new UsuariosServicio();

    public GestionUsuarios() {
        setTitle("Activos Fijos - Gestión de Usuarios");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenidoPrincipal());
    }

    private JPanel crearContenidoPrincipal() {
        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));
        principal.add(crearEncabezado(), BorderLayout.NORTH);
        principal.add(crearFormulario(), BorderLayout.CENTER);
        principal.add(crearAcciones(), BorderLayout.SOUTH);
        return principal;
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JButton botonRegresar = ComponentesFabrica.crearBotonRegresar();
        botonRegresar.addActionListener(e -> regresarAlMenuPrincipal());
        encabezado.add(botonRegresar, BorderLayout.WEST);
        encabezado.add(ComponentesFabrica.crearTituloSeccion("Administración de Usuarios", 22, TemaVisual.AZUL_INSTITUCIONAL), BorderLayout.CENTER);
        return encabezado;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoUsuario = ComponentesFabrica.crearCampoTexto(20);
        campoClave = ComponentesFabrica.crearCampoClave(20);
        comboRol = ComponentesFabrica.crearComboOpciones(new String[]{"Administrador", "Operador"});

        agregarCampo(formulario, gbc, 0, "Usuario", campoUsuario);
        agregarCampo(formulario, gbc, 1, "Clave", campoClave);
        agregarCampo(formulario, gbc, 2, "Rol", comboRol);
        return formulario;
    }

    private JPanel crearAcciones() {
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton botonCrear = ComponentesFabrica.crearBotonCrearUsuario();
        JButton botonEditar = ComponentesFabrica.crearBotonEditarUsuario();
        JButton botonEliminar = ComponentesFabrica.crearBotonEliminar();
        botonCrear.addActionListener(e -> crearUsuario());
        botonEditar.addActionListener(e -> editarUsuario());
        botonEliminar.addActionListener(e -> eliminarUsuario());
        acciones.add(botonCrear);
        acciones.add(botonEditar);
        acciones.add(botonEliminar);
        return acciones;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.25; panel.add(ComponentesFabrica.crearEtiquetaFormulario(etiqueta), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75; panel.add(componente, gbc);
    }

    private void crearUsuario() { String usuario = campoUsuario.getText().trim(); String clave = new String(campoClave.getPassword()).trim(); String rol = (String) comboRol.getSelectedItem(); if (usuario.isEmpty() || clave.isEmpty()) { JOptionPane.showMessageDialog(this, "Usuario y clave son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE); return; } try { usuariosServicio.insertar(usuario, clave, mapearRol(rol)); JOptionPane.showMessageDialog(this, "Usuario creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); } }
    private void editarUsuario() { String usuario = campoUsuario.getText().trim(); String clave = new String(campoClave.getPassword()).trim(); String rol = (String) comboRol.getSelectedItem(); if (usuario.isEmpty()) { JOptionPane.showMessageDialog(this, "Indique el usuario a editar.", "Validación", JOptionPane.WARNING_MESSAGE); return; } try { int filas = usuariosServicio.actualizar(usuario, clave, mapearRol(rol)); if (filas > 0) { JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE); } else { JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE); } } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); } }
    private void eliminarUsuario() { String usuario = campoUsuario.getText().trim(); if (usuario.isEmpty()) { JOptionPane.showMessageDialog(this, "Indique el usuario a eliminar.", "Validación", JOptionPane.WARNING_MESSAGE); return; } try { int filas = usuariosServicio.eliminar(usuario); if (filas > 0) { JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE); } else { JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE); } } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); } }

    private void regresarAlMenuPrincipal() { Window menu = buscarMenuPrincipal(); dispose(); if (menu != null) { menu.setVisible(true); menu.toFront(); menu.requestFocus(); } }
    private Window buscarMenuPrincipal() { for (Window ventana : Window.getWindows()) { if (ventana instanceof MenuPrincipal) { return ventana; } } return null; }
    private String mapearRol(String rol) { return "Administrador".equalsIgnoreCase(rol) ? "admin" : "operador"; }
}
