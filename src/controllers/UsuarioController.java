package controllers;

import dao.RepositorioEmpleado;
import dao.RepositorioUsuario;
import models.Empleado;
import models.Usuario;
import utils.SesionGlobal;
import views.FrmGestionUsuarios;
import java.util.List;

// @author DialeMRZ
public class UsuarioController {
    private final RepositorioUsuario repo;
    private final RepositorioEmpleado repoEmpleado;
    private final FrmGestionUsuarios vista;

    public UsuarioController(FrmGestionUsuarios vista) {
        this.vista = vista;
        this.repo = new RepositorioUsuario();
        this.repoEmpleado = new RepositorioEmpleado();
    }

    public List<Usuario> listarUsuarios() {
        return repo.findAll();
    }

    public void crearUsuario(String username, String password, String rol, int idEmpleado) {
        Usuario u = new Usuario(0, idEmpleado, username, password, rol);
        u.setActivo(true);
        repo.create(u);
    }

    public void editarUsuario(int id, String username, String rol, int idEmpleado, boolean activo) {
        Usuario u = repo.findById(id);
        if (u == null) {
            vista.mostrarMensaje("error", "Usuario no encontrado.");
            return;
        }
        u.setUsername(username);
        u.setRol(rol);
        u.setIdEmpleado(idEmpleado);
        u.setActivo(activo);
        repo.update(u);
    }

    public void desactivarUsuario(int id) {
        // Validar que no sea autodesactivación
        Usuario userActual = SesionGlobal.getUsuarioActivo();
        if (userActual != null && userActual.getIdUsuario() == id) {
            vista.mostrarMensaje("error", "No puedes desactivar tu propio usuario.");
            return;
        }
        repo.desactivarUsuario(id);
    }

    public void reactivarUsuario(int id) {
        repo.reactivarUsuario(id);
    }

    public List<Empleado> obtenerEmpleadosSinUsuario() {
        return repo.findEmpleadosSinUsuario();
    }
}
