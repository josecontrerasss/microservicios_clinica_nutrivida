package cl.nutrivida.catalogo.config;

import cl.nutrivida.catalogo.model.Servicio;
import cl.nutrivida.catalogo.repository.ServicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ServicioRepository repository;

    public DataInitializer(ServicioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            Servicio s1 = new Servicio();
            s1.setTitulo("Nutrición Deportiva");
            s1.setPrecio(25000);
            s1.setDescripcion("Plan alimenticio enfocado en el rendimiento físico y recuperación.");
            s1.setImagen("/Imagenes/Servicios/deportiva.jpg");

            Servicio s2 = new Servicio();
            s2.setTitulo("Control de Peso");
            s2.setPrecio(20000);
            s2.setDescripcion("Evaluación y seguimiento para alcanzar tu peso ideal de forma sana.");
            s2.setImagen("/Imagenes/Servicios/peso.jpg");

            Servicio s3 = new Servicio();
            s3.setTitulo("Nutrición Infantil");
            s3.setPrecio(22000);
            s3.setDescripcion("Asesoría nutricional para el desarrollo saludable de los niños.");
            s3.setImagen("/Imagenes/Servicios/infantil.jpg");

            Servicio s4 = new Servicio();
            s4.setTitulo("Asesoría Vegana/Vegetariana");
            s4.setPrecio(23000);
            s4.setDescripcion("Transición y mantenimiento seguro de dietas basadas en plantas.");
            s4.setImagen("/Imagenes/Servicios/vegana.jpg");

            repository.saveAll(List.of(s1, s2, s3, s4));
        }
    }
}
