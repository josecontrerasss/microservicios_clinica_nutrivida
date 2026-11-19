package cl.nutrivida.contacto.controller;

import cl.nutrivida.contacto.model.Contacto;
import cl.nutrivida.contacto.service.ContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Contacto", description = "Gestión de consultas de pacientes y respuestas administrativas")
public class ContactoController {

    private final ContactoService service;

    public ContactoController(ContactoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registrar mensaje de contacto", description = "Recibe una consulta desde el formulario público y la guarda como PENDIENTE")
    public ResponseEntity<Contacto> guardar(@Valid @RequestBody Contacto contacto) {
        Contacto guardado = service.guardar(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los mensajes", description = "Permite al administrador revisar los contactos recibidos")
    public ResponseEntity<List<Contacto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PutMapping("/{id}/responder")
    @Operation(summary = "Responder un mensaje de contacto", description = "Guarda la respuesta del administrador y cambia el estado a RESPONDIDO")
    public ResponseEntity<Contacto> responder(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String respuesta = body.get("respuestaAdmin");
        Contacto actualizado = service.responder(id, respuesta);
        return ResponseEntity.ok(actualizado);
    }
}
