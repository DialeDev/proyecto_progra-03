package dao;

import models.Planilla;
import java.sql.*;
import java.util.*;

// @author DialeMRZ
public class RepositorioPlanilla {
    private static final String SQL_INSERT = "INSERT INTO Planilla (mes, anio, fechaEmision, estado) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Planilla ORDER BY anio DESC, mes DESC";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Planilla WHERE idPlanilla = ?";
    private static final String SQL_UPDATE_ESTADO = "UPDATE Planilla SET estado = ? WHERE idPlanilla = ?";
    private static final String SQL_EXISTS_MES_ANIO = "SELECT COUNT(*) FROM Planilla WHERE mes = ? AND anio = ?";

    public int crear(Planilla planilla) {
        try (Connection conn = Conexion.getConnection()) {
            return crear(conn, planilla);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear planilla: " + e.getMessage(), e);
        }
    }

    public int crear(Connection conn, Planilla planilla) {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, planilla.getMes());
            ps.setInt(2, planilla.getAnio());
            ps.setDate(3, java.sql.Date.valueOf(planilla.getFechaEmision()));
            String estadoStr = planilla.getEstado().name();
            ps.setString(4, estadoStr.substring(0, 1).toUpperCase() + estadoStr.substring(1).toLowerCase());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new RuntimeException("No se pudo obtener el ID generado para la planilla.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear planilla: " + e.getMessage(), e);
        }
    }

    public List<Planilla> findAll() {
        return obtenerTodas();
    }

    public List<Planilla> obtenerTodas() {
        List<Planilla> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar planillas: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Planilla> obtenerPorId(Integer id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar planilla: " + e.getMessage(), e);
        }
    }

    public boolean existePorMesYAnio(int mes, int anio) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_MES_ANIO)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar planilla duplicada: " + e.getMessage(), e);
        }
    }

    public boolean actualizarEstado(Integer id, Planilla.EstadoPlanilla nuevoEstado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_ESTADO)) {
            String estadoStr = nuevoEstado.name();
            ps.setString(1, estadoStr.substring(0, 1).toUpperCase() + estadoStr.substring(1).toLowerCase());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage(), e);
        }
    }

    private Planilla mapResultSet(ResultSet rs) throws SQLException {
        return new Planilla(rs.getInt("idPlanilla"), rs.getInt("mes"), rs.getInt("anio"),
            rs.getDate("fechaEmision").toLocalDate(), Planilla.EstadoPlanilla.valueOf(rs.getString("estado").toUpperCase()));
    }
}
