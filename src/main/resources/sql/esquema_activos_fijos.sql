CREATE DATABASE IF NOT EXISTS activos_fijos;
USE activos_fijos;

-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'operador'
);

-- Tabla de activos inventariados
CREATE TABLE IF NOT EXISTS activos (
    id_activo INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barra VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    serie VARCHAR(50),
    valor_compra DECIMAL(12, 2),
    fecha_ingreso DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('Disponible', 'Asignado', 'Mantenimiento', 'Baja') NOT NULL DEFAULT 'Disponible'
);

-- Usuario inicial de pruebas
INSERT INTO usuarios (nombre_usuario, clave, rol)
VALUES ('admin', 'admin123', 'administrador')
ON DUPLICATE KEY UPDATE nombre_usuario = VALUES(nombre_usuario);
