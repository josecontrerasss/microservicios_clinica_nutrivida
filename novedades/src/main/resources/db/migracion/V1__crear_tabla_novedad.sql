CREATE TABLE novedad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    resumen VARCHAR(300) NOT NULL,
    imagen VARCHAR(255) NOT NULL,
    parrafo1 TEXT NOT NULL,
    parrafo2 TEXT NOT NULL,
    parrafo3 TEXT NOT NULL,
    fecha_publicacion DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);