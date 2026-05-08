package controllers;

import dao.RepositorioContratacion;
import models.Contratacion;
import javax.swing.JOptionPane;
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
            JOptionPane.showMessageDialog(vistaRegistro, "Seleccione empleado y tipo de contrato.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String inicioStr = vistaRegistro.getTxtInicio().getText().trim();
        String finStr = vistaRegistro.getTxtFin().getText().trim();
        String salarioStr = vistaRegistro.getTxtSalario().getText().trim();
        if (inicioStr.isEmpty()) {
            JOptionPane.showMessageDialog(vistaRegistro, "La fecha de inicio es obligatoria (yyyy-MM-dd).", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate fechaInicio;
        try { fechaInicio = LocalDate.parse(inicioStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
        catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vistaRegistro, "Formato inválido. Use yyyy-MM-dd.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate fechaFin = null;
        if (!finStr.isEmpty()) {
            try { fechaFin = LocalDate.parse(finStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
            catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(vistaRegistro, "Formato de fecha fin inválido.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaFin.isBefore(fechaInicio)) {
                JOptionPane.showMessageDialog(vistaRegistro, "La fecha fin no puede ser anterior a la fecha de inicio.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        double salarioBase = 0;
        if (!salarioStr.isEmpty()) {
            try { salarioBase = Double.parseDouble(salarioStr); if (salarioBase < 0) throw new NumberFormatException(); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vistaRegistro, "Salario inválido.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        double valorHora = salarioBase > 0 ? Math.round(salarioBase / 160.0 * 100.0) / 100.0 : 0;
        Contratacion c = new Contratacion(0, idEmpleado, idContrato, fechaInicio, salarioBase, valorHora);
        if (fechaFin != null) c.setFechaFin(fechaFin);
        c.setVigente(true);
        try {
            contratacionDAO.create(c);
            JOptionPane.showMessageDialog(vistaRegistro, "Contratación registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            DefaultTableModel modelo = (DefaultTableModel) vistaPrincipal.getTablaContratos().getModel();
            modelo.addRow(new Object[]{c.getIdContratacion(), vistaRegistro.getCmbEmpleado().getSelectedItem(),
                vistaRegistro.getCmbTipo().getSelectedItem(), inicioStr, finStr.isEmpty() ? "Indefinido" : finStr, salarioStr, "Vigente"});
            vistaRegistro.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaRegistro, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
