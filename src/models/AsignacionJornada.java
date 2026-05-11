package models;
import java.time.LocalDate;

public class AsignacionJornada {
    private int idAsignacion;
    private int idEmpleado;
    private int idJornada;
    private LocalDate fechaAsignacion;
    private LocalDate fechaFin;
    private boolean activa;

    public AsignacionJornada(int idAsignacion, int idEmpleado, int idJornada, LocalDate fechaAsignacion, LocalDate fechaFin, boolean activa) {
        this.idAsignacion = idAsignacion;
        this.idEmpleado = idEmpleado;
        this.idJornada = idJornada;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaFin = fechaFin;
        this.activa = activa;
    }

    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public int getIdJornada() { return idJornada; }
    public void setIdJornada(int idJornada) { this.idJornada = idJornada; }
    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
