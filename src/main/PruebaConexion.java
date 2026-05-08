package main;

import dao.Conexion;
import javax.swing.JOptionPane;
import java.sql.Connection;

// @author DialeMRZ
public class PruebaConexion {
    public static void main(String[] args) {
        try (Connection conn = Conexion.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                JOptionPane.showMessageDialog(null, "Conexión exitosa a SQL Server!\nBD: AsistenciaPlanillasDB", "Prueba de Conexión", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(">>> CONEXIÓN EXITOSA <<<");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión.\nVerifique que SQL Server esté ejecutándose.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(">>> ERROR: " + e.getMessage() + " <<<");
        }
    }
}
