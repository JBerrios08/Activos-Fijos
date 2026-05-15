package com.activosfijos.servicio;

import com.activosfijos.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AutenticacionServicio {

    public String autenticar(String usuario, String clave) {
        String sql = "SELECT rol FROM usuarios WHERE nombre_usuario = ? AND clave = ?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rol");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error de conexión: " + ex.getMessage(), ex);
        }
        return null;
    }
}
