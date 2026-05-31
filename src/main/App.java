package main;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import views.FrmLogin;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Taskbar;

// @author DialeMRZ
public class App {
    public static void main(String[] args) {
      
        // Se reemplazó el bucle de Nimbus por la configuración directa de FlatLaf
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Error al cargar FlatLaf. Usando tema por defecto.");
        }

        // Icono global de la aplicación (barra de tareas / dock) - INTACTO
        var icono = new ImageIcon(App.class.getResource("/img/icono.png")).getImage();
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                try {
                    taskbar.setIconImage(icono);
                } catch (Exception ex) {
                    System.err.println("No se pudo establecer el icono en la barra de tareas.");
                }
            }
        }

        // Inicialización de la ventana - INTACTO
        SwingUtilities.invokeLater(() -> {
            var login = new FrmLogin();
            login.setIconImage(icono);
            login.setVisible(true);
        });
    }
}