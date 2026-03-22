package models;

/**
 *
 * @author DialeMRZ
 */

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
    
}
