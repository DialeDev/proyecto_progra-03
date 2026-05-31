package views;

import dao.RepositorioContratacion;
import dao.RepositorioContrato;
import dao.RepositorioEmpleado;
import models.Contratacion;
import models.Contrato;
import models.Empleado;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// @author WinterOS
public class FrmRegistroContratos extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmRegistroContratos.class.getName());
    private final RepositorioEmpleado empleadoDAO;
    private final RepositorioContrato contratoDAO;
    private final RepositorioContratacion contratacionDAO;
    private int[] idsEmpleados;
    private int[] idsContratos;

    public FrmRegistroContratos() {
        this.empleadoDAO = new RepositorioEmpleado();
        this.contratoDAO = new RepositorioContrato();
        this.contratacionDAO = new RepositorioContratacion();
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes();
    }

    private void configurarComponentes() {
        cargarEmpleados();
        cargarTiposContrato();
    }

    private void cargarEmpleados() {
        cmbEmpleado.removeAllItems();
        try {
            List<Empleado> empleados = empleadoDAO.findAll();
            idsEmpleados = new int[empleados.size()];
            int idx = 0;
            for (Empleado e : empleados) {
                if (e.isActivo()) {
                    cmbEmpleado.addItem(e.getNombres() + " " + e.getApellidos() + " - " + e.getDui());
                    idsEmpleados[idx++] = e.getIdEmpleado();
                }
            }
            if (idx < idsEmpleados.length) {
                int[] temp = new int[idx];
                System.arraycopy(idsEmpleados, 0, temp, 0, idx);
                idsEmpleados = temp;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTiposContrato() {
        cmbTipo.removeAllItems();
        try {
            List<Contrato> contratos = contratoDAO.findActive();
            idsContratos = new int[contratos.size()];
            int idx = 0;
            for (Contrato c : contratos) {
                cmbTipo.addItem(c.getNombre());
                idsContratos[idx++] = c.getIdContrato();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -- MVC Messaging Methods --
    public void mostrarMensaje(String tipo, String mensaje) {
        switch (tipo) {
            case "exito" -> JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            case "error" -> JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            case "advertencia" -> JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
            case "info" -> JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public int getEmpleadoSeleccionadoId() {
        int sel = cmbEmpleado.getSelectedIndex();
        return (sel >= 0 && sel < idsEmpleados.length) ? idsEmpleados[sel] : -1;
    }

    public int getContratoSeleccionadoId() {
        int sel = cmbTipo.getSelectedIndex();
        return (sel >= 0 && sel < idsContratos.length) ? idsContratos[sel] : -1;
    }

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
        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Cargando..."}));
        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Cargando..."}));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5).addComponent(jLabel4).addComponent(jLabel6)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(71, 71, 71)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInicio).addComponent(txtFin).addComponent(txtSalario)
                    .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbEmpleado, 0, 220, Short.MAX_VALUE).addComponent(txtObservaciones)).addContainerGap(100, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup().addGap(102, 102, 102).addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnCancelar).addGap(89, 89, 89)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(txtFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addGap(40, 40, 40).addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnGuardar).addComponent(btnCancelar)).addGap(15, 15, 15)));
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

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

    // Getters para compatibilidad con RegistroContratoController
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JTextField getTxtFin() { return txtFin; }
    public JTextField getTxtInicio() { return txtInicio; }
    public JTextField getTxtObservaciones() { return txtObservaciones; }
    public JTextField getTxtSalario() { return txtSalario; }
    public JComboBox<String> getCmbTipo() { return cmbTipo; }
    public JComboBox<String> getCmbEmpleado() { return cmbEmpleado; }
}
