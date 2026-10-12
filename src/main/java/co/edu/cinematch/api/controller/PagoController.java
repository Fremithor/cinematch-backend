package co.edu.cinematch.api.controller;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.model.Pago;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final CineMatchBackend backend;
    public PagoController(CineMatchBackend backend) { this.backend = backend; }
    @GetMapping public List<Pago> listar() { return backend.pagos.listar(); }
    @GetMapping("/{id}") public Pago consultar(@PathVariable String id) { return backend.pagos.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Pago crear(@RequestBody Map<String, String> datos) { return backend.pagos.procesar(datos.get("reservaId"), datos.get("metodoPago")); }
}

