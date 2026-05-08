package dao;

import models.BoletaPago;
import models.BoletaDetalle;
import java.sql.*;
import java.util.*;

public class RepositorioBoletaPago {
    private static final String SQL_INSERT = "INSERT INTO BoletaPago (IdPlanilla, IdEmpleado, SueldoBaseAplicado, HorasOrdinariasCalculadas, HorasExtraDiurnas, HorasExtraNocturnas, TotalIngresos, TotalDeducciones, LiquidoAPagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM BoletaPago WHERE IdBoleta = ?";
    private static final String SQL_SELECT_BY_PLANILLA = "SELECT * FROM BoletaPago WHERE IdPlanilla = ?";
    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM BoletaPago WHERE IdEmpleado = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM BoletaPago";

    public Long create(BoletaPago bp) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bp.idPlanilla());
            ps.setInt(2, bp.idEmpleado());
            ps.setBigDecimal(3, bp.sueldoBaseAplicado());
            ps.setBigDecimal(4, bp.horasOrdinariasCalculadas());
            ps.setBigDecimal(5, bp.horasExtraDiurnas());
            ps.setBigDecimal(6, bp.horasExtraNocturnas());
            ps.setBigDecimal(7, bp.totalIngresos());
            ps.setBigDecimal(8, bp.totalDeducciones());
            ps.setBigDecimal(9, bp.liquidoAPagar());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear boleta: " + e.getMessage(), e);
        }
    }

    public Optional<BoletaPago> findById(Long id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar boleta: " + e.getMessage(), e);
        }
    }

    public List<BoletaPago> findByPlanilla(int idPlanilla) {
        List<BoletaPago> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PLANILLA)) {
            ps.setInt(1, idPlanilla);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar boletas por planilla: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<BoletaPago> findByEmpleado(int idEmpleado) {
        List<BoletaPago> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar boletas por empleado: " + e.getMessage(), e);
        }
        return lista;
    }

    private BoletaPago map(ResultSet rs) throws SQLException {
        return new BoletaPago(
            rs.getLong("IdBoleta"), rs.getInt("IdPlanilla"), rs.getInt("IdEmpleado"),
            rs.getBigDecimal("SueldoBaseAplicado"), rs.getBigDecimal("HorasOrdinariasCalculadas"),
            rs.getBigDecimal("HorasExtraDiurnas"), rs.getBigDecimal("HorasExtraNocturnas"),
            rs.getBigDecimal("TotalIngresos"), rs.getBigDecimal("TotalDeducciones"),
            rs.getBigDecimal("LiquidoAPagar"), List.of());
    }
}
