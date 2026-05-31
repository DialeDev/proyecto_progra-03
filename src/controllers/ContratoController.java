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
        try {
            java.util.List<Contrato> contratos = contratoDAO.findAll();
            DefaultTableModel nuevoModelo = new DefaultTableModel();
            nuevoModelo.addColumn("ID");
            nuevoModelo.addColumn("Nombre");
            nuevoModelo.addColumn("Tipo");
            nuevoModelo.addColumn("Descripción");
            nuevoModelo.addColumn("Activo");
            contratos.forEach(c -> nuevoModelo.addRow(new Object[]{
                c.getIdContrato(), c.getNombre(), c.getTipo().name(), c.getDescripcion(), c.isActivo() ? "Activo" : "Inactivo"}));
            modeloTabla = nuevoModelo;
            vista.getTablaContratos().setModel(nuevoModelo);
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
        TipoContrato tipo = parsearTipo(tipoStr);
        if (tipo == null) {
            vista.mostrarMensaje("advertencia", "Tipo no válido.");
            return;
        }
        if (contratoDAO.buscarPorNombre(nombre) != null) {
            vista.mostrarMensaje("advertencia", "Ya existe un tipo con el nombre '" + nombre + "'.");
            return;
        }
        try {
            Contrato nuevoContrato = new Contrato(0, nombre, tipo, descripcion);
            nuevoContrato.setActivo(vista.getChkActivo().isSelected());
            contratoDAO.create(nuevoContrato);
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
        TipoContrato tipo = parsearTipo(tipoStr);
        if (tipo == null) {
            vista.mostrarMensaje("advertencia", "Tipo no válido.");
            return;
        }
        if (vista.mostrarConfirmacion("¿Actualizar '" + nombre + "'?")) {
            try {
                Contrato contratoActualizado = new Contrato(id, nombre, tipo, descripcion);
                contratoActualizado.setActivo(vista.getChkActivo().isSelected());
                contratoDAO.update(contratoActualizado);
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

    private TipoContrato parsearTipo(String tipoStr) {
        if ("Fijo".equalsIgnoreCase(tipoStr) || "FIJO".equals(tipoStr)) return TipoContrato.FIJO;
        if ("Por Hora".equalsIgnoreCase(tipoStr) || "POR_HORA".equals(tipoStr)) return TipoContrato.POR_HORA;
        return null;
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
