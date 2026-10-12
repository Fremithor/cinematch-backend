package co.edu.cinematch.api.controller;

import java.util.List;
import java.util.Set;
import co.edu.cinematch.api.model.Cine;
import co.edu.cinematch.api.model.Sala;
import co.edu.cinematch.api.service.VistaMapper;
import co.edu.cinematch.core.CineMatchBackend;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cines")
public class CineController {
    private final CineMatchBackend backend; private final VistaMapper mapper;
    public CineController(CineMatchBackend backend, VistaMapper mapper) { this.backend = backend; this.mapper = mapper; }
    @GetMapping public List<Cine> listar() { return backend.cines.listarCines().stream().map(mapper::cine).toList(); }
    @GetMapping("/{id}") public Cine consultar(@PathVariable String id) { return mapper.cine(backend.cines.buscarCine(id)); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Cine crear(@RequestBody Cine cine) { return guardar(null, cine); }
    @PutMapping("/{id}") public Cine actualizar(@PathVariable String id, @RequestBody Cine cine) { return guardar(id, cine); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable String id) { backend.cines.eliminarCine(id); }
    private Cine guardar(String id, Cine datos) {
        co.edu.cinematch.core.model.Cine dominio = new co.edu.cinematch.core.model.Cine(datos.getNombre(), datos.getDireccion(), datos.getCiudad());
        dominio.setCadena(datos.getCadena());
        co.edu.cinematch.core.model.Cine guardado = id == null ? backend.cines.crearCine(dominio) : backend.cines.actualizarCine(id, dominio);
        List<co.edu.cinematch.core.model.Sala> salasActuales = backend.cines.listarSalas(guardado.getId());
        Set<String> idsActuales = salasActuales.stream().map(co.edu.cinematch.core.model.Sala::getId)
                .collect(java.util.stream.Collectors.toSet());
        Set<String> idsRecibidos = new java.util.HashSet<>();
        for (Sala sala : datos.getSalas()) {
            co.edu.cinematch.core.model.Sala salaDominio = new co.edu.cinematch.core.model.Sala(guardado.getId(), sala.getNombre(), sala.getCapacidad(), sala.getTipo());
            // Un id solo es actualizable si pertenece a este cine. Cualquier
            // id temporal o desconocido se interpreta como sala nueva.
            if (sala.getId() == null || sala.getId().isBlank() || !idsActuales.contains(sala.getId())) {
                backend.cines.crearSala(guardado.getId(), salaDominio);
            } else {
                backend.cines.actualizarSala(sala.getId(), salaDominio);
                idsRecibidos.add(sala.getId());
            }
        }
        // Quitar una fila del formulario también debe quitar esa sala del
        // cine; de otro modo la interfaz y el backend quedarían desfasados.
        for (co.edu.cinematch.core.model.Sala salaActual : salasActuales) {
            if (!idsRecibidos.contains(salaActual.getId())) backend.cines.eliminarSala(salaActual.getId());
        }
        return mapper.cine(guardado);
    }
}

