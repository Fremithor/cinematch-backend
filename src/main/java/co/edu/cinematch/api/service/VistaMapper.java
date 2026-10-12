package co.edu.cinematch.api.service;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.api.model.Cine;
import co.edu.cinematch.api.model.Funcion;
import co.edu.cinematch.api.model.Sala;
import org.springframework.stereotype.Component;

@Component
public class VistaMapper {
    private final CineMatchBackend backend;
    public VistaMapper(CineMatchBackend backend) { this.backend = backend; }

    public Cine cine(co.edu.cinematch.core.model.Cine origen) {
        Cine destino = new Cine(); destino.setId(origen.getId()); destino.setCadena(origen.getCadena());
        destino.setNombre(origen.getNombre()); destino.setDireccion(origen.getDireccion()); destino.setCiudad(origen.getCiudad());
        destino.setSalas(backend.cines.listarSalas(origen.getId()).stream().map(this::sala).toList());
        return destino;
    }

    public Sala sala(co.edu.cinematch.core.model.Sala origen) {
        Sala destino = new Sala(); destino.setId(origen.getId()); destino.setNombre(origen.getNombre());
        destino.setCapacidad(origen.getCapacidad()); destino.setTipo(origen.getTipo()); return destino;
    }

    public Funcion funcion(co.edu.cinematch.core.model.Funcion origen) {
        Funcion destino = new Funcion(); destino.setId(origen.getId()); destino.setPeliculaId(origen.getPeliculaId());
        destino.setSalaId(origen.getSalaId()); destino.setFecha(origen.getFechaFuncion());
        destino.setHoraInicio(origen.getHoraInicio().toString()); destino.setHoraFin(origen.getHoraFin().toString());
        destino.setTipoFuncion(origen.getTipoFuncion()); destino.setFormato(origen.getFormato()); destino.setPrecio(origen.getPrecio());
        co.edu.cinematch.core.model.Sala salaOrigen = backend.cines.buscarSala(origen.getSalaId());
        destino.setSala(sala(salaOrigen));
        co.edu.cinematch.core.model.Cine cineOrigen = backend.cines.listarCines().stream()
                .filter(c -> c.getId().equals(salaOrigen.getCineId())).findFirst().orElseThrow();
        destino.setCine(cine(cineOrigen)); return destino;
    }
}

