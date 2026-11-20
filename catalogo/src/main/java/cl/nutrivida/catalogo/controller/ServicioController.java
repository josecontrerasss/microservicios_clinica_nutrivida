package cl.nutrivida.catalogo.controller;

import cl.nutrivida.catalogo.model.Servicio;
import cl.nutrivida.catalogo.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Catálogo de Servicios", description = "Consulta pública y administración CRUD de servicios nutricionales")
public class ServicioController {

    private final ServicioService service;

    public ServicioController(ServicioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar servicios del catálogo", description = "Obtiene todos los servicios o filtra por coincidencia en el título")
    public ResponseEntity<List<Servicio>> listar(@RequestParam(required = false) String titulo) {
        return ResponseEntity.ok(service.listarTodos(titulo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un servicio", description = "Busca un servicio específico por su identificador")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo servicio (Solo ADMINISTRADOR)", description = "Registra un nuevo plan nutricional en el catálogo", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Servicio> crear(@Valid @RequestBody Servicio servicio) {
        Servicio creado = service.crear(servicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio (Solo ADMINISTRADOR)", description = "Modifica título, descripción, precio o imagen de un servicio existente", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @Valid @RequestBody Servicio servicio) {
        return service.actualizar(id, servicio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio (Solo ADMINISTRADOR)", description = "Elimina un servicio del catálogo por su identificador", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}