package cl.nutrivida.auth.service;

import cl.nutrivida.auth.dto.*;
import cl.nutrivida.auth.model.Usuario;
import cl.nutrivida.auth.repository.UsuarioRepository;
import cl.nutrivida.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioResponse registrar(RegistroRequest request) {
        if (repository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado.");
        }
        if (repository.existsByRun(request.getRun())) {
            throw new RuntimeException("El RUN ya está registrado.");
        }

        Usuario nuevo = new Usuario();
        nuevo.setRun(request.getRun().trim());
        nuevo.setNombre(request.getNombre().trim());
        nuevo.setApellidos(request.getApellidos().trim());
        nuevo.setCorreo(request.getCorreo().trim().toLowerCase());
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevo.setFechaNacimiento(request.getFechaNacimiento());
        nuevo.setRol("PACIENTE");
        nuevo.setRegion(request.getRegion());
        nuevo.setComuna(request.getComuna());
        nuevo.setDireccion(request.getDireccion());

        Usuario guardado = repository.save(nuevo);
        return convertirAResponse(guardado);
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = repository.findByCorreo(request.getCorreo().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas.");
        }

        String token = jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol(), usuario.getId());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol());
    }

    public List<UsuarioResponse> listarUsuarios() {
        return repository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public UsuarioResponse crearUsuarioAdmin(Usuario usuario) {
        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya existe.");
        }
        usuario.setId(null);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(usuario.getRol().toUpperCase());
        return convertirAResponse(repository.save(usuario));
    }

    public UsuarioResponse actualizarUsuario(Long id, Usuario datos) {
        Usuario existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        existente.setNombre(datos.getNombre());
        existente.setApellidos(datos.getApellidos());
        existente.setCorreo(datos.getCorreo());
        existente.setRol(datos.getRol().toUpperCase());

        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(datos.getPassword()));
        }

        return convertirAResponse(repository.save(existente));
    }

    public void eliminarUsuario(Long id) {
        repository.deleteById(id);
    }

    private UsuarioResponse convertirAResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getRun(), u.getNombre(), u.getApellidos(), u.getCorreo(), u.getRol());
    }
}