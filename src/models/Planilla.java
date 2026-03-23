package models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 *
 * @author DialeMRZ
 */


public class Planilla {
    //opciones para "Estado"
    public enum EstadoPlanilla { BORRADOR, EMITIDA, ANULADA }

    //definicion de variables y constantes
    private Integer idPlanilla;
    private final int mes;
    private final int anio;
    private final LocalDate fechaEmision;
    private EstadoPlanilla estado;
    
    //definicion de lista
    private final List<BoletaPago> boletas;
    
    //constructor
    public Planilla(int idPlanilla, int mes, int anio, LocalDate fechaEmision, EstadoPlanilla estado) {
        this.idPlanilla = idPlanilla;
        this.mes = mes;
        this.anio = anio;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.boletas = new ArrayList<>();
    }

    //get
    public Integer getIdPlanilla(){return idPlanilla;}
    public int getMes(){return mes;}
    public int getAnio(){return anio;}
    public LocalDate getFechaEmision(){return fechaEmision;}
    public EstadoPlanilla getEstado(){return estado;}
    
    //lista inmodificable
    public List<BoletaPago>getBoletas(){
        return Collections.unmodifiableList(boletas);
    }
    
    //set
    public void setIdPlanilla(Integer idPlanilla){
        this.idPlanilla = idPlanilla;}
    public void setEstado(EstadoPlanilla estado){
        this.estado = estado;}
    
    //meotdo para agregar boletas
    public void agregarBoleta(BoletaPago boleta){
        this.boletas.add(boleta);
    }
}