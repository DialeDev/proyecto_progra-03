package controllers;

import dao.RepositorioAsistencia;
import dao.RepositorioBoletaDetalle;
import dao.RepositorioBoletaPago;
import dao.RepositorioEmpleado;
import dao.RepositorioPlanilla;
import models.BoletaPago;
import models.Empleado;
import models.Planilla;
import models.RegistroAsistencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteController {
    private final RepositorioAsistencia asistenciaDAO;
    private final RepositorioBoletaPago boletaDAO;
    private final RepositorioBoletaDetalle detalleDAO;
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioPlanilla planillaDAO;

    public ReporteController() {
        this.asistenciaDAO = new RepositorioAsistencia();
        this.boletaDAO = new RepositorioBoletaPago();
        this.detalleDAO = new RepositorioBoletaDetalle();
        this.empleadoDAO = new RepositorioEmpleado();
        this.planillaDAO = new RepositorioPlanilla();
    }

    public List<Empleado> listarEmpleadosActivos() {
        return empleadoDAO.findAllActivos();
    }

    public List<Planilla> listarPlanillas() {
        return planillaDAO.findAll();
    }

    public List<RegistroAsistencia> reporteAsistencia(int idEmpleado, LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta))
            throw new IllegalArgumentException("La fecha desde debe ser anterior a la fecha hasta.");
        return asistenciaDAO.findByEmpleadoAndRango(idEmpleado, desde, hasta);
    }

    public List<Object[]> reporteAsistenciaFormateado(int idEmpleado, LocalDate desde, LocalDate hasta, AsistenciaController asisCtrl) {
        String nombreEmpleado = empleadoDAO.findById(idEmpleado)
                .map(e -> e.getNombres() + " " + e.getApellidos()).orElse("?");
        List<RegistroAsistencia> registros = reporteAsistencia(idEmpleado, desde, hasta);
        List<Object[]> filas = new ArrayList<>();
        for (RegistroAsistencia r : registros) {
            String entrada = r.getMarcaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String salida = r.getMarcaSalida() != null
                    ? r.getMarcaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "—";
            String horas = r.getMarcaSalida() != null
                    ? String.format("%.2f", asisCtrl.calcularHorasTrabajadas(r)) + " hrs" : "En curso";
            String estado = r.getMarcaSalida() != null ? "Completada" : "Activa";
            filas.add(new Object[]{r.getIdRegistro(), nombreEmpleado, entrada, salida, horas, estado});
        }
        return filas;
    }

    public List<BoletaPago> boletasPorPlanilla(int idPlanilla) {
        return boletaDAO.findByPlanilla(idPlanilla);
    }

    public List<Object[]> boletasPorPlanillaFormateado(int idPlanilla) {
        List<BoletaPago> boletas = boletasPorPlanilla(idPlanilla);
        List<Object[]> filas = new ArrayList<>();
        for (BoletaPago b : boletas) {
            String empleado = empleadoDAO.findById(b.idEmpleado())
                    .map(e -> e.getNombres() + " " + e.getApellidos()).orElse("?");
            filas.add(new Object[]{
                b.idBoleta(), empleado, String.valueOf(b.idPlanilla()),
                String.format("$%.2f", b.totalIngresos()),
                String.format("$%.2f", b.totalDeducciones()),
                String.format("$%.2f", b.liquidoAPagar())
            });
        }
        return filas;
    }

    public BoletaPago boletaCompleta(long idBoleta) {
        return boletaDAO.findById(idBoleta).orElse(null);
    }

    public Object[] totalesPlanilla(int idPlanilla) {
        List<BoletaPago> boletas = boletasPorPlanilla(idPlanilla);
        BigDecimal totalIngresos = boletas.stream()
                .map(BoletaPago::totalIngresos).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDeducciones = boletas.stream()
                .map(BoletaPago::totalDeducciones).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLiquido = boletas.stream()
                .map(BoletaPago::liquidoAPagar).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Object[]{totalIngresos, totalDeducciones, totalLiquido};
    }
}
