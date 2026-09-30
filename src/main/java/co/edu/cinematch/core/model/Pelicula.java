package co.edu.cinematch.core.model;
import java.time.LocalDate;
public class Pelicula implements Identifiable {
  private String id; private String titulo; private String sinopsis; private int duracion; private String genero; private String clasificacion; private String idioma; private LocalDate fechaEstreno; private String rutaImagen;
  public Pelicula() { }
  public Pelicula(String titulo, String sinopsis, int duracion, String genero, String clasificacion, String idioma, LocalDate fechaEstreno, String rutaImagen) { this.titulo=titulo; this.sinopsis=sinopsis; this.duracion=duracion; this.genero=genero; this.clasificacion=clasificacion; this.idioma=idioma; this.fechaEstreno=fechaEstreno; this.rutaImagen=rutaImagen; }
  public String getId(){return id;} public void setId(String v){id=v;} public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;} public String getSinopsis(){return sinopsis;} public void setSinopsis(String v){sinopsis=v;} public int getDuracion(){return duracion;} public void setDuracion(int v){duracion=v;} public String getGenero(){return genero;} public void setGenero(String v){genero=v;} public String getClasificacion(){return clasificacion;} public void setClasificacion(String v){clasificacion=v;} public String getIdioma(){return idioma;} public void setIdioma(String v){idioma=v;} public LocalDate getFechaEstreno(){return fechaEstreno;} public void setFechaEstreno(LocalDate v){fechaEstreno=v;} public String getRutaImagen(){return rutaImagen;} public void setRutaImagen(String v){rutaImagen=v;}
}
