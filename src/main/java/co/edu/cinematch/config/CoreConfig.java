package co.edu.cinematch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.model.Cine;
import co.edu.cinematch.core.model.Funcion;
import co.edu.cinematch.core.model.Pelicula;
import co.edu.cinematch.core.model.Rol;
import co.edu.cinematch.core.model.Sala;
import co.edu.cinematch.core.model.Usuario;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {
    /** JSON compartido por los controladores y el cliente HTTP de pagos. */
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    CineMatchBackend cineMatchBackend() {
        CineMatchBackend backend = new CineMatchBackend();
        backend.auth.registrar(new Usuario("Administrador", "admin@cinematch.com", "admin123", "3000000000", Rol.ADMINISTRADOR));

        // Datos de demostración. Se crean al iniciar porque el proyecto usa
        // repositorios en memoria; Interestelar no forma parte de la muestra.
        Pelicula duna = pelicula(backend, "Duna: Parte Dos", "Paul Atreides une fuerzas con Chani para enfrentar a quienes destruyeron a su familia.", 166, "Ciencia ficción", "PG-13", "2024-02-29", "duna-parte-dos");
        Pelicula spiderman = pelicula(backend, "Spider-Man: A Través del Spider-Verso", "Miles Morales emprende una aventura por el multiverso junto a nuevos Spider-People.", 140, "Animación", "PG", "2023-06-01", "spider-verse");
        Pelicula oppenheimer = pelicula(backend, "Oppenheimer", "La historia del físico que lideró el desarrollo de la bomba atómica.", 180, "Drama", "R", "2023-07-20", "oppenheimer");
        Pelicula wonka = pelicula(backend, "Wonka", "Los primeros años del famoso inventor y chocolatero Willy Wonka.", 116, "Familiar", "PG", "2023-12-07", "wonka");
        Pelicula garza = pelicula(backend, "El niño y la garza", "Un joven descubre un mundo fantástico guiado por una garza parlante.", 124, "Animación", "PG", "2023-12-07", "el-nino-y-la-garza");

        Cine centro = cine(backend, "Cine Colombia", "Cine Colombia Centro", "Calle 10 # 5-20");
        Cine multicentro = cine(backend, "Cinemark", "Cinemark Multicentro", "Carrera 5 # 60-45");
        Cine estacion = cine(backend, "Royal Films", "Royal Films La Estación", "Avenida Ambalá # 42-15");

        Sala centroDosD = sala(backend, centro, "Sala 1", 30, "2D");
        Sala centroTresD = sala(backend, centro, "Sala 2", 24, "3D");
        Sala multiDosD = sala(backend, multicentro, "Sala 1", 28, "2D");
        Sala multiVip = sala(backend, multicentro, "Sala VIP", 18, "VIP");
        Sala estacionDosD = sala(backend, estacion, "Sala 1", 32, "2D");
        Sala estacionTresD = sala(backend, estacion, "Sala 2", 26, "3D");

        LocalDate hoy = LocalDate.now();
        funcion(backend, duna, centroTresD, hoy, 15, 0, 17, 46, 22_000, "3D");
        funcion(backend, duna, multiDosD, hoy, 16, 0, 18, 46, 19_000, "2D");
        funcion(backend, duna, estacionTresD, hoy.plusDays(1), 18, 0, 20, 46, 21_000, "3D");
        funcion(backend, spiderman, centroDosD, hoy, 14, 30, 16, 50, 16_000, "2D");
        funcion(backend, oppenheimer, multiVip, hoy.plusDays(1), 19, 0, 22, 0, 28_000, "VIP");
        funcion(backend, wonka, estacionDosD, hoy, 13, 0, 14, 56, 15_000, "2D");
        funcion(backend, garza, centroDosD, hoy.plusDays(1), 11, 0, 13, 4, 14_000, "2D");
        return backend;
    }

    private Pelicula pelicula(CineMatchBackend backend, String titulo, String sinopsis, int duracion,
                              String genero, String clasificacion, String estreno, String imagen) {
        return backend.peliculas.crear(new Pelicula(titulo, sinopsis, duracion, genero, clasificacion,
                "Español", LocalDate.parse(estreno), "https://placehold.co/600x900/9B1C31/FFFFFF?text=" + imagen));
    }

    private Cine cine(CineMatchBackend backend, String cadena, String nombre, String direccion) {
        Cine cine = new Cine(nombre, direccion, "Ibagué");
        cine.setCadena(cadena);
        return backend.cines.crearCine(cine);
    }

    private Sala sala(CineMatchBackend backend, Cine cine, String nombre, int capacidad, String tipo) {
        return backend.cines.crearSala(cine.getId(), new Sala(null, nombre, capacidad, tipo));
    }

    private void funcion(CineMatchBackend backend, Pelicula pelicula, Sala sala, LocalDate fecha,
                         int inicioHora, int inicioMinuto, int finHora, int finMinuto, double precio, String formato) {
        backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "Doblada", fecha,
                LocalTime.of(inicioHora, inicioMinuto), LocalTime.of(finHora, finMinuto), precio, formato));
    }
}
