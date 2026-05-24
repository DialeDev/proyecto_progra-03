package controllers;

import dao.RepositorioContratacion;
import dao.RepositorioEmpleado;
import dao.RepositorioPlanilla;
import models.Empleado;
import models.Planilla;
import models.Planilla.EstadoPlanilla;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

// @author DialeMRZ
public class PlanillaController {
    private static final Logger logger = Logger.getLogger(PlanillaController.class.getName());
    private final RepositorioPlanilla planillaDAO;
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioContratacion contratacionDAO;

    public PlanillaController() {
        this.planillaDAO = new RepositorioPlanilla();
        this.empleadoDAO = new RepositorioEmpleado();
        this.contratacionDAO = new RepositorioContratacion();
    }

    public String generarNuevaPlanilla(int mes, int anio) {
        List<Empleado> activos = empleadoDAO.findAllActivos();
        if (activos.isEmpty()) return "No hay empleados activos para generar planilla.";

        if (planillaDAO.existePorMesYAnio(mes, anio))
            return "Ya existe una planilla para " + mes + "/" + anio + ". No se puede crear duplicado.";

        // Pre-validar contratos activos
        List<String> errores = new ArrayList<>();
        activos.forEach(e -> {
            if (contratacionDAO.findContratoActivoByEmpleado(e.getIdEmpleado()).isEmpty()) {
                errores.add("- " + e.getNombres() + " " + e.getApellidos() + " (ID: " + e.getIdEmpleado() + ")");
            }
        });
        if (!errores.isEmpty()) {
            return "No se pudo generar la planilla. Los siguientes empleados no tienen contratación activa:\n"
                + String.join("\n", errores);
        }

        // Todos tienen contrato, delegar transacción al repositorio
        return planillaDAO.crearPlanillaConBoletas(
            new Planilla(0, mes, anio, LocalDate.now(), EstadoPlanilla.BORRADOR),
            activos, mes, anio);
    }

    public List<Planilla> listarTodas() { return planillaDAO.obtenerTodas(); }

    public List<Planilla> filtrarPorEstado(EstadoPlanilla estado) {
        return planillaDAO.obtenerTodas().stream().filter(p -> p.getEstado() == estado).toList();
    }

    public Optional<Planilla> buscarPlanilla(Integer id) { return planillaDAO.obtenerPorId(id); }

    public boolean emitirPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.EMITIDA); }

    public boolean anularPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.ANULADA); }
}
