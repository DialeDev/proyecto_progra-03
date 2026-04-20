package dao;

import models.Planilla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DialeMRZ
 */
public class RepositorioPlanilla {

    private static final String SQL_INSERT = 
        "INSERT INTO Planilla (idPlanilla, mes, anio, fechaEmision, estado) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = 
        "SELECT * FROM Planilla ORDER BY anio DESC, mes DESC";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Planilla WHERE idPlanilla = ?";
    private static final String SQL_UPDATE_ESTADO = 
        "UPDATE Planilla SET estado = ? WHERE idPlanilla = ?";

    public void crear(Planilla planilla) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planilla.getIdPlanilla());
            ps.setInt(2, planilla.getMes());
            ps.setInt(3, planilla.getAnio());
            ps.setDate(4, java.sql.Date.valueOf(planilla.getFechaEmision()));
            ps.setString(5, planilla.getEstado().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear planilla: " + e.getMessage(), e);
        }
    }

    public List<Planilla> obtenerTodas() {
        String sql = SQL_SELECT_ALL;
        List<Planilla> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar planillas: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Planilla> obtenerPorId(Integer id) {
        String sql = SQL_SELECT_BY_ID;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar planilla: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean actualizarEstado(Integer id, Planilla.EstadoPlanilla nuevoEstado) {
        String sql = SQL_UPDATE_ESTADO;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage(), e);
        }
    }

    private Planilla mapResultSet(ResultSet rs) throws SQLException {
        return new Planilla(
            rs.getInt("idPlanilla"),
            rs.getInt("mes"),
            rs.getInt("anio"),
            rs.getDate("fechaEmision").toLocalDate(),
            Planilla.EstadoPlanilla.valueOf(rs.getString("estado"))
        );
    }
}