package models;
import java.math.BigDecimal;
/**
 *
 * @author ADMIN
 */
//creamos una clase record, es una clase especial
//evitamos que sean modificados accidentalmente
    
public record BoletaDetalle(
        Long idDetalle,
        Long idBoleta,
        String concepto,
        String tipo,
        BigDecimal monto
        ){}
