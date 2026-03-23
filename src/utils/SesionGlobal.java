package utils;

import models.Usuario;

/**
 *
 * @author DialeMRZ
 */

public class SesionGlobal {
    private static Usuario usuarioActivo = null;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActivo = usuario;
    }

    public static void cerrarSesion() {
        usuarioActivo = null;
    }

    public static Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public static boolean haySesionActiva() {
        return usuarioActivo != null;
    }
}
