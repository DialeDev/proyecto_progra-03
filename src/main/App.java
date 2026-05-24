package main;

import views.FrmLogin;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Taskbar;

// @author DialeMRZ
public class App {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar Nimbus. Usando tema por defecto.");
        }

        // Icono global de la aplicación (barra de tareas / dock)
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

        SwingUtilities.invokeLater(() -> {
            var login = new FrmLogin();
            login.setIconImage(icono);
            login.setVisible(true);
        });
    }
}
