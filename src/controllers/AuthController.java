package controllers;

import dao.interfaces.IUsuarioDAO;
import dao.memory.UsuarioDAOMemory;
import models.Usuario;
import utils.SesionGlobal;
import views.FrmLogin;
import views.FrmDashboard;
import javax.swing.JOptionPane;

/**
 *
 * @author DialeMRZ
 */

public class AuthController {
    
    private IUsuarioDAO usuarioDAO;
    private FrmLogin vista;

    public AuthController(FrmLogin vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAOMemory(); //Creamos una instancia del DAO en memoria
    }

// Este es el método que la vista llamará cuando el usuario presione "Ingresar"
    public void procesarLogin() {
        String username = vista.getTxtUsuario().getText();
        String password = new String(vista.getTxtPassword().getPassword());

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese usuario y contraseña.", 
                                          "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioValidado = usuarioDAO.autenticar(username, password);

        if (usuarioValidado != null) {
            SesionGlobal.iniciarSesion(usuarioValidado);
            
            FrmDashboard dashboard = new FrmDashboard();
            dashboard.setVisible(true);
            dashboard.setLocationRelativeTo(null); 
            
            vista.dispose(); 
            
        } else { //login fallido
            JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o usuario inactivo.", 
                                          "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            vista.getTxtPassword().setText(""); 
        }
    }
}
