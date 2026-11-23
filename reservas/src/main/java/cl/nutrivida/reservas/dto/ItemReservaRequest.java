package cl.nutrivida.reservas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemReservaRequest {

    @NotNull(message = "El identificador del servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "El identificador del bloque horario es obligatorio")
    private Long horarioId;
}