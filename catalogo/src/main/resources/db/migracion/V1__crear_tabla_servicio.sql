CREATE TABLE servicio (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    precio INT NOT NULL,
    imagen VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

