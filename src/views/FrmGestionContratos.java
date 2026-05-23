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

    // Componentes para CRUD de Tipos de Contrato
    private javax.swing.JPanel panelTipoContrato;
    private javax.swing.JLabel lblNombreTipo;
    private javax.swing.JTextField txtNombreTipo;
    private javax.swing.JLabel lblTipoContrato;
    private javax.swing.JComboBox<String> cmbTipoContrato;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JButton btnGuardarTipo;
    private javax.swing.JButton btnActualizarTipo;
    private javax.swing.JButton btnEliminarTipo;
    private javax.swing.JButton btnLimpiarTipo;

    public FrmGestionContratos() {
        initComponents();
        inicializarPanelTiposContrato();
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

    private void inicializarPanelTiposContrato() {
        panelTipoContrato = new javax.swing.JPanel();
        panelTipoContrato.setBorder(javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createEtchedBorder(), "Gestión de Tipos de Contrato",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new java.awt.Font("Segoe UI", 1, 14)));
        lblNombreTipo = new javax.swing.JLabel("Nombre:");
        txtNombreTipo = new javax.swing.JTextField(20);
        lblTipoContrato = new javax.swing.JLabel("Tipo:");
        cmbTipoContrato = new javax.swing.JComboBox<>(new String[]{"FIJO", "POR_HORA"});
        lblDescripcion = new javax.swing.JLabel("Descripción:");
        txtDescripcion = new javax.swing.JTextField(20);
        btnGuardarTipo = new javax.swing.JButton("Guardar");
        btnActualizarTipo = new javax.swing.JButton("Actualizar");
        btnEliminarTipo = new javax.swing.JButton("Eliminar");
        btnLimpiarTipo = new javax.swing.JButton("Limpiar");
        btnGuardarTipo.setBackground(new java.awt.Color(204, 255, 204));
        btnGuardarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnActualizarTipo.setBackground(new java.awt.Color(255, 255, 204));
        btnActualizarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnEliminarTipo.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnLimpiarTipo.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        java.awt.GridBagLayout gbl = new java.awt.GridBagLayout();
        panelTipoContrato.setLayout(gbl);
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(3, 5, 3, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        panelTipoContrato.add(lblNombreTipo, gbc);
        gbc.gridx = 1;
        panelTipoContrato.add(txtNombreTipo, gbc);
        gbc.gridx = 2;
        panelTipoContrato.add(lblTipoContrato, gbc);
        gbc.gridx = 3;
        panelTipoContrato.add(cmbTipoContrato, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panelTipoContrato.add(lblDescripcion, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelTipoContrato.add(txtDescripcion, gbc);
        gbc.gridwidth = 1;
        javax.swing.JPanel panelBotones = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnGuardarTipo);
        panelBotones.add(btnActualizarTipo);
        panelBotones.add(btnEliminarTipo);
        panelBotones.add(btnLimpiarTipo);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        panelTipoContrato.add(panelBotones, gbc);
        gbc.gridwidth = 1;
        javax.swing.GroupLayout layout = (javax.swing.GroupLayout) getContentPane().getLayout();
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addGap(111, 111, 111).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup().addGap(47, 47, 47).addComponent(panelTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(panelTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE)));
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de contratos");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("GESTION CONTRATOS");

        btnNuevo.setText("NUEVO");
        btnNuevo.setName("btnNuevo"); // NOI18N

        btnEliminar.setText("ELIMINAR");

        btnCerrar.setText("CERRAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnNuevo)
                .addGap(116, 116, 116)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnEliminar)
                    .addComponent(btnCerrar))
                .addGap(38, 38, 38))
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>

    private void seleccionarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) return;
        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        txtNombreTipo.setText(model.getValueAt(fila, 1).toString());
        cmbTipoContrato.setSelectedItem(model.getValueAt(fila, 2).toString());
        txtDescripcion.setText(model.getValueAt(fila, 3) != null ? model.getValueAt(fila, 3).toString() : "");
    }

    private void limpiarFormularioTipo() {
        txtNombreTipo.setText("");
        cmbTipoContrato.setSelectedIndex(0);
        txtDescripcion.setText("");
        tablaContratos.clearSelection();
        txtNombreTipo.requestFocus();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmGestionContratos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaContratos;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnCerrar() { return btnCerrar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JTable getTablaContratos() { return tablaContratos; }
}
