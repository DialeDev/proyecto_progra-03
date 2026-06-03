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
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
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
        // Listeners (ya registrados en initComponents via NetBeans)
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

        lblMes = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox();
        lblAnio = new javax.swing.JLabel();
        spnAnio = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlanillas = new javax.swing.JTable();
        btnGenerar = new javax.swing.JButton();
        btnEmitir = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de Planillas");
        setResizable(false);

        lblMes.setText("Mes:");

        lblAnio.setText("Año:");

        tblPlanillas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Mes", "Año", "Fecha Emisión", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tblPlanillas);

        btnGenerar.setText("Generar Planilla");
        btnGenerar.addActionListener(this::btnGenerarActionPerformed);

        btnEmitir.setText("Emitir");
        btnEmitir.addActionListener(this::btnEmitirActionPerformed);

        btnAnular.setText("Anular");
        btnAnular.addActionListener(this::btnAnularActionPerformed);

        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(this::btnRefrescarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PLANILLAS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMes)
                        .addGap(6, 6, 6)
                        .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAnio)
                        .addGap(6, 6, 6)
                        .addComponent(spnAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEmitir, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMes)
                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnio)
                    .addComponent(spnAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEmitir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
    private javax.swing.JComboBox cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblMes;
    private javax.swing.JSpinner spnAnio;
    private javax.swing.JTable tblPlanillas;
    // End of variables declaration//GEN-END:variables
}
