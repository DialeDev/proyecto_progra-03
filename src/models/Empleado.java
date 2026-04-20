package models;

/**
 *
 * @author DialeMRZ
 */

public class Empleado {

    private int idEmpleado;
    private int idDepartamento; 
    private String nombres;
    private String apellidos;
    private String dui;
    private boolean activo;

    public Empleado(int idEmpleado, int idDepartamento, String nombres, String apellidos, 
                    String dui) {
        this.idEmpleado = idEmpleado;
        this.idDepartamento = idDepartamento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dui = dui;
        this.activo = true;
    }
    
    public Empleado(int idEmpleado, int idDepartamento, String nombres, String apellidos, 
                    String dui, boolean activo) {
        this.idEmpleado = idEmpleado;
        this.idDepartamento = idDepartamento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dui = dui;
        this.activo = activo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
}