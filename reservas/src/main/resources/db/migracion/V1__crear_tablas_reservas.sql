CREATE TABLE reserva (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_boleta VARCHAR(30) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    usuario_correo VARCHAR(100) NOT NULL,
    usuario_nombre VARCHAR(150) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    total INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PAGADA',
    fecha_reserva DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE detalle_reserva (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reserva_id BIGINT NOT NULL,
    servicio_id BIGINT NOT NULL,
    servicio_titulo VARCHAR(100) NOT NULL,
    precio_unitario INT NOT NULL,
    medico_id BIGINT NOT NULL,
    medico_nombre VARCHAR(120) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    horario_id BIGINT NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_reserva FOREIGN KEY (reserva_id) REFERENCES reserva (id) ON DELETE CASCADE
);