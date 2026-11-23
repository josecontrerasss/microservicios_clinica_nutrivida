package cl.nutrivida.reservas.repository;

import cl.nutrivida.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioIdOrderByFechaReservaDesc(Long usuarioId);
    List<Reserva> findAllByOrderByFechaReservaDesc();
    long countByUsuarioId(Long usuarioId);
}