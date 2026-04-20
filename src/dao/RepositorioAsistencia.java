package dao;

import models.RegistroAsistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DialeMRZ
 */
public class RepositorioAsistencia {

    private static final String SQL_INSERT = 
        "INSERT INTO RegistroAsistencia (idRegistro, idEmpleado, marcaEntrada, marcaSalida, faltaJustificada) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ENTRADA_BY_EMPLEADO = 
        "SELECT * FROM RegistroAsistencia WHERE idEmpleado = ? AND marcaSalida IS NULL ORDER BY marcaEntrada DESC";
    private static final String SQL_UPDATE_SALIDA = 
        "UPDATE RegistroAsistencia SET marcaSalida = ? WHERE idRegistro = ?";
    private static final String SQL_SELECT_BY_MES = 
        "SELECT * FROM RegistroAsistencia WHERE idEmpleado = ? AND MONTH(marcaEntrada) = ? AND YEAR(marcaEntrada) = ? ORDER BY marcaEntrada";

    public void registrar(RegistroAsistencia asistencia) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, asistencia.getIdRegistro());
            ps.setInt(2, asistencia.getIdEmpleado());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(asistencia.getMarcaEntrada()));
            if (asistencia.getMarcaSalida() != null) {
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(asistencia.getMarcaSalida()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }
            ps.setBoolean(5, asistencia.isFaltaJustificada());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar asistencia: " + e.getMessage(), e);
        }
    }

    public RegistroAsistencia findEntradaActivaByEmpleado(int idEmpleado) {
        String sql = SQL_SELECT_ENTRADA_BY_EMPLEADO;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar entrada activa: " + e.getMessage(), e);
        }
        return null;
    }

    public void actualizarSalida(RegistroAsistencia asistencia) {
        String sql = SQL_UPDATE_SALIDA;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(asistencia.getMarcaSalida()));
            ps.setLong(2, asistencia.getIdRegistro());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar salida: " + e.getMessage(), e);
        }
    }

    public List<RegistroAsistencia> findByEmpleadoAndMes(int idEmpleado, int mes, int anio) {
        String sql = SQL_SELECT_BY_MES;
        List<RegistroAsistencia> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asistencia por mes: " + e.getMessage(), e);
        }
        return lista;
    }

    private RegistroAsistencia mapResultSet(ResultSet rs) throws SQLException {
        RegistroAsistencia asistencia = new RegistroAsistencia(
            rs.getLong("idRegistro"),
            rs.getInt("idEmpleado"),
            rs.getTimestamp("marcaEntrada").toLocalDateTime()
        );
        java.sql.Timestamp salida = rs.getTimestamp("marcaSalida");
        if (salida != null) {
            asistencia.setMarcaSalida(salida.toLocalDateTime());
        }
        asistencia.setFaltaJustificada(rs.getBoolean("faltaJustificada"));
        return asistencia;
    }
}