package cl.nutrivida.contacto.service;

import cl.nutrivida.contacto.model.Contacto;
import cl.nutrivida.contacto.repository.ContactoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactoService {

    private final ContactoRepository repository;

    public ContactoService(ContactoRepository repository) {
        this.repository = repository;
    }

    public Contacto guardar(Contacto contacto) {
        contacto.setId(null);
        contacto.setEstado("PENDIENTE");
        contacto.setRespuestaAdmin(null);
        contacto.setFechaRespuesta(null);
        return repository.save(contacto);
    }

    public List<Contacto> listar() {
        return repository.findAll();
    }

    public Contacto responder(Long id, String respuesta) {
        Contacto existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje de contacto no encontrado con ID: " + id));
        
        existente.setRespuestaAdmin(respuesta);
        existente.setEstado("RESPONDIDO");
        existente.setFechaRespuesta(LocalDateTime.now());
        return repository.save(existente);
    }
}