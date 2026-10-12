package co.edu.cinematch.api.model;

import java.util.ArrayList;
import java.util.List;

public class Cine {
    private String id;
    private String nombre;
    private String cadena;
    private String direccion;
    private String ciudad;
    private List<Sala> salas = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCadena() { return cadena; }
    public void setCadena(String cadena) { this.cadena = cadena; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public List<Sala> getSalas() { return salas; }
    public void setSalas(List<Sala> salas) { this.salas = salas == null ? new ArrayList<>() : salas; }
}

