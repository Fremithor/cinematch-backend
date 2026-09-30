package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Asiento;
import co.edu.cinematch.core.model.DetalleReserva;
import co.edu.cinematch.core.model.EstadoReserva;
import co.edu.cinematch.core.model.Funcion;
import co.edu.cinematch.core.model.Reserva;
import co.edu.cinematch.core.model.Rol;
import co.edu.cinematch.core.model.Usuario;
import co.edu.cinematch.core.repository.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {

    private final Repository<Reserva> repo;
    private final AuthService auth;
    private final FuncionService funciones;
    private final CineService cines;

    public ReservaService(Repository<Reserva> repo, AuthService auth,
                          FuncionService funciones, CineService cines) {
        this.repo = repo;
        this.auth = auth;
        this.funciones = funciones;
        this.cines = cines;
    }

    public Reserva crear(String clienteId, String funcionId) {
        Usuario usuario = auth.buscar(clienteId);
        if (usuario.getRol() != Rol.CLIENTE) {
            throw new ValidationException("La reserva debe pertenecer a un cliente.");
        }
        funciones.buscar(funcionId);
        return repo.save(new Reserva(clienteId, funcionId, LocalDate.now()));
    }

    public Reserva agregarAsiento(String reservaId, String asientoId) {
        Reserva reserva = buscar(reservaId);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new ValidationException("No se pueden agregar asientos a una reserva cancelada.");
        }

        Funcion funcion = funciones.buscar(reserva.getFuncionId());
        Asiento asiento = cines.buscarAsiento(asientoId);
        if (!funcion.getSalaId().equals(asiento.getSalaId())) {
            throw new ValidationException("El asiento no pertenece a la sala de la función.");
        }
        if (asiento.isOcupado()) {
            throw new ValidationException("El asiento no está disponible.");
        }

        asiento.setOcupado(true);
        reserva.agregarDetalle(asiento.getId(), funcion.getPrecio());
        return repo.save(reserva);
    }

    public Reserva confirmar(String id) {
        Reserva reserva = buscar(id);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        return repo.save(reserva);
    }

    public Reserva cancelar(String id) {
        Reserva reserva = buscar(id);
        for (DetalleReserva detalle : reserva.getDetalles()) {
            cines.buscarAsiento(detalle.getAsientoId()).setOcupado(false);
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        return repo.save(reserva);
    }

    public Reserva buscar(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));
    }

    public List<Reserva> listar() {
        return repo.findAll();
    }

    public List<Reserva> porCliente(String clienteId) {
        auth.buscar(clienteId);
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : repo.findAll()) {
            if (clienteId.equals(reserva.getClienteId())) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }
}
