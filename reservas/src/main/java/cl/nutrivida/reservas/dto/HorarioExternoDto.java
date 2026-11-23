package cl.nutrivida.reservas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class HorarioExternoDto {
    private Long id;
    private MedicoExternoDto medico;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private boolean disponible;
}