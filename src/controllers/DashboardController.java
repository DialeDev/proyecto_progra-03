package controllers;

import views.*;
import models.Usuario;
import services.PermisoService;
import utils.SesionGlobal;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class DashboardController {
    private final FrmDashboard vista;
    private final PermisoService permisoService;

    public DashboardController(FrmDashboard vista) {
        this.vista = vista;
        this.permisoService = new PermisoService();
    }

    /**
     * Loads permissions for the current user and applies button visibility.
     * Called from FrmDashboard constructor after login.
     */
    public void cargarPermisos() {
        Usuario user = SesionGlobal.getUsuarioActivo();
        if (user == null) return;

        permisoService.cargarPermisos(user);
        SesionGlobal.setPermisoService(permisoService);

        // Set button visibility based on PuedeVer permission
        vista.getBtnEmpleados().setVisible(permisoService.tieneAcceso(user, "Empleados", "ver"));
        vista.getBtnMarcacion().setVisible(permisoService.tieneAcceso(user, "Marcacion", "ver"));
        vista.getBtnPlanilla().setVisible(permisoService.tieneAcceso(user, "Planillas", "ver"));
        vista.getBtnContratos().setVisible(permisoService.tieneAcceso(user, "Contratos", "ver"));
        vista.getBtnHorarios().setVisible(permisoService.tieneAcceso(user, "Horarios", "ver"));
        vista.getBtnReportes().setVisible(permisoService.tieneAcceso(user, "Reportes", "ver"));
        vista.getBtnDepartamentos().setVisible(permisoService.tieneAcceso(user, "Departamentos", "ver"));
        vista.getBtnAsignacion().setVisible(permisoService.tieneAcceso(user, "AsignacionJornada", "ver"));
        vista.getBtnPermisos().setVisible(permisoService.tieneAcceso(user, "Permisos", "ver"));
        vista.getBtnUsuarios().setVisible(permisoService.tieneAcceso(user, "Usuarios", "ver"));
    }

    /**
     * Checks if the current user has access to the given module.
     * Shows "Acceso denegado" message and returns false if denied.
     */
    private boolean verificarAcceso(String modulo) {
        Usuario user = SesionGlobal.getUsuarioActivo();
        if (user == null) return false;
        if (permisoService.tieneAcceso(user, modulo, "ver")) return true;
        vista.mostrarMensaje("error", "Acceso denegado");
        return false;
    }

    private void ocultarDashboardAlAbrirForm(java.awt.Window ventanaHija) {
        vista.setVisible(false);
        ventanaHija.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                vista.setVisible(true);
                // Restaurar icono después de hacerlo visible (workaround para comportamiento de Swing)
                vista.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
            }
        });
    }

    public void abrirGestionEmpleados() {
        if (!verificarAcceso("Empleados")) return;
        FrmGestionEmpleados frm = new FrmGestionEmpleados();
        new EmpleadoController(frm);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirMarcacion() {
        if (!verificarAcceso("Marcacion")) return;
        FrmMarcacion frm = new FrmMarcacion();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirPlanillas() {
        if (!verificarAcceso("Planillas")) return;
        FrmPlanillas frm = new FrmPlanillas();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirGestionHorarios() {
        if (!verificarAcceso("Horarios")) return;
        FrmGestionHorarios frm = new FrmGestionHorarios();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirGestionContratos() {
        if (!verificarAcceso("Contratos")) return;
        FrmGestionContratos frm = new FrmGestionContratos();
        new ContratoController(frm);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirDepartamentos() {
        if (!verificarAcceso("Departamentos")) return;
        FrmGestionDepartamentos frm = new FrmGestionDepartamentos();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirAsignacionJornada() {
        if (!verificarAcceso("AsignacionJornada")) return;
        FrmAsignacionJornada frm = new FrmAsignacionJornada();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirReportes() {
        if (!verificarAcceso("Reportes")) return;
        FrmReportes frm = new FrmReportes();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    /**
     * Opens the User Management form.
     */
    public void abrirGestionUsuarios() {
        if (!verificarAcceso("Usuarios")) return;
        FrmGestionUsuarios frm = new FrmGestionUsuarios();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    /**
     * Opens the Permission Management form. Admin-only access.
     */
    public void abrirGestionPermisos() {
        Usuario user = SesionGlobal.getUsuarioActivo();
        if (user == null || !"Admin".equals(user.getRol())) {
            vista.mostrarMensaje("error", "Acceso denegado");
            return;
        }
        FrmGestionPermisos frm = new FrmGestionPermisos();
        new PermisoController(frm);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void cerrarSesion() {
        SesionGlobal.cerrarSesion();
        FrmLogin frmLogin = new FrmLogin();
        frmLogin.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        frmLogin.setVisible(true);
        vista.dispose();
    }
}
