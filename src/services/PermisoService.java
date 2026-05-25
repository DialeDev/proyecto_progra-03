package services;

import dao.RepositorioEmpleado;
import dao.RepositorioPermiso;
import models.Empleado;
import models.Permiso;
import models.Usuario;
import java.util.*;

// @author DialeMRZ
public class PermisoService {
    private final RepositorioPermiso repo;
    private final Map<String, Permiso> cache; // key: module name
    private boolean cacheLoaded = false;

    public PermisoService() {
        this.repo = new RepositorioPermiso();
        this.cache = new HashMap<>();
    }

    /**
     * Loads permissions for the given user into cache.
     * Admin bypass: Admin role skips the DB query entirely.
     */
    public void cargarPermisos(Usuario usuario) {
        cache.clear();
        cacheLoaded = false;
        if (usuario == null || !usuario.isActivo()) return;

        // Admin bypass: no need to query DB
        if ("Admin".equals(usuario.getRol())) {
            cacheLoaded = true;
            return;
        }

        Integer idDepto = obtenerIdDepartamento(usuario);
        List<Permiso> permisos = repo.findByRolYDepto(usuario.getRol(), idDepto);
        for (Permiso p : permisos) {
            cache.put(p.getModulo(), p);
        }
        cacheLoaded = true;
    }

    /**
     * Checks if a user has access to a specific module+action.
     * Admin bypass: returns true for Admin role without querying cache.
     * Inactive user: always returns false.
     */
    public boolean tieneAcceso(Usuario usuario, String modulo, String accion) {
        if (usuario == null || !usuario.isActivo()) return false;

        // Admin bypass
        if ("Admin".equals(usuario.getRol())) return true;

        if (!cacheLoaded) cargarPermisos(usuario);

        Permiso p = cache.get(modulo);
        if (p == null) return false;

        if ("ver".equals(accion)) return p.isPuedeVer();
        if ("operar".equals(accion)) return p.isPuedeOperar();
        return false;
    }

    /**
     * Checks if the user has "ver" (view) access to a module.
     */
    public boolean tieneAcceso(Usuario usuario, String modulo) {
        return tieneAcceso(usuario, modulo, "ver");
    }

    /**
     * Reloads permissions from DB.
     */
    public void recargarPermisos(Usuario usuario) {
        cacheLoaded = false;
        cargarPermisos(usuario);
    }

    public boolean isCacheLoaded() { return cacheLoaded; }

    private Integer obtenerIdDepartamento(Usuario usuario) {
        try {
            RepositorioEmpleado empRepo = new RepositorioEmpleado();
            Optional<Empleado> opt = empRepo.findById(usuario.getIdEmpleado());
            return opt.map(Empleado::getIdDepartamento).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
