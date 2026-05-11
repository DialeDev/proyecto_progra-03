package controllers;

import dao.RepositorioEmpleado;
import models.Departamento;
import models.Empleado;
import views.FrmGestionEmpleados;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

// @author DialeMRZ
public class EmpleadoController {
    private final RepositorioEmpleado empleadoDAO;
    private final FrmGestionEmpleados vista;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int empleadoEnEdicion = 0;

    public EmpleadoController(FrmGestionEmpleados vista) {
        this.vista = vista;
        this.empleadoDAO = new RepositorioEmpleado();
        initView();
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
        String nombres = vista.getTxtNombres().getText().trim();
        String apellidos = vista.getTxtApellidos().getText().trim();
        String dui = vista.getTxtDui().getText().trim();
        if (nombres.isEmpty() || apellidos.isEmpty() || dui.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!dui.matches("\\d{8}-\\d")) {
            JOptionPane.showMessageDialog(vista, "Formato DUI incorrecto (ej: 12345678-9).", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Departamento depto = (Departamento) vista.getCmbDepartamento().getSelectedItem();
        int idDepartamento = depto != null ? depto.getIdDepartamento() : 1;
        if (editando) {
            if (empleadoDAO.existeDui(dui)) {
                Optional<Empleado> existente = empleadoDAO.findById(empleadoEnEdicion);
                if (existente.isPresent() && !existente.get().getDui().equals(dui) && empleadoDAO.existeDui(dui)) {
                    JOptionPane.showMessageDialog(vista, "El DUI " + dui + " ya está registrado por otro empleado.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            try {
                empleadoDAO.update(new Empleado(empleadoEnEdicion, idDepartamento, nombres, apellidos, dui));
                JOptionPane.showMessageDialog(vista, "Empleado actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (empleadoDAO.existeDui(dui)) {
                JOptionPane.showMessageDialog(vista, "El DUI " + dui + " ya está registrado.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                empleadoDAO.create(new Empleado(0, idDepartamento, nombres, apellidos, dui));
                JOptionPane.showMessageDialog(vista, "Empleado registrado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        int fila = vista.getTblEmpleados().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un empleado.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) vista.getTblEmpleados().getModel();
        int id = (int) model.getValueAt(fila, 0);
        String nombre = (String) model.getValueAt(fila, 2);
        if (JOptionPane.showConfirmDialog(vista, "¿Desactivar a " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            empleadoDAO.delete(id);
            JOptionPane.showMessageDialog(vista, "Empleado desactivado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        }
    }
}
