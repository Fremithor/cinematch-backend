package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.EstadoReserva;
import co.edu.cinematch.core.model.Pago;
import co.edu.cinematch.core.model.Reserva;
import co.edu.cinematch.core.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class PagoService {

    private final Repository<Pago> repo;
    private final ReservaService reservas;

    public PagoService(Repository<Pago> repo, ReservaService reservas) {
        this.repo = repo;
        this.reservas = reservas;
    }

    public Pago procesar(String reservaId, String metodoPago) {
        Reserva reserva = reservas.buscar(reservaId);
        return confirmar(reservaId, metodoPago, reserva.getTotal(), "APROBADO");
    }

    /** Registra el resultado recibido desde Django; el webhook es idempotente por reserva. */
    public Pago confirmar(String reservaId, String metodoPago, double monto, String estado) {
        Reserva reserva = reservas.buscar(reservaId);
        if (reserva.getDetalles().isEmpty()) {
            throw new ValidationException("La reserva debe tener al menos un asiento.");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new ValidationException("El método de pago es obligatorio.");
        }

        if (monto != reserva.getTotal()) {
            throw new ValidationException("El monto confirmado no coincide con el total de la reserva.");
        }
        for (Pago existente : repo.findAll()) {
            if (reservaId.equals(existente.getReservaId())) return existente;
        }
        Pago pago = new Pago(reservaId, metodoPago, monto, LocalDateTime.now(), estado);
        if ("APROBADO".equalsIgnoreCase(estado)) reserva.setEstado(EstadoReserva.PAGADA);
        return repo.save(pago);
    }

    public Pago buscar(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
    }

    public List<Pago> listar() {
        return repo.findAll();
    }
}
