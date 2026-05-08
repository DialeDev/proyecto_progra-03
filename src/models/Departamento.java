package models;

public class Departamento {
    private int idDepartamento;
    private String nombre;
    private boolean activo;

    public Departamento(int idDepartamento, String nombre, boolean activo) {
        this.idDepartamento = idDepartamento;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(int idDepartamento) { this.idDepartamento = idDepartamento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
