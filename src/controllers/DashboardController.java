/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        new EmpleadoController(frm); // Conectamos su controlador
        frm.setVisible(true);
    }

    public void abrirMarcacion() {
        FrmMarcacion frm = new FrmMarcacion();
        new AsistenciaController(); // Se actualizará cuando tu equipo termine este controlador
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirPlanillas() {
        FrmPlanillas frm = new FrmPlanillas();
        new PlanillaController(); // Se actualizará cuando tu equipo termine este controlador
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }
    
    public void abrirGestionHorarios() {
        FrmGestionHorarios frm = new FrmGestionHorarios();
        new HorarioController(); // Se actualizará cuando tu equipo termine este controlador
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void abrirGestionContratos() {
        FrmGestionContratos frm = new FrmGestionContratos();
        new ContratoController(); // Se actualizará cuando tu equipo termine este controlador
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public void cerrarSesion() {
        SesionGlobal.cerrarSesion(); // Limpiamos la memoria
        FrmLogin frmLogin = new FrmLogin();
        frmLogin.setVisible(true);
        vista.dispose(); // Cerramos el menú principal
    }
}
