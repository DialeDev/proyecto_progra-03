package views;

import controllers.ContratoController;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmGestionContratos extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmGestionContratos.class.getName());
    private ContratoController controller;

    // -- MVC Messaging Methods --
    public void mostrarMensaje(String tipo, String mensaje) {
        switch (tipo) {
            case "exito" -> JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            case "error" -> JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            case "advertencia" -> JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
            case "info" -> JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean mostrarConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public FrmGestionContratos() {
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes();
        this.controller = new ContratoController(this);
    }

    private void configurarComponentes() {
        btnGuardarTipo.addActionListener(e -> {
            controller.guardarTipoContrato(txtNombreTipo.getText().trim(), cmbTipoContrato.getSelectedItem().toString(), txtDescripcion.getText().trim());
            limpiarFormularioTipo();
        });
        btnActualizarTipo.addActionListener(e -> {
            int fila = tablaContratos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un tipo.", "Error", JOptionPane.WARNING_MESSAGE); return; }
            int id = (int) ((DefaultTableModel) tablaContratos.getModel()).getValueAt(fila, 0);
            controller.actualizarTipoContrato(id, txtNombreTipo.getText().trim(), cmbTipoContrato.getSelectedItem().toString(), txtDescripcion.getText().trim());
            limpiarFormularioTipo();
        });
        btnEliminarTipo.addActionListener(e -> {
            int fila = tablaContratos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un tipo.", "Error", JOptionPane.WARNING_MESSAGE); return; }
            DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
            int id = (int) model.getValueAt(fila, 0);
            String nombre = (String) model.getValueAt(fila, 1);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                controller.eliminarTipoContrato(id, nombre);
                limpiarFormularioTipo();
            }
        });
        btnLimpiarTipo.addActionListener(e -> limpiarFormularioTipo());
        tablaContratos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarTipoContrato(); }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaContratos = new javax.swing.JTable();
        panelTipoContrato = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreTipo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoContrato = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnGuardarTipo = new javax.swing.JButton();
        btnActualizarTipo = new javax.swing.JButton();
        btnEliminarTipo = new javax.swing.JButton();
        btnLimpiarTipo = new javax.swing.JButton();
        chkActivo = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de contratos");
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GESTION CONTRATOS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 770, 63));

        btnNuevo.setText("NUEVO");
        btnNuevo.setName("btnNuevo"); // NOI18N
        jPanel1.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        btnEliminar.setText("ELIMINAR");
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

        btnCerrar.setText("CERRAR");
        jPanel1.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, -1, -1));

        tablaContratos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Tipo", "Descripción", "Activo"
            }
        ));
        jScrollPane1.setViewportView(tablaContratos);

        panelTipoContrato.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gestión de Tipos de Contrato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        panelTipoContrato.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Nombre");
        panelTipoContrato.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        txtNombreTipo.setColumns(20);
        panelTipoContrato.add(txtNombreTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabel3.setText("Tipo");
        panelTipoContrato.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        cmbTipoContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fijo", "Por Hora" }));
        cmbTipoContrato.addActionListener(this::cmbTipoContratoActionPerformed);
        panelTipoContrato.add(cmbTipoContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, -1, -1));

        jLabel4.setText("Descripcion");
        panelTipoContrato.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, -1, -1));

        txtDescripcion.setColumns(20);
        panelTipoContrato.add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, -1, 50));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 7));

        btnGuardarTipo.setBackground(new java.awt.Color(204, 255, 204));
        btnGuardarTipo.setText("Guardar");
        jPanel2.add(btnGuardarTipo);

        btnActualizarTipo.setBackground(new java.awt.Color(255, 255, 204));
        btnActualizarTipo.setText("Actualizar");
        jPanel2.add(btnActualizarTipo);

        btnEliminarTipo.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarTipo.setText("Eliminar");
        jPanel2.add(btnEliminarTipo);

        btnLimpiarTipo.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiarTipo.setText("Limpiar");
        jPanel2.add(btnLimpiarTipo);

        panelTipoContrato.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 580, 60));

        chkActivo.setText("Activo");
        panelTipoContrato.add(chkActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                    .addComponent(panelTipoContrato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(panelTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTipoContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoContratoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoContratoActionPerformed

    private void seleccionarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) return;
        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        txtNombreTipo.setText(model.getValueAt(fila, 1).toString());
        cmbTipoContrato.setSelectedItem(model.getValueAt(fila, 2).toString());
        txtDescripcion.setText(model.getValueAt(fila, 3) != null ? model.getValueAt(fila, 3).toString() : "");
        chkActivo.setSelected("Activo".equals(model.getValueAt(fila, 4)));
    }

    private void limpiarFormularioTipo() {
        txtNombreTipo.setText("");
        cmbTipoContrato.setSelectedIndex(0);
        txtDescripcion.setText("");
        chkActivo.setSelected(true);
        tablaContratos.clearSelection();
        txtNombreTipo.requestFocus();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarTipo;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarTipo;
    private javax.swing.JButton btnGuardarTipo;
    private javax.swing.JButton btnLimpiarTipo;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JComboBox<String> cmbTipoContrato;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelTipoContrato;
    private javax.swing.JTable tablaContratos;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombreTipo;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnCerrar() { return btnCerrar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JTable getTablaContratos() { return tablaContratos; }
    public javax.swing.JCheckBox getChkActivo() { return chkActivo; }
}
