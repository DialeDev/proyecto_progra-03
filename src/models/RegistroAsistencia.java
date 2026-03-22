package models;
import java.time.LocalDateTime;

/**
 *
 * @author DialeMRZ
 */


public class RegistroAsistencia {
    private long idRegistro;
    private int idEmpleado;
    private LocalDateTime marcaEntrada; 
    private LocalDateTime marcaSalida;  
    private boolean faltaJustificada;

    public RegistroAsistencia(long idRegistro, int idEmpleado, LocalDateTime marcaEntrada) {
        this.idRegistro = idRegistro;
        this.idEmpleado = idEmpleado;
        this.marcaEntrada = marcaEntrada;
        this.marcaSalida = null; 
        this.faltaJustificada = false;
    }
    
}