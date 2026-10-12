package co.edu.cinematch.api.controller;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.model.Pago;
import co.edu.cinematch.api.model.PagoSolicitud;
import co.edu.cinematch.api.model.PagoWebhook;
import co.edu.cinematch.api.service.PagoDjangoClient;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final CineMatchBackend backend;
    private final PagoDjangoClient pagosDjango;
    public PagoController(CineMatchBackend backend, PagoDjangoClient pagosDjango) {
        this.backend = backend;
        this.pagosDjango = pagosDjango;
    }
    @GetMapping public List<Pago> listar() { return backend.pagos.listar(); }
    @GetMapping("/{id}") public Pago consultar(@PathVariable String id) { return backend.pagos.buscar(id); }
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody PagoSolicitud solicitud) {
        var reserva = backend.reservas.buscar(solicitud.reservaId());
        return ResponseEntity.accepted().body(
                pagosDjango.solicitar(reserva.getId(), solicitud.metodoPago(), reserva.getTotal()));
    }

    @PostMapping("/webhook")
    public Pago confirmarDesdeDjango(@RequestBody PagoWebhook pago) {
        return backend.pagos.confirmar(pago.reservaId(), pago.metodoPago(), pago.monto(), pago.estado());
    }
}

