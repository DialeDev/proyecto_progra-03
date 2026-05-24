package controllers;

import dao.RepositorioContrato;
import dao.RepositorioContratacion;
import models.Contrato;
import models.Contrato.TipoContrato;
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
            vista.mostrarMensaje("error", "Error al cargar: " + e.getMessage());
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
            vista.mostrarMensaje("advertencia", "El nombre es obligatorio.");
            return;
        }
        TipoContrato tipo;
        try { tipo = TipoContrato.valueOf(tipoStr); }
        catch (IllegalArgumentException e) {
            vista.mostrarMensaje("advertencia", "Tipo no válido.");
            return;
        }
        if (contratoDAO.buscarPorNombre(nombre) != null) {
            vista.mostrarMensaje("advertencia", "Ya existe un tipo con el nombre '" + nombre + "'.");
            return;
        }
        try {
            contratoDAO.create(new Contrato(0, nombre, tipo, descripcion));
            vista.mostrarMensaje("exito", "Guardado exitosamente.");
            cargarTabla();
        } catch (Exception ex) {
            vista.mostrarMensaje("error", "Error: " + ex.getMessage());
        }
    }

    public void actualizarTipoContrato(int id, String nombre, String tipoStr, String descripcion) {
        if (nombre.isEmpty()) {
            vista.mostrarMensaje("advertencia", "El nombre es obligatorio.");
            return;
        }
        TipoContrato tipo;
        try { tipo = TipoContrato.valueOf(tipoStr); }
        catch (IllegalArgumentException e) {
            vista.mostrarMensaje("advertencia", "Tipo no válido.");
            return;
        }
        if (vista.mostrarConfirmacion("¿Actualizar '" + nombre + "'?")) {
            try {
                contratoDAO.update(new Contrato(id, nombre, tipo, descripcion));
                vista.mostrarMensaje("exito", "Actualizado exitosamente.");
                cargarTabla();
            } catch (Exception ex) {
                vista.mostrarMensaje("error", "Error: " + ex.getMessage());
            }
        }
    }

    public void eliminarTipoContrato(int id, String nombre) {
        if (vista.mostrarConfirmacion("¿Eliminar '" + nombre + "'?")) {
            try {
                contratoDAO.delete(id);
                vista.mostrarMensaje("exito", "Eliminado exitosamente.");
                cargarTabla();
            } catch (Exception ex) {
                vista.mostrarMensaje("error", "Error: " + ex.getMessage());
            }
        }
    }

    private void eliminarContrato() {
        int fila = vista.getTablaContratos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("advertencia", "Seleccione un contrato.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaContratos().getModel();
        int id = (int) modelo.getValueAt(fila, 0);
        if (vista.mostrarConfirmacion("¿Eliminar el contrato seleccionado?")) {
            try {
                contratoDAO.delete(id);
                cargarTabla();
                vista.mostrarMensaje("exito", "Contrato eliminado exitosamente.");
            } catch (Exception e) {
                vista.mostrarMensaje("error", "Error al eliminar: " + e.getMessage());
            }
        }
    }
}
