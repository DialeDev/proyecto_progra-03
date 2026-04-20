package controllers;

import dao.RepositorioUsuario;
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
    
    private RepositorioUsuario usuarioDAO;
    private FrmLogin vista;

    public AuthController(FrmLogin vista) {
        this.vista = vista;
        this.usuarioDAO = new RepositorioUsuario();
    }

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
            
        } else {
            JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o usuario inactivo.", 
                                          "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            vista.getTxtPassword().setText(""); 
        }
    }
}
