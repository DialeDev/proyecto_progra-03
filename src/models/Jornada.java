package models;
import java.time.LocalTime;

/**
 *
 * @author DialeMRZ
 */

public class Jornada {
    private int idJornada;
    private String nombre;
    private LocalTime horaEntradaEsperada; 
    private LocalTime horaSalidaEsperada;  
    private int margenToleranciaMinutos;   
    private boolean activa;

    public Jornada(int idJornada, String nombre, LocalTime horaEntradaEsperada, 
                   LocalTime horaSalidaEsperada, int margenToleranciaMinutos) {
        this.idJornada = idJornada;
        this.nombre = nombre;
        this.horaEntradaEsperada = horaEntradaEsperada;
        this.horaSalidaEsperada = horaSalidaEsperada;
        this.margenToleranciaMinutos = margenToleranciaMinutos;
        this.activa = true;
    }
    
}