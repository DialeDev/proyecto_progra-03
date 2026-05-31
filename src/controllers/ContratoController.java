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
    private java.util.List<java.util.Map<String, Object>> contrataciones;

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
            dao.RepositorioContratacion repContratacion = new dao.RepositorioContratacion();
            contrataciones = repContratacion.findAllWithDetails();
            DefaultTableModel nuevoModelo = new DefaultTableModel();
            nuevoModelo.addColumn("ID");
            nuevoModelo.addColumn("Empleado"); 
            nuevoModelo.addColumn("Tipo Contrato");   
            nuevoModelo.addColumn("Descripción"); 
            nuevoModelo.addColumn("Estado");      
            
            for (java.util.Map<String, Object> c : contrataciones) {
                String inicioStr = c.get("FechaInicio") != null ? c.get("FechaInicio").toString() : "";
                boolean vigente = (boolean) c.get("Vigente");
                
                nuevoModelo.addRow(new Object[]{
                    c.get("Id"),
                    c.get("Empleado"),
                    c.get("TipoContrato"),
                    "Inicio: " + inicioStr,
                    vigente ? "Activo" : "Inactivo"
                });
            }
            
            vista.getTablaContratos().setModel(nuevoModelo);
            modeloTabla = nuevoModelo;
        } catch (Exception e) {
            vista.mostrarMensaje("error", "Error al cargar: " + e.getMessage());
        }
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
        boolean activo = vista.getChkActivo().isSelected();
        String inicioStr = vista.getTxtFechaInicio().getText().trim();
        String finStr = vista.getTxtFechaFin().getText().trim();
        String salarioStr = vista.getTxtSalario().getText().trim();
        String tipoSeleccionado = vista.getCmbTipoContrato().getSelectedItem().toString();

        if (vista.mostrarConfirmacion("¿Actualizar la información de este contrato?")) {
            try {
                // Parse dates and salary
                java.time.LocalDate fechaInicio = java.time.LocalDate.parse(inicioStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                java.time.LocalDate fechaFin = finStr.isEmpty() ? null : java.time.LocalDate.parse(finStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                double salarioBase = salarioStr.isEmpty() ? 0 : Double.parseDouble(salarioStr);
                double valorHora = salarioBase > 0 ? Math.round(salarioBase / 160.0 * 100.0) / 100.0 : 0;

                // Lookup contract type ID
                models.Contrato tipoContrato = contratoDAO.buscarPorNombre(tipoSeleccionado);
                if (tipoContrato == null) {
                    vista.mostrarMensaje("error", "El tipo de contrato seleccionado no existe.");
                    return;
                }

                // Update using RepositorioContratacion
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
