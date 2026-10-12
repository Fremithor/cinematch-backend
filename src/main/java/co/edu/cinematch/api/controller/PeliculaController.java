package co.edu.cinematch.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.model.Pelicula;
import co.edu.cinematch.api.service.VistaMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {
    private final CineMatchBackend backend;
    private final VistaMapper mapper;
    public PeliculaController(CineMatchBackend backend, VistaMapper mapper) { this.backend = backend; this.mapper = mapper; }
    @GetMapping public List<Pelicula> listar() { return backend.peliculas.listar(); }
    @GetMapping("/{id}") public Pelicula consultar(@PathVariable String id) { return backend.peliculas.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Pelicula crear(@RequestBody Pelicula pelicula) { return backend.peliculas.crear(pelicula); }
    @PutMapping("/{id}") public Pelicula actualizar(@PathVariable String id, @RequestBody Pelicula pelicula) { return backend.peliculas.actualizar(id, pelicula); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable String id) { backend.peliculas.eliminar(id); }

    @GetMapping("/{id}/funciones")
    public List<co.edu.cinematch.api.model.Funcion> funciones(@PathVariable String id) {
        return backend.funciones.porPelicula(id).stream().map(mapper::funcion)
                .sorted(Comparator.comparing(co.edu.cinematch.api.model.Funcion::getFecha).thenComparing(co.edu.cinematch.api.model.Funcion::getHoraInicio)).toList();
    }

    @GetMapping("/{id}/comparador")
    public List<Map<String, Object>> comparar(@PathVariable String id) {
        return funciones(id).stream().collect(Collectors.groupingBy(f -> f.getCine().getId())).values().stream()
                .map(lista -> Map.<String, Object>of("cine", lista.get(0).getCine(), "precioDesde", lista.stream().mapToDouble(co.edu.cinematch.api.model.Funcion::getPrecio).min().orElse(0), "funciones", lista))
                .sorted(Comparator.comparing(item -> (Double) item.get("precioDesde"))).toList();
    }
}

