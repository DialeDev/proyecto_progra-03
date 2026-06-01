package models;

// @author DialeMRZ
public class Contrato {
    private int idContrato;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public Contrato(int idContrato, String nombre, String descripcion) {
        this.idContrato = idContrato;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
