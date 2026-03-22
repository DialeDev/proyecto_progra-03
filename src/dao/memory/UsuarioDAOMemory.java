package dao.memory;

import dao.interfaces.IUsuarioDAO;
import models.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class UsuarioDAOMemory implements IUsuarioDAO {

    private static int autoIncrement = 1;

    static {
        DatabaseMock.tablaUsuarios.add(
            new Usuario(autoIncrement++, 1, "admin", "12345", "ADMINISTRADOR")
        );
    }

    @Override
    public Usuario autenticar(String username, String password) {
        for (Usuario u : DatabaseMock.tablaUsuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                if(u.isActivo()) {
                    return u;
                }
            }
        }
        return null; 
    }

    @Override
    public void create(Usuario usuario) {
        usuario.setIdUsuario(autoIncrement++);
        DatabaseMock.tablaUsuarios.add(usuario);
    }

    @Override
    public Usuario findByEmpleado(int idEmpleado) {
        for (Usuario u : DatabaseMock.tablaUsuarios) {
            if (u.getIdEmpleado() == idEmpleado) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void desactivarUsuario(int idUsuario) {
        for (Usuario u : DatabaseMock.tablaUsuarios) {
            if (u.getIdUsuario() == idUsuario) {
                u.setActivo(false);
                break;
            }
        }
    }
}
