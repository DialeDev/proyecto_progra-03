package dao;

import models.BoletaDetalle;
import java.sql.*;
import java.util.*;

public class RepositorioBoletaDetalle {
    private static final String SQL_INSERT = "INSERT INTO BoletaDetalle (IdBoleta, Concepto, Tipo, Monto) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_BOLETA = "SELECT * FROM BoletaDetalle WHERE IdBoleta = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM BoletaDetalle";

    public Long create(BoletaDetalle bd) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, bd.idBoleta());
            ps.setString(2, bd.concepto());
            ps.setString(3, bd.tipo());
            ps.setBigDecimal(4, bd.monto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear detalle: " + e.getMessage(), e);
        }
    }

    public List<BoletaDetalle> findByBoleta(Long idBoleta) {
        List<BoletaDetalle> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_BOLETA)) {
            ps.setLong(1, idBoleta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar detalles: " + e.getMessage(), e);
        }
        return lista;
    }

    private BoletaDetalle map(ResultSet rs) throws SQLException {
        return new BoletaDetalle(rs.getLong("IdDetalle"), rs.getLong("IdBoleta"),
            rs.getString("Concepto"), rs.getString("Tipo"), rs.getBigDecimal("Monto"));
    }
}
