package controllers;

import dao.RepositorioEmpleado;
import models.Empleado;
import views.FrmGestionEmpleados;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class EmpleadoController {

    private final RepositorioEmpleado empleadoDAO;
    private final FrmGestionEmpleados vista;
    private DefaultTableModel modeloTabla;

    public EmpleadoController(FrmGestionEmpleados vista) {
        this.vista = vista;
        this.empleadoDAO = new RepositorioEmpleado();
        
        initView();
    }

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
        int idDepartamento = 1; 

        if (nombres.isEmpty() || apellidos.isEmpty() || dui.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", 
                                          "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dui.matches("\\d{8}-\\d")) {
            JOptionPane.showMessageDialog(vista, "El formato del DUI es incorrecto (ej: 12345678-9).", 
                                          "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar DUI duplicado
        if (empleadoDAO.existeDui(dui)) {
            JOptionPane.showMessageDialog(vista, "El DUI " + dui + " ya está registrado en el sistema.", 
                                          "DUI Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado nuevoEmpleado = new Empleado(
            0, 
            idDepartamento, 
            nombres, 
            apellidos, 
            dui
        );

        try {
            empleadoDAO.create(nuevoEmpleado);
            JOptionPane.showMessageDialog(vista, "Empleado registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al registrar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        // Obtener el modelo directamente de la tabla para asegurar consistencia
        DefaultTableModel model = (DefaultTableModel) vista.getTblEmpleados().getModel();
        model.setRowCount(0);

        List<Empleado> lista = empleadoDAO.findAll();

        for (Empleado e : lista) {
            Object[] fila = new Object[5];
            fila[0] = e.getIdEmpleado();
            fila[1] = e.getDui();
            fila[2] = e.getNombres();
            fila[3] = e.getApellidos();
            fila[4] = e.isActivo() ? "Activo" : "Inactivo";
            
            model.addRow(fila);
        }
        
        // Guardar referencia para otros métodos
        this.modeloTabla = model;
        
        // Forzar refresh de la tabla
        vista.getTblEmpleados().updateUI();
    }

    public void limpiarFormulario() {
        vista.getTxtNombres().setText("");
        vista.getTxtApellidos().setText("");
        vista.getTxtDui().setText("");
        vista.getTblEmpleados().clearSelection();
        vista.getTxtNombres().requestFocus();
    }

    public void desactivarEmpleado() {
        int filaSeleccionada = vista.getTblEmpleados().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un empleado de la tabla para desactivarlo.", 
                                          "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vista.getTblEmpleados().getModel();
        int idEmpleado = (int) model.getValueAt(filaSeleccionada, 0);
        String nombreEmpleado = (String) model.getValueAt(filaSeleccionada, 2);

        int respuesta = JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro que desea dar de baja al empleado " + nombreEmpleado + "?", 
                "Confirmar Desactivación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            empleadoDAO.delete(idEmpleado);

            JOptionPane.showMessageDialog(vista, "Empleado desactivado exitosamente.", 
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        }
    }

}