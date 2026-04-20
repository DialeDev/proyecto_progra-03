package dao;

import models.Jornada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DialeMRZ
 */
public class RepositorioJornada {

    private static final String SQL_INSERT = 
        "INSERT INTO Jornada (Nombre, HoraEntradaEsperada, HoraSalidaEsperada, MargenToleranciaMinutos, Activa) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Jornada WHERE IdJornada = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT * FROM Jornada WHERE Activa = 1";
    private static final String SQL_UPDATE = 
        "UPDATE Jornada SET Nombre = ?, HoraEntradaEsperada = ?, HoraSalidaEsperada = ?, MargenToleranciaMinutos = ? WHERE IdJornada = ?";
    private static final String SQL_DELETE = 
        "UPDATE Jornada SET Activa = 0 WHERE IdJornada = ?";

    public void create(Jornada jornada) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public Jornada findById(int idJornada) {
        String sql = SQL_SELECT_BY_ID;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJornada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jornada: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Jornada> findAll() {
        String sql = SQL_SELECT_ALL;
        List<Jornada> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar jornadas: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Jornada jornada) {
        String sql = SQL_UPDATE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = SQL_DELETE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJornada);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar jornada: " + e.getMessage(), e);
        }
    }

    private Jornada mapResultSet(ResultSet rs) throws SQLException {
        return new Jornada(
            rs.getInt("IdJornada"),
            rs.getString("Nombre"),
            rs.getTime("HoraEntradaEsperada").toLocalTime(),
            rs.getTime("HoraSalidaEsperada").toLocalTime(),
            rs.getInt("MargenToleranciaMinutos")
        );
    }
}