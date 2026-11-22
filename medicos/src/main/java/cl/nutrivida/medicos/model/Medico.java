package cl.nutrivida.medicos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String especialidad;

    @NotBlank(message = "La ruta de imagen es obligatoria")
    @Size(max = 255, message = "La ruta de imagen no puede superar los 255 caracteres")
    @Column(nullable = false, length = 255)
    private String imagen;

    @Column(nullable = false)
    private Boolean activo = true;
}
