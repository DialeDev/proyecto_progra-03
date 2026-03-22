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
    
}