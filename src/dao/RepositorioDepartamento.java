package dao;

import models.Departamento;
import java.sql.*;
import java.util.*;

public class RepositorioDepartamento {
    private static final String SQL_SELECT_ALL = "SELECT * FROM Departamento WHERE Activo = 1";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Departamento WHERE IdDepartamento = ?";

    public List<Departamento> findAll() {
        List<Departamento> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar departamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Departamento> findById(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar departamento: " + e.getMessage(), e);
        }
    }

    private Departamento map(ResultSet rs) throws SQLException {
        return new Departamento(rs.getInt("IdDepartamento"), rs.getString("Nombre"), rs.getBoolean("Activo"));
    }
}
