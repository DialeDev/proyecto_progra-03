package views;
import controllers.PlanillaController;
import models.Planilla;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author DialeMRZ
 */
public class FrmPlanillas extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPlanillas.class.getName());

    // controlador
    private final PlanillaController controller;
    
    public FrmPlanillas() {
        initComponents();
        // iniciamos controlador
        this.controller = new PlanillaController();
        //centrar ventana
        this.setLocationRelativeTo(null);
    }

    private void actualizarTabla() {
        // modelo de tabla
        DefaultTableModel modelo = (DefaultTableModel) tblPlanillas.getModel();
        
        // limpiar tabla
        modelo.setRowCount(0); 
        
        // foreach para recorrer los datos
        List<Planilla> historial = controller.listarTodas();
        
        historial.forEach(planilla -> {
            modelo.addRow(new Object[]{
                planilla.getIdPlanilla(),
                planilla.getMes(),
                planilla.getAnio(),
                planilla.getFechaEmision(),
                planilla.getEstado()
            });
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Generar = new javax.swing.JButton();
        txtMes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPlanillas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Ver Detalles");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Emitir Planilla");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Anular Planilla");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Crear Nueva Planilla"));

        jLabel1.setText("Mes:");

        Generar.setText("Generar Planilla");
        Generar.addActionListener(this::GenerarActionPerformed);

        txtMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel2.setText("Año");

        txtAnio.setModel(new javax.swing.SpinnerNumberModel(2020, null, null, 1));
        txtAnio.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Generar)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Generar)
                    .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        tblPlanillas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Planilla", "Mes", "Anio", "Fecha Emision", "Estado"
            }
        ));
        tblPlanillas.setToolTipText("");
        jScrollPane2.setViewportView(tblPlanillas);

        jScrollPane1.setViewportView(jScrollPane2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(40, 40, 40)
                        .addComponent(jButton3)
                        .addGap(45, 45, 45)
                        .addComponent(jButton4))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerarActionPerformed
        try {
            // 1. Capturar el MES
            // getSelectedIndex() devuelve 0 para Enero, 1 para Febrero, etc.
            // Le sumamos 1 para que Enero = 1, Diciembre = 12 (como lo espera la base de datos)
            int mes = txtMes.getSelectedIndex() + 1;
            
            // 2. Capturar el AÑO
            // El JSpinner devuelve un Object que generalmente es un Integer.
            // Primero lo convertimos a String limpio y luego lo parseamos de forma segura.
            int anio = Integer.parseInt(txtAnio.getValue().toString().replace(".", "").replace(",", ""));
            
            // le pasamos al controlador los datos
            controller.generarNuevaPlanilla(mes, anio);
            
            //mensaje de confirmación
            javax.swing.JOptionPane.showMessageDialog(this, "Planilla generada con éxito en estado BORRADOR");
            
            // actualizamos tabla
            actualizarTabla();
            
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, ingresa números válidos para mes y año.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Un catch genérico por si falla otra cosa (ej. si el modelo no puede conectarse)
            javax.swing.JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage(), "Error Crítico", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    
    
    }//GEN-LAST:event_GenerarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Generar;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPlanillas;
    private javax.swing.JSpinner txtAnio;
    private javax.swing.JComboBox<String> txtMes;
    // End of variables declaration//GEN-END:variables
    /*
    MÉTODO MAIN TEMPORAL PARA PROBAR LA VISTA AISLADA
    */
    public static void main(String args[]) {
        // Esto le dice a Java que construya la ventana de forma segura
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Instanciamos tu vista y la hacemos visible
                new FrmPlanillas().setVisible(true);
            }
        });
    }
}