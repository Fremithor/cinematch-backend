package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Asiento;
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
        if (asientoYaReservado(funcion.getId(), asiento.getId())) {
            throw new ValidationException("El asiento no está disponible.");
        }

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
        reserva.setEstado(EstadoReserva.CANCELADA);
        return repo.save(reserva);
    }

    /**
     * Los asientos describen la distribución física de una sala. La
     * disponibilidad, en cambio, depende de cada función: una reserva de las
     * 3:00 p. m. no puede bloquear el mismo asiento para la función de las
     * 6:00 p. m. aunque ambas usen la misma sala.
     */
    public List<Asiento> asientosParaFuncion(String funcionId) {
        Funcion funcion = funciones.buscar(funcionId);
        List<Asiento> resultado = new ArrayList<>();
        for (Asiento asiento : cines.listarAsientos(funcion.getSalaId())) {
            Asiento vista = new Asiento(asiento.getSalaId(), asiento.getFila(), asiento.getNumero());
            vista.setId(asiento.getId());
            vista.setOcupado(asientoYaReservado(funcionId, asiento.getId()));
            resultado.add(vista);
        }
        return resultado;
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

    private boolean asientoYaReservado(String funcionId, String asientoId) {
        for (Reserva otraReserva : repo.findAll()) {
            if (!funcionId.equals(otraReserva.getFuncionId())
                    || otraReserva.getEstado() == EstadoReserva.CANCELADA) {
                continue;
            }
            if (otraReserva.getDetalles().stream()
                    .anyMatch(detalle -> asientoId.equals(detalle.getAsientoId()))) {
                return true;
            }
        }
        return false;
    }
}
