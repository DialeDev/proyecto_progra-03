package models;
import java.time.LocalDate;
import java.util.*;

// @author DialeMRZ
public class Planilla {
    public enum EstadoPlanilla { BORRADOR, EMITIDA, ANULADA }

    private Integer idPlanilla;
    private final int mes;
    private final int anio;
    private final LocalDate fechaEmision;
    private EstadoPlanilla estado;
    private final List<BoletaPago> boletas;

    public Planilla(int idPlanilla, int mes, int anio, LocalDate fechaEmision, EstadoPlanilla estado) {
        this.idPlanilla = idPlanilla;
        this.mes = mes;
        this.anio = anio;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.boletas = new ArrayList<>();
    }

    public Integer getIdPlanilla() { return idPlanilla; }
    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public EstadoPlanilla getEstado() { return estado; }
    public List<BoletaPago> getBoletas() { return Collections.unmodifiableList(boletas); }
    public void setIdPlanilla(Integer idPlanilla) { this.idPlanilla = idPlanilla; }
    public void setEstado(EstadoPlanilla estado) { this.estado = estado; }
    public void agregarBoleta(BoletaPago boleta) { this.boletas.add(boleta); }
}
