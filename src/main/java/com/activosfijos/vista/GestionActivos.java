package com.activosfijos.vista;

import com.activosfijos.servicio.ReportesServicio;
import com.activosfijos.servicio.ActivosServicio;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class GestionActivos extends JFrame {

    private static final Color AZUL_MEDIANOCHE = Color.decode("#2C3E50");
    private static final Color BLANCO_NIEVE = Color.decode("#ECF0F1");
    private static final Color AZUL_ELECTRICO = Color.decode("#3498DB");

    private final ReportesServicio reportesServicio = new ReportesServicio();
    private final ActivosServicio activosServicio = new ActivosServicio();
    private JTextField campoCodigoBarra;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSerie;
    private JTextField campoValorCompra;
    private JLabel etiquetaIva;
    private JTextField campoBuscar;
    private JTable tablaActivos;
    private JComboBox<String> comboEstado;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private Border bordeCodigoOriginal;
    private Border bordeNombreOriginal;

    public GestionActivos() {
        setTitle("Activos Fijos - Gestión de Activos");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(BLANCO_NIEVE);
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(AZUL_MEDIANOCHE);
        titulo.setBorder(new EmptyBorder(12, 16, 12, 16));
        titulo.add(new JLabel("Módulo de Gestión de Activos") {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
        }}, BorderLayout.WEST);

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
        etiquetaIva = new JLabel("IVA (13%): 0.00");
        comboEstado = new JComboBox<>(new String[]{"Disponible", "Asignado", "Mantenimiento", "Baja"});
        campoBuscar = new JTextField();

        campoValorCompra.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarIva(); }
            public void removeUpdate(DocumentEvent e) { actualizarIva(); }
            public void changedUpdate(DocumentEvent e) { actualizarIva(); }
        });

        bordeCodigoOriginal = campoCodigoBarra.getBorder();
        bordeNombreOriginal = campoNombre.getBorder();

        agregarCampo(formulario, gbc, 0, "Código de Barra", campoCodigoBarra);
        agregarCampo(formulario, gbc, 1, "Nombre", campoNombre);
        agregarCampo(formulario, gbc, 2, "Descripción", campoDescripcion);
        agregarCampo(formulario, gbc, 3, "Serie", campoSerie);
        agregarCampo(formulario, gbc, 4, "Valor de Compra", campoValorCompra);
        agregarCampo(formulario, gbc, 5, "", etiquetaIva);
        agregarCampo(formulario, gbc, 6, "Estado", comboEstado);
        agregarCampo(formulario, gbc, 7, "Buscar", campoBuscar);

        tablaActivos = crearTablaModerna();
        JScrollPane scroll = new JScrollPane(tablaActivos);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        acciones.setBackground(BLANCO_NIEVE);

        JButton botonGuardar = crearBotonAccion("Guardar", FontAwesomeSolid.SAVE);
        JButton botonPdf = crearBotonAccion("PDF", FontAwesomeSolid.FILE_PDF);
        JButton botonEliminar = crearBotonAccion("Eliminar", FontAwesomeSolid.TRASH_ALT);
        JButton botonCsv = crearBotonAccion("Buscar", FontAwesomeSolid.SEARCH);

        botonGuardar.addActionListener(e -> guardarActivo());
        botonPdf.addActionListener(e -> exportarPdf());
        botonEliminar.addActionListener(e -> limpiarFormulario());
        botonCsv.addActionListener(e -> exportarCsv());

        acciones.add(botonGuardar);
        acciones.add(botonPdf);
        acciones.add(botonEliminar);
        acciones.add(botonCsv);

        campoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarTabla(); }
            public void removeUpdate(DocumentEvent e) { filtrarTabla(); }
            public void changedUpdate(DocumentEvent e) { filtrarTabla(); }
        });

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

    private void actualizarIva() {
        try {
            double valor = Double.parseDouble(campoValorCompra.getText().trim());
            etiquetaIva.setText(String.format("IVA (13%%): %.2f", valor * 0.13));
        } catch (Exception e) {
            etiquetaIva.setText("IVA (13%): 0.00");
        }
    }

    private void filtrarTabla() {
        String texto = campoBuscar.getText().trim();
        sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
    }

    private JTable crearTablaModerna() {
        String[] columnas = {"Código", "Nombre", "Serie", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabla = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);
        tabla.setDefaultRenderer(Object.class, new EstadoRenderer());
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(AZUL_MEDIANOCHE);
        header.setForeground(Color.WHITE);
        return tabla;
    }

    private void exportarPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("activos.pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                reportesServicio.exportarTablaAPdf(tablaActivos.getModel(), chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "PDF generado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportarCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("activos.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                reportesServicio.exportarTablaACsv(tablaActivos.getModel(), chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "CSV generado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        if (!etiqueta.isBlank()) {
            gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.2;
            panel.add(new JLabel(etiqueta), gbc);
        }
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 0.8; panel.add(componente, gbc);
    }

    private JButton crearBotonAccion(String texto, FontAwesomeSolid icono) {
        JButton boton = new JButton(texto, FontIcon.of(icono, 14, Color.WHITE));
        boton.setBackground(AZUL_ELECTRICO);
        boton.setForeground(Color.WHITE);
        return boton;
    }

    private void guardarActivo() {
        boolean invalido = false;
        campoNombre.setBorder(bordeNombreOriginal);
        campoCodigoBarra.setBorder(bordeCodigoOriginal);
        if (campoNombre.getText().trim().isEmpty()) { campoNombre.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); invalido = true; }
        if (campoCodigoBarra.getText().trim().isEmpty()) { campoCodigoBarra.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); invalido = true; }
        if (invalido) {
            JOptionPane.showMessageDialog(this, "Nombre y Código de Barra son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            activosServicio.insertar(campoCodigoBarra.getText().trim(), campoNombre.getText().trim(), campoDescripcion.getText().trim(), campoSerie.getText().trim(), campoValorCompra.getText().trim(), (String) comboEstado.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Activo guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (ActivosServicio.ActivoFila fila : activosServicio.listar()) {
                modeloTabla.addRow(new Object[]{fila.codigo(), fila.nombre(), fila.serie(), fila.estado()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        campoCodigoBarra.setText(""); campoNombre.setText(""); campoDescripcion.setText(""); campoSerie.setText(""); campoValorCompra.setText(""); comboEstado.setSelectedIndex(0);
        actualizarIva();
    }

    private static class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int modelRow = table.convertRowIndexToModel(row);
            String estado = String.valueOf(table.getModel().getValueAt(modelRow, 3));
            if (!isSelected) {
                if ("Mantenimiento".equalsIgnoreCase(estado)) c.setBackground(new Color(255, 248, 196));
                else if ("Baja".equalsIgnoreCase(estado)) c.setBackground(new Color(255, 220, 220));
                else c.setBackground(Color.WHITE);
            }
            return c;
        }
    }
}
