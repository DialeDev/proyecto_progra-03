package controllers;

import dao.Conexion;
import dao.RepositorioContratacion;
import dao.RepositorioEmpleado;
import dao.RepositorioPlanilla;
import models.Empleado;
import models.Planilla;
import models.Planilla.EstadoPlanilla;
import services.GeneradorBoletas;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

// @author DialeMRZ
public class PlanillaController {
    private static final Logger logger = Logger.getLogger(PlanillaController.class.getName());
    private final RepositorioPlanilla planillaDAO;
    private final GeneradorBoletas generador;
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioContratacion contratacionDAO;

    public PlanillaController() {
        this.planillaDAO = new RepositorioPlanilla();
        this.generador = new GeneradorBoletas();
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

        // Todos tienen contrato, proceder con transaccion
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            int idPlanilla = planillaDAO.crear(conn, new Planilla(0, mes, anio, LocalDate.now(), EstadoPlanilla.BORRADOR));
            for (Empleado e : activos) {
                generador.generarYGuardarBoleta(conn, idPlanilla, e.getIdEmpleado(), mes, anio);
            }
            conn.commit();
            return "Planilla creada. Boletas generadas: " + activos.size() + " de " + activos.size() + " empleados activos.";
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { logger.severe("Rollback fallo: " + ex.getMessage()); }
            return "Error al generar planilla: " + e.getMessage();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { logger.severe("Error cerrando conexion: " + e.getMessage()); }
        }
    }

    public List<Planilla> listarTodas() { return planillaDAO.obtenerTodas(); }

    public List<Planilla> filtrarPorEstado(EstadoPlanilla estado) {
        return planillaDAO.obtenerTodas().stream().filter(p -> p.getEstado() == estado).toList();
    }

    public Optional<Planilla> buscarPlanilla(Integer id) { return planillaDAO.obtenerPorId(id); }

    public boolean emitirPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.EMITIDA); }

    public boolean anularPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.ANULADA); }
}
