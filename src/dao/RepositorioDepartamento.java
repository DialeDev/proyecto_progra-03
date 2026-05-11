package dao;

import models.Departamento;
import java.sql.*;
import java.util.*;

public class RepositorioDepartamento {
    private static final String SQL_SELECT_ALL = "SELECT * FROM Departamento WHERE Activo = 1";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Departamento WHERE IdDepartamento = ?";
    private static final String SQL_EXISTS_NOMBRE = "SELECT COUNT(*) FROM Departamento WHERE Nombre = ? AND Activo = 1";
    private static final String SQL_INSERT = "INSERT INTO Departamento (Nombre, Activo) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE Departamento SET Nombre = ?, Activo = ? WHERE IdDepartamento = ?";
    private static final String SQL_DELETE = "UPDATE Departamento SET Activo = 0 WHERE IdDepartamento = ?";

    public List<Departamento> findAll() {
        List<Departamento> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar departamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Departamento> findById(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar departamento: " + e.getMessage(), e);
        }
    }

    public void create(Departamento d) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, d.getNombre());
            ps.setBoolean(2, d.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear departamento: " + e.getMessage(), e);
        }
    }

    public void update(Departamento d) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, d.getNombre());
            ps.setBoolean(2, d.isActivo());
            ps.setInt(3, d.getIdDepartamento());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar departamento: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar departamento: " + e.getMessage(), e);
        }
    }

    public boolean existePorNombre(String nombre) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_NOMBRE)) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar departamento duplicado: " + e.getMessage(), e);
        }
    }

    private Departamento map(ResultSet rs) throws SQLException {
        return new Departamento(rs.getInt("IdDepartamento"), rs.getString("Nombre"), rs.getBoolean("Activo"));
    }
}
