package models;
import java.time.LocalDate;

// @author DialeMRZ
public class Contratacion {
    private int idContratacion;
    private int idEmpleado;
    private int idContrato;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double salarioBaseAcordado;
    private double valorHoraAcordado;
    private boolean vigente;

    public Contratacion(int idContratacion, int idEmpleado, int idContrato, LocalDate fechaInicio, double salarioBaseAcordado, double valorHoraAcordado) {
        this.idContratacion = idContratacion;
        this.idEmpleado = idEmpleado;
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
        this.salarioBaseAcordado = salarioBaseAcordado;
        this.valorHoraAcordado = valorHoraAcordado;
        this.vigente = true;
    }

    public int getIdContratacion() { return idContratacion; }
    public void setIdContratacion(int idContratacion) { this.idContratacion = idContratacion; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public double getSalarioBaseAcordado() { return salarioBaseAcordado; }
    public void setSalarioBaseAcordado(double salarioBaseAcordado) { this.salarioBaseAcordado = salarioBaseAcordado; }
    public double getValorHoraAcordado() { return valorHoraAcordado; }
    public void setValorHoraAcordado(double valorHoraAcordado) { this.valorHoraAcordado = valorHoraAcordado; }
    public boolean isVigente() { return vigente; }
    public void setVigente(boolean vigente) { this.vigente = vigente; }
}
