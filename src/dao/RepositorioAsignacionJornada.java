package dao;

import models.AsignacionJornada;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RepositorioAsignacionJornada {
    private static final String SQL_INSERT = "INSERT INTO AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, FechaFin, Activa) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT a.*, e.Nombres, e.Apellidos, j.Nombre as JornadaNombre FROM AsignacionJornada a JOIN Empleado e ON a.IdEmpleado = e.IdEmpleado JOIN Jornada j ON a.IdJornada = j.IdJornada";
    private static final String SQL_SELECT_BY_ID = "SELECT a.*, e.Nombres, e.Apellidos, j.Nombre as JornadaNombre FROM AsignacionJornada a JOIN Empleado e ON a.IdEmpleado = e.IdEmpleado JOIN Jornada j ON a.IdJornada = j.IdJornada WHERE a.IdAsignacion = ?";
    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM AsignacionJornada WHERE IdEmpleado = ? AND Activa = 1";
    private static final String SQL_UPDATE = "UPDATE AsignacionJornada SET IdEmpleado = ?, IdJornada = ?, FechaAsignacion = ?, FechaFin = ?, Activa = ? WHERE IdAsignacion = ?";
    private static final String SQL_DELETE = "UPDATE AsignacionJornada SET Activa = 0 WHERE IdAsignacion = ?";
    private static final String SQL_EXISTS_SOLAPADA = "SELECT COUNT(*) FROM AsignacionJornada WHERE IdEmpleado = ? AND Activa = 1 AND IdAsignacion != ? AND FechaAsignacion <= COALESCE(?, '9999-12-31') AND COALESCE(FechaFin, '9999-12-31') >= ?";

    public void create(AsignacionJornada a) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setInt(2, a.getIdJornada());
            ps.setDate(3, java.sql.Date.valueOf(a.getFechaAsignacion()));
            if (a.getFechaFin() != null) ps.setDate(4, java.sql.Date.valueOf(a.getFechaFin()));
            else ps.setNull(4, java.sql.Types.DATE);
            ps.setBoolean(5, a.isActiva());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear asignacion: " + e.getMessage(), e);
        }
    }

    public void update(AsignacionJornada a) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setInt(2, a.getIdJornada());
            ps.setDate(3, java.sql.Date.valueOf(a.getFechaAsignacion()));
            if (a.getFechaFin() != null) ps.setDate(4, java.sql.Date.valueOf(a.getFechaFin()));
            else ps.setNull(4, java.sql.Types.DATE);
            ps.setBoolean(5, a.isActiva());
            ps.setInt(6, a.getIdAsignacion());
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

    public List<Object[]> findAllWithNames() {
        List<Object[]> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("IdAsignacion");
                fila[1] = rs.getString("Nombres") + " " + rs.getString("Apellidos");
                fila[2] = rs.getString("JornadaNombre");
                fila[3] = rs.getDate("FechaAsignacion");
                java.sql.Date fechaFin = rs.getDate("FechaFin");
                fila[4] = fechaFin != null ? fechaFin : "";
                fila[5] = rs.getBoolean("Activa");
                lista.add(fila);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar asignaciones con nombres: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<AsignacionJornada> findById(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asignacion por ID: " + e.getMessage(), e);
        }
    }

    public Optional<AsignacionJornada> findByEmpleado(int idEmpleado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapSimple(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asignacion por empleado: " + e.getMessage(), e);
        }
    }

    public boolean existeSolapada(int idEmpleado, LocalDate fechaInicio, LocalDate fechaFin, int excludeId) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_SOLAPADA)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, excludeId);
            if (fechaFin != null) ps.setDate(3, java.sql.Date.valueOf(fechaFin));
            else ps.setNull(3, java.sql.Types.DATE);
            ps.setDate(4, java.sql.Date.valueOf(fechaInicio));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar solapamiento: " + e.getMessage(), e);
        }
    }

    private AsignacionJornada map(ResultSet rs) throws SQLException {
        LocalDate fechaFin = Optional.ofNullable(rs.getDate("FechaFin"))
            .map(java.sql.Date::toLocalDate).orElse(null);
        return new AsignacionJornada(
            rs.getInt("IdAsignacion"), rs.getInt("IdEmpleado"), rs.getInt("IdJornada"),
            rs.getDate("FechaAsignacion").toLocalDate(), fechaFin, rs.getBoolean("Activa"));
    }

    private AsignacionJornada mapSimple(ResultSet rs) throws SQLException {
        LocalDate fechaFin = Optional.ofNullable(rs.getDate("FechaFin"))
            .map(java.sql.Date::toLocalDate).orElse(null);
        return new AsignacionJornada(
            rs.getInt("IdAsignacion"), rs.getInt("IdEmpleado"), rs.getInt("IdJornada"),
            rs.getDate("FechaAsignacion").toLocalDate(), fechaFin, rs.getBoolean("Activa"));
    }
}
