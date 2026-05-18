package services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraDeduccionesLey {
    private static final BigDecimal AFP_TASA = new BigDecimal("0.0725");
    private static final BigDecimal ISSS_TASA = new BigDecimal("0.03");
    private static final BigDecimal ISR_EXENTO = new BigDecimal("472.00");
    private static final BigDecimal ISR_TRAMO1 = new BigDecimal("895.24");
    private static final BigDecimal ISR_TRAMO2 = new BigDecimal("2038.10");
    private static final BigDecimal ISR_CUOTA1 = new BigDecimal("17.67");
    private static final BigDecimal ISR_CUOTA2 = new BigDecimal("60.00");
    private static final BigDecimal ISR_CUOTA3 = new BigDecimal("288.57");
    private static final BigDecimal ISR_TASA1 = new BigDecimal("0.10");
    private static final BigDecimal ISR_TASA2 = new BigDecimal("0.20");
    private static final BigDecimal ISR_TASA3 = new BigDecimal("0.30");

    public static BigDecimal calcularAFP(BigDecimal salarioBase) {
        return salarioBase.multiply(AFP_TASA).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularISSS(BigDecimal salarioBase) {
        BigDecimal isss = salarioBase.multiply(ISSS_TASA).setScale(2, RoundingMode.HALF_UP);
        return isss.compareTo(new BigDecimal("30.00")) > 0 ? new BigDecimal("30.00") : isss;
    }

    public static BigDecimal calcularISR(BigDecimal salarioBase) {
        BigDecimal salario = salarioBase.setScale(2, RoundingMode.HALF_UP);
        if (salario.compareTo(ISR_EXENTO) <= 0) return BigDecimal.ZERO;
        BigDecimal excedente = salario.subtract(ISR_EXENTO);
        if (salario.compareTo(ISR_TRAMO1) <= 0)
            return excedente.multiply(ISR_TASA1).setScale(2, RoundingMode.HALF_UP);
        if (salario.compareTo(ISR_TRAMO2) <= 0)
            return salario.subtract(ISR_TRAMO1).multiply(ISR_TASA2).add(ISR_CUOTA2).setScale(2, RoundingMode.HALF_UP);
        return salario.subtract(ISR_TRAMO2).multiply(ISR_TASA3).add(ISR_CUOTA3).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularDeduccionesTotales(BigDecimal salarioBase) {
        return calcularAFP(salarioBase).add(calcularISSS(salarioBase)).add(calcularISR(salarioBase));
    }
}
