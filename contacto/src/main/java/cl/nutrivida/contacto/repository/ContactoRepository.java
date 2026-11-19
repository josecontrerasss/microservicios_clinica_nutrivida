package cl.nutrivida.contacto.repository;

import cl.nutrivida.contacto.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByEstado(String estado);
}