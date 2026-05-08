package models;
import java.math.BigDecimal;
import java.util.List;

// @author ADMIN
// Record inmutable para boletas de pago con BigDecimal para precisión monetaria
public record BoletaPago(
    Long idBoleta,
    Integer idPlanilla,
    Integer idEmpleado,
    BigDecimal sueldoBaseAplicado,
    BigDecimal horasOrdinariasCalculadas,
    BigDecimal horasExtraDiurnas,
    BigDecimal horasExtraNocturnas,
    BigDecimal totalIngresos,
    BigDecimal totalDeducciones,
    BigDecimal liquidoAPagar,
    List<BoletaDetalle> detalles
) {
    public BoletaPago {
        if (liquidoAPagar.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El líquido a pagar no puede ser negativo");
        detalles = List.copyOf(detalles);
    }
}
