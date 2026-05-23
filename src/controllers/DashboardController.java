package controllers;

import views.*;
import utils.SesionGlobal;
import javax.swing.JFrame;

public class DashboardController {
    private final JFrame vista;

    public DashboardController(JFrame vista) {
        this.vista = vista;
    }

    public void abrirGestionEmpleados() {
        FrmGestionEmpleados frm = new FrmGestionEmpleados();
        new EmpleadoController(frm);
        frm.setVisible(true);
    }

    public void abrirMarcacion() {
        FrmMarcacion frm = new FrmMarcacion();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirPlanillas() {
        FrmPlanillas frm = new FrmPlanillas();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirGestionHorarios() {
        FrmGestionHorarios frm = new FrmGestionHorarios();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirGestionContratos() {
        FrmGestionContratos frm = new FrmGestionContratos();
        new ContratoController(frm);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirDepartamentos() {
        FrmGestionDepartamentos frm = new FrmGestionDepartamentos();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirAsignacionJornada() {
        FrmAsignacionJornada frm = new FrmAsignacionJornada();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirReportes() {
        FrmReportes frm = new FrmReportes();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void cerrarSesion() {
        SesionGlobal.cerrarSesion();
        FrmLogin frmLogin = new FrmLogin();
        frmLogin.setVisible(true);
        vista.dispose();
    }
}
