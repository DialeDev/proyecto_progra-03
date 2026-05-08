package controllers;

import dao.RepositorioUsuario;
import models.Usuario;
import utils.SesionGlobal;
import views.FrmLogin;
import views.FrmDashboard;
import javax.swing.JOptionPane;
import java.util.Optional;

// @author DialeMRZ
public class AuthController {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AuthController.class.getName());
    private RepositorioUsuario usuarioDAO;
    private FrmLogin vista;

    public AuthController(FrmLogin vista) {
        this.vista = vista;
        this.usuarioDAO = new RepositorioUsuario();
    }

    public void procesarLogin() {
        try {
            String username = vista.getTxtUsuario().getText();
            String password = new String(vista.getTxtPassword().getPassword());
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, ingrese usuario y contraseña.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Optional<Usuario> usuarioValidado = usuarioDAO.autenticar(username, password);
            if (usuarioValidado.isPresent()) {
                SesionGlobal.iniciarSesion(usuarioValidado.get());
                FrmDashboard dashboard = new FrmDashboard();
                dashboard.setVisible(true);
                dashboard.setLocationRelativeTo(null);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o usuario inactivo.",
                    "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                vista.getTxtPassword().setText("");
            }
        } catch (Exception e) {
            // Muestra errores inesperados (driver BD, imagen, etc.) en pantalla
            JOptionPane.showMessageDialog(vista,
                "Error inesperado al iniciar sesión: " + e.getMessage(),
                "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error en procesarLogin: " + e.getMessage());
        }
    }
}
