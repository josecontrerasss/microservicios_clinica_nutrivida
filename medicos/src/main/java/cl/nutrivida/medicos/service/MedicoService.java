package cl.nutrivida.medicos.service;

import cl.nutrivida.medicos.dto.HorarioRequest;
import cl.nutrivida.medicos.model.HorarioMedico;
import cl.nutrivida.medicos.model.Medico;
import cl.nutrivida.medicos.repository.HorarioMedicoRepository;
import cl.nutrivida.medicos.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final HorarioMedicoRepository horarioRepository;

    public MedicoService(MedicoRepository medicoRepository, HorarioMedicoRepository horarioRepository) {
        this.medicoRepository = medicoRepository;
        this.horarioRepository = horarioRepository;
    }

    public List<Medico> listarMedicos(String especialidad, boolean incluirInactivos) {
        if (incluirInactivos) {
            return medicoRepository.findAll();
        }
        if (especialidad != null && !especialidad.isBlank()) {
            return medicoRepository.findByEspecialidadContainingIgnoreCaseAndActivoTrue(especialidad.trim());
        }
        return medicoRepository.findByActivoTrue();
    }

    public Optional<Medico> obtenerMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico crearMedico(Medico medico) {
        medico.setId(null);
        if (medico.getActivo() == null) {
            medico.setActivo(true);
        }
        return medicoRepository.save(medico);
    }

    public Optional<Medico> actualizarMedico(Long id, Medico datos) {
        return medicoRepository.findById(id).map(existente -> {
            existente.setNombreCompleto(datos.getNombreCompleto());
            existente.setEspecialidad(datos.getEspecialidad());
            existente.setImagen(datos.getImagen());
            if (datos.getActivo() != null) {
                existente.setActivo(datos.getActivo());
            }
            return medicoRepository.save(existente);
        });
    }

    public boolean eliminarMedico(Long id) {
        if (!medicoRepository.existsById(id)) {
            return false;
        }
        medicoRepository.deleteById(id);
        return true;
    }

    public List<HorarioMedico> listarHorariosPorMedico(Long medicoId, boolean soloDisponibles) {
        if (soloDisponibles) {
            return horarioRepository.findByMedicoIdAndDisponibleTrueOrderByFechaAscHoraInicioAsc(medicoId);
        }
        return horarioRepository.findByMedicoIdOrderByFechaAscHoraInicioAsc(medicoId);
    }

    public Optional<HorarioMedico> obtenerHorarioPorId(Long horarioId) {
        return horarioRepository.findById(horarioId);
    }

    public Optional<HorarioMedico> crearHorario(Long medicoId, HorarioRequest request) {
        return medicoRepository.findById(medicoId).map(medico -> {
            HorarioMedico horario = new HorarioMedico();
            horario.setMedico(medico);
            horario.setFecha(request.getFecha());
            horario.setHoraInicio(request.getHoraInicio());
            horario.setDisponible(true);
            return horarioRepository.save(horario);
        });
    }

    public Optional<HorarioMedico> reservarHorario(Long horarioId) {
        return horarioRepository.findById(horarioId).map(horario -> {
            if (!Boolean.TRUE.equals(horario.getDisponible())) {
                throw new IllegalStateException("El bloque horario seleccionado ya no se encuentra disponible");
            }
            horario.setDisponible(false);
            return horarioRepository.save(horario);
        });
    }

    public Optional<HorarioMedico> liberarHorario(Long horarioId) {
        return horarioRepository.findById(horarioId).map(horario -> {
            horario.setDisponible(true);
            return horarioRepository.save(horario);
        });
    }

    public boolean eliminarHorario(Long horarioId) {
        if (!horarioRepository.existsById(horarioId)) {
            return false;
        }
        horarioRepository.deleteById(horarioId);
        return true;
    }
}