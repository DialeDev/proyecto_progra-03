package controllers;

import views.*;
import utils.SesionGlobal;

public class DashboardController {
    private final FrmDashboard vista;

    public DashboardController(FrmDashboard vista) {
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

    public void cerrarSesion() {
        SesionGlobal.cerrarSesion();
        FrmLogin frmLogin = new FrmLogin();
        frmLogin.setVisible(true);
        vista.dispose();
    }
}
