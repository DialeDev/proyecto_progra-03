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

    public void crear(Planilla planilla) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, planilla.getMes());
            ps.setInt(2, planilla.getAnio());
            ps.setDate(3, java.sql.Date.valueOf(planilla.getFechaEmision()));
            ps.setString(4, planilla.getEstado().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear planilla: " + e.getMessage(), e);
        }
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

    public boolean actualizarEstado(Integer id, Planilla.EstadoPlanilla nuevoEstado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_ESTADO)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage(), e);
        }
    }

    private Planilla mapResultSet(ResultSet rs) throws SQLException {
        return new Planilla(rs.getInt("idPlanilla"), rs.getInt("mes"), rs.getInt("anio"),
            rs.getDate("fechaEmision").toLocalDate(), Planilla.EstadoPlanilla.valueOf(rs.getString("estado")));
    }
}
