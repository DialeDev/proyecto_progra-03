package dao;

import java.sql.*;

// @author DialeMRZ
public class Conexion {
    // Linux / Docker: confiar en certificado auto-firmado
    // private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AsistenciaPlanillasDB;user=sa;password=Password_2022!;encrypt=true;trustServerCertificate=true;";
    // Windows (SQL Server local / autenticacion mixta): descomentar y comentar la de arriba
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AsistenciaPlanillasDB;user=sa;password=Password_2022!;encrypt=false;";

    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL); }
}
