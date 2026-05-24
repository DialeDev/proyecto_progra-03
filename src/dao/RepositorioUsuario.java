package dao;

import models.Usuario;
import java.sql.*;
import java.util.*;

// @author DialeMRZ
public class RepositorioUsuario {
    private static final String SQL_AUTH = "SELECT * FROM Usuario WHERE Username = ? AND PasswordHash = ? AND Activo = 1";
    private static final String SQL_INSERT = "INSERT INTO Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM Usuario WHERE IdEmpleado = ?";
    private static final String SQL_DESACTIVAR = "UPDATE Usuario SET Activo = 0 WHERE IdUsuario = ?";

    public Optional<Usuario> autenticar(String username, String password) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AUTH)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar: " + e.getMessage(), e);
        }
    }

    public void create(Usuario usuario) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setInt(1, usuario.getIdEmpleado());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getRol());
            ps.setBoolean(5, usuario.isActivo());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Error fatal al hacer rollback de usuario: " + ex.getMessage(), ex);
                }
            }
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* ignored */ }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
    }

    public Usuario findByEmpleado(int idEmpleado) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    public void desactivarUsuario(int idUsuario) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DESACTIVAR)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(rs.getInt("IdUsuario"), rs.getInt("IdEmpleado"),
            rs.getString("Username"), rs.getString("PasswordHash"), rs.getString("Rol"));
        u.setActivo(rs.getBoolean("Activo"));
        return u;
    }
}
