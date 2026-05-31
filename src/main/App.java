package main;

import com.formdev.flatlaf.FlatLightLaf;
import views.FrmLogin;
import javax.swing.*;
import java.awt.*;

// @author DialeMRZ
public class App {
    public static void main(String[] args) {
        FlatLightLaf.setup();

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
