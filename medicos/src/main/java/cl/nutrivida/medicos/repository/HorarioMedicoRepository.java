package cl.nutrivida.medicos.repository;

import cl.nutrivida.medicos.model.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {
    List<HorarioMedico> findByMedicoIdOrderByFechaAscHoraInicioAsc(Long medicoId);
    List<HorarioMedico> findByMedicoIdAndDisponibleTrueOrderByFechaAscHoraInicioAsc(Long medicoId);
}