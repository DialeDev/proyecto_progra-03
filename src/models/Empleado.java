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
    
}