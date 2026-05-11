package views;

import controllers.PlanillaController;
import models.Planilla;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// @author DialeMRZ
public class FrmPlanillas extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPlanillas.class.getName());
    private final PlanillaController controller;

    public FrmPlanillas() {
        this.controller = new PlanillaController();
        initComponents();
        configurarComponentes();
        this.setLocationRelativeTo(null);
    }

    private void configurarComponentes() {
        // Inicializar combo de meses
        cmbMes.removeAllItems();
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        for (String m : meses) cmbMes.addItem(m);
        cmbMes.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);
        // Spinner de ano
        spnAnio.setModel(new javax.swing.SpinnerNumberModel(
            java.time.LocalDate.now().getYear(), 2020, 2035, 1));
        // Listeners
        btnGenerar.addActionListener(this::btnGenerarActionPerformed);
        btnEmitir.addActionListener(this::btnEmitirActionPerformed);
        btnAnular.addActionListener(this::btnAnularActionPerformed);
        btnRefrescar.addActionListener(this::btnRefrescarActionPerformed);
        actualizarTabla();
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tblPlanillas.getModel();
        modelo.setRowCount(0);
        controller.listarTodas().forEach(p -> modelo.addRow(new Object[]{
            p.getIdPlanilla(), p.getMes(), p.getAnio(), p.getFechaEmision(), p.getEstado()}));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        lblAnio = new javax.swing.JLabel();
        spnAnio = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlanillas = new javax.swing.JTable();
        btnGenerar = new javax.swing.JButton();
        btnEmitir = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gesti\u00f3n de Planillas");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("PLANILLAS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(51, 51, 51)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(275, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)));

        lblMes.setText("Mes:");
        lblAnio.setText("A\u00f1o:");

        tblPlanillas.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {"ID", "Mes", "A\u00f1o", "Fecha Emisi\u00f3n", "Estado"}));
        jScrollPane1.setViewportView(tblPlanillas);

        btnGenerar.setText("Generar Planilla");
        btnEmitir.setText("Emitir");
        btnAnular.setText("Anular");
        btnRefrescar.setText("Refrescar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(111, 111, 111).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(lblMes).addGap(6, 6, 6)
                .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
                .addComponent(lblAnio).addGap(6, 6, 6)
                .addComponent(spnAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(btnGenerar).addGap(18, 18, 18)
                .addComponent(btnEmitir).addGap(18, 18, 18).addComponent(btnAnular).addGap(18, 18, 18).addComponent(btnRefrescar)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMes).addComponent(cmbMes).addComponent(lblAnio).addComponent(spnAnio))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerar).addComponent(btnEmitir).addComponent(btnAnular).addComponent(btnRefrescar))
                .addContainerGap(23, Short.MAX_VALUE)));
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int mes = cmbMes.getSelectedIndex() + 1; // 1-12
            int anio = ((Number) spnAnio.getValue()).intValue();
            String resultado = controller.generarNuevaPlanilla(mes, anio);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, resultado, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            logger.severe("Error al generar planilla: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al generar planilla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblPlanillas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una planilla para emitir.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = ((Number) tblPlanillas.getModel().getValueAt(fila, 0)).intValue();
            controller.emitirPlanilla(id);
            actualizarTabla();
        } catch (Exception ex) {
            logger.severe("Error al emitir planilla: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al emitir planilla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblPlanillas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una planilla para anular.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = ((Number) tblPlanillas.getModel().getValueAt(fila, 0)).intValue();
            controller.anularPlanilla(id);
            actualizarTabla();
        } catch (Exception ex) {
            logger.severe("Error al anular planilla: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al anular planilla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) { actualizarTabla(); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblMes;
    private javax.swing.JSpinner spnAnio;
    private javax.swing.JTable tblPlanillas;
    // End of variables declaration//GEN-END:variables
}
