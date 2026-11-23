package cl.nutrivida.reservas.controller;

import cl.nutrivida.reservas.dto.ConteoReservasResponse;
import cl.nutrivida.reservas.dto.CrearReservaRequest;
import cl.nutrivida.reservas.model.Reserva;
import cl.nutrivida.reservas.security.UsuarioPrincipal;
import cl.nutrivida.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas y Pagos", description = "Procesamiento de reservas, emisión de boletas e historial del paciente")
@SecurityRequirement(name = "bearerAuth")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Confirmar reserva y emitir boleta", description = "Valida precios en ms-catalogo, bloquea horarios en ms-medicos y registra la boleta del paciente")
    public ResponseEntity<?> crearReserva(
            @Valid @RequestBody CrearReservaRequest request,
            @AuthenticationPrincipal UsuarioPrincipal usuario,
            HttpServletRequest httpRequest) {
        try {
            String authHeader = httpRequest.getHeader("Authorization");
            Reserva creada = service.procesarReserva(request, usuario, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("mensaje", "Error de comunicación con los servicios de catálogo o médicos."));
        }
    }

    @GetMapping("/mis-reservas")
    @Operation(summary = "Listar mis reservas", description = "Obtiene el historial completo de reservas y boletas del usuario autenticado")
    public ResponseEntity<List<Reserva>> listarMisReservas(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(service.listarPorUsuario(usuario.id()));
    }

    @GetMapping("/mis-reservas/conteo")
    @Operation(summary = "Obtener conteo de mis reservas", description = "Devuelve la cantidad de boletas y citas agendadas por el usuario autenticado")
    public ResponseEntity<ConteoReservasResponse> contarMisReservas(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(service.obtenerConteoPorUsuario(usuario.id(), usuario.correo()));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar reservas por ID de usuario", description = "Consulta el historial de un paciente específico (propio o ADMINISTRADOR)")
    public ResponseEntity<?> listarPorUsuario(
            @PathVariable Long usuarioId,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        if (!esPropietarioOAdmin(usuarioId, usuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "No autorizado para ver estas reservas."));
        }
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/conteo")
    @Operation(summary = "Contar reservas por ID de usuario", description = "Obtiene el total de reservas realizadas por un usuario específico")
    public ResponseEntity<?> contarPorUsuario(
            @PathVariable Long usuarioId,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        if (!esPropietarioOAdmin(usuarioId, usuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "No autorizado para consultar este conteo."));
        }
        return ResponseEntity.ok(service.obtenerConteoPorUsuario(usuarioId, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener boleta de reserva por ID", description = "Consulta el detalle completo de una reserva específica")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return service.obtenerPorId(id)
                .<ResponseEntity<?>>map(reserva -> {
                    if (!esPropietarioOAdmin(reserva.getUsuarioId(), usuario)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "No autorizado para ver esta boleta."));
                    }
                    return ResponseEntity.ok(reserva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas (Solo ADMINISTRADOR)", description = "Obtiene todas las reservas registradas en la clínica")
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    private boolean esPropietarioOAdmin(Long usuarioId, UsuarioPrincipal usuario) {
        if (usuario == null) return false;
        return "ADMINISTRADOR".equals(usuario.rol()) || usuarioId.equals(usuario.id());
    }
}