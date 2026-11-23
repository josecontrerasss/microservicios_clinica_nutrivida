package cl.nutrivida.reservas.dto;

import lombok.Data;

@Data
public class MedicoExternoDto {
    private Long id;
    private String nombreCompleto;
    private String especialidad;
    private String imagen;
    private boolean activo;
}