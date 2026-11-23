package cl.nutrivida.novedades.repository;

import cl.nutrivida.novedades.model.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NovedadRepository extends JpaRepository<Novedad, Long> {
    List<Novedad> findByTituloContainingIgnoreCaseOrderByIdAsc(String titulo);
    List<Novedad> findAllByOrderByIdAsc();
}