package models;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ADMIN
 */

/*implementamos 'record' que es una clase especial, sus datos no 
cambian una vez creados, su constructor, get, etc se generan automaticamente

Y usamos BigDecimal para el tema del dinero
*/
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
            ){
        public BoletaPago{
            if(liquidoAPagar.compareTo(BigDecimal.ZERO)<0){
                throw new IllegalArgumentException("El líquido a pagar no puede ser negativo");
            }
            detalles = List.copyOf(detalles);
        }
    }

