package dao;

import models.Permiso;
import java.sql.*;
import java.util.*;

// @author DialeMRZ
public class RepositorioPermiso {
    private static final String SQL_SELECT_ALL = "SELECT * FROM Permiso ORDER BY Rol, Modulo";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Permiso WHERE IdPermiso = ?";
    private static final String SQL_FIND_BY_ROL_DEPTO =
        "SELECT * FROM Permiso WHERE Rol = ? AND (IdDepartamento = ? OR IdDepartamento IS NULL)";
    private static final String SQL_INSERT =
        "INSERT INTO Permiso (Rol, IdDepartamento, Modulo, PuedeVer, PuedeOperar) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
        "UPDATE Permiso SET Rol = ?, IdDepartamento = ?, Modulo = ?, PuedeVer = ?, PuedeOperar = ? WHERE IdPermiso = ?";
    private static final String SQL_DELETE = "DELETE FROM Permiso WHERE IdPermiso = ?";

    public List<Permiso> findAll() {
        List<Permiso> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar permisos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Permiso> findById(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar permiso: " + e.getMessage(), e);
        }
    }

    /**
     * Find permissions by role and optional department.
     * IdDepartamento = NULL matches all departments (global permission).
     */
    public List<Permiso> findByRolYDepto(String rol, Integer idDepartamento) {
        List<Permiso> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROL_DEPTO)) {
            ps.setString(1, rol);
            if (idDepartamento != null) {
                ps.setInt(2, idDepartamento);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar permisos por rol y departamento: " + e.getMessage(), e);
        }
        return lista;
    }

    public void create(Permiso p) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, p.getRol());
            if (p.getIdDepartamento() != null) {
                ps.setInt(2, p.getIdDepartamento());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, p.getModulo());
            ps.setBoolean(4, p.isPuedeVer());
            ps.setBoolean(5, p.isPuedeOperar());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear permiso: " + e.getMessage(), e);
        }
    }

    public void update(Permiso p) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, p.getRol());
            if (p.getIdDepartamento() != null) {
                ps.setInt(2, p.getIdDepartamento());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, p.getModulo());
            ps.setBoolean(4, p.isPuedeVer());
            ps.setBoolean(5, p.isPuedeOperar());
            ps.setInt(6, p.getIdPermiso());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar permiso: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar permiso: " + e.getMessage(), e);
        }
    }

    private Permiso map(ResultSet rs) throws SQLException {
        int idDepto = rs.getInt("IdDepartamento");
        Integer idDepartamento = rs.wasNull() ? null : idDepto;
        return new Permiso(
            rs.getInt("IdPermiso"),
            rs.getString("Rol"),
            idDepartamento,
            rs.getString("Modulo"),
            rs.getBoolean("PuedeVer"),
            rs.getBoolean("PuedeOperar")
        );
    }
}
