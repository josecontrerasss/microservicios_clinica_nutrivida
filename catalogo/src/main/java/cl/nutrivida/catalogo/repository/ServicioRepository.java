package cl.nutrivida.catalogo.repository;

import cl.nutrivida.catalogo.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByTituloContainingIgnoreCase(String titulo);
}