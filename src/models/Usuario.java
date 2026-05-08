package models;

// @author DialeMRZ
public class Usuario {
    private int idUsuario;
    private int idEmpleado;
    private String username;
    private String passwordHash;
    private String rol;
    private boolean activo;

    public Usuario(int idUsuario, int idEmpleado, String username, String passwordHash, String rol) {
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = true;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
