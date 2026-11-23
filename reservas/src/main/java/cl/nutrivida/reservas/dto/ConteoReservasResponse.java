package cl.nutrivida.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConteoReservasResponse {
    private Long usuarioId;
    private String usuarioCorreo;
    private long totalReservas;
    private long totalCitasAgendadas;
}