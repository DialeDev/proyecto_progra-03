package models;
import java.time.LocalTime;

// @author DialeMRZ
public class Jornada {
    private int idJornada;
    private String nombre;
    private LocalTime horaEntradaEsperada;
    private LocalTime horaSalidaEsperada;
    private int margenToleranciaMinutos;
    private boolean activa;

    public Jornada(int idJornada, String nombre, LocalTime horaEntradaEsperada, LocalTime horaSalidaEsperada, int margenToleranciaMinutos) {
        this.idJornada = idJornada;
        this.nombre = nombre;
        this.horaEntradaEsperada = horaEntradaEsperada;
        this.horaSalidaEsperada = horaSalidaEsperada;
        this.margenToleranciaMinutos = margenToleranciaMinutos;
        this.activa = true;
    }

    public int getIdJornada() { return idJornada; }
    public void setIdJornada(int idJornada) { this.idJornada = idJornada; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalTime getHoraEntradaEsperada() { return horaEntradaEsperada; }
    public void setHoraEntradaEsperada(LocalTime horaEntradaEsperada) { this.horaEntradaEsperada = horaEntradaEsperada; }
    public LocalTime getHoraSalidaEsperada() { return horaSalidaEsperada; }
    public void setHoraSalidaEsperada(LocalTime horaSalidaEsperada) { this.horaSalidaEsperada = horaSalidaEsperada; }
    public int getMargenToleranciaMinutos() { return margenToleranciaMinutos; }
    public void setMargenToleranciaMinutos(int margenToleranciaMinutos) { this.margenToleranciaMinutos = margenToleranciaMinutos; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
