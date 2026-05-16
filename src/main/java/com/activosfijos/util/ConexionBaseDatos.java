package com.activosfijos.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBaseDatos {

    private static final String USUARIO = "root";
    private static final String CLAVE = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST_POR_DEFECTO = "localhost";
    private static final String PUERTO_POR_DEFECTO = "3307";
    private static final String BASE_DATOS = "activos_fijos";
    private static final String ARCHIVO_CONFIG = "config.properties";
    private static final Properties PROPIEDADES = cargarPropiedades();
    private static final String URL = construirUrl();

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("No se encontró el driver de MySQL: " + ex.getMessage());
        }
    }

    private ConexionBaseDatos() {
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        Path rutaConfig = Paths.get(ARCHIVO_CONFIG);
        if (Files.exists(rutaConfig)) {
            try (InputStream entrada = Files.newInputStream(rutaConfig)) {
                propiedades.load(entrada);
            } catch (IOException ex) {
                throw new ExceptionInInitializerError("No se pudo cargar " + ARCHIVO_CONFIG + ": " + ex.getMessage());
            }
        }
        return propiedades;
    }

    private static String construirUrl() {
        String host = PROPIEDADES.getProperty("db.host", HOST_POR_DEFECTO).trim();
        String puerto = PROPIEDADES.getProperty("db.port", PUERTO_POR_DEFECTO).trim();
        return "jdbc:mysql://" + host + ":" + puerto + "/" + BASE_DATOS;
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
