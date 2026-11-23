package cl.nutrivida.reservas.dto;

import lombok.Data;

@Data
public class ServicioExternoDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer precio;
    private String imagen;
}