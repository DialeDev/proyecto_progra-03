package controllers;

import dao.RepositorioContratacion;
import models.Contratacion;
import javax.swing.table.DefaultTableModel;
import views.FrmRegistroContratos;
import views.FrmGestionContratos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegistroContratoController implements ActionListener {
    private final FrmRegistroContratos vistaRegistro;
    private final FrmGestionContratos vistaPrincipal;
    private final RepositorioContratacion contratacionDAO;

    public RegistroContratoController(FrmRegistroContratos vistaRegistro, FrmGestionContratos vistaPrincipal) {
        this.vistaRegistro = vistaRegistro;
        this.vistaPrincipal = vistaPrincipal;
        this.contratacionDAO = new RepositorioContratacion();
        this.vistaRegistro.getBtnGuardar().addActionListener(this);
        this.vistaRegistro.getBtnCancelar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaRegistro.getBtnGuardar()) {
            guardarContratacion();
        } else if (e.getSource() == vistaRegistro.getBtnCancelar()) {
            vistaRegistro.dispose();
        }
    }

    private void guardarContratacion() {
        int idEmpleado = vistaRegistro.getEmpleadoSeleccionadoId();
        int idContrato = vistaRegistro.getContratoSeleccionadoId();
        if (idEmpleado <= 0 || idContrato <= 0) {
            vistaRegistro.mostrarMensaje("advertencia", "Seleccione empleado y tipo de contrato.");
            return;
        }
        String inicioStr = vistaRegistro.getTxtInicio().getText().trim();
        String finStr = vistaRegistro.getTxtFin().getText().trim();
        String salarioStr = vistaRegistro.getTxtSalario().getText().trim();
        if (inicioStr.isEmpty()) {
            vistaRegistro.mostrarMensaje("advertencia", "La fecha de inicio es obligatoria (yyyy-MM-dd).");
            return;
        }
        LocalDate fechaInicio;
        try { fechaInicio = LocalDate.parse(inicioStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
        catch (DateTimeParseException ex) {
            vistaRegistro.mostrarMensaje("advertencia", "Formato inválido. Use yyyy-MM-dd.");
            return;
        }
        LocalDate fechaFin = null;
        if (!finStr.isEmpty()) {
            try { fechaFin = LocalDate.parse(finStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
            catch (DateTimeParseException ex) {
                vistaRegistro.mostrarMensaje("advertencia", "Formato de fecha fin inválido.");
                return;
            }
            if (fechaFin.isBefore(fechaInicio)) {
                vistaRegistro.mostrarMensaje("advertencia", "La fecha fin no puede ser anterior a la fecha de inicio.");
                return;
            }
        }
        double salarioBase = 0;
        if (!salarioStr.isEmpty()) {
            try { salarioBase = Double.parseDouble(salarioStr); if (salarioBase < 0) throw new NumberFormatException(); }
            catch (NumberFormatException ex) {
                vistaRegistro.mostrarMensaje("advertencia", "Salario inválido.");
                return;
            }
        }
        double valorHora = salarioBase > 0 ? Math.round(salarioBase / 160.0 * 100.0) / 100.0 : 0;
        Contratacion c = new Contratacion(0, idEmpleado, idContrato, fechaInicio, salarioBase, valorHora);
        if (fechaFin != null) c.setFechaFin(fechaFin);
        c.setObservaciones(vistaRegistro.getTxtObservaciones().getText().trim());
        c.setVigente(true);
        try {
            contratacionDAO.create(c);
            vistaRegistro.mostrarMensaje("exito", "Contratación registrada exitosamente.");
            vistaRegistro.dispose();
            
            if (vistaPrincipal.getController() != null) {
                vistaPrincipal.getController().cargarTabla();
            }
        } catch (Exception ex) {
            vistaRegistro.mostrarMensaje("error", "Error al guardar: " + ex.getMessage());
        }
    }
}
