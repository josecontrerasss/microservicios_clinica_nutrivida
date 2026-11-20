package cl.nutrivida.auth.config;

import cl.nutrivida.auth.model.Usuario;
import cl.nutrivida.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!repository.existsByCorreo("admin@demo.cl")) {
            Usuario admin = new Usuario();
            admin.setRun("111111111");
            admin.setNombre("Juan");
            admin.setApellidos("Pérez");
            admin.setCorreo("admin@demo.cl");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRol("ADMINISTRADOR");
            admin.setRegion("Región de La Araucanía");
            admin.setComuna("Temuco");
            admin.setDireccion("Av. Alemania 0450");
            repository.save(admin);
        }

        if (!repository.existsByCorreo("usuario@demo.cl")) {
            Usuario paciente = new Usuario();
            paciente.setRun("222222222");
            paciente.setNombre("María");
            paciente.setApellidos("López");
            paciente.setCorreo("usuario@demo.cl");
            paciente.setPassword(passwordEncoder.encode("5678"));
            paciente.setRol("PACIENTE");
            paciente.setRegion("Región de La Araucanía");
            paciente.setComuna("Temuco");
            paciente.setDireccion("Av. Alemania 0450");
            repository.save(paciente);
        }
    }
}