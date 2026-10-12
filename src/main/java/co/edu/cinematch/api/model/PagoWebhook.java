package co.edu.cinematch.api.model;

public record PagoWebhook(String transaccionId, String reservaId, String metodoPago, double monto, String estado) {
}
