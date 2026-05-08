package models;

// @author DialeMRZ
public class Contrato {
    public enum TipoContrato { FIJO, POR_HORA }

    private int idContrato;
    private String nombre;
    private TipoContrato tipo;
    private String descripcion;
    private boolean activo;

    public Contrato(int idContrato, String nombre, TipoContrato tipo, String descripcion) {
        this.idContrato = idContrato;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activo = true;
    }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoContrato getTipo() { return tipo; }
    public void setTipo(TipoContrato tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
