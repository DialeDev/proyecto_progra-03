/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import dao.RepositorioContratacion;
import dao.RepositorioContrato;
import dao.RepositorioEmpleado;
import models.Contratacion;
import models.Contrato;
import models.Empleado;

import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Formulario para registrar una contratación (asignar contrato a un empleado).
 * @author WinterOS
 */
public class FrmRegistroContratos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmRegistroContratos.class.getName());

    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioContrato contratoDAO;
    private final RepositorioContratacion contratacionDAO;

    // Arreglos para mapear nombres a IDs
    private int[] idsEmpleados;
    private int[] idsContratos;

    /**
     * Creates new form FrmRegistroContratos
     */
    public FrmRegistroContratos() {
        this.empleadoDAO = new RepositorioEmpleado();
        this.contratoDAO = new RepositorioContrato();
        this.contratacionDAO = new RepositorioContratacion();

        initComponents();

        // Cargar datos desde la BD
        cargarEmpleados();
        cargarTiposContrato();
    }

    /**
     * Carga la lista de empleados activos desde la BD en el combo box.
     */
    private void cargarEmpleados() {
        cmbEmpleado.removeAllItems();
        try {
            List<Empleado> empleados = empleadoDAO.findAll();
            idsEmpleados = new int[empleados.size()];
            int idx = 0;
            for (Empleado e : empleados) {
                if (e.isActivo()) {
                    cmbEmpleado.addItem(e.getNombres() + " " + e.getApellidos() + " - " + e.getDui());
                    idsEmpleados[idx] = e.getIdEmpleado();
                    idx++;
                }
            }
            // Redimensionar si hay inactivos
            if (idx < idsEmpleados.length) {
                int[] temp = new int[idx];
                System.arraycopy(idsEmpleados, 0, temp, 0, idx);
                idsEmpleados = temp;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar empleados: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga la lista de tipos de contrato desde la BD en el combo box.
     */
    private void cargarTiposContrato() {
        cmbTipo.removeAllItems();
        try {
            List<Contrato> contratos = contratoDAO.findAll();
            idsContratos = new int[contratos.size()];
            int idx = 0;
            for (Contrato c : contratos) {
                cmbTipo.addItem(c.getNombre());
                idsContratos[idx] = c.getIdContrato();
                idx++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar tipos de contrato: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene el ID del empleado seleccionado.
     */
    private int getEmpleadoSeleccionadoId() {
        int sel = cmbEmpleado.getSelectedIndex();
        if (sel >= 0 && sel < idsEmpleados.length) {
            return idsEmpleados[sel];
        }
        return -1;
    }

    /**
     * Obtiene el ID del tipo de contrato seleccionado.
     */
    private int getContratoSeleccionadoId() {
        int sel = cmbTipo.getSelectedIndex();
        if (sel >= 0 && sel < idsContratos.length) {
            return idsContratos[sel];
        }
        return -1;
    }

    /**
     * Guarda la contratación en la BD.
     */
    private void guardarContratacion() {
        // Validar selecciones
        if (cmbEmpleado.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cmbTipo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de contrato.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEmpleado = getEmpleadoSeleccionadoId();
        int idContrato = getContratoSeleccionadoId();

        if (idEmpleado == -1 || idContrato == -1) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos seleccionados.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar fechas
        String inicioStr = txtInicio.getText().trim();
        String finStr = txtFin.getText().trim();

        if (inicioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio es obligatoria (formato: yyyy-MM-dd).",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtInicio.requestFocus();
            return;
        }

        LocalDate fechaInicio;
        try {
            fechaInicio = LocalDate.parse(inicioStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha de inicio inválido. Use: yyyy-MM-dd (ej: 2025-01-15).",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtInicio.requestFocus();
            return;
        }

        LocalDate fechaFin = null;
        if (!finStr.isEmpty()) {
            try {
                fechaFin = LocalDate.parse(finStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha de fin inválido. Use: yyyy-MM-dd (ej: 2025-12-31).",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                txtFin.requestFocus();
                return;
            }

            // Validar que fecha fin sea después de fecha inicio
            if (fechaFin.isBefore(fechaInicio)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha de fin no puede ser anterior a la fecha de inicio.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                txtFin.requestFocus();
                return;
            }
        }

        // Validar salario
        String salarioStr = txtSalario.getText().trim();
        double salarioBase = 0;
        if (!salarioStr.isEmpty()) {
            try {
                salarioBase = Double.parseDouble(salarioStr);
                if (salarioBase < 0) {
                    JOptionPane.showMessageDialog(this, "El salario no puede ser negativo.",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    txtSalario.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El salario debe ser un valor numérico válido.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                txtSalario.requestFocus();
                return;
            }
        }

        // Crear y guardar la contratación
        try {
            // Calcular valor por hora (aproximado) basado en salario mensual / 160 horas mensuales
            double valorHora = salarioBase > 0 ? Math.round(salarioBase / 160.0 * 100.0) / 100.0 : 0;

            Contratacion contratacion = new Contratacion(
                0, idEmpleado, idContrato, fechaInicio, salarioBase, valorHora
            );
            if (fechaFin != null) {
                contratacion.setFechaFin(fechaFin);
            }
            contratacion.setVigente(true);

            contratacionDAO.create(contratacion);

            JOptionPane.showMessageDialog(this,
                "Contratación registrada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la contratación: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtInicio = new javax.swing.JTextField();
        txtFin = new javax.swing.JTextField();
        txtSalario = new javax.swing.JTextField();
        txtObservaciones = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cmbTipo = new javax.swing.JComboBox<>();
        cmbEmpleado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrar Contratación");

        jLabel1.setText("Empleado");

        jLabel2.setText("Tipo de Contrato");

        jLabel3.setText("Fecha Inicio (yyyy-MM-dd)");

        jLabel4.setText("Fecha Fin (yyyy-MM-dd)");

        jLabel5.setText("Salario Base ($)");

        jLabel6.setText("Observaciones");

        txtInicio.setToolTipText("Formato: yyyy-MM-dd (ej: 2025-01-15)");

        txtFin.setToolTipText("Formato: yyyy-MM-dd (ej: 2025-12-31)");

        txtSalario.setToolTipText("Ej: 1200.50");

        btnGuardar.setText("GUARDAR");

        btnCancelar.setText("CANCELAR");

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));

        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(71, 71, 71)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInicio)
                    .addComponent(txtFin)
                    .addComponent(txtSalario)
                    .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbEmpleado, 0, 220, Short.MAX_VALUE)
                    .addComponent(txtObservaciones))
                .addContainerGap(100, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(89, 89, 89))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento del combo box de tipo (se mantiene para compatibilidad).
     */
    private void cmbTipoActionPerformed(java.awt.event.ActionEvent evt) {
        // No se requiere acción adicional
    }

    /**
     * Evento del botón Guardar: valida campos y guarda en BD.
     */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        guardarContratacion();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FrmRegistroContratos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtFin;
    private javax.swing.JTextField txtInicio;
    private javax.swing.JTextField txtObservaciones;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables

    // =========================================================================
    //  GETTERS (para compatibilidad con RegistroContratoController)
    // =========================================================================

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JTextField getTxtFin() {
        return txtFin;
    }

    public JTextField getTxtInicio() {
        return txtInicio;
    }

    public JTextField getTxtObservaciones() {
        return txtObservaciones;
    }

    public JTextField getTxtSalario() {
        return txtSalario;
    }

    public static Logger getLogger() {
        return logger;
    }

    public JComboBox<String> getCmbTipo() {
        return cmbTipo;
    }

    public JComboBox<String> getCmbEmpleado() {
        return cmbEmpleado;
    }
}
