package co.edu.cinematch.core.model;

/**
 * Conserva el asiento y el precio acordado al momento de reservarlo.
 * El precio de una función puede cambiar después, pero no debe modificar
 * el total histórico de una reserva ya creada.
 */
public class DetalleReserva {

    private String asientoId;
    private double precio;

    public DetalleReserva() {
    }

    public DetalleReserva(String asientoId, double precio) {
        this.asientoId = asientoId;
        this.precio = precio;
    }

    public String getAsientoId() {
        return asientoId;
    }

    public void setAsientoId(String asientoId) {
        this.asientoId = asientoId;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
