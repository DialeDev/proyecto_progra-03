package dao;

import models.Contratacion;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

// @author DialeMRZ
public class RepositorioContratacion {
    private static final String SQL_INSERT = "INSERT INTO Contratacion (IdEmpleado, IdContrato, FechaInicio, FechaFin, SalarioBaseAcordado, ValorHoraAcordado, Vigente) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ACTIVO_BY_EMPLEADO = "SELECT * FROM Contratacion WHERE IdEmpleado = ? AND Vigente = 1";
    private static final String SQL_SELECT_HISTORIAL_BY_EMPLEADO = "SELECT * FROM Contratacion WHERE IdEmpleado = ? ORDER BY FechaInicio DESC";
    private static final String SQL_UPDATE = "UPDATE Contratacion SET IdContrato = ?, FechaInicio = ?, FechaFin = ?, SalarioBaseAcordado = ?, ValorHoraAcordado = ?, Vigente = ? WHERE IdContratacion = ?";

    public void create(Contratacion contratacion) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, contratacion.getIdEmpleado());
            ps.setInt(2, contratacion.getIdContrato());
            ps.setDate(3, java.sql.Date.valueOf(contratacion.getFechaInicio()));
            if (contratacion.getFechaFin() != null) ps.setDate(4, java.sql.Date.valueOf(contratacion.getFechaFin()));
            else ps.setNull(4, java.sql.Types.DATE);
            ps.setDouble(5, contratacion.getSalarioBaseAcordado());
            ps.setDouble(6, contratacion.getValorHoraAcordado());
            ps.setBoolean(7, contratacion.isVigente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear contratacion: " + e.getMessage(), e);
        }
    }

    public Optional<Contratacion> findContratoActivoByEmpleado(int idEmpleado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ACTIVO_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratacion activa: " + e.getMessage(), e);
        }
    }

    public List<Contratacion> findHistorialByEmpleado(int idEmpleado) {
        List<Contratacion> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_HISTORIAL_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar historial: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Contratacion contratacion) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, contratacion.getIdContrato());
            ps.setDate(2, java.sql.Date.valueOf(contratacion.getFechaInicio()));
            if (contratacion.getFechaFin() != null) ps.setDate(3, java.sql.Date.valueOf(contratacion.getFechaFin()));
            else ps.setNull(3, java.sql.Types.DATE);
            ps.setDouble(4, contratacion.getSalarioBaseAcordado());
            ps.setDouble(5, contratacion.getValorHoraAcordado());
            ps.setBoolean(6, contratacion.isVigente());
            ps.setInt(7, contratacion.getIdContratacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contratacion: " + e.getMessage(), e);
        }
    }

    private Contratacion mapResultSet(ResultSet rs) throws SQLException {
        Contratacion c = new Contratacion(rs.getInt("IdContratacion"), rs.getInt("IdEmpleado"),
            rs.getInt("IdContrato"), rs.getDate("FechaInicio").toLocalDate(),
            rs.getDouble("SalarioBaseAcordado"), rs.getDouble("ValorHoraAcordado"));
        java.sql.Date fechaFin = rs.getDate("FechaFin");
        if (fechaFin != null) c.setFechaFin(fechaFin.toLocalDate());
        c.setVigente(rs.getBoolean("Vigente"));
        return c;
    }
}
