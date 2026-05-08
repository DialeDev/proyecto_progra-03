package controllers;

import dao.RepositorioContrato;
import dao.RepositorioContratacion;
import models.Contrato;
import models.Contrato.TipoContrato;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import views.FrmGestionContratos;
import views.FrmRegistroContratos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ContratoController implements ActionListener {
    private final FrmGestionContratos vista;
    private final RepositorioContrato contratoDAO;
    private final RepositorioContratacion contratacionDAO;
    private DefaultTableModel modeloTabla;

    public ContratoController(FrmGestionContratos vista) {
        this.vista = vista;
        this.contratoDAO = new RepositorioContrato();
        this.contratacionDAO = new RepositorioContratacion();
        this.vista.getBtnNuevo().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnCerrar().addActionListener(this);
        iniciarTabla();
        cargarTabla();
    }

    private void iniciarTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Activo");
        vista.getTablaContratos().setModel(modeloTabla);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            contratoDAO.findAll().forEach(c -> modeloTabla.addRow(new Object[]{
                c.getIdContrato(), c.getNombre(), c.getTipo().name(), c.getDescripcion(), c.isActivo() ? "Activo" : "Inactivo"}));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnNuevo()) {
            FrmRegistroContratos vistaRegistro = new FrmRegistroContratos();
            new RegistroContratoController(vistaRegistro, vista);
            vistaRegistro.setLocationRelativeTo(null);
            vistaRegistro.setVisible(true);
        } else if (e.getSource() == vista.getBtnEliminar()) {
            eliminarContrato();
        } else if (e.getSource() == vista.getBtnCerrar()) {
            vista.dispose();
        }
    }

    public void guardarTipoContrato(String nombre, String tipoStr, String descripcion) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TipoContrato tipo;
        try { tipo = TipoContrato.valueOf(tipoStr); }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Tipo no válido.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (contratoDAO.buscarPorNombre(nombre) != null) {
            JOptionPane.showMessageDialog(vista, "Ya existe un tipo con el nombre '" + nombre + "'.", "Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            contratoDAO.create(new Contrato(0, nombre, tipo, descripcion));
            JOptionPane.showMessageDialog(vista, "Guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTipoContrato(int id, String nombre, String tipoStr, String descripcion) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TipoContrato tipo;
        try { tipo = TipoContrato.valueOf(tipoStr); }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Tipo no válido.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(vista, "¿Actualizar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                contratoDAO.update(new Contrato(id, nombre, tipo, descripcion));
                JOptionPane.showMessageDialog(vista, "Actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void eliminarTipoContrato(int id, String nombre) {
        if (JOptionPane.showConfirmDialog(vista, "¿Eliminar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                contratoDAO.delete(id);
                JOptionPane.showMessageDialog(vista, "Eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarContrato() {
        int fila = vista.getTablaContratos().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un contrato.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaContratos().getModel();
        modelo.removeRow(fila);
        JOptionPane.showMessageDialog(vista, "Contrato eliminado.");
    }
}
