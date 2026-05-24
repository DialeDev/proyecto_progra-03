package controllers;

import views.*;
import utils.SesionGlobal;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class DashboardController {
    private final FrmDashboard vista;

    public DashboardController(FrmDashboard vista) {
        this.vista = vista;
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
        FrmGestionEmpleados frm = new FrmGestionEmpleados();
        new EmpleadoController(frm);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirMarcacion() {
        FrmMarcacion frm = new FrmMarcacion();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirPlanillas() {
        FrmPlanillas frm = new FrmPlanillas();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirGestionHorarios() {
        FrmGestionHorarios frm = new FrmGestionHorarios();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirGestionContratos() {
        FrmGestionContratos frm = new FrmGestionContratos();
        new ContratoController(frm);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirDepartamentos() {
        FrmGestionDepartamentos frm = new FrmGestionDepartamentos();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirAsignacionJornada() {
        FrmAsignacionJornada frm = new FrmAsignacionJornada();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        ocultarDashboardAlAbrirForm(frm);
    }

    public void abrirReportes() {
        FrmReportes frm = new FrmReportes();
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
