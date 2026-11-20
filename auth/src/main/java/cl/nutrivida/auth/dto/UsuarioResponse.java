package cl.nutrivida.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String rol;
}