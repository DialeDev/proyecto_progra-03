package models;
import java.time.LocalDateTime;

// @author DialeMRZ
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

    public long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(long idRegistro) { this.idRegistro = idRegistro; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public LocalDateTime getMarcaEntrada() { return marcaEntrada; }
    public void setMarcaEntrada(LocalDateTime marcaEntrada) { this.marcaEntrada = marcaEntrada; }
    public LocalDateTime getMarcaSalida() { return marcaSalida; }
    public void setMarcaSalida(LocalDateTime marcaSalida) { this.marcaSalida = marcaSalida; }
    public boolean isFaltaJustificada() { return faltaJustificada; }
    public void setFaltaJustificada(boolean faltaJustificada) { this.faltaJustificada = faltaJustificada; }
}
