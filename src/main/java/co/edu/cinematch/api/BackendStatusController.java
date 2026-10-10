package co.edu.cinematch.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackendStatusController {
    @GetMapping("/internal/salud")
    public java.util.Map<String, String> salud() { return java.util.Map.of("estado", "ok", "servicio", "cinematch-backend"); }
}
