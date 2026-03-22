package models;
import java.time.LocalDate;

/**
 *
 * @author DialeMRZ
 */

public class Contratacion {
    private int idContratacion;
    private int idEmpleado;
    private int idContrato;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; 
    private double salarioBaseAcordado; 
    private double valorHoraAcordado;   
    private boolean vigente;

    public Contratacion(int idContratacion, int idEmpleado, int idContrato, 
                        LocalDate fechaInicio, double salarioBaseAcordado, double valorHoraAcordado) {
        this.idContratacion = idContratacion;
        this.idEmpleado = idEmpleado;
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null; 
        this.salarioBaseAcordado = salarioBaseAcordado;
        this.valorHoraAcordado = valorHoraAcordado;
        this.vigente = true;
    }
    
}