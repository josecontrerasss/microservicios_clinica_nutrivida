package cl.nutrivida.novedades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "novedad")
public class Novedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "El resumen es obligatorio")
    @Size(max = 300, message = "El resumen no puede superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String resumen;

    @NotBlank(message = "La imagen es obligatoria")
    @Size(max = 255, message = "La ruta de imagen no puede superar los 255 caracteres")
    @Column(nullable = false, length = 255)
    private String imagen;

    @NotBlank(message = "El primer párrafo es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String parrafo1;

    @NotBlank(message = "El segundo párrafo es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String parrafo2;

    @NotBlank(message = "El tercer párrafo es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String parrafo3;

    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @PrePersist
    protected void asignarFechaPublicacion() {
        this.fechaPublicacion = LocalDateTime.now();
    }
}