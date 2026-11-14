package co.edu.cinematch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 5173 = portal de clientes (React + Vite)
                // 4200 = panel de administración (Angular)
                // Durante desarrollo se aceptan los dos frontends desde el
                // mismo equipo y desde una red local privada.
                .allowedOriginPatterns(
                        "http://localhost:*", "http://127.0.0.1:*",
                        "http://192.168.*.*:*", "http://10.*.*.*:*",
                        "http://172.16.*.*:*", "http://172.17.*.*:*",
                        "http://172.18.*.*:*", "http://172.19.*.*:*",
                        "http://172.2*.*.*:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization");
    }
}
