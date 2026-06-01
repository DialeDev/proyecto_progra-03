package utils;

import models.Usuario;
import services.PermisoService;

// @author DialeMRZ
public class SesionGlobal {
    private static Usuario usuarioActivo = null;
    private static PermisoService permisoService = null;

    public static void iniciarSesion(Usuario usuario) { usuarioActivo = usuario; }
    public static void cerrarSesion() {
        usuarioActivo = null;
        permisoService = null;
    }
    public static Usuario getUsuarioActivo() { return usuarioActivo; }
    public static boolean haySesionActiva() { return usuarioActivo != null; }

    /** Stores the active PermisoService so any controller can check permissions. */
    public static void setPermisoService(PermisoService service) { permisoService = service; }
    public static PermisoService getPermisoService() { return permisoService; }

    /**
     * Checks if the active user has "ver" (view) access to the given module.
     * Safe to call from any controller — returns false if no session or service.
     */
    public static boolean puedeVer(String modulo) {
        if (usuarioActivo == null || permisoService == null) return false;
        return permisoService.tieneAcceso(usuarioActivo, modulo, "ver");
    }

    /**
     * Checks if the active user has "operar" (operate) access to the given module.
     * Safe to call from any controller — returns false if no session or service.
     */
    public static boolean puedeOperar(String modulo) {
        if (usuarioActivo == null || permisoService == null) return false;
        return permisoService.tieneAcceso(usuarioActivo, modulo, "operar");
    }
}
