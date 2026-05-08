package models;
import java.math.BigDecimal;

// @author ADMIN
// Record inmutable para detalles de boleta
public record BoletaDetalle(
    Long idDetalle,
    Long idBoleta,
    String concepto,
    String tipo,
    BigDecimal monto
) {}
