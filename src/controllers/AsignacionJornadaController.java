package controllers;

import dao.RepositorioAsignacionJornada;
import dao.RepositorioEmpleado;
import dao.RepositorioJornada;
import models.AsignacionJornada;
import models.Empleado;
import models.Jornada;
import utils.SesionGlobal;
import java.util.List;

public class AsignacionJornadaController {
    private final RepositorioAsignacionJornada asignacionDAO;
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioJornada jornadaDAO;

    public AsignacionJornadaController() {
        this.asignacionDAO = new RepositorioAsignacionJornada();
        this.empleadoDAO = new RepositorioEmpleado();
        this.jornadaDAO = new RepositorioJornada();
    }

    public List<Empleado> listarEmpleadosActivos() {
        return empleadoDAO.findAllActivos();
    }

    public List<Jornada> listarJornadasActivas() {
        return jornadaDAO.findAll();
    }

    public String guardar(AsignacionJornada a) {
        if (!SesionGlobal.puedeOperar("AsignacionJornada"))
            return "No tiene permisos para realizar esta operación.";
        String error = validar(a, 0);
        if (error != null) return error;
        asignacionDAO.create(a);
        return "Asignacion guardada correctamente.";
    }

    public String actualizar(AsignacionJornada a) {
        if (!SesionGlobal.puedeOperar("AsignacionJornada"))
            return "No tiene permisos para realizar esta operación.";
        String error = validar(a, a.getIdAsignacion());
        if (error != null) return error;
        asignacionDAO.update(a);
        return "Asignacion actualizada correctamente.";
    }

    public String eliminar(int id) {
        if (!SesionGlobal.puedeOperar("AsignacionJornada"))
            return "No tiene permisos para realizar esta operación.";
        asignacionDAO.delete(id);
        return "Asignacion eliminada correctamente.";
    }

    public List<Object[]> listarConNombres() {
        return asignacionDAO.findAllWithNames();
    }

    public AsignacionJornada buscarPorId(int id) {
        return asignacionDAO.findById(id).orElse(null);
    }

    private String validar(AsignacionJornada a, int excludeId) {
        if (a.getIdEmpleado() <= 0) return "Debe seleccionar un empleado.";
        if (a.getIdJornada() <= 0) return "Debe seleccionar una jornada.";
        if (a.getFechaAsignacion() == null) return "La fecha de asignacion es obligatoria.";
        if (a.getFechaFin() != null && !a.getFechaFin().isAfter(a.getFechaAsignacion()))
            return "La fecha fin debe ser posterior a la fecha de inicio.";
        if (asignacionDAO.existeSolapada(a.getIdEmpleado(), a.getFechaAsignacion(), a.getFechaFin(), excludeId))
            return "El empleado ya tiene una asignacion activa en ese periodo.";
        return null;
    }
}
