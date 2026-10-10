package co.edu.cinematch.config;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.model.Rol;
import co.edu.cinematch.core.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {
    @Bean
    CineMatchBackend cineMatchBackend() {
        CineMatchBackend backend = new CineMatchBackend();
        backend.auth.registrar(new Usuario("Administrador", "admin@cinematch.com", "admin123", "3000000000", Rol.ADMINISTRADOR));
        return backend;
    }
}
