package views;

import controllers.PlanillaController;
import models.Planilla;
import java.util.List;
import javax.swing.table.DefaultTableModel;

// @author DialeMRZ
public class FrmPlanillas extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPlanillas.class.getName());
    private final PlanillaController controller;

    public FrmPlanillas() {
        initComponents();
        this.controller = new PlanillaController();
        this.setLocationRelativeTo(null);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlanillas = new javax.swing.JTable();
        btnGenerar = new javax.swing.JButton();
        btnEmitir = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Planillas");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("PLANILLAS");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(51, 51, 51)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(275, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)));
        tblPlanillas.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {"ID", "Mes", "Año", "Fecha Emisión", "Estado"}));
        jScrollPane1.setViewportView(tblPlanillas);
        btnGenerar.setText("Generar Planilla");
        btnGenerar.addActionListener(this::btnGenerarActionPerformed);
        btnEmitir.setText("Emitir");
        btnEmitir.addActionListener(this::btnEmitirActionPerformed);
        btnAnular.setText("Anular");
        btnAnular.addActionListener(this::btnAnularActionPerformed);
        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(this::btnRefrescarActionPerformed);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(111, 111, 111).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(btnGenerar).addGap(18, 18, 18)
                .addComponent(btnEmitir).addGap(18, 18, 18).addComponent(btnAnular).addGap(18, 18, 18).addComponent(btnRefrescar)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerar).addComponent(btnEmitir).addComponent(btnAnular).addComponent(btnRefrescar)).addContainerGap(23, Short.MAX_VALUE)));
        pack();
    }

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {
        controller.generarNuevaPlanilla(5, 2025);
        actualizarTabla();
    }
    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblPlanillas.getSelectedRow();
        if (fila >= 0) {
            int id = (int) tblPlanillas.getModel().getValueAt(fila, 0);
            controller.emitirPlanilla(id);
            actualizarTabla();
        }
    }
    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblPlanillas.getSelectedRow();
        if (fila >= 0) {
            int id = (int) tblPlanillas.getModel().getValueAt(fila, 0);
            controller.anularPlanilla(id);
            actualizarTabla();
        }
    }
    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) { actualizarTabla(); }

    // Variables declaration - do not modify
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPlanillas;
}
