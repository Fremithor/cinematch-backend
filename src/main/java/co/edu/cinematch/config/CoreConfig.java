package co.edu.cinematch.config;

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
    @Bean
    CineMatchBackend cineMatchBackend() {
        CineMatchBackend backend = new CineMatchBackend();
        backend.auth.registrar(new Usuario("Administrador", "admin@cinematch.com", "admin123", "3000000000", Rol.ADMINISTRADOR));
        Pelicula pelicula = backend.peliculas.crear(new Pelicula("Interestelar", "Una misión espacial para salvar a la humanidad.", 169, "Ciencia ficción", "PG-13", "Español", LocalDate.of(2014, 11, 7), "https://m.media-amazon.com/images/I/81kz06XxKQL._AC_UF894,1000_QL80_.jpg"));
        Cine cine = backend.cines.crearCine(new Cine("CineMatch Centro", "Calle 10 # 5-20", "Ibagué"));
        cine.setCadena("CineMatch");
        Sala sala = backend.cines.crearSala(cine.getId(), new Sala(null, "Sala 1", 20, "2D"));
        backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "Doblada", LocalDate.now().plusDays(1), LocalTime.of(18, 0), LocalTime.of(20, 49), 18_000, "2D"));
        return backend;
    }
}
