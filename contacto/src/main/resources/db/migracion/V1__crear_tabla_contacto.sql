CREATE TABLE contacto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre_completo VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    respuesta_admin TEXT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    fecha_respuesta DATETIME(6) NULL,
    PRIMARY KEY (id)
);