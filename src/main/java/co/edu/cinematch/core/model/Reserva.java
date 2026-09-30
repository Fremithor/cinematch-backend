package co.edu.cinematch.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva implements Identifiable {

    private String id;
    private String clienteId;
    private String funcionId;
    private LocalDate fechaReserva;
    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    private double total;
    private List<DetalleReserva> detalles = new ArrayList<>();

    public Reserva() {
    }

    public Reserva(String clienteId, String funcionId, LocalDate fechaReserva) {
        this.clienteId = clienteId;
        this.funcionId = funcionId;
        this.fechaReserva = fechaReserva;
    }

    public void agregarDetalle(String asientoId, double precio) {
        detalles.add(new DetalleReserva(asientoId, precio));
        calcularTotal();
    }

    public double calcularTotal() {
        total = detalles.stream().mapToDouble(DetalleReserva::getPrecio).sum();
        return total;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getFuncionId() { return funcionId; }
    public void setFuncionId(String funcionId) { this.funcionId = funcionId; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<DetalleReserva> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleReserva> detalles) {
        this.detalles = detalles == null ? new ArrayList<>() : new ArrayList<>(detalles);
        calcularTotal();
    }
}
