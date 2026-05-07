package dao;

import models.Contrato;
import models.Contrato.TipoContrato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DialeMRZ
 */
public class RepositorioContrato {

    private static final String SQL_INSERT = 
        "INSERT INTO Contrato (Nombre, Tipo, Descripcion, Activo) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Contrato WHERE IdContrato = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT * FROM Contrato WHERE Activo = 1";
    private static final String SQL_UPDATE = 
        "UPDATE Contrato SET Nombre = ?, Tipo = ?, Descripcion = ? WHERE IdContrato = ?";
    private static final String SQL_DELETE = 
        "UPDATE Contrato SET Activo = 0 WHERE IdContrato = ?";

    public void create(Contrato contrato) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contrato.getNombre());
            ps.setString(2, contrato.getTipo().name());
            ps.setString(3, contrato.getDescripcion());
            ps.setBoolean(4, contrato.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear contrato: " + e.getMessage(), e);
        }
    }

    public Contrato findById(int idContrato) {
        String sql = SQL_SELECT_BY_ID;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contrato: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Contrato> findAll() {
        String sql = SQL_SELECT_ALL;
        List<Contrato> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contratos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Contrato contrato) {
        String sql = SQL_UPDATE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contrato.getNombre());
            ps.setString(2, contrato.getTipo().name());
            ps.setString(3, contrato.getDescripcion());
            ps.setInt(4, contrato.getIdContrato());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contrato: " + e.getMessage(), e);
        }
    }

    public void delete(int idContrato) {
        String sql = SQL_DELETE;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contrato: " + e.getMessage(), e);
        }
    }

    public String[] listarNombres() {
        List<Contrato> contratos = findAll();
        List<String> nombres = new ArrayList<>();
        for (Contrato c : contratos) {
            nombres.add(c.getNombre());
        }
        return nombres.toArray(new String[0]);
    }

    public Contrato buscarPorNombre(String nombre) {
        List<Contrato> contratos = findAll();
        for (Contrato c : contratos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    private Contrato mapResultSet(ResultSet rs) throws SQLException {
        String tipoStr = rs.getString("Tipo");
        TipoContrato tipo;
        if ("Fijo".equalsIgnoreCase(tipoStr) || "FIJO".equals(tipoStr)) {
            tipo = TipoContrato.FIJO;
        } else {
            tipo = TipoContrato.POR_HORA;
        }
        Contrato c = new Contrato(
            rs.getInt("IdContrato"),
            rs.getString("Nombre"),
            tipo,
            rs.getString("Descripcion")
        );
        c.setActivo(rs.getBoolean("Activo"));
        return c;
    }
}
