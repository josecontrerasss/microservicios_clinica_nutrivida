package cl.nutrivida.reservas.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_boleta", nullable = false, unique = true, length = 30)
    private String numeroBoleta;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "usuario_correo", nullable = false, length = 100)
    private String usuarioCorreo;

    @Column(name = "usuario_nombre", nullable = false, length = 150)
    private String usuarioNombre;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, length = 20)
    private String estado = "PAGADA";

    @Column(name = "fecha_reserva", nullable = false, updatable = false)
    private LocalDateTime fechaReserva;

    @JsonManagedReference
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleReserva> detalles = new ArrayList<>();

    @PrePersist
    protected void asignarFechaReserva() {
        this.fechaReserva = LocalDateTime.now();
    }

    public void agregarDetalle(DetalleReserva detalle) {
        detalle.setReserva(this);
        this.detalles.add(detalle);
    }
}