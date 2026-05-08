package services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraHorasExtras {
    private static final BigDecimal DIURNO_FACTOR = new BigDecimal("2.0");
    private static final BigDecimal NOCTURNO_FACTOR = new BigDecimal("2.25");
    private static final BigDecimal FERIADO_FACTOR = new BigDecimal("3.0");

    public enum TipoHoraExtra { DIURNA, NOCTURNA, FERIADO }

    public static BigDecimal calcularPagoHorasExtras(BigDecimal valorHora, double horas, TipoHoraExtra tipo) {
        BigDecimal factor = switch (tipo) {
            case DIURNA -> DIURNO_FACTOR;
            case NOCTURNA -> NOCTURNO_FACTOR;
            case FERIADO -> FERIADO_FACTOR;
        };
        return valorHora.multiply(factor).multiply(BigDecimal.valueOf(horas)).setScale(2, RoundingMode.HALF_UP);
    }
}
