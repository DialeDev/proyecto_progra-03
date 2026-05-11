package services;

import dao.*;
import models.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GeneradorBoletas {
    private final RepositorioAsistencia asistenciaDAO;
    private final RepositorioContratacion contratacionDAO;
    private final RepositorioBoletaPago boletaPagoDAO;
    private final RepositorioBoletaDetalle boletaDetalleDAO;

    public GeneradorBoletas() {
        this.asistenciaDAO = new RepositorioAsistencia();
        this.contratacionDAO = new RepositorioContratacion();
        this.boletaPagoDAO = new RepositorioBoletaPago();
        this.boletaDetalleDAO = new RepositorioBoletaDetalle();
    }

    public BoletaPago generarBoleta(int idEmpleado, int mes, int anio) {
        // Obtener contratacion activa del empleado
        Optional<Contratacion> optCont = contratacionDAO.findContratoActivoByEmpleado(idEmpleado);
        if (optCont.isEmpty()) throw new RuntimeException("El empleado no tiene contratación activa.");
        Contratacion contratacion = optCont.get();

        BigDecimal valorHora = BigDecimal.valueOf(contratacion.getValorHoraAcordado());
        BigDecimal salarioBase = BigDecimal.valueOf(contratacion.getSalarioBaseAcordado());
        if (valorHora.compareTo(BigDecimal.ZERO) <= 0) valorHora = salarioBase.divide(BigDecimal.valueOf(160), 2, RoundingMode.HALF_UP);

        // Obtener asistencias del mes
        List<RegistroAsistencia> asistencias = asistenciaDAO.findByEmpleadoAndMes(idEmpleado, mes, anio);
        BigDecimal horasOrdinarias = BigDecimal.ZERO;
        BigDecimal horasExtraDiurnas = BigDecimal.ZERO;
        BigDecimal horasExtraNocturnas = BigDecimal.ZERO;

        for (RegistroAsistencia r : asistencias) {
            if (r.getMarcaSalida() != null) {
                long minutos = ChronoUnit.MINUTES.between(r.getMarcaEntrada(), r.getMarcaSalida());
                BigDecimal horas = BigDecimal.valueOf(minutos).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
                horasOrdinarias = horasOrdinarias.add(horas);
            }
        }

        BigDecimal totalIngresos = salarioBase;
        BigDecimal afp = CalculadoraDeduccionesLey.calcularAFP(salarioBase);
        BigDecimal isss = CalculadoraDeduccionesLey.calcularISSS(salarioBase);
        BigDecimal isr = CalculadoraDeduccionesLey.calcularISR(salarioBase.subtract(afp).subtract(isss));
        BigDecimal totalDeducciones = afp.add(isss).add(isr);
        BigDecimal liquido = totalIngresos.subtract(totalDeducciones);

        List<BoletaDetalle> detalles = new ArrayList<>();
        detalles.add(new BoletaDetalle(null, null, "Sueldo Base", "Ingreso", salarioBase));
        detalles.add(new BoletaDetalle(null, null, "AFP (7.25%)", "Deduccion", afp));
        detalles.add(new BoletaDetalle(null, null, "ISSS (3%)", "Deduccion", isss));
        if (isr.compareTo(BigDecimal.ZERO) > 0)
            detalles.add(new BoletaDetalle(null, null, "ISR", "Deduccion", isr));

        return new BoletaPago(null, null, idEmpleado, salarioBase, horasOrdinarias,
            horasExtraDiurnas, horasExtraNocturnas, totalIngresos, totalDeducciones, liquido, detalles);
    }

    public BoletaPago generarYGuardarBoleta(int idPlanilla, int idEmpleado, int mes, int anio) {
        BoletaPago bp = generarBoleta(idEmpleado, mes, anio);
        BoletaPago bpConId = new BoletaPago(null, idPlanilla, bp.idEmpleado(),
            bp.sueldoBaseAplicado(), bp.horasOrdinariasCalculadas(),
            bp.horasExtraDiurnas(), bp.horasExtraNocturnas(),
            bp.totalIngresos(), bp.totalDeducciones(), bp.liquidoAPagar(), List.of());
        Long idBoleta = boletaPagoDAO.create(bpConId);
        if (idBoleta == null) throw new RuntimeException("Error al crear boleta en BD.");
        List<BoletaDetalle> detallesGuardados = bp.detalles().stream().map(d -> {
            BoletaDetalle nuevo = new BoletaDetalle(null, idBoleta, d.concepto(), d.tipo(), d.monto());
            boletaDetalleDAO.create(nuevo);
            return nuevo;
        }).toList();
        return new BoletaPago(idBoleta, idPlanilla, bp.idEmpleado(),
            bp.sueldoBaseAplicado(), bp.horasOrdinariasCalculadas(),
            bp.horasExtraDiurnas(), bp.horasExtraNocturnas(),
            bp.totalIngresos(), bp.totalDeducciones(), bp.liquidoAPagar(), detallesGuardados);
    }

    public BoletaPago generarYGuardarBoleta(Connection conn, int idPlanilla, int idEmpleado, int mes, int anio) {
        BoletaPago bp = generarBoleta(idEmpleado, mes, anio);
        BoletaPago bpConId = new BoletaPago(null, idPlanilla, bp.idEmpleado(),
            bp.sueldoBaseAplicado(), bp.horasOrdinariasCalculadas(),
            bp.horasExtraDiurnas(), bp.horasExtraNocturnas(),
            bp.totalIngresos(), bp.totalDeducciones(), bp.liquidoAPagar(), List.of());
        Long idBoleta = boletaPagoDAO.create(conn, bpConId);
        if (idBoleta == null) throw new RuntimeException("Error al crear boleta en BD.");
        List<BoletaDetalle> detallesGuardados = bp.detalles().stream().map(d -> {
            BoletaDetalle nuevo = new BoletaDetalle(null, idBoleta, d.concepto(), d.tipo(), d.monto());
            boletaDetalleDAO.create(conn, nuevo);
            return nuevo;
        }).toList();
        return new BoletaPago(idBoleta, idPlanilla, bp.idEmpleado(),
            bp.sueldoBaseAplicado(), bp.horasOrdinariasCalculadas(),
            bp.horasExtraDiurnas(), bp.horasExtraNocturnas(),
            bp.totalIngresos(), bp.totalDeducciones(), bp.liquidoAPagar(), detallesGuardados);
    }
}
