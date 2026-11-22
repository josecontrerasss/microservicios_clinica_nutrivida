package cl.nutrivida.medicos.config;

import cl.nutrivida.medicos.model.HorarioMedico;
import cl.nutrivida.medicos.model.Medico;
import cl.nutrivida.medicos.repository.HorarioMedicoRepository;
import cl.nutrivida.medicos.repository.MedicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MedicoRepository medicoRepository;
    private final HorarioMedicoRepository horarioRepository;

    public DataInitializer(MedicoRepository medicoRepository, HorarioMedicoRepository horarioRepository) {
        this.medicoRepository = medicoRepository;
        this.horarioRepository = horarioRepository;
    }

    @Override
    public void run(String... args) {
        if (medicoRepository.count() == 0) {
            Medico m1 = new Medico();
            m1.setNombreCompleto("Diego Sepúlveda");
            m1.setEspecialidad("Nutrición deportiva");
            m1.setImagen("/Imagenes/Doctores/doktor1.jpg");
            m1.setActivo(true);

            Medico m2 = new Medico();
            m2.setNombreCompleto("Carolina Muñoz");
            m2.setEspecialidad("Pérdida de peso");
            m2.setImagen("/Imagenes/Doctores/doktor3.jpg");
            m2.setActivo(true);

            Medico m3 = new Medico();
            m3.setNombreCompleto("Andrés Painemal");
            m3.setEspecialidad("Enfermedades metabólicas");
            m3.setImagen("/Imagenes/Doctores/doktor2.jpg");
            m3.setActivo(true);

            Medico m4 = new Medico();
            m4.setNombreCompleto("Paula Riquelme");
            m4.setEspecialidad("Alimentación vegetariana");
            m4.setImagen("/Imagenes/Doctores/doktor4.jpg");
            m4.setActivo(true);

            List<Medico> medicosGuardados = medicoRepository.saveAll(List.of(m1, m2, m3, m4));

            LocalDate manana = LocalDate.now().plusDays(1);
            LocalDate pasadoManana = LocalDate.now().plusDays(2);

            for (Medico medico : medicosGuardados) {
                horarioRepository.saveAll(List.of(
                        crearBloque(medico, manana, LocalTime.of(9, 0)),
                        crearBloque(medico, manana, LocalTime.of(10, 30)),
                        crearBloque(medico, manana, LocalTime.of(15, 0)),
                        crearBloque(medico, pasadoManana, LocalTime.of(9, 30)),
                        crearBloque(medico, pasadoManana, LocalTime.of(16, 0))
                ));
            }
        }
    }

    private HorarioMedico crearBloque(Medico medico, LocalDate fecha, LocalTime hora) {
        HorarioMedico horario = new HorarioMedico();
        horario.setMedico(medico);
        horario.setFecha(fecha);
        horario.setHoraInicio(hora);
        horario.setDisponible(true);
        return horario;
    }
}
