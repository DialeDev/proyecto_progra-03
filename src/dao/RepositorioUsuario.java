package dao;

import models.Empleado;
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
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
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

    private static final String SQL_FIND_ALL = "SELECT u.*, e.Nombres, e.Apellidos FROM Usuario u JOIN Empleado e ON u.IdEmpleado = e.IdEmpleado";
    private static final String SQL_FIND_BY_ID = "SELECT u.*, e.Nombres, e.Apellidos FROM Usuario u JOIN Empleado e ON u.IdEmpleado = e.IdEmpleado WHERE u.IdUsuario = ?";
    private static final String SQL_UPDATE = "UPDATE Usuario SET Username = ?, Rol = ?, IdEmpleado = ?, Activo = ? WHERE IdUsuario = ?";
    private static final String SQL_REACTIVAR = "UPDATE Usuario SET Activo = 1 WHERE IdUsuario = ?";
    private static final String SQL_EMPLEADOS_SIN_USUARIO = "SELECT e.* FROM Empleado e WHERE e.IdEmpleado NOT IN (SELECT IdEmpleado FROM Usuario WHERE Activo = 1)";

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetConEmpleado(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    public Usuario findById(int id) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetConEmpleado(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    public void update(Usuario usuario) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getRol());
            ps.setInt(3, usuario.getIdEmpleado());
            ps.setBoolean(4, usuario.isActivo());
            ps.setInt(5, usuario.getIdUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    public void reactivarUsuario(int idUsuario) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REACTIVAR)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al reactivar usuario: " + e.getMessage(), e);
        }
    }

    public List<Empleado> findEmpleadosSinUsuario() {
        String sql = SQL_EMPLEADOS_SIN_USUARIO;
        List<Empleado> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapEmpleadoResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener empleados sin usuario: " + e.getMessage(), e);
        }
        return lista;
    }

    private Usuario mapResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(rs.getInt("IdUsuario"), rs.getInt("IdEmpleado"),
            rs.getString("Username"), rs.getString("PasswordHash"), rs.getString("Rol"));
        u.setActivo(rs.getBoolean("Activo"));
        return u;
    }

    private Usuario mapResultSetConEmpleado(ResultSet rs) throws SQLException {
        Usuario u = mapResultSet(rs);
        String nombres = rs.getString("Nombres");
        String apellidos = rs.getString("Apellidos");
        u.setNombreEmpleado((nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : ""));
        return u;
    }

    private Empleado mapEmpleadoResultSet(ResultSet rs) throws SQLException {
        return new Empleado(rs.getInt("IdEmpleado"), rs.getInt("IdDepartamento"),
            rs.getString("Nombres"), rs.getString("Apellidos"), rs.getString("DUI"), rs.getBoolean("Activo"));
    }
}
