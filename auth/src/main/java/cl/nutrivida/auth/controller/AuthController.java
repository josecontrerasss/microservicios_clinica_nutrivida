package cl.nutrivida.auth.controller;

import cl.nutrivida.auth.dto.*;
import cl.nutrivida.auth.model.Usuario;
import cl.nutrivida.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Autenticación y Usuarios", description = "Registro, Login con JWT y administración de usuarios por rol")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/api/auth/registro")
    @Operation(summary = "Registrar nuevo paciente", description = "Endpoint público para crear una cuenta con rol PACIENTE")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/auth/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y retorna un token JWT con el rol del usuario")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(service.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/usuarios")
    @Operation(summary = "Listar usuarios (Solo ADMINISTRADOR)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @PostMapping("/api/usuarios")
    @Operation(summary = "Crear usuario desde panel Admin (Solo ADMINISTRADOR)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crearUsuarioAdmin(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/api/usuarios/{id}")
    @Operation(summary = "Actualizar usuario (Solo ADMINISTRADOR)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.actualizarUsuario(id, usuario));
    }

    @DeleteMapping("/api/usuarios/{id}")
    @Operation(summary = "Eliminar usuario (Solo ADMINISTRADOR)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}