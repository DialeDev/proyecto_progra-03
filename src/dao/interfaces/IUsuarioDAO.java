package dao.interfaces;

import models.Usuario;

/**
 *
 * @author DialeMRZ
 */

public interface IUsuarioDAO {
    Usuario autenticar(String username, String password);
    
    void create(Usuario usuario);
    Usuario findByEmpleado(int idEmpleado);
    void desactivarUsuario(int idUsuario);
}
