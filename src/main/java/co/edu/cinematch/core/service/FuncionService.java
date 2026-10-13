package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Funcion;
import co.edu.cinematch.core.repository.Repository;
import java.util.ArrayList;
import java.util.List;

public class FuncionService {
    /** Bloque estándar de anuncios y tráilers incluido en cada función. */
    public static final int MINUTOS_PREVIO_FUNCION = 15;
    private final Repository<Funcion> repo;
    private final PeliculaService peliculas;
    private final CineService cines;

    public FuncionService(Repository<Funcion> repo, PeliculaService peliculas, CineService cines) {
        this.repo = repo;
        this.peliculas = peliculas;
        this.cines = cines;
    }

    public Funcion crear(Funcion funcion) {
        validar(funcion, null);
        return repo.save(funcion);
    }

    public Funcion buscar(String id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Función", id));
    }

    public List<Funcion> listar() {
        return repo.findAll();
    }

    public List<Funcion> porPelicula(String peliculaId) {
        peliculas.buscar(peliculaId);
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : repo.findAll()) {
            if (peliculaId.equals(funcion.getPeliculaId())) resultado.add(funcion);
        }
        return resultado;
    }

    public Funcion actualizar(String id, Funcion datos) {
        Funcion funcion = buscar(id);
        validar(datos, id);
        funcion.setPeliculaId(datos.getPeliculaId());
        funcion.setSalaId(datos.getSalaId());
        funcion.setFechaFuncion(datos.getFechaFuncion());
        funcion.setHoraInicio(datos.getHoraInicio());
        funcion.setHoraFin(datos.getHoraFin());
        funcion.setPrecio(datos.getPrecio());
        funcion.setFormato(datos.getFormato());
        funcion.setTipoFuncion(datos.getTipoFuncion());
        return repo.save(funcion);
    }

    public void cancelar(String id) {
        if (!repo.deleteById(id)) throw new ResourceNotFoundException("Función", id);
    }

    private void validar(Funcion funcion, String idActual) {
        if (funcion == null) throw new ValidationException("La función no puede ser nula.");
        if (funcion.getFechaFuncion() == null) throw new ValidationException("La fecha de la función es obligatoria.");
        if (funcion.getPeliculaId() == null || funcion.getPeliculaId().isBlank()) {
            throw new ValidationException("La película es obligatoria.");
        }
        if (funcion.getSalaId() == null || funcion.getSalaId().isBlank()) {
            throw new ValidationException("La sala es obligatoria.");
        }
        if (funcion.getTipoFuncion() == null || funcion.getTipoFuncion().isBlank()) {
            throw new ValidationException("El tipo de función es obligatorio.");
        }
        if (funcion.getFormato() == null || funcion.getFormato().isBlank()) {
            throw new ValidationException("El formato de la función es obligatorio.");
        }
        if (funcion.getPrecio() <= 0) throw new ValidationException("El precio debe ser mayor a 0.");
        if (funcion.getHoraInicio() == null || funcion.getHoraFin() == null
                || !funcion.getHoraFin().isAfter(funcion.getHoraInicio())) {
            throw new ValidationException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        var pelicula = peliculas.buscar(funcion.getPeliculaId());
        var sala = cines.buscarSala(funcion.getSalaId());
        if (!sala.getTipo().equalsIgnoreCase(funcion.getFormato())) {
            throw new ValidationException("El formato debe coincidir con el tipo de la sala.");
        }

        // La duración no se decide manualmente: una función termina justo al
        // completar los minutos de la película seleccionada. Así la API
        // tampoco acepta horarios manipulados fuera del formulario web.
        var horaEsperada = funcion.getHoraInicio()
                .plusMinutes(pelicula.getDuracion() + MINUTOS_PREVIO_FUNCION);
        if (!horaEsperada.isAfter(funcion.getHoraInicio())) {
            throw new ValidationException("La función no puede terminar al día siguiente.");
        }
        if (!funcion.getHoraFin().equals(horaEsperada)) {
            throw new ValidationException("La hora de fin debe ser " + horaEsperada + ": "
                    + pelicula.getDuracion() + " min de película + " + MINUTOS_PREVIO_FUNCION
                    + " min de anuncios y tráilers.");
        }

        for (Funcion existente : repo.findAll()) {
            if (existente.getId().equals(idActual)) continue;
            boolean mismaSalaYFecha = funcion.getSalaId().equals(existente.getSalaId())
                    && funcion.getFechaFuncion().equals(existente.getFechaFuncion());
            boolean seCruzan = funcion.getHoraInicio().isBefore(existente.getHoraFin())
                    && funcion.getHoraFin().isAfter(existente.getHoraInicio());
            if (mismaSalaYFecha && seCruzan) {
                throw new ValidationException("La sala ya tiene una función programada en ese horario.");
            }
        }
    }
}
