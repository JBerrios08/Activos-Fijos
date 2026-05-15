package com.activosfijos.servicio;

import com.activosfijos.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadosServicio {

    public Map<String, Integer> obtenerEstados() {
        Map<String, Integer> estados = new LinkedHashMap<>();
        String sql = "SELECT estado, COUNT(*) total FROM activos GROUP BY estado";
        try (Connection conn = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                estados.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (Exception ex) {
            throw new RuntimeException("No fue posible cargar estados: " + ex.getMessage(), ex);
        }
        return estados;
    }

    public boolean noHayActivosRegistrados() {
        String sql = "SELECT COUNT(*) total FROM activos";
        try (Connection conn = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total") == 0;
            }
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("No fue posible validar activos registrados: " + ex.getMessage(), ex);
        }
    }
}
