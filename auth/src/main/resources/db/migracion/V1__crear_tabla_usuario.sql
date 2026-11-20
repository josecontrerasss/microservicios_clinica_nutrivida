CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    run VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'PACIENTE',
    region VARCHAR(100) NULL,
    comuna VARCHAR(100) NULL,
    direccion VARCHAR(300) NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);