package com.activosfijos.vista;

import com.activosfijos.util.ConexionBaseDatos;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Formulario base para administración de activos.
 */
public class GestionActivos extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private JTextField campoCodigoBarra;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSerie;
    private JTextField campoValorCompra;
    private JComboBox<String> comboEstado;
    private DefaultTableModel modeloTabla;
    private Border bordeCodigoOriginal;
    private Border bordeNombreOriginal;

    public GestionActivos() {
        setTitle("Activos Fijos - Gestión de Activos");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(AZUL_MEDIANOCHE);
        titulo.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel etiquetaTitulo = new JLabel("Módulo de Gestión de Activos");
        etiquetaTitulo.setForeground(Color.WHITE);
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.add(etiquetaTitulo, BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO_NIEVE);
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigoBarra = new JTextField();
        campoNombre = new JTextField();
        campoDescripcion = new JTextField();
        campoSerie = new JTextField();
        campoValorCompra = new JTextField();
        comboEstado = new JComboBox<>(new String[]{"Disponible", "Asignado", "Mantenimiento", "Baja"});

        campoCodigoBarra.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoSerie.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoValorCompra.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        bordeCodigoOriginal = campoCodigoBarra.getBorder();
        bordeNombreOriginal = campoNombre.getBorder();

        agregarCampo(formulario, gbc, 0, "Código de Barra", campoCodigoBarra);
        agregarCampo(formulario, gbc, 1, "Nombre", campoNombre);
        agregarCampo(formulario, gbc, 2, "Descripción", campoDescripcion);
        agregarCampo(formulario, gbc, 3, "Serie", campoSerie);
        agregarCampo(formulario, gbc, 4, "Valor de Compra", campoValorCompra);
        agregarCampo(formulario, gbc, 5, "Estado", comboEstado);

        JTable tablaActivos = crearTablaModerna();
        JScrollPane scroll = new JScrollPane(tablaActivos);
        scroll.setBorder(new EmptyBorder(0, 8, 0, 8));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        acciones.setBackground(BLANCO_NIEVE);
        acciones.setBorder(new EmptyBorder(0, 0, 8, 8));

        JButton botonGuardar = crearBotonAccion("Guardar", FontAwesomeSolid.SAVE);
        JButton botonEliminar = crearBotonAccion("Eliminar", FontAwesomeSolid.TRASH_ALT);
        JButton botonBuscar = crearBotonAccion("Buscar", FontAwesomeSolid.SEARCH);

        botonGuardar.addActionListener(e -> guardarActivo());

        acciones.add(botonGuardar);
        acciones.add(botonEliminar);
        acciones.add(botonBuscar);

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setBackground(BLANCO_NIEVE);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        principal.add(titulo, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);
        principal.add(acciones, BorderLayout.SOUTH);

        setContentPane(principal);
        cargarTabla();
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(componente, gbc);
    }

    private JTable crearTablaModerna() {
        String[] columnas = {"Código", "Nombre", "Serie", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(AZUL_MEDIANOCHE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        return tabla;
    }

    private JButton crearBotonAccion(String texto, FontAwesomeSolid icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, Color.WHITE));
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(new RoundedBorder(12));
        return boton;
    }

    private void guardarActivo() {
        boolean invalido = false;
        campoNombre.setBorder(bordeNombreOriginal);
        campoCodigoBarra.setBorder(bordeCodigoOriginal);

        if (campoNombre.getText().trim().isEmpty()) {
            campoNombre.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            invalido = true;
        }
        if (campoCodigoBarra.getText().trim().isEmpty()) {
            campoCodigoBarra.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            invalido = true;
        }

        if (invalido) {
            JOptionPane.showMessageDialog(this, "Nombre y Código de Barra son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO activos (codigo_barra, nombre_activo, descripcion, numero_serie, valor_compra, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, campoCodigoBarra.getText().trim());
            ps.setString(2, campoNombre.getText().trim());
            ps.setString(3, campoDescripcion.getText().trim());
            ps.setString(4, campoSerie.getText().trim());

            String valorTexto = campoValorCompra.getText().trim();
            if (valorTexto.isEmpty()) {
                ps.setNull(5, java.sql.Types.DECIMAL);
            } else {
                double valorCompra = Double.parseDouble(valorTexto);
                ps.setDouble(5, valorCompra);
            }

            ps.setString(6, (String) comboEstado.getSelectedItem());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Activo guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            System.err.println("Error SQL al guardar activo: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No fue posible guardar el activo.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Valor de compra inválido.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        String sql = "SELECT codigo_barra, nombre_activo, numero_serie, estado FROM activos ORDER BY id_activo DESC";

        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = {
                        rs.getString("codigo_barra"),
                        rs.getString("nombre_activo"),
                        rs.getString("numero_serie"),
                        rs.getString("estado")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException ex) {
            System.err.println("Error SQL al cargar activos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        campoCodigoBarra.setText("");
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoSerie.setText("");
        campoValorCompra.setText("");
        comboEstado.setSelectedIndex(0);
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
