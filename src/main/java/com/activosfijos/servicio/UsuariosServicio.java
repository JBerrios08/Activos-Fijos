package com.activosfijos.servicio;

import com.activosfijos.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuariosServicio {

    public int insertar(String usuario, String clave, String rol) {
        String sql = "INSERT INTO usuarios (nombre_usuario, clave, rol) VALUES (?, ?, ?)";
        return ejecutarActualizacion(sql, clave, rol, usuario, true);
    }

    public int actualizar(String usuario, String clave, String rol) {
        String sql = "UPDATE usuarios SET clave = ?, rol = ? WHERE nombre_usuario = ?";
        return ejecutarActualizacion(sql, clave, rol, usuario, false);
    }

    public int eliminar(String usuario) {
        String sql = "DELETE FROM usuarios WHERE nombre_usuario = ?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error SQL al eliminar usuario: " + ex.getMessage(), ex);
        } finally {
            ConexionBaseDatos.cerrarSilencioso(ps);
            ConexionBaseDatos.cerrarSilencioso(conexion);
        }
    }

    private int ejecutarActualizacion(String sql, String clave, String rol, String usuario, boolean insertar) {
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            if (insertar) {
                ps.setString(1, usuario);
                ps.setString(2, clave);
                ps.setString(3, rol);
            } else {
                ps.setString(1, clave);
                ps.setString(2, rol);
                ps.setString(3, usuario);
            }
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error SQL en usuarios: " + ex.getMessage(), ex);
        } finally {
            ConexionBaseDatos.cerrarSilencioso(ps);
            ConexionBaseDatos.cerrarSilencioso(conexion);
        }
    }
}
