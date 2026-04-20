package dao;

import models.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DialeMRZ
 */
public class RepositorioEmpleado {

    private static final String SQL_INSERT = 
        "INSERT INTO Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Empleado WHERE IdEmpleado = ?";
    private static final String SQL_SELECT_BY_DUI = 
        "SELECT * FROM Empleado WHERE DUI = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT * FROM Empleado";
    private static final String SQL_UPDATE = 
        "UPDATE Empleado SET IdDepartamento = ?, Nombres = ?, Apellidos = ?, DUI = ?, Activo = ? WHERE IdEmpleado = ?";
    private static final String SQL_DELETE = 
        "UPDATE Empleado SET Activo = 0 WHERE IdEmpleado = ?";

    public void create(Empleado empleado) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleado.getIdDepartamento());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getDui());
            ps.setBoolean(5, empleado.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear empleado: " + e.getMessage(), e);
        }
    }

    public Empleado findById(int idEmpleado) {
        String sql = SQL_SELECT_BY_ID;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean existeDui(String dui) {
        String sql = SQL_SELECT_BY_DUI;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dui);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar DUI: " + e.getMessage(), e);
        }
    }

    public List<Empleado> findAll() {
        String sql = SQL_SELECT_ALL;
        List<Empleado> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Empleado empleado) {
        String sql = SQL_UPDATE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleado.getIdDepartamento());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getDui());
            ps.setBoolean(5, empleado.isActivo());
            ps.setInt(6, empleado.getIdEmpleado());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al atualizar empleado: " + e.getMessage(), e);
        }
    }

    public void delete(int idEmpleado) {
        String sql = SQL_DELETE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado: " + e.getMessage(), e);
        }
    }

    private Empleado mapResultSet(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getInt("IdEmpleado"),
            rs.getInt("IdDepartamento"),
            rs.getString("Nombres"),
            rs.getString("Apellidos"),
            rs.getString("DUI"),
            rs.getBoolean("Activo")
        );
    }
}