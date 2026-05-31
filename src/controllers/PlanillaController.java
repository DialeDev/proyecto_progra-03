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

        // Separar empleados CON contrato activo de los que no tienen
        List<Empleado> conContrato = new ArrayList<>();
        List<String> omitidos = new ArrayList<>();
        activos.forEach(e -> {
            if (contratacionDAO.findContratoActivoByEmpleado(e.getIdEmpleado()).isPresent()) {
                conContrato.add(e);
            } else {
                omitidos.add("- " + e.getNombres() + " " + e.getApellidos() + " (ID: " + e.getIdEmpleado() + ")");
            }
        });

        if (conContrato.isEmpty())
            return "Ningún empleado activo tiene contratación vigente. No se generó la planilla.";

        // Generar planilla solo con los empleados que tienen contrato
        String resultado = planillaDAO.crearPlanillaConBoletas(
            new Planilla(0, mes, anio, LocalDate.now(), EstadoPlanilla.BORRADOR),
            conContrato, mes, anio);

        // Si hubo omitidos, advertir al final
        if (!omitidos.isEmpty()) {
            resultado += "\n\nADVERTENCIA: Los siguientes empleados no tienen contratación activa y fueron omitidos:\n"
                + String.join("\n", omitidos);
        }
        return resultado;
    }

    public List<Planilla> listarTodas() { return planillaDAO.obtenerTodas(); }

    public List<Planilla> filtrarPorEstado(EstadoPlanilla estado) {
        return planillaDAO.obtenerTodas().stream().filter(p -> p.getEstado() == estado).toList();
    }

    public Optional<Planilla> buscarPlanilla(Integer id) { return planillaDAO.obtenerPorId(id); }

    public boolean emitirPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.EMITIDA); }

    public boolean anularPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.ANULADA); }
}
