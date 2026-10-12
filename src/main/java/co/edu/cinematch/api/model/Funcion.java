package co.edu.cinematch.api.model;

import java.time.LocalDate;

public class Funcion {
    private String id;
    private String peliculaId;
    private String cineId;
    private String salaId;
    private Cine cine;
    private Sala sala;
    private LocalDate fecha;
    private String horaInicio;
    private String horaFin;
    private String tipoFuncion;
    private String formato;
    private double precio;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPeliculaId() { return peliculaId; }
    public void setPeliculaId(String peliculaId) { this.peliculaId = peliculaId; }
    public String getCineId() { return cineId; }
    public void setCineId(String cineId) { this.cineId = cineId; }
    public String getSalaId() { return salaId; }
    public void setSalaId(String salaId) { this.salaId = salaId; }
    public Cine getCine() { return cine; }
    public void setCine(Cine cine) { this.cine = cine; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public String getTipoFuncion() { return tipoFuncion; }
    public void setTipoFuncion(String tipoFuncion) { this.tipoFuncion = tipoFuncion; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}

