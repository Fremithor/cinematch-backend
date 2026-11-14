package co.edu.cinematch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/** Metadatos que se muestran en Swagger UI para documentar la API pública. */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "CineMatch API",
                version = "1.0.0",
                description = "API REST para catálogo, cines, funciones, clientes, reservas y pagos de CineMatch."),
        tags = {
                @Tag(name = "Películas", description = "Catálogo y comparación de funciones."),
                @Tag(name = "Cines", description = "Cines, salas y asientos."),
                @Tag(name = "Funciones", description = "Programación de funciones."),
                @Tag(name = "Clientes", description = "Registro, acceso, perfiles y reservas."),
                @Tag(name = "Pagos", description = "Comunicación con el microservicio Django.")
        })
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
