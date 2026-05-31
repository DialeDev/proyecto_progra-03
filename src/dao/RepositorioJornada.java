package dao;

import models.Jornada;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;

// @author DialeMRZ
public class RepositorioJornada {
    private static final String SQL_INSERT = "INSERT INTO Jornada (Nombre, HoraEntradaEsperada, HoraSalidaEsperada, MargenToleranciaMinutos, Activa) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Jornada WHERE IdJornada = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Jornada WHERE Activa = 1";
    private static final String SQL_UPDATE = "UPDATE Jornada SET Nombre = ?, HoraEntradaEsperada = ?, HoraSalidaEsperada = ?, MargenToleranciaMinutos = ? WHERE IdJornada = ?";
    private static final String SQL_DELETE = "UPDATE Jornada SET Activa = 0 WHERE IdJornada = ?";
    private static final String SQL_EXISTE_TRASLAPE = "SELECT COUNT(*) FROM Jornada WHERE HoraEntradaEsperada < CAST(? AS TIME) AND HoraSalidaEsperada > CAST(? AS TIME) AND Activa = 1";
    private static final String SQL_EXISTE_TRASLAPE_EXCLUYENDO = "SELECT COUNT(*) FROM Jornada WHERE HoraEntradaEsperada < CAST(? AS TIME) AND HoraSalidaEsperada > CAST(? AS TIME) AND Activa = 1 AND IdJornada != ?";

    public boolean existeTraslape(LocalTime entrada, LocalTime salida) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTE_TRASLAPE)) {
            ps.setTime(1, java.sql.Time.valueOf(salida));
            ps.setTime(2, java.sql.Time.valueOf(entrada));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar traslape de jornada: " + e.getMessage(), e);
        }
    }

    public boolean existeTraslape(LocalTime entrada, LocalTime salida, int excludeId) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTE_TRASLAPE_EXCLUYENDO)) {
            ps.setTime(1, java.sql.Time.valueOf(salida));
            ps.setTime(2, java.sql.Time.valueOf(entrada));
            ps.setInt(3, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar traslape de jornada: " + e.getMessage(), e);
        }
    }

    public void create(Jornada jornada) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, jornada.getNombre());
            ps.setTime(2, java.sql.Time.valueOf(jornada.getHoraEntradaEsperada()));
            ps.setTime(3, java.sql.Time.valueOf(jornada.getHoraSalidaEsperada()));
            ps.setInt(4, jornada.getMargenToleranciaMinutos());
            ps.setBoolean(5, jornada.isActiva());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear jornada: " + e.getMessage(), e);
        }
    }

    public Optional<Jornada> findById(int idJornada) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idJornada);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jornada: " + e.getMessage(), e);
        }
    }

    public List<Jornada> findAll() {
        List<Jornada> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar jornadas: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Jornada jornada) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, jornada.getNombre());
            ps.setTime(2, java.sql.Time.valueOf(jornada.getHoraEntradaEsperada()));
            ps.setTime(3, java.sql.Time.valueOf(jornada.getHoraSalidaEsperada()));
            ps.setInt(4, jornada.getMargenToleranciaMinutos());
            ps.setInt(5, jornada.getIdJornada());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar jornada: " + e.getMessage(), e);
        }
    }

    public void delete(int idJornada) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idJornada);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar jornada: " + e.getMessage(), e);
        }
    }

    private Jornada mapResultSet(ResultSet rs) throws SQLException {
        Jornada j = new Jornada(rs.getInt("IdJornada"), rs.getString("Nombre"),
            rs.getTime("HoraEntradaEsperada").toLocalTime(), rs.getTime("HoraSalidaEsperada").toLocalTime(),
            rs.getInt("MargenToleranciaMinutos"));
        j.setActiva(rs.getBoolean("Activa"));
        return j;
    }
}
