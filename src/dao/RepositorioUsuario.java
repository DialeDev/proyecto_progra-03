package dao;

import models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DialeMRZ
 */
public class RepositorioUsuario {

    private static final String SQL_AUTH = 
        "SELECT * FROM Usuario WHERE Username = ? AND PasswordHash = ? AND Activo = 1";
    private static final String SQL_INSERT = 
        "INSERT INTO Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMPLEADO = 
        "SELECT * FROM Usuario WHERE IdEmpleado = ?";
    private static final String SQL_DESACTIVAR = 
        "UPDATE Usuario SET Activo = 0 WHERE IdUsuario = ?";

    public Usuario autenticar(String username, String password) {
        String sql = SQL_AUTH;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar usuario: " + e.getMessage(), e);
        }
        return null;
    }

    public void create(Usuario usuario) {
        String sql = SQL_INSERT;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdEmpleado());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getRol());
            ps.setBoolean(5, usuario.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    public Usuario findByEmpleado(int idEmpleado) {
        String sql = SQL_SELECT_BY_EMPLEADO;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por empleado: " + e.getMessage(), e);
        }
        return null;
    }

    public void desactivarUsuario(int idUsuario) {
        String sql = SQL_DESACTIVAR;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapResultSet(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("IdUsuario"),
            rs.getInt("IdEmpleado"),
            rs.getString("Username"),
            rs.getString("PasswordHash"),
            rs.getString("Rol")
        );
    }
}