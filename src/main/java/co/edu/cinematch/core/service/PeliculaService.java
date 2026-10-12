package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Pelicula;
import co.edu.cinematch.core.repository.Repository;
import java.util.List;

public class PeliculaService {
    private final Repository<Pelicula> repo;

    public PeliculaService(Repository<Pelicula> repo) {
        this.repo = repo;
    }

    public Pelicula crear(Pelicula pelicula) {
        validar(pelicula, null);
        return repo.save(pelicula);
    }

    public List<Pelicula> listar() {
        return repo.findAll();
    }

    public Pelicula buscar(String id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Película", id));
    }

    public Pelicula actualizar(String id, Pelicula datos) {
        Pelicula pelicula = buscar(id);
        validar(datos, id);
        pelicula.setTitulo(datos.getTitulo());
        pelicula.setSinopsis(datos.getSinopsis());
        pelicula.setDuracion(datos.getDuracion());
        pelicula.setGenero(datos.getGenero());
        pelicula.setClasificacion(datos.getClasificacion());
        pelicula.setIdioma(datos.getIdioma());
        pelicula.setFechaEstreno(datos.getFechaEstreno());
        pelicula.setRutaImagen(datos.getRutaImagen());
        return repo.save(pelicula);
    }

    public void eliminar(String id) {
        if (!repo.deleteById(id)) throw new ResourceNotFoundException("Película", id);
    }

    private void validar(Pelicula pelicula, String idActual) {
        if (pelicula == null) throw new ValidationException("La película no puede ser nula.");
        if (blank(pelicula.getTitulo())) throw new ValidationException("El título de la película es obligatorio.");
        if (blank(pelicula.getSinopsis())) throw new ValidationException("La sinopsis de la película es obligatoria.");
        if (pelicula.getDuracion() <= 0) throw new ValidationException("La duración debe ser mayor a 0 minutos.");
        if (blank(pelicula.getGenero())) throw new ValidationException("El género es obligatorio.");
        if (blank(pelicula.getClasificacion())) throw new ValidationException("La clasificación es obligatoria.");
        if (blank(pelicula.getIdioma())) throw new ValidationException("El idioma es obligatorio.");
        if (pelicula.getFechaEstreno() == null) throw new ValidationException("La fecha de estreno es obligatoria.");
        if (blank(pelicula.getRutaImagen())) throw new ValidationException("La URL del póster es obligatoria.");
        String tituloNormalizado = pelicula.getTitulo().trim();
        for (Pelicula existente : repo.findAll()) {
            if (existente.getId().equals(idActual)) continue;
            if (existente.getTitulo().trim().equalsIgnoreCase(tituloNormalizado)) {
                throw new ValidationException("Ya existe una película registrada con ese título.");
            }
        }
    }

    private boolean blank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
