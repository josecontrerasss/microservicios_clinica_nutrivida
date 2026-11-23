package cl.nutrivida.novedades.controller;

import cl.nutrivida.novedades.model.Novedad;
import cl.nutrivida.novedades.service.NovedadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novedades")
@Tag(name = "Novedades y Blog Clínico", description = "Consulta pública y administración CRUD de artículos nutricionales")
public class NovedadController {

    private final NovedadService service;

    public NovedadController(NovedadService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar novedades", description = "Obtiene todos los artículos publicados o filtra por coincidencia en el título")
    public ResponseEntity<List<Novedad>> listar(@RequestParam(required = false) String titulo) {
        return ResponseEntity.ok(service.listarTodas(titulo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una novedad", description = "Busca un artículo específico por su identificador")
    public ResponseEntity<Novedad> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Publicar nueva novedad (Solo ADMINISTRADOR)", description = "Crea un nuevo artículo en el blog clínico", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Novedad> crear(@Valid @RequestBody Novedad novedad) {
        Novedad creada = service.crear(novedad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar novedad (Solo ADMINISTRADOR)", description = "Modifica título, resumen, imagen o párrafos de un artículo existente", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Novedad> actualizar(@PathVariable Long id, @Valid @RequestBody Novedad novedad) {
        return service.actualizar(id, novedad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar novedad (Solo ADMINISTRADOR)", description = "Elimina un artículo del blog por su identificador", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}