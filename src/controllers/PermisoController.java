package controllers;

import dao.RepositorioPermiso;
import models.Permiso;
import utils.SesionGlobal;
import views.FrmGestionPermisos;
import java.util.List;
import java.util.Optional;

// @author DialeMRZ
public class PermisoController {
    private final RepositorioPermiso permisoDAO;
    private final FrmGestionPermisos vista;

    public PermisoController(FrmGestionPermisos vista) {
        this.vista = vista;
        this.permisoDAO = new RepositorioPermiso();
        vista.setController(this);
    }

    public String guardar(Permiso p) {
        if (!SesionGlobal.puedeOperar("Permisos"))
            return "No tiene permisos para realizar esta operación.";
        if (p.getRol() == null || p.getRol().trim().isEmpty())
            return "El rol es obligatorio.";
        if (p.getModulo() == null || p.getModulo().trim().isEmpty())
            return "El módulo es obligatorio.";
        try {
            permisoDAO.create(p);
            return "Permiso guardado correctamente.";
        } catch (Exception e) {
            return "Error al guardar permiso: " + e.getMessage();
        }
    }

    public List<Permiso> listar() {
        return permisoDAO.findAll();
    }

    public Optional<Permiso> buscarPorId(int id) {
        return permisoDAO.findById(id);
    }

    public String actualizar(Permiso p) {
        if (!SesionGlobal.puedeOperar("Permisos"))
            return "No tiene permisos para realizar esta operación.";
        if (p.getRol() == null || p.getRol().trim().isEmpty())
            return "El rol es obligatorio.";
        if (p.getModulo() == null || p.getModulo().trim().isEmpty())
            return "El módulo es obligatorio.";
        try {
            permisoDAO.update(p);
            return "Permiso actualizado correctamente.";
        } catch (Exception e) {
            return "Error al actualizar permiso: " + e.getMessage();
        }
    }

    public String eliminar(int id) {
        if (!SesionGlobal.puedeOperar("Permisos"))
            return "No tiene permisos para realizar esta operación.";
        try {
            permisoDAO.delete(id);
            return "Permiso eliminado correctamente.";
        } catch (Exception e) {
            return "Error al eliminar permiso: " + e.getMessage();
        }
    }
}
