package co.edu.cinematch.core;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Cine;
import co.edu.cinematch.core.model.Funcion;
import co.edu.cinematch.core.model.Pelicula;
import co.edu.cinematch.core.model.Reserva;
import co.edu.cinematch.core.model.Rol;
import co.edu.cinematch.core.model.Sala;
import co.edu.cinematch.core.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CineMatchBackendTest {

    @Test
    void conservaElPrecioDelAsientoAunqueCambieLaFuncion() {
        CineMatchBackend backend = new CineMatchBackend();
        Usuario cliente = backend.auth.registrar(
                new Usuario("Ana", "ana@cine.com", "Clave123", "3001234567", Rol.CLIENTE));
        Pelicula pelicula = backend.peliculas.crear(
                new Pelicula("Película", "Sinopsis", 120, "Drama", "PG", "Español", LocalDate.now(), "https://example.com/pelicula.jpg"));
        Cine cine = crearCine(backend);
        Sala sala = backend.cines.crearSala(cine.getId(), new Sala(null, "Sala 1", 5, "2D"));
        Funcion funcion = backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 15), 20_000, "2D"));

        Reserva reserva = backend.reservas.crear(cliente.getId(), funcion.getId());
        backend.reservas.agregarAsiento(reserva.getId(), backend.cines.listarAsientos(sala.getId()).get(0).getId());
        funcion.setPrecio(30_000);

        assertEquals(20_000, reserva.getTotal());
    }

    @Test
    void eliminarCineEliminaSusSalasYFunciones() {
        CineMatchBackend backend = new CineMatchBackend();
        Pelicula pelicula = backend.peliculas.crear(
                new Pelicula("Película", "Sinopsis", 120, "Drama", "PG", "Español", LocalDate.now(), "https://example.com/pelicula.jpg"));
        Cine cine = crearCine(backend);
        Sala sala = backend.cines.crearSala(cine.getId(), new Sala(null, "Sala 1", 5, "2D"));
        Funcion funcion = backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 15), 20_000, "2D"));

        backend.cines.eliminarCine(cine.getId());

        assertThrows(ResourceNotFoundException.class, () -> backend.cines.buscarSala(sala.getId()));
        assertThrows(ResourceNotFoundException.class, () -> backend.funciones.buscar(funcion.getId()));
    }

    @Test
    void impideTitulosUsuariosYHorariosDuplicados() {
        CineMatchBackend backend = new CineMatchBackend();
        Pelicula pelicula = backend.peliculas.crear(
                new Pelicula("Interestelar", "Sinopsis", 120, "Drama", "PG", "Español", LocalDate.now(), "https://example.com/pelicula.jpg"));

        assertThrows(ValidationException.class, () -> backend.peliculas.crear(
                new Pelicula(" interestelar ", "Otra", 100, "Drama", "PG", "Español", LocalDate.now(), "https://example.com/otra.jpg")));

        backend.auth.registrar(new Usuario("Ana", "ana@cine.com", "Clave123", "3001234567", Rol.CLIENTE));
        assertThrows(ValidationException.class, () -> backend.auth.registrar(
                new Usuario("Otra Ana", "ANA@CINE.COM", "Clave123", "3007654321", Rol.CLIENTE)));

        Cine cine = crearCine(backend);
        Sala sala = backend.cines.crearSala(cine.getId(), new Sala(null, "Sala 1", 5, "2D"));
        backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 15), 20_000, "2D"));

        assertThrows(ValidationException.class, () -> backend.funciones.crear(
                new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                        LocalDate.now(), LocalTime.of(11, 0), LocalTime.of(13, 15), 20_000, "2D")));

        backend.funciones.crear(new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                LocalDate.now(), LocalTime.of(12, 15), LocalTime.of(14, 30), 20_000, "2D"));

        assertThrows(ValidationException.class, () -> backend.funciones.crear(
                new Funcion(pelicula.getId(), sala.getId(), "DOBLADA",
                        LocalDate.now(), LocalTime.of(14, 30), LocalTime.of(15, 30), 20_000, "2D")));
    }

    private Cine crearCine(CineMatchBackend backend) {
        Cine cine = new Cine("Cine", "Calle 1", "Ibagué");
        cine.setCadena("CineMatch");
        return backend.cines.crearCine(cine);
    }
}
