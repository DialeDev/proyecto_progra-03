package main;

import views.FrmLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author DialeMRZ
 */

public class App {

    public static void main(String[] args) {
        
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Error al cargar el tema visual. Se usará el tema por defecto.");
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrmLogin login = new FrmLogin();
                login.setVisible(true);
            }
        });
    }
}
