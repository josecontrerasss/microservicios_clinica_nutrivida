package cl.nutrivida.reservas.service;

import cl.nutrivida.reservas.dto.*;
import cl.nutrivida.reservas.model.DetalleReserva;
import cl.nutrivida.reservas.model.Reserva;
import cl.nutrivida.reservas.repository.ReservaRepository;
import cl.nutrivida.reservas.security.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final RestClient catalogoClient;
    private final RestClient medicosClient;

    public ReservaService(
            ReservaRepository repository,
            @Value("${catalogo.service.url}") String catalogoUrl,
            @Value("${medicos.service.url}") String medicosUrl) {
        this.repository = repository;
        this.catalogoClient = RestClient.builder().baseUrl(catalogoUrl).build();
        this.medicosClient = RestClient.builder().baseUrl(medicosUrl).build();
    }

    @Transactional
    public Reserva procesarReserva(CrearReservaRequest request, UsuarioPrincipal usuario, String authHeader) {
        if (usuario == null || usuario.id() == null) {
            throw new IllegalArgumentException("El token JWT no contiene un identificador de usuario válido.");
        }

        Set<Long> horariosEnSolicitud = new HashSet<>();
        for (ItemReservaRequest item : request.getItems()) {
            if (!horariosEnSolicitud.add(item.getHorarioId())) {
                throw new IllegalArgumentException("No puedes incluir dos veces el mismo bloque horario en una reserva.");
            }
        }

        List<ServicioExternoDto> serviciosValidados = new ArrayList<>();
        List<HorarioExternoDto> horariosValidados = new ArrayList<>();

        for (ItemReservaRequest item : request.getItems()) {
            ServicioExternoDto servicio = consultarServicio(item.getServicioId());
            HorarioExternoDto horario = consultarHorario(item.getHorarioId());

            if (!horario.isDisponible()) {
                throw new IllegalStateException("El bloque horario con ID " + item.getHorarioId() + " ya no está disponible.");
            }
            if (horario.getMedico() == null || !horario.getMedico().isActivo()) {
                throw new IllegalStateException("El especialista asociado al bloque horario no está activo.");
            }

            serviciosValidados.add(servicio);
            horariosValidados.add(horario);
        }

        for (HorarioExternoDto horario : horariosValidados) {
            ocuparHorarioEnMedicos(horario.getId(), authHeader);
        }

        Reserva reserva = new Reserva();
        reserva.setNumeroBoleta(generarNumeroBoleta());
        reserva.setUsuarioId(usuario.id());
        reserva.setUsuarioCorreo(usuario.correo());
        reserva.setUsuarioNombre(request.getUsuarioNombre().trim());
        reserva.setMetodoPago(request.getMetodoPago().trim());
        reserva.setEstado("PAGADA");

        int totalCalculado = 0;

        for (int i = 0; i < request.getItems().size(); i++) {
            ServicioExternoDto servicio = serviciosValidados.get(i);
            HorarioExternoDto horario = horariosValidados.get(i);

            DetalleReserva detalle = new DetalleReserva();
            detalle.setServicioId(servicio.getId());
            detalle.setServicioTitulo(servicio.getTitulo());
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setMedicoId(horario.getMedico().getId());
            detalle.setMedicoNombre(horario.getMedico().getNombreCompleto());
            detalle.setEspecialidad(horario.getMedico().getEspecialidad());
            detalle.setHorarioId(horario.getId());
            detalle.setFechaCita(horario.getFecha());
            detalle.setHoraCita(horario.getHoraInicio());

            reserva.agregarDetalle(detalle);
            totalCalculado += servicio.getPrecio();
        }

        reserva.setTotal(totalCalculado);
        return repository.save(reserva);
    }

    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByFechaReservaDesc(usuarioId);
    }

    public List<Reserva> listarTodas() {
        return repository.findAllByOrderByFechaReservaDesc();
    }

    public Optional<Reserva> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public ConteoReservasResponse obtenerConteoPorUsuario(Long usuarioId, String usuarioCorreo) {
        List<Reserva> historial = repository.findByUsuarioIdOrderByFechaReservaDesc(usuarioId);
        long totalReservas = historial.size();
        long totalCitas = historial.stream()
                .mapToLong(r -> r.getDetalles() != null ? r.getDetalles().size() : 0)
                .sum();

        String correoResuelto = usuarioCorreo;
        if ((correoResuelto == null || correoResuelto.isBlank()) && !historial.isEmpty()) {
            correoResuelto = historial.get(0).getUsuarioCorreo();
        }

        return new ConteoReservasResponse(usuarioId, correoResuelto, totalReservas, totalCitas);
    }

    private ServicioExternoDto consultarServicio(Long servicioId) {
        try {
            ServicioExternoDto servicio = catalogoClient.get()
                    .uri("/api/servicios/{id}", servicioId)
                    .retrieve()
                    .body(ServicioExternoDto.class);

            if (servicio == null) {
                throw new IllegalArgumentException("El servicio con ID " + servicioId + " no existe en el catálogo.");
            }
            return servicio;
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("El servicio con ID " + servicioId + " no existe en el catálogo.");
        }
    }

    private HorarioExternoDto consultarHorario(Long horarioId) {
        try {
            HorarioExternoDto horario = medicosClient.get()
                    .uri("/api/horarios/{id}", horarioId)
                    .retrieve()
                    .body(HorarioExternoDto.class);

            if (horario == null) {
                throw new IllegalArgumentException("El bloque horario con ID " + horarioId + " no existe.");
            }
            return horario;
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("El bloque horario con ID " + horarioId + " no existe.");
        }
    }

    private void ocuparHorarioEnMedicos(Long horarioId, String authHeader) {
        try {
            medicosClient.put()
                    .uri("/api/horarios/{id}/reservar", horarioId)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException("El bloque horario con ID " + horarioId + " ya fue reservado por otro paciente.");
        }
    }

    private String generarNumeroBoleta() {
        String marcaTiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int sufijo = new Random().nextInt(900) + 100;
        return "BOL-" + marcaTiempo + "-" + sufijo;
    }
}