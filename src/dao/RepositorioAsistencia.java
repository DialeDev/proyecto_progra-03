package dao;

import models.RegistroAsistencia;
import java.sql.*;
import java.util.*;

// @author DialeMRZ
public class RepositorioAsistencia {
    private static final String SQL_INSERT = "INSERT INTO RegistroAsistencia (idEmpleado, marcaEntrada, marcaSalida, faltaJustificada) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_ENTRADA_BY_EMPLEADO = "SELECT * FROM RegistroAsistencia WHERE idEmpleado = ? AND marcaSalida IS NULL ORDER BY marcaEntrada DESC";
    private static final String SQL_UPDATE_SALIDA = "UPDATE RegistroAsistencia SET marcaSalida = ? WHERE idRegistro = ?";
    private static final String SQL_SELECT_BY_MES = "SELECT * FROM RegistroAsistencia WHERE idEmpleado = ? AND MONTH(marcaEntrada) = ? AND YEAR(marcaEntrada) = ? ORDER BY marcaEntrada";

    public void registrar(RegistroAsistencia asistencia) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(asistencia.getMarcaEntrada()));
            if (asistencia.getMarcaSalida() != null) {
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(asistencia.getMarcaSalida()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            ps.setBoolean(4, asistencia.isFaltaJustificada());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar asistencia: " + e.getMessage(), e);
        }
    }

    public Optional<RegistroAsistencia> findEntradaActivaByEmpleado(int idEmpleado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ENTRADA_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar entrada activa: " + e.getMessage(), e);
        }
    }

    public void actualizarSalida(RegistroAsistencia asistencia) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_SALIDA)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(asistencia.getMarcaSalida()));
            ps.setLong(2, asistencia.getIdRegistro());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar salida: " + e.getMessage(), e);
        }
    }

    public List<RegistroAsistencia> findByEmpleadoAndMes(int idEmpleado, int mes, int anio) {
        List<RegistroAsistencia> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_MES)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asistencia por mes: " + e.getMessage(), e);
        }
        return lista;
    }

    private RegistroAsistencia mapResultSet(ResultSet rs) throws SQLException {
        RegistroAsistencia a = new RegistroAsistencia(rs.getLong("idRegistro"), rs.getInt("idEmpleado"),
            rs.getTimestamp("marcaEntrada").toLocalDateTime());
        java.sql.Timestamp salida = rs.getTimestamp("marcaSalida");
        if (salida != null) a.setMarcaSalida(salida.toLocalDateTime());
        a.setFaltaJustificada(rs.getBoolean("faltaJustificada"));
        return a;
    }
}
