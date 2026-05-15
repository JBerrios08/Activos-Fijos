package com.activosfijos.servicio;

import com.activosfijos.util.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivosServicio {

    public record ActivoFila(int id, String codigo, String nombre, String descripcion, String serie, Double valorCompra, String estado) {}

    public void insertar(String codigoBarra, String nombre, String descripcion, String serie, String valorCompra, String estado) {
        String sql = "INSERT INTO activos (codigo_barra, nombre_activo, descripcion, numero_serie, valor_compra, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoBarra);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setString(4, serie);
            asignarValorCompra(ps, valorCompra, 5);
            ps.setString(6, estado);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("No fue posible guardar el activo: " + ex.getMessage(), ex);
        }
    }

    public int actualizar(int id, String codigoBarra, String nombre, String descripcion, String serie, String valorCompra, String estado) {
        String sql = "UPDATE activos SET codigo_barra=?, nombre_activo=?, descripcion=?, numero_serie=?, valor_compra=?, estado=? WHERE id_activo=?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoBarra);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setString(4, serie);
            asignarValorCompra(ps, valorCompra, 5);
            ps.setString(6, estado);
            ps.setInt(7, id);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("No fue posible actualizar el activo: " + ex.getMessage(), ex);
        }
    }

    public int eliminar(int id) {
        String sql = "DELETE FROM activos WHERE id_activo=?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("No fue posible eliminar el activo: " + ex.getMessage(), ex);
        }
    }

    public List<ActivoFila> listar() {
        List<ActivoFila> filas = new ArrayList<>();
        String sql = "SELECT id_activo, codigo_barra, nombre_activo, descripcion, numero_serie, valor_compra, estado FROM activos ORDER BY id_activo DESC";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double valor = rs.getDouble("valor_compra");
                Double valorCompra = rs.wasNull() ? null : valor;
                filas.add(new ActivoFila(rs.getInt("id_activo"), rs.getString("codigo_barra"), rs.getString("nombre_activo"), rs.getString("descripcion"), rs.getString("numero_serie"), valorCompra, rs.getString("estado")));
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

    private void asignarValorCompra(PreparedStatement ps, String valorCompra, int indice) throws SQLException {
        if (valorCompra == null || valorCompra.isBlank()) {
            ps.setNull(indice, Types.DECIMAL);
        } else {
            ps.setDouble(indice, Double.parseDouble(valorCompra));
        }
    }
}
