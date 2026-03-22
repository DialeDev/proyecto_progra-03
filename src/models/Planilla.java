package models;
import java.time.LocalDate;

/**
 *
 * @author DialeMRZ
 */


public class Planilla {
    public enum EstadoPlanilla { BORRADOR, EMITIDA, ANULADA }

    private int idPlanilla;
    private int mes;
    private int anio;
    private LocalDate fechaEmision;
    private EstadoPlanilla estado;

    public Planilla(int idPlanilla, int mes, int anio) {
        this.idPlanilla = idPlanilla;
        this.mes = mes;
        this.anio = anio;
        this.fechaEmision = LocalDate.now(); 
        this.estado = EstadoPlanilla.BORRADOR;
    }

    public int getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(int idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public EstadoPlanilla getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlanilla estado) {
        this.estado = estado;
    }
    
    
    
}