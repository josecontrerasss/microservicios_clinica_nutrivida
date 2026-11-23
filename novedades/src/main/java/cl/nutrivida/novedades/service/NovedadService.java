package cl.nutrivida.novedades.service;

import cl.nutrivida.novedades.model.Novedad;
import cl.nutrivida.novedades.repository.NovedadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NovedadService {

    private final NovedadRepository repository;

    public NovedadService(NovedadRepository repository) {
        this.repository = repository;
    }

    public List<Novedad> listarTodas(String titulo) {
        if (titulo != null && !titulo.trim().isEmpty()) {
            return repository.findByTituloContainingIgnoreCaseOrderByIdAsc(titulo.trim());
        }
        return repository.findAllByOrderByIdAsc();
    }

    public Optional<Novedad> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Novedad crear(Novedad novedad) {
        novedad.setId(null);
        return repository.save(novedad);
    }

    public Optional<Novedad> actualizar(Long id, Novedad datosActualizados) {
        return repository.findById(id).map(existente -> {
            existente.setTitulo(datosActualizados.getTitulo());
            existente.setResumen(datosActualizados.getResumen());
            existente.setImagen(datosActualizados.getImagen());
            existente.setParrafo1(datosActualizados.getParrafo1());
            existente.setParrafo2(datosActualizados.getParrafo2());
            existente.setParrafo3(datosActualizados.getParrafo3());
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