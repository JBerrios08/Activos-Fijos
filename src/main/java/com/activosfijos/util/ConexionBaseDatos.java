package com.activosfijos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBaseDatos {

    private static final String URL = "jdbc:mysql://localhost:3306/activos_fijos";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("No se encontró el driver de MySQL: " + ex.getMessage());
        }
    }

    private ConexionBaseDatos() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static void cerrarSilencioso(AutoCloseable recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
