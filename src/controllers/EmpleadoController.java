package controllers;

import dao.RepositorioDepartamento;
import dao.RepositorioEmpleado;
import models.Departamento;
import models.Empleado;
import utils.SesionGlobal;
import views.FrmGestionEmpleados;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

// @author DialeMRZ
public class EmpleadoController {
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioDepartamento departamentoDAO;
    private final FrmGestionEmpleados vista;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int empleadoEnEdicion = 0;

    public EmpleadoController(FrmGestionEmpleados vista) {
        this.vista = vista;
        this.empleadoDAO = new RepositorioEmpleado();
        this.departamentoDAO = new RepositorioDepartamento();
        initView();
    }

    public List<Departamento> obtenerDepartamentos() {
        return departamentoDAO.findActive();
    }

    public void setEditando(boolean editando) { this.editando = editando; }
    public void setEmpleadoEnEdicion(int id) { this.empleadoEnEdicion = id; }

    private void initView() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DUI");
        modeloTabla.addColumn("Nombres");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Estado");
        vista.getTblEmpleados().setModel(modeloTabla);
        cargarTabla();
    }

    public void guardarEmpleado() {
        if (!SesionGlobal.puedeOperar("Empleados")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        String nombres = vista.getTxtNombres().getText().trim();
        String apellidos = vista.getTxtApellidos().getText().trim();
        String dui = vista.getTxtDui().getText().trim();
        if (nombres.isEmpty() || apellidos.isEmpty() || dui.isEmpty()) {
            vista.mostrarMensaje("advertencia", "Todos los campos son obligatorios.");
            return;
        }
        if (!dui.matches("\\d{8}-\\d")) {
            vista.mostrarMensaje("advertencia", "Formato DUI incorrecto (ej: 12345678-9).");
            return;
        }
        Departamento depto = (Departamento) vista.getCmbDepartamento().getSelectedItem();
        int idDepartamento = depto != null ? depto.getIdDepartamento() : 1;
        if (editando) {
            if (empleadoDAO.existeDui(dui)) {
                Optional<Empleado> existente = empleadoDAO.findById(empleadoEnEdicion);
                if (existente.isPresent() && !existente.get().getDui().equals(dui) && empleadoDAO.existeDui(dui)) {
                    vista.mostrarMensaje("advertencia", "El DUI " + dui + " ya está registrado por otro empleado.");
                    return;
                }
            }
            try {
                empleadoDAO.update(new Empleado(empleadoEnEdicion, idDepartamento, nombres, apellidos, dui));
                vista.mostrarMensaje("exito", "Empleado actualizado.");
            } catch (Exception e) {
                vista.mostrarMensaje("error", "Error: " + e.getMessage());
                return;
            }
        } else {
            if (empleadoDAO.existeDui(dui)) {
                vista.mostrarMensaje("advertencia", "El DUI " + dui + " ya está registrado.");
                return;
            }
            try {
                empleadoDAO.create(new Empleado(0, idDepartamento, nombres, apellidos, dui));
                vista.mostrarMensaje("exito", "Empleado registrado.");
            } catch (Exception e) {
                vista.mostrarMensaje("error", "Error: " + e.getMessage());
                return;
            }
        }
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblEmpleados().getModel();
        model.setRowCount(0);
        empleadoDAO.findAll().forEach(e -> model.addRow(new Object[]{
            e.getIdEmpleado(), e.getDui(), e.getNombres(), e.getApellidos(), e.isActivo() ? "Activo" : "Inactivo"}));
        vista.getTblEmpleados().updateUI();
    }

    public void limpiarFormulario() {
        editando = false;
        empleadoEnEdicion = 0;
        vista.getBtnGuardar().setText("Guardar");
        vista.getTxtNombres().setText("");
        vista.getTxtApellidos().setText("");
        vista.getTxtDui().setText("");
        vista.getCmbDepartamento().setSelectedIndex(-1);
        vista.getTblEmpleados().clearSelection();
        vista.getTxtNombres().requestFocus();
    }

    public void desactivarEmpleado() {
        if (!SesionGlobal.puedeOperar("Empleados")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        int fila = vista.getTblEmpleados().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("advertencia", "Seleccione un empleado.");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) vista.getTblEmpleados().getModel();
        int id = (int) model.getValueAt(fila, 0);
        String nombre = (String) model.getValueAt(fila, 2);
        if (vista.mostrarConfirmacion("¿Desactivar a " + nombre + "?")) {
            empleadoDAO.delete(id);
            vista.mostrarMensaje("exito", "Empleado desactivado.");
            cargarTabla();
            limpiarFormulario();
        }
    }
}
