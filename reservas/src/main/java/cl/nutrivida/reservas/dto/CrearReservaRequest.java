package cl.nutrivida.reservas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class CrearReservaRequest {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String usuarioNombre;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @Valid
    @NotEmpty(message = "Debes incluir al menos un servicio con horario en la reserva")
    private List<ItemReservaRequest> items;
}