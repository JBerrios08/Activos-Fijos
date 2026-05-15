package com.activosfijos.servicio;

import com.activosfijos.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivosServicio {

    public record ActivoFila(String codigo, String nombre, String serie, String estado) {}

    public void insertar(String codigoBarra, String nombre, String descripcion, String serie, String valorCompra, String estado) {
        String sql = "INSERT INTO activos (codigo_barra, nombre_activo, descripcion, numero_serie, valor_compra, estado) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoBarra);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setString(4, serie);
            if (valorCompra == null || valorCompra.isBlank()) {
                ps.setNull(5, java.sql.Types.DECIMAL);
            } else {
                ps.setDouble(5, Double.parseDouble(valorCompra));
            }
            ps.setString(6, estado);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("No fue posible guardar el activo: " + ex.getMessage(), ex);
        } finally {
            ConexionBaseDatos.cerrarSilencioso(ps);
            ConexionBaseDatos.cerrarSilencioso(conexion);
        }
    }

    public List<ActivoFila> listar() {
        List<ActivoFila> filas = new ArrayList<>();
        String sql = "SELECT codigo_barra, nombre_activo, numero_serie, estado FROM activos ORDER BY id_activo DESC";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new ActivoFila(rs.getString("codigo_barra"), rs.getString("nombre_activo"), rs.getString("numero_serie"), rs.getString("estado")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("No fue posible cargar los activos: " + ex.getMessage(), ex);
        }
        return filas;
    }

    public double calcularDepreciacionLineaRecta(double valorCompra, double valorRescate, int vidaUtil) {
        validarDatosDepreciacion(valorCompra, valorRescate, vidaUtil);
        return (valorCompra - valorRescate) / vidaUtil;
    }

    private void validarDatosDepreciacion(double valorCompra, double valorRescate, int vidaUtil) {
        if (vidaUtil <= 0) {
            throw new IllegalArgumentException("La vida útil debe ser mayor a cero.");
        }
        if (valorCompra <= 0) {
            throw new IllegalArgumentException("El valor de compra debe ser mayor a cero.");
        }
        if (valorRescate < 0) {
            throw new IllegalArgumentException("El valor de rescate no puede ser negativo.");
        }
        if (valorRescate >= valorCompra) {
            throw new IllegalArgumentException("El valor de rescate debe ser menor al valor de compra.");
        }
    }

}
