package models;

/**
 *
 * @author DialeMRZ
 */

public class Usuario {
    private int idUsuario;
    private int idEmpleado; 
    private String username;
    private String password; 
    private String rol; 
    private boolean activo;

    public Usuario(int idUsuario, int idEmpleado, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = true;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
   
    
    
}
