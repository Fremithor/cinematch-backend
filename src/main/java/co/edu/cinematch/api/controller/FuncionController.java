package co.edu.cinematch.api.controller;

import java.util.List;

import co.edu.cinematch.api.model.Funcion;
import co.edu.cinematch.api.service.VistaMapper;
import co.edu.cinematch.core.CineMatchBackend;
import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {
    private final CineMatchBackend backend; private final VistaMapper mapper;
    public FuncionController(CineMatchBackend backend, VistaMapper mapper) { this.backend = backend; this.mapper = mapper; }
    @GetMapping public List<Funcion> listar() { return backend.funciones.listar().stream().map(mapper::funcion).toList(); }
    @GetMapping("/{id}") public Funcion consultar(@PathVariable String id) { return mapper.funcion(backend.funciones.buscar(id)); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Funcion crear(@RequestBody Funcion funcion) { return mapper.funcion(backend.funciones.crear(dominio(funcion))); }
    @PutMapping("/{id}") public Funcion actualizar(@PathVariable String id, @RequestBody Funcion funcion) { return mapper.funcion(backend.funciones.actualizar(id, dominio(funcion))); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable String id) { backend.funciones.cancelar(id); }
    private co.edu.cinematch.core.model.Funcion dominio(Funcion f) {
        return new co.edu.cinematch.core.model.Funcion(f.getPeliculaId(), f.getSalaId(), f.getTipoFuncion(), f.getFecha(),
                LocalTime.parse(f.getHoraInicio()), LocalTime.parse(f.getHoraFin()), f.getPrecio(), f.getFormato());
    }
}

