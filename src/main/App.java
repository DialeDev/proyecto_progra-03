package main;

import views.FrmLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}
