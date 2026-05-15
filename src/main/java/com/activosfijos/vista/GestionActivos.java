package com.activosfijos.vista;

import com.activosfijos.componentes.ComponentesFabrica;
import com.activosfijos.servicio.ActivosServicio;
import com.activosfijos.servicio.ReportesServicio;
import com.activosfijos.util.TemaVisual;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;

public class GestionActivos extends JFrame {
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
    private Integer idActivoSeleccionado;

    public GestionActivos() {
        setTitle("Activos Fijos - Gestión de Activos");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenidoPrincipal());
        cargarTabla();
    }

    private JPanel crearContenidoPrincipal() {
        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));
        principal.add(crearEncabezado(), BorderLayout.NORTH);
        principal.add(crearCentro(), BorderLayout.CENTER);
        principal.add(crearAcciones(), BorderLayout.SOUTH);
        return principal;
    }

    private JPanel crearEncabezado() {
        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(TemaVisual.AZUL_INSTITUCIONAL);
        titulo.setBorder(new EmptyBorder(12, 16, 12, 16));
        JButton botonRegresar = ComponentesFabrica.crearBotonRegresar();
        botonRegresar.addActionListener(e -> regresarAlMenuPrincipal());
        JLabel tituloModulo = ComponentesFabrica.crearTituloSeccion("Módulo de Gestión de Activos", 20, UIManager.getColor("Label.foreground"));
        titulo.add(botonRegresar, BorderLayout.WEST);
        titulo.add(tituloModulo, BorderLayout.CENTER);
        return titulo;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(8, 8));
        JPanel formulario = crearFormulario();
        JScrollPane scrollTabla = new JScrollPane(crearTablaActivos());
        scrollTabla.setPreferredSize(new Dimension(0, 360));
        scrollTabla.setMinimumSize(new Dimension(0, 325));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formulario, scrollTabla);
        split.setResizeWeight(0.38);
        split.setDividerLocation(245);
        split.setDividerSize(6);
        split.setBorder(null);
        centro.add(split, BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new BorderLayout(10, 8));
        formulario.setBorder(new EmptyBorder(8, 12, 8, 12));
        JPanel campos = new JPanel(new GridLayout(4, 2, 12, 6));

        campoCodigoBarra = ComponentesFabrica.crearCampoTexto(20);
        campoNombre = ComponentesFabrica.crearCampoTexto(20);
        campoDescripcion = ComponentesFabrica.crearCampoTexto(20);
        campoSerie = ComponentesFabrica.crearCampoTexto(20);
        campoValorCompra = ComponentesFabrica.crearCampoTexto(20);
        etiquetaIva = ComponentesFabrica.crearEtiquetaFormulario("IVA (13%): 0.00");
        comboEstado = ComponentesFabrica.crearComboOpciones(new String[]{"Disponible", "Asignado", "Mantenimiento", "Baja"});
        campoBuscar = ComponentesFabrica.crearCampoTexto(20);

        bordeCodigoOriginal = campoCodigoBarra.getBorder();
        bordeNombreOriginal = campoNombre.getBorder();

        campoValorCompra.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarIva(); }
            public void removeUpdate(DocumentEvent e) { actualizarIva(); }
            public void changedUpdate(DocumentEvent e) { actualizarIva(); }
        });
        campoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarTabla(); }
            public void removeUpdate(DocumentEvent e) { filtrarTabla(); }
            public void changedUpdate(DocumentEvent e) { filtrarTabla(); }
        });

        campos.add(crearCampoFormulario("Código de Barra", campoCodigoBarra));
        campos.add(crearCampoFormulario("Nombre", campoNombre));
        campos.add(crearCampoFormulario("Descripción", campoDescripcion));
        campos.add(crearCampoFormulario("Serie", campoSerie));
        campos.add(crearCampoFormulario("Valor de Compra", campoValorCompra));
        campos.add(crearCampoFormulario("Estado", comboEstado));
        campos.add(crearCampoFormulario("Buscar", campoBuscar));
        campos.add(crearCampoFormulario("", etiquetaIva));
        formulario.add(campos, BorderLayout.CENTER);
        return formulario;
    }

    private JTable crearTablaActivos() {
        String[] columnas = {"ID", "Código", "Nombre", "Descripción", "Serie", "Valor", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tablaActivos = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tablaActivos.setRowSorter(sorter);
        tablaActivos.setDefaultRenderer(Object.class, new EstadoRenderer());
        tablaActivos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarActivoSeleccionadoEnFormulario();
            }
        });
        JTableHeader header = tablaActivos.getTableHeader();
        header.setBackground(UIManager.getColor("TableHeader.background"));
        header.setForeground(UIManager.getColor("TableHeader.foreground"));
        return tablaActivos;
    }

    private JPanel crearAcciones() {
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton botonGuardar = ComponentesFabrica.crearBotonGuardar();
        JButton botonModificar = ComponentesFabrica.crearBotonModificar();
        JButton botonEliminar = ComponentesFabrica.crearBotonEliminar();
        JButton botonPdf = ComponentesFabrica.crearBotonPdf();
        JButton botonLimpiar = ComponentesFabrica.crearBotonLimpiar();
        JButton botonCsv = ComponentesFabrica.crearBotonCsv();
        JButton botonDepreciacion = ComponentesFabrica.crearBotonDepreciacion();

        botonGuardar.addActionListener(e -> guardarActivo());
        botonModificar.addActionListener(e -> modificarActivo());
        botonEliminar.addActionListener(e -> eliminarActivo());
        botonPdf.addActionListener(e -> exportarPdf());
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonCsv.addActionListener(e -> exportarCsv());
        botonDepreciacion.addActionListener(e -> calcularDepreciacionLineaRecta());

        acciones.add(botonGuardar);
        acciones.add(botonModificar);
        acciones.add(botonEliminar);
        acciones.add(botonPdf);
        acciones.add(botonLimpiar);
        acciones.add(botonCsv);
        acciones.add(botonDepreciacion);
        return acciones;
    }

    private void modificarActivo() { if (idActivoSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un activo.", "Validación", JOptionPane.WARNING_MESSAGE); return; } try { int filas = activosServicio.actualizar(idActivoSeleccionado, campoCodigoBarra.getText().trim(), campoNombre.getText().trim(), campoDescripcion.getText().trim(), campoSerie.getText().trim(), campoValorCompra.getText().trim(), (String) comboEstado.getSelectedItem()); if (filas > 0) { JOptionPane.showMessageDialog(this, "Activo modificado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE); refrescarTabla(); limpiarFormulario(); } } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);} }
    private void eliminarActivo() { if (idActivoSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un activo.", "Validación", JOptionPane.WARNING_MESSAGE); return; } int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el activo seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); if (opcion != JOptionPane.YES_OPTION) { return; } try { int filas = activosServicio.eliminar(idActivoSeleccionado); if (filas > 0) { JOptionPane.showMessageDialog(this, "Activo eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE); refrescarTabla(); limpiarFormulario(); } } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);} }

    private void cargarActivoSeleccionadoEnFormulario() {
        int vista = tablaActivos.getSelectedRow();
        if (vista < 0) {
            return;
        }
        int fila = tablaActivos.convertRowIndexToModel(vista);
        idActivoSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoCodigoBarra.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        campoNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        campoDescripcion.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        campoSerie.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        Object valor = modeloTabla.getValueAt(fila, 5);
        campoValorCompra.setText(valor == null ? "" : String.valueOf(valor));
        comboEstado.setSelectedItem(String.valueOf(modeloTabla.getValueAt(fila, 6)));
        actualizarIva();
    }

    private void refrescarTabla() { modeloTabla.setRowCount(0); cargarTabla(); }
    private void actualizarIva() { try { double valor = Double.parseDouble(campoValorCompra.getText().trim()); etiquetaIva.setText(String.format("IVA (13%%): %.2f", valor * 0.13)); } catch (Exception e) { etiquetaIva.setText("IVA (13%): 0.00"); } }
    private void filtrarTabla() { String texto = campoBuscar.getText().trim(); sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto)); }
    private void exportarPdf() { JFileChooser chooser = new JFileChooser(); chooser.setSelectedFile(new File("activos.pdf")); if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { try { reportesServicio.exportarTablaAPdf(tablaActivos.getModel(), chooser.getSelectedFile()); JOptionPane.showMessageDialog(this, "PDF generado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);} } }
    private void exportarCsv() { JFileChooser chooser = new JFileChooser(); chooser.setSelectedFile(new File("activos.csv")); if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { try { reportesServicio.exportarTablaACsv(tablaActivos.getModel(), chooser.getSelectedFile()); JOptionPane.showMessageDialog(this, "CSV generado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al generar CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);} } }

    private JPanel crearCampoFormulario(String etiqueta, JComponent componente) {
        JPanel campo = new JPanel(new BorderLayout(0, 4));
        campo.setOpaque(false);
        if (!etiqueta.isBlank()) {
            campo.add(ComponentesFabrica.crearEtiquetaFormulario(etiqueta), BorderLayout.NORTH);
        }
        campo.add(componente, BorderLayout.CENTER);
        return campo;
    }

    private void guardarActivo() {
        boolean invalido = false;
        campoNombre.setBorder(bordeNombreOriginal);
        campoCodigoBarra.setBorder(bordeCodigoOriginal);
        if (campoNombre.getText().trim().isEmpty()) { campoNombre.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); invalido = true; }
        if (campoCodigoBarra.getText().trim().isEmpty()) { campoCodigoBarra.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); invalido = true; }
        if (invalido) { JOptionPane.showMessageDialog(this, "Nombre y Código de Barra son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE); return; }
        try { activosServicio.insertar(campoCodigoBarra.getText().trim(), campoNombre.getText().trim(), campoDescripcion.getText().trim(), campoSerie.getText().trim(), campoValorCompra.getText().trim(), (String) comboEstado.getSelectedItem()); JOptionPane.showMessageDialog(this, "Activo guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE); limpiarFormulario(); refrescarTabla(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void cargarTabla() {
        try {
            for (ActivosServicio.ActivoFila fila : activosServicio.listar()) {
                modeloTabla.addRow(new Object[]{fila.id(), fila.codigo(), fila.nombre(), fila.descripcion(), fila.serie(), fila.valorCompra(), fila.estado()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idActivoSeleccionado = null;
        tablaActivos.clearSelection();
        campoCodigoBarra.setText("");
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoSerie.setText("");
        campoValorCompra.setText("");
        comboEstado.setSelectedIndex(0);
        actualizarIva();
    }

    private void calcularDepreciacionLineaRecta() {
        try {
            double valorCompra = Double.parseDouble(campoValorCompra.getText().trim());
            String vidaUtilTexto = JOptionPane.showInputDialog(this, "Vida Útil (en años):");
            if (vidaUtilTexto == null) { return; }
            String valorRescateTexto = JOptionPane.showInputDialog(this, "Valor de Rescate:");
            if (valorRescateTexto == null) { return; }
            int vidaUtil = Integer.parseInt(vidaUtilTexto.trim());
            double valorRescate = Double.parseDouble(valorRescateTexto.trim());
            double depreciacionAnual = activosServicio.calcularDepreciacionLineaRecta(valorCompra, valorRescate, vidaUtil);
            JOptionPane.showMessageDialog(this, String.format("Depreciación anual: $%,.2f", depreciacionAnual), "Resultado de Depreciación", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingresa números válidos para el cálculo de depreciación.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void regresarAlMenuPrincipal() { Window menu = buscarMenuPrincipal(); dispose(); if (menu != null) { menu.setVisible(true); menu.toFront(); menu.requestFocus(); } }
    private Window buscarMenuPrincipal() { for (Window ventana : Window.getWindows()) { if (ventana instanceof MenuPrincipal) { return ventana; } } return null; }

    private static class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int modelRow = table.convertRowIndexToModel(row);
            String estado = String.valueOf(table.getModel().getValueAt(modelRow, 6));
            if (!isSelected) {
                if ("Mantenimiento".equalsIgnoreCase(estado)) { c.setBackground(new Color(255, 248, 196)); }
                else if ("Baja".equalsIgnoreCase(estado)) { c.setBackground(new Color(255, 220, 220)); }
                else { c.setBackground(UIManager.getColor("Table.background")); }
            }
            return c;
        }
    }
}
