package cl.nutrivida.catalogo.service;

import cl.nutrivida.catalogo.model.Servicio;
import cl.nutrivida.catalogo.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository repository;

    public ServicioService(ServicioRepository repository) {
        this.repository = repository;
    }

    public List<Servicio> listarTodos(String titulo) {
        if (titulo != null && !titulo.trim().isEmpty()) {
            return repository.findByTituloContainingIgnoreCase(titulo.trim());
        }
        return repository.findAll();
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Servicio crear(Servicio servicio) {
        servicio.setId(null);
        return repository.save(servicio);
    }

    public Optional<Servicio> actualizar(Long id, Servicio datosActualizados) {
        return repository.findById(id).map(existente -> {
            existente.setTitulo(datosActualizados.getTitulo());
            existente.setDescripcion(datosActualizados.getDescripcion());
            existente.setPrecio(datosActualizados.getPrecio());
            existente.setImagen(datosActualizados.getImagen());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}