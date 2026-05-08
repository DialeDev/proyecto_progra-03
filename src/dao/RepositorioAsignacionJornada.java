package dao;

import models.AsignacionJornada;
import java.sql.*;
import java.util.*;

public class RepositorioAsignacionJornada {
    private static final String SQL_INSERT = "INSERT INTO AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM AsignacionJornada WHERE IdEmpleado = ? AND Activa = 1";
    private static final String SQL_SELECT_ALL = "SELECT * FROM AsignacionJornada";
    private static final String SQL_UPDATE = "UPDATE AsignacionJornada SET IdJornada = ?, Activa = ? WHERE IdAsignacion = ?";
    private static final String SQL_DELETE = "UPDATE AsignacionJornada SET Activa = 0 WHERE IdAsignacion = ?";

    public void create(AsignacionJornada a) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setInt(2, a.getIdJornada());
            ps.setDate(3, java.sql.Date.valueOf(a.getFechaAsignacion()));
            ps.setBoolean(4, a.isActiva());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear asignacion: " + e.getMessage(), e);
        }
    }

    public Optional<AsignacionJornada> findByEmpleado(int idEmpleado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asignacion: " + e.getMessage(), e);
        }
    }

    public List<AsignacionJornada> findAll() {
        List<AsignacionJornada> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar asignaciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(AsignacionJornada a) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, a.getIdJornada());
            ps.setBoolean(2, a.isActiva());
            ps.setInt(3, a.getIdAsignacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar asignacion: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar asignacion: " + e.getMessage(), e);
        }
    }

    private AsignacionJornada map(ResultSet rs) throws SQLException {
        return new AsignacionJornada(
            rs.getInt("IdAsignacion"), rs.getInt("IdEmpleado"), rs.getInt("IdJornada"),
            rs.getDate("FechaAsignacion").toLocalDate(), rs.getBoolean("Activa"));
    }
}
