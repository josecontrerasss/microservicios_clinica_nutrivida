package cl.nutrivida.medicos.controller;

import cl.nutrivida.medicos.dto.HorarioRequest;
import cl.nutrivida.medicos.model.HorarioMedico;
import cl.nutrivida.medicos.model.Medico;
import cl.nutrivida.medicos.service.MedicoService;
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
@RequestMapping("/api")
@Tag(name = "Médicos y Agenda", description = "Catálogo de nutricionistas, especialidades y bloques horarios disponibles")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping("/medicos")
    @Operation(summary = "Listar médicos", description = "Obtiene los profesionales activos o filtra por especialidad")
    public ResponseEntity<List<Medico>> listarMedicos(
            @RequestParam(required = false) String especialidad,
            @RequestParam(defaultValue = "false") boolean incluirInactivos) {
        return ResponseEntity.ok(service.listarMedicos(especialidad, incluirInactivos));
    }

    @GetMapping("/medicos/{id}")
    @Operation(summary = "Obtener médico por ID", description = "Consulta los datos de un nutricionista específico")
    public ResponseEntity<Medico> obtenerMedico(@PathVariable Long id) {
        return service.obtenerMedicoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/medicos")
    @Operation(summary = "Registrar médico (Solo ADMINISTRADOR)", description = "Agrega un nuevo nutricionista al equipo clínico", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Medico> crearMedico(@Valid @RequestBody Medico medico) {
        Medico creado = service.crearMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/medicos/{id}")
    @Operation(summary = "Actualizar médico (Solo ADMINISTRADOR)", description = "Modifica datos o estado activo de un nutricionista", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Medico> actualizarMedico(@PathVariable Long id, @Valid @RequestBody Medico medico) {
        return service.actualizarMedico(id, medico)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/medicos/{id}")
    @Operation(summary = "Eliminar médico (Solo ADMINISTRADOR)", description = "Elimina un nutricionista y sus horarios asociados", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        if (service.eliminarMedico(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/medicos/{id}/horarios")
    @Operation(summary = "Listar bloques horarios de un médico", description = "Consulta los bloques horarios disponibles o todos los del especialista")
    public ResponseEntity<List<HorarioMedico>> listarHorarios(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean soloDisponibles) {
        if (service.obtenerMedicoPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.listarHorariosPorMedico(id, soloDisponibles));
    }

    @PostMapping("/medicos/{id}/horarios")
    @Operation(summary = "Crear bloque horario (Solo ADMINISTRADOR)", description = "Agrega una fecha y hora disponible para un médico", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HorarioMedico> crearHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest request) {
        return service.crearHorario(id, request)
                .map(creado -> ResponseEntity.status(HttpStatus.CREATED).body(creado))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/horarios/{id}")
    @Operation(summary = "Obtener bloque horario por ID", description = "Consulta el detalle de un bloque horario y su médico asignado")
    public ResponseEntity<HorarioMedico> obtenerHorario(@PathVariable Long id) {
        return service.obtenerHorarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/horarios/{id}/reservar")
    @Operation(summary = "Reservar bloque horario", description = "Marca el bloque horario como ocupado al confirmar una reserva", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> reservarHorario(@PathVariable Long id) {
        try {
            return service.reservarHorario(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/horarios/{id}/liberar")
    @Operation(summary = "Liberar bloque horario (Solo ADMINISTRADOR)", description = "Vuelve a habilitar un bloque horario como disponible", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HorarioMedico> liberarHorario(@PathVariable Long id) {
        return service.liberarHorario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/horarios/{id}")
    @Operation(summary = "Eliminar bloque horario (Solo ADMINISTRADOR)", description = "Elimina un bloque horario por su identificador", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        if (service.eliminarHorario(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}