package controllers;

import dao.RepositorioContrato;
import dao.RepositorioContratacion;
import models.Contrato;
import utils.SesionGlobal;
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
    private java.util.List<java.util.Map<String, Object>> contrataciones;

    public ContratoController(FrmGestionContratos vista) {
        this.vista = vista;
        this.vista.setController(this);
        this.contratoDAO = new RepositorioContrato();
        this.contratacionDAO = new RepositorioContratacion();
        this.vista.getBtnNuevo().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnCerrar().addActionListener(this);
        
        // Listeners para la pestaña Crear Tipo
        this.vista.getBtnGuardar().addActionListener(this);
        this.vista.getBtnLimpiar().addActionListener(this);
        this.vista.getBtnEditar().addActionListener(this);
        this.vista.getBtnBorrar().addActionListener(this);
        
        // Listener para seleccionar fila en tablaTipos
        this.vista.getTablaTipos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarTipoDesdeTablaTipos();
            }
        });
        
        iniciarTabla();
        cargarTabla();
        cargarTablaTipos();
    }

    public void iniciarTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Empleado");
        modeloTabla.addColumn("Tipo Contrato");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Estado");
        vista.getTablaContratos().setModel(modeloTabla);
    }

    public void cargarTabla() {
        try {
            dao.RepositorioContratacion repContratacion = new dao.RepositorioContratacion();
            contrataciones = repContratacion.findAllWithDetails();
            DefaultTableModel nuevoModelo = new DefaultTableModel();
            nuevoModelo.addColumn("ID");
            nuevoModelo.addColumn("Empleado"); 
            nuevoModelo.addColumn("Tipo Contrato");   
            nuevoModelo.addColumn("Descripción"); 
            nuevoModelo.addColumn("Estado");      
            
            for (java.util.Map<String, Object> c : contrataciones) {
                boolean vigente = (boolean) c.get("Vigente");
                
                nuevoModelo.addRow(new Object[]{
                    c.get("Id"),
                    c.get("Empleado"),
                    c.get("TipoContrato"),
                    c.get("Observaciones"),
                    vigente ? "Activo" : "Inactivo"
                });
            }
            
            vista.getTablaContratos().setModel(nuevoModelo);
            modeloTabla = nuevoModelo;
        } catch (Exception e) {
            vista.mostrarMensaje("error", "Error al cargar: " + e.getMessage());
        }
    }

    private void cargarTablaTipos() {
        try {
            List<Contrato> contratos = contratoDAO.findAll();
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Descripción");
            modelo.addColumn("Activo");
            
            for (Contrato c : contratos) {
                modelo.addRow(new Object[]{
                    c.getIdContrato(),
                    c.getNombre(),
                    c.getDescripcion(),
                    c.isActivo() ? "Activo" : "Inactivo"
                });
            }
            vista.getTablaTipos().setModel(modelo);
        } catch (Exception e) {
            vista.mostrarMensaje("error", "Error al cargar tipos: " + e.getMessage());
        }
    }

    private void seleccionarTipoDesdeTablaTipos() {
        int fila = vista.getTablaTipos().getSelectedRow();
        if (fila == -1) return;
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaTipos().getModel();
        
        vista.getTxtNombreTipo().setText(modelo.getValueAt(fila, 1).toString());
        String desc = modelo.getValueAt(fila, 2) != null ? modelo.getValueAt(fila, 2).toString() : "";
        vista.getTxtDescripcionTipo().setText(desc);
        vista.getChkActivo1().setSelected("Activo".equals(modelo.getValueAt(fila, 3)));
    }

    public void cargarDetallesContratacion(int id) {
        if (contrataciones == null) return;
        for (java.util.Map<String, Object> c : contrataciones) {
            if ((int) c.get("Id") == id) {
                vista.getTxtFechaInicio().setText(c.get("FechaInicio") != null ? c.get("FechaInicio").toString() : "");
                vista.getTxtFechaFin().setText(c.get("FechaFin") != null ? c.get("FechaFin").toString() : "");
                vista.getTxtSalario().setText(c.get("Salario") != null ? c.get("Salario").toString() : "");
                break;
            }
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
        } else if (e.getSource() == vista.getBtnGuardar()) {
            guardarTipoContratoNuevo();
        } else if (e.getSource() == vista.getBtnLimpiar()) {
            limpiarFormularioNuevoTipo();
        } else if (e.getSource() == vista.getBtnEditar()) {
            editarTipoContrato();
        } else if (e.getSource() == vista.getBtnBorrar()) {
            borrarTipoContrato();
        }
    }

    private void limpiarFormularioNuevoTipo() {
        vista.getTxtNombreTipo().setText("");
        vista.getTxtDescripcionTipo().setText("");
        vista.getChkActivo1().setSelected(true); // Mejor UX: Activo por defecto
        vista.getTablaTipos().clearSelection();
    }

    private void guardarTipoContratoNuevo() {
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        String nombre = vista.getTxtNombreTipo().getText().trim();
        String descripcion = vista.getTxtDescripcionTipo().getText().trim();
        boolean activo = vista.getChkActivo1().isSelected();

        if (nombre.isEmpty()) {
            vista.mostrarMensaje("advertencia", "El nombre es obligatorio.");
            return;
        }
        if (contratoDAO.buscarPorNombre(nombre) != null) {
            vista.mostrarMensaje("advertencia", "Ya existe un tipo con el nombre '" + nombre + "'.");
            return;
        }
        try {
            Contrato nuevoContrato = new Contrato(0, nombre, descripcion);
            nuevoContrato.setActivo(activo);
            contratoDAO.create(nuevoContrato);
            vista.mostrarMensaje("exito", "Tipo de contrato guardado exitosamente.");
            vista.recargarTiposContrato();
            cargarTablaTipos();
            limpiarFormularioNuevoTipo();
        } catch (Exception ex) {
            vista.mostrarMensaje("error", "Error: " + ex.getMessage());
        }
    }

    private void editarTipoContrato() {
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        int fila = vista.getTablaTipos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("advertencia", "Seleccione un tipo de contrato de la tabla.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaTipos().getModel();
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = vista.getTxtNombreTipo().getText().trim();
        String descripcion = vista.getTxtDescripcionTipo().getText().trim();
        boolean activo = vista.getChkActivo1().isSelected();

        if (nombre.isEmpty()) {
            vista.mostrarMensaje("advertencia", "El nombre es obligatorio.");
            return;
        }
        
        models.Contrato existente = contratoDAO.buscarPorNombre(nombre);
        if (existente != null && existente.getIdContrato() != id) {
            vista.mostrarMensaje("advertencia", "Ya existe otro tipo de contrato con el nombre '" + nombre + "'.");
            return;
        }

        if (vista.mostrarConfirmacion("¿Actualizar el tipo de contrato '" + nombre + "'?")) {
            try {
                Contrato contrato = new Contrato(id, nombre, descripcion);
                contrato.setActivo(activo);
                contratoDAO.update(contrato);
                vista.mostrarMensaje("exito", "Tipo de contrato actualizado exitosamente.");
                vista.recargarTiposContrato();
                cargarTablaTipos();
                limpiarFormularioNuevoTipo();
            } catch (Exception ex) {
                vista.mostrarMensaje("error", "Error: " + ex.getMessage());
            }
        }
    }

    private void borrarTipoContrato() {
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        int fila = vista.getTablaTipos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("advertencia", "Seleccione un tipo de contrato de la tabla.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaTipos().getModel();
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1).toString();

        if (vista.mostrarConfirmacion("¿Eliminar el tipo '" + nombre + "'? Se desactivará (borrado lógico).")) {
            try {
                contratoDAO.delete(id);
                vista.mostrarMensaje("exito", "Tipo de contrato eliminado exitosamente.");
                vista.recargarTiposContrato();
                cargarTablaTipos();
                limpiarFormularioNuevoTipo();
            } catch (Exception ex) {
                vista.mostrarMensaje("error", "Error: " + ex.getMessage());
            }
        }
    }

    public void actualizarTipoContrato(int id, String nombre, String tipoStr, String descripcion) {
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        boolean activo = vista.getChkActivo().isSelected();
        String inicioStr = vista.getTxtFechaInicio().getText().trim();
        String finStr = vista.getTxtFechaFin().getText().trim();
        String salarioStr = vista.getTxtSalario().getText().trim();
        String tipoSeleccionado = vista.getCmbTipoContrato().getSelectedItem().toString();

        if (vista.mostrarConfirmacion("¿Actualizar la información de este contrato?")) {
            try {
                java.time.LocalDate fechaInicio = java.time.LocalDate.parse(inicioStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                java.time.LocalDate fechaFin = finStr.isEmpty() ? null : java.time.LocalDate.parse(finStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                double salarioBase = salarioStr.isEmpty() ? 0 : Double.parseDouble(salarioStr);
                double valorHora = salarioBase > 0 ? Math.round(salarioBase / 160.0 * 100.0) / 100.0 : 0;

                models.Contrato tipoContrato = contratoDAO.buscarPorNombre(tipoSeleccionado);
                if (tipoContrato == null) {
                    vista.mostrarMensaje("error", "El tipo de contrato seleccionado no existe.");
                    return;
                }

                dao.RepositorioContratacion rep = new dao.RepositorioContratacion();
                models.Contratacion c = new models.Contratacion(id, 0, tipoContrato.getIdContrato(), fechaInicio, salarioBase, valorHora);
                c.setFechaFin(fechaFin);
                c.setVigente(activo);
                
                rep.update(c);
                vista.mostrarMensaje("exito", "Contrato actualizado exitosamente.");
                cargarTabla();
            } catch (java.time.format.DateTimeParseException ex) {
                vista.mostrarMensaje("error", "Formato de fecha inválido. Use yyyy-MM-dd.");
            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("error", "Salario inválido.");
            } catch (Exception ex) {
                vista.mostrarMensaje("error", "Error: " + ex.getMessage());
            }
        }
    }

    public void eliminarTipoContrato(int id, String nombre) {
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
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
        if (!SesionGlobal.puedeOperar("Contratos")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        int fila = vista.getTablaContratos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("advertencia", "Seleccione un contrato.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaContratos().getModel();
        int id = (int) modelo.getValueAt(fila, 0);
        if (vista.mostrarConfirmacion("¿Eliminar el contrato seleccionado?")) {
            try {
                dao.RepositorioContratacion repContratacion = new dao.RepositorioContratacion();
                repContratacion.delete(id);
                cargarTabla();
                vista.mostrarMensaje("exito", "Contrato eliminado exitosamente.");
            } catch (Exception e) {
                vista.mostrarMensaje("error", "Error al eliminar: " + e.getMessage());
            }
        }
    }
}
