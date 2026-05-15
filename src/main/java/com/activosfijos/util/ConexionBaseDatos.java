package com.activosfijos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión con MySQL.
 * Nota para el equipo (Alexis): mantener credenciales en variables de entorno en producción.
 */
public final class ConexionBaseDatos {

    private static final String URL = "jdbc:mysql://localhost:3306/activos_fijos?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private ConexionBaseDatos() {
        // Constructor privado para evitar instancias.
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
