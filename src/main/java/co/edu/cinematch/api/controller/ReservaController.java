package co.edu.cinematch.api.controller;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.model.Asiento;
import co.edu.cinematch.core.model.Reserva;
import co.edu.cinematch.api.service.VistaMapper;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReservaController {
    private final CineMatchBackend backend; private final VistaMapper mapper;
    public ReservaController(CineMatchBackend backend, VistaMapper mapper) { this.backend = backend; this.mapper = mapper; }

    @GetMapping("/funciones/{id}/asientos")
    public List<Asiento> asientos(@PathVariable String id) {
        var funcion = backend.funciones.buscar(id);
        return backend.cines.listarAsientos(funcion.getSalaId());
    }

    @PostMapping("/reservas") @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> crear(@RequestBody Map<String, Object> datos) {
        String correo = String.valueOf(datos.get("correoCliente"));
        String funcionId = String.valueOf(datos.get("funcionId"));
        var cliente = backend.auth.listar().stream().filter(u -> u.getEmail().equalsIgnoreCase(correo)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", correo));
        Reserva reserva = backend.reservas.crear(cliente.getId(), funcionId);
        Object seleccion = datos.get("asientos");
        if (seleccion instanceof List<?> ids) for (Object id : ids) backend.reservas.agregarAsiento(reserva.getId(), String.valueOf(id));
        return vista(reserva);
    }

    @GetMapping("/clientes/{correo}/reservas")
    public List<Map<String, Object>> listar(@PathVariable String correo) {
        var cliente = backend.auth.listar().stream().filter(u -> u.getEmail().equalsIgnoreCase(correo)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", correo));
        return backend.reservas.porCliente(cliente.getId()).stream().map(this::vista).toList();
    }

    @DeleteMapping("/reservas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable String id) {
        backend.reservas.cancelar(id);
    }

    private Map<String, Object> vista(Reserva reserva) {
        var funcion = backend.funciones.buscar(reserva.getFuncionId());
        var pelicula = backend.peliculas.buscar(funcion.getPeliculaId());
        var funcionVista = mapper.funcion(funcion);
        var asientos = reserva.getDetalles().stream().map(d -> backend.cines.buscarAsiento(d.getAsientoId()))
                .map(a -> a.getFila() + a.getNumero()).toList();
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("id", reserva.getId()); resultado.put("peliculaId", pelicula.getId()); resultado.put("peliculaTitulo", pelicula.getTitulo());
        resultado.put("cineNombre", funcionVista.getCine().getNombre()); resultado.put("salaNombre", funcionVista.getSala().getNombre());
        resultado.put("formato", funcionVista.getFormato()); resultado.put("fecha", funcionVista.getFecha()); resultado.put("horaInicio", funcionVista.getHoraInicio());
        resultado.put("asientos", asientos); resultado.put("total", reserva.getTotal()); resultado.put("estado", reserva.getEstado().name());
        return resultado;
    }
}

