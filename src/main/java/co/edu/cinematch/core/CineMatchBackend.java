package co.edu.cinematch.core;

import co.edu.cinematch.core.model.*;
import co.edu.cinematch.core.repository.*;
import co.edu.cinematch.core.service.*;

public final class CineMatchBackend {
    public final PeliculaService peliculas;
    public final CineService cines;
    public final FuncionService funciones;
    public final AuthService auth;
    public final ReservaService reservas;
    public final PagoService pagos;

    public CineMatchBackend() {
        this(new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>());
    }

    public CineMatchBackend(Repository<Pelicula> peliculasRepo, Repository<Cine> cinesRepo, Repository<Sala> salasRepo, Repository<Asiento> asientosRepo, Repository<Funcion> funcionesRepo, Repository<Usuario> usuariosRepo, Repository<Reserva> reservasRepo, Repository<Pago> pagosRepo) {
        peliculas = new PeliculaService(peliculasRepo);
        cines = new CineService(cinesRepo, salasRepo, asientosRepo, funcionesRepo);
        funciones = new FuncionService(funcionesRepo, peliculas, cines);
        auth = new AuthService(usuariosRepo);
        reservas = new ReservaService(reservasRepo, auth, funciones, cines);
        pagos = new PagoService(pagosRepo, reservas);
    }
}
