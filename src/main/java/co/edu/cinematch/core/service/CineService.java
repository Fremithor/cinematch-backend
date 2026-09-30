package co.edu.cinematch.core.service;

import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import co.edu.cinematch.core.model.Asiento;
import co.edu.cinematch.core.model.Cine;
import co.edu.cinematch.core.model.Funcion;
import co.edu.cinematch.core.model.Sala;
import co.edu.cinematch.core.repository.Repository;
import java.util.ArrayList;
import java.util.List;

public class CineService {
    private final Repository<Cine> cines;
    private final Repository<Sala> salas;
    private final Repository<Asiento> asientos;
    private final Repository<Funcion> funciones;

    public CineService(Repository<Cine> cines, Repository<Sala> salas, Repository<Asiento> asientos, Repository<Funcion> funciones) {
        this.cines = cines;
        this.salas = salas;
        this.asientos = asientos;
        this.funciones = funciones;
    }

    public Cine crearCine(Cine cine) {
        validarCine(cine);
        return cines.save(cine);
    }

    public List<Cine> listarCines() { return cines.findAll(); }

    public Cine buscarCine(String id) {
        return cines.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cine", id));
    }

    public Cine actualizarCine(String id, Cine datos) {
        validarCine(datos);
        Cine cine = buscarCine(id);
        cine.setNombre(datos.getNombre());
        cine.setDireccion(datos.getDireccion());
        cine.setCiudad(datos.getCiudad());
        return cines.save(cine);
    }

    public void eliminarCine(String id) {
        buscarCine(id);
        for (Sala sala : listarSalas(id)) eliminarSala(sala.getId());
        cines.deleteById(id);
    }

    public Sala crearSala(String cineId, Sala sala) {
        buscarCine(cineId);
        validarSala(sala);
        sala.setCineId(cineId);
        Sala creada = salas.save(sala);
        for (int posicion = 1; posicion <= creada.getCapacidad(); posicion++) {
            String fila = String.valueOf((char) ('A' + ((posicion - 1) / 5)));
            int numero = ((posicion - 1) % 5) + 1;
            asientos.save(new Asiento(creada.getId(), fila, numero));
        }
        return creada;
    }

    public List<Sala> listarSalas(String cineId) {
        buscarCine(cineId);
        List<Sala> resultado = new ArrayList<>();
        for (Sala sala : salas.findAll()) if (cineId.equals(sala.getCineId())) resultado.add(sala);
        return resultado;
    }

    public Sala buscarSala(String id) {
        return salas.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala", id));
    }

    public Sala actualizarSala(String id, Sala datos) {
        validarSala(datos);
        Sala sala = buscarSala(id);
        sala.setNombre(datos.getNombre());
        sala.setCapacidad(datos.getCapacidad());
        sala.setTipo(datos.getTipo());
        return salas.save(sala);
    }

    public void eliminarSala(String id) {
        buscarSala(id);
        for (Funcion funcion : funciones.findAll()) if (id.equals(funcion.getSalaId())) funciones.deleteById(funcion.getId());
        for (Asiento asiento : asientos.findAll()) if (id.equals(asiento.getSalaId())) asientos.deleteById(asiento.getId());
        salas.deleteById(id);
    }

    public List<Asiento> listarAsientos(String salaId) {
        buscarSala(salaId);
        List<Asiento> resultado = new ArrayList<>();
        for (Asiento asiento : asientos.findAll()) if (salaId.equals(asiento.getSalaId())) resultado.add(asiento);
        return resultado;
    }

    public Asiento buscarAsiento(String id) {
        return asientos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asiento", id));
    }

    private void validarCine(Cine cine) {
        if (cine == null || vacio(cine.getNombre())) throw new ValidationException("El nombre del cine es obligatorio.");
        if (vacio(cine.getCiudad())) throw new ValidationException("La ciudad es obligatoria.");
    }

    private void validarSala(Sala sala) {
        if (sala == null || vacio(sala.getNombre())) throw new ValidationException("El nombre de la sala es obligatorio.");
        if (sala.getCapacidad() <= 0) throw new ValidationException("La capacidad debe ser mayor a 0.");
    }

    private boolean vacio(String valor) { return valor == null || valor.trim().isEmpty(); }
}
