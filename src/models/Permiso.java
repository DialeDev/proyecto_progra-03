package models;

// @author DialeMRZ
public class Permiso {
    private int idPermiso;
    private String rol;
    private Integer idDepartamento; // nullable = applies to all depts
    private String modulo;
    private boolean puedeVer;
    private boolean puedeOperar;

    public Permiso() {}

    public Permiso(int idPermiso, String rol, Integer idDepartamento, String modulo, boolean puedeVer, boolean puedeOperar) {
        this.idPermiso = idPermiso;
        this.rol = rol;
        this.idDepartamento = idDepartamento;
        this.modulo = modulo;
        this.puedeVer = puedeVer;
        this.puedeOperar = puedeOperar;
    }

    public int getIdPermiso() { return idPermiso; }
    public void setIdPermiso(int idPermiso) { this.idPermiso = idPermiso; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Integer idDepartamento) { this.idDepartamento = idDepartamento; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public boolean isPuedeVer() { return puedeVer; }
    public void setPuedeVer(boolean puedeVer) { this.puedeVer = puedeVer; }

    public boolean isPuedeOperar() { return puedeOperar; }
    public void setPuedeOperar(boolean puedeOperar) { this.puedeOperar = puedeOperar; }
}
