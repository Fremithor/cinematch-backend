package co.edu.cinematch.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Cliente HTTP del microservicio Django; Spring no procesa la transacción por sí mismo. */
@Service
public class PagoDjangoClient {
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final ObjectMapper json;
    private final String djangoUrl;
    private final String webhookUrl;

    public PagoDjangoClient(
            ObjectMapper json,
            @Value("${pagos.django.url}") String djangoUrl,
            @Value("${pagos.webhook.url}") String webhookUrl) {
        this.json = json;
        this.djangoUrl = djangoUrl;
        this.webhookUrl = webhookUrl;
    }

    public Map<String, Object> solicitar(String reservaId, String metodoPago, double monto) {
        try {
            Map<String, Object> solicitud = new LinkedHashMap<>();
            solicitud.put("reservaId", reservaId);
            solicitud.put("metodoPago", metodoPago);
            solicitud.put("monto", monto);
            solicitud.put("webhookUrl", webhookUrl);
            HttpRequest request = HttpRequest.newBuilder(URI.create(djangoUrl + "/api/pagos/procesar"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(solicitud)))
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "El microservicio de pagos rechazó la operación.");
            }
            return json.readValue(response.body(), new TypeReference<>() { });
        } catch (ResponseStatusException error) {
            throw error;
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con el microservicio Django de pagos.", error);
        }
    }
}
