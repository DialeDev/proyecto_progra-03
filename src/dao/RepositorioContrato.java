package dao;

import models.Contrato;
import java.sql.*;
import java.util.*;

// @author DialeMRZ
public class RepositorioContrato {
    private static final String SQL_INSERT = "INSERT INTO Contrato (Nombre, Descripcion, Activo) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Contrato WHERE IdContrato = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Contrato";
    private static final String SQL_SELECT_ACTIVE = "SELECT * FROM Contrato WHERE Activo = 1";
    private static final String SQL_UPDATE = "UPDATE Contrato SET Nombre = ?, Descripcion = ?, Activo = ? WHERE IdContrato = ?";
    private static final String SQL_DELETE = "UPDATE Contrato SET Activo = 0 WHERE IdContrato = ?";

    public void create(Contrato contrato) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, contrato.getNombre());
            ps.setString(2, contrato.getDescripcion());
            ps.setBoolean(3, contrato.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear contrato: " + e.getMessage(), e);
        }
    }

    public Optional<Contrato> findById(int idContrato) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idContrato);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contrato: " + e.getMessage(), e);
        }
    }

    public List<Contrato> findAll() {
        List<Contrato> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contratos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Contrato> findActive() {
        List<Contrato> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ACTIVE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contratos activos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Contrato contrato) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, contrato.getNombre());
            ps.setString(2, contrato.getDescripcion());
            ps.setBoolean(3, contrato.isActivo());
            ps.setInt(4, contrato.getIdContrato());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contrato: " + e.getMessage(), e);
        }
    }

    public void delete(int idContrato) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idContrato);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contrato: " + e.getMessage(), e);
        }
    }

    public String[] listarNombres() {
        return findAll().stream().map(Contrato::getNombre).toArray(String[]::new);
    }

    public Contrato buscarPorNombre(String nombre) {
        return findAll().stream().filter(c -> c.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
    }

    private Contrato mapResultSet(ResultSet rs) throws SQLException {
        Contrato c = new Contrato(rs.getInt("IdContrato"), rs.getString("Nombre"), rs.getString("Descripcion"));
        c.setActivo(rs.getBoolean("Activo"));
        return c;
    }
}
