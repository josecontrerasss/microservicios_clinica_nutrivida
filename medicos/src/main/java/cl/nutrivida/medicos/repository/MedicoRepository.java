package cl.nutrivida.medicos.repository;

import cl.nutrivida.medicos.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByActivoTrue();
    List<Medico> findByEspecialidadContainingIgnoreCaseAndActivoTrue(String especialidad);
}