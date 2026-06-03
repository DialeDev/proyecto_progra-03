package services;

import dao.*;
import models.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneradorBoletas {
    /** Horas mensuales estándar (8 h × 20 días). */
    private static final BigDecimal HORAS_MENSUALES = BigDecimal.valueOf(160);
    private static final LocalTime DIURNO_INICIO = LocalTime.of(6, 0);
    private static final LocalTime DIURNO_FIN = LocalTime.of(20, 0); // exclusive

    private final RepositorioAsistencia asistenciaDAO;
    private final RepositorioContratacion contratacionDAO;
    private final RepositorioBoletaPago boletaPagoDAO;
    private final RepositorioBoletaDetalle boletaDetalleDAO;
    private final RepositorioAsignacionJornada asignacionDAO;
    private final RepositorioJornada jornadaDAO;

    public GeneradorBoletas() {
        this.asistenciaDAO = new RepositorioAsistencia();
        this.contratacionDAO = new RepositorioContratacion();
        this.boletaPagoDAO = new RepositorioBoletaPago();
        this.boletaDetalleDAO = new RepositorioBoletaDetalle();
        this.asignacionDAO = new RepositorioAsignacionJornada();
        this.jornadaDAO = new RepositorioJornada();
    }

    /**
     * Calcula los minutos de superposición entre dos rangos horarios.
     * Ambos rangos se asumen dentro del mismo día.
     */
    private long overlapMinutos(LocalTime inicio, LocalTime fin,
                                LocalTime rInicio, LocalTime rFin) {
        LocalTime s = inicio.isAfter(rInicio) ? inicio : rInicio;
        LocalTime e = fin.isBefore(rFin) ? fin : rFin;
        if (s.isBefore(e)) return ChronoUnit.MINUTES.between(s, e);
        return 0;
    }

    public BoletaPago generarBoleta(int idEmpleado, int mes, int anio) {
        // ── 1. Contratación activa ──────────────────────────────────
        Optional<Contratacion> optCont = contratacionDAO.findContratoActivoByEmpleado(idEmpleado);
        if (optCont.isEmpty()) throw new RuntimeException("El empleado no tiene contratación activa.");
        Contratacion contratacion = optCont.get();

        BigDecimal valorHora = BigDecimal.valueOf(contratacion.getValorHoraAcordado());
        BigDecimal salarioBase = BigDecimal.valueOf(contratacion.getSalarioBaseAcordado());
        if (valorHora.compareTo(BigDecimal.ZERO) <= 0)
            valorHora = salarioBase.divide(HORAS_MENSUALES, 2, RoundingMode.HALF_UP);

        // ── 2. Jornada asignada al empleado ─────────────────────────
        Optional<Jornada> optJornada = asignacionDAO.findByEmpleado(idEmpleado)
            .flatMap(a -> jornadaDAO.findById(a.getIdJornada()));

        // ── 3. Asistencias del mes ──────────────────────────────────
        List<RegistroAsistencia> asistencias = asistenciaDAO.findByEmpleadoAndMes(idEmpleado, mes, anio);

        BigDecimal horasOrdinarias = BigDecimal.ZERO;
        BigDecimal horasExtraDiurnas = BigDecimal.ZERO;
        BigDecimal horasExtraNocturnas = BigDecimal.ZERO;

        for (RegistroAsistencia r : asistencias) {
            if (r.getMarcaSalida() == null) continue;

            LocalTime entrada = r.getMarcaEntrada().toLocalTime();
            LocalTime salida  = r.getMarcaSalida().toLocalTime();
            long totalMin = ChronoUnit.MINUTES.between(r.getMarcaEntrada(), r.getMarcaSalida());

            if (optJornada.isPresent()) {
                Jornada j = optJornada.get();
                LocalTime jEnt = j.getHoraEntradaEsperada();
                LocalTime jSal = j.getHoraSalidaEsperada();

                // Ordinarias = dentro del turno
                long ordMin = overlapMinutos(entrada, salida, jEnt, jSal);
                horasOrdinarias = horasOrdinarias.add(
                    BigDecimal.valueOf(ordMin).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));

                // Extra = fuera del turno
                long extraAntes = 0, extraDespues = 0;
                if (entrada.isBefore(jEnt))
                    extraAntes = overlapMinutos(entrada, salida, LocalTime.MIN, jEnt);
                if (salida.isAfter(jSal))
                    extraDespues = overlapMinutos(entrada, salida, jSal, LocalTime.MAX);

                // Clasificar extra como diurna/nocturna (diurna = 06:00–19:59)
                long extraDiurno = 0;
                if (extraAntes > 0) {
                    LocalTime antesFin = salida.isBefore(jEnt) ? salida : jEnt;
                    extraDiurno += overlapMinutos(entrada, antesFin, DIURNO_INICIO, DIURNO_FIN);
                }
                if (extraDespues > 0) {
                    LocalTime despuesIni = entrada.isAfter(jSal) ? entrada : jSal;
                    extraDiurno += overlapMinutos(despuesIni, salida, DIURNO_INICIO, DIURNO_FIN);
                }
                long extraNocturno = (extraAntes + extraDespues) - extraDiurno;
                if (extraNocturno < 0) extraNocturno = 0;

                horasExtraDiurnas = horasExtraDiurnas.add(
                    BigDecimal.valueOf(extraDiurno).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
                horasExtraNocturnas = horasExtraNocturnas.add(
                    BigDecimal.valueOf(extraNocturno).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
            } else {
                // Sin jornada asignada: todas las horas son ordinarias
                horasOrdinarias = horasOrdinarias.add(
                    BigDecimal.valueOf(totalMin).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
            }
        }

        // ── 4. Cálculo de pagos ─────────────────────────────────────
        // Pago ordinario: valorHora × horas, topeado al salario base mensual
        BigDecimal pagoOrdinario = valorHora.multiply(horasOrdinarias)
            .setScale(2, RoundingMode.HALF_UP);
        if (pagoOrdinario.compareTo(salarioBase) > 0)
            pagoOrdinario = salarioBase;

        BigDecimal pagoExtraDiurno = horasExtraDiurnas.compareTo(BigDecimal.ZERO) > 0
            ? CalculadoraHorasExtras.calcularPagoHorasExtras(
                  valorHora, horasExtraDiurnas.doubleValue(),
                  CalculadoraHorasExtras.TipoHoraExtra.DIURNA)
            : BigDecimal.ZERO;

        BigDecimal pagoExtraNocturno = horasExtraNocturnas.compareTo(BigDecimal.ZERO) > 0
            ? CalculadoraHorasExtras.calcularPagoHorasExtras(
                  valorHora, horasExtraNocturnas.doubleValue(),
                  CalculadoraHorasExtras.TipoHoraExtra.NOCTURNA)
            : BigDecimal.ZERO;

        BigDecimal totalIngresos = pagoOrdinario.add(pagoExtraDiurno).add(pagoExtraNocturno);

        // ── 5. Deducciones de ley sobre el total de ingresos ────────
        BigDecimal afp = CalculadoraDeduccionesLey.calcularAFP(totalIngresos);
        BigDecimal isss = CalculadoraDeduccionesLey.calcularISSS(totalIngresos);
        BigDecimal isr = CalculadoraDeduccionesLey.calcularISR(totalIngresos.subtract(afp).subtract(isss));
        BigDecimal totalDeducciones = afp.add(isss).add(isr);
        BigDecimal liquido = totalIngresos.subtract(totalDeducciones);

        // ── 6. Detalles de la boleta ────────────────────────────────
        List<BoletaDetalle> detalles = new ArrayList<>();
        detalles.add(new BoletaDetalle(null, null, "Sueldo Base", "Ingreso", salarioBase));
        detalles.add(new BoletaDetalle(null, null,
            "Horas Ordinarias (" + horasOrdinarias + " h)", "Ingreso", pagoOrdinario));
        if (pagoExtraDiurno.compareTo(BigDecimal.ZERO) > 0)
            detalles.add(new BoletaDetalle(null, null,
                "Horas Extra Diurnas (" + horasExtraDiurnas + " h ×2.0)", "Ingreso", pagoExtraDiurno));
        if (pagoExtraNocturno.compareTo(BigDecimal.ZERO) > 0)
            detalles.add(new BoletaDetalle(null, null,
                "Horas Extra Nocturnas (" + horasExtraNocturnas + " h ×2.25)", "Ingreso", pagoExtraNocturno));
        detalles.add(new BoletaDetalle(null, null, "AFP (7.25%)", "Deduccion", afp));
        detalles.add(new BoletaDetalle(null, null, "ISSS (3%)", "Deduccion", isss));
        if (isr.compareTo(BigDecimal.ZERO) > 0)
            detalles.add(new BoletaDetalle(null, null, "ISR", "Deduccion", isr));

        return new BoletaPago(null, null, idEmpleado,
            pagoOrdinario, horasOrdinarias,
            horasExtraDiurnas, horasExtraNocturnas,
            totalIngresos, totalDeducciones, liquido, detalles);
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
