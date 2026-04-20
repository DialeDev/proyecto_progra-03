package main;

import dao.Conexion;
import javax.swing.JOptionPane;
import java.sql.Connection;

/**
 *
 * @author DialeMRZ
 */
public class PruebaConexion {

    public static void main(String[] args) {
        Connection conn = null;
        
        try {
            conn = Conexion.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                JOptionPane.showMessageDialog(null, 
                    "Conexión exitosa a SQL Server!\n\n"
                    + "Base de datos: AsistenciaPlanillasDB\n"
                    + "Servidor: localhost:1433\n"
                    + "Estado: Conectado",
                    "Prueba de Conexión",
                    JOptionPane.INFORMATION_MESSAGE);
                
                System.out.println(">>> CONEXIÓN EXITOSA <<<");
                System.out.println("Base de datos: AsistenciaPlanillasDB");
                System.out.println("Servidor: localhost:1433");
            }
            
        } catch (Exception e) {
            // Mensaje amigable para el usuario
            JOptionPane.showMessageDialog(null,
                "Error de conexión\n\n"
                + "Verifica que:\n"
                + "1. SQL Server esté ejecutándose\n"
                + "2. La base de datos 'AsistenciaPlanillasDB' exista\n"
                + "3. Tengas permisos de acceso\n\n"
                + "Consulta la consola para ver el error técnico.",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
            
            // Error detallado en consola para depuración
            System.out.println(">>> ERROR DE CONEXIÓN <<<");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Causa: " + e.getCause());
            System.out.println("Stack trace:");
            e.printStackTrace();
            
        } finally {
            Conexion.close(conn);
        }
    }
}