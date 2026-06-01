package views;

import controllers.ContratoController;
import dao.RepositorioContrato;
import models.Contrato;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FrmGestionContratos extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmGestionContratos.class.getName());
    private ContratoController controller;
    private final RepositorioContrato contratoDAO = new RepositorioContrato();
    private int[] idsContratos;

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
    }
    
    public void setController(ContratoController controller) {
        this.controller = controller;
    }
    
    public ContratoController getController() {
        return this.controller;
    }

    private void configurarComponentes() {
        cargarTiposContrato();
        btnActualizarTipo.addActionListener(e -> {
            int fila = tablaContratos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un tipo.", "Error", JOptionPane.WARNING_MESSAGE); return; }
            int id = (int) ((DefaultTableModel) tablaContratos.getModel()).getValueAt(fila, 0);
            if (controller != null) controller.actualizarTipoContrato(id, txtNombre.getText().trim(), cmbTipoContrato.getSelectedItem().toString(), txtDescripcion.getText().trim());
            limpiarFormularioTipo();
        });
        btnEliminarTipo.addActionListener(e -> {
            int fila = tablaContratos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un tipo.", "Error", JOptionPane.WARNING_MESSAGE); return; }
            DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
            int id = (int) model.getValueAt(fila, 0);
            String nombre = (String) model.getValueAt(fila, 1);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (controller != null) controller.eliminarTipoContrato(id, nombre);
                limpiarFormularioTipo();
            }
        });
        btnLimpiarTipo.addActionListener(e -> limpiarFormularioTipo());
        tablaContratos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarTipoContrato(); }
        });
    }

    private void cargarTiposContrato() {
        cmbTipoContrato.removeAllItems();
        try {
            List<Contrato> contratos = contratoDAO.findActive();
            idsContratos = new int[contratos.size()];
            int idx = 0;
            for (Contrato c : contratos) {
                cmbTipoContrato.addItem(c.getNombre());
                idsContratos[idx++] = c.getIdContrato();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTiposContrato() {
        cargarTiposContrato();
    }

    public int getContratoSeleccionadoId() {
        int sel = cmbTipoContrato.getSelectedIndex();
        return (sel >= 0 && sel < idsContratos.length) ? idsContratos[sel] : -1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaContratos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaTipos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        panelTipoContrato = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoContrato = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnActualizarTipo = new javax.swing.JButton();
        btnEliminarTipo = new javax.swing.JButton();
        btnLimpiarTipo = new javax.swing.JButton();
        chkActivo = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        txtFechaFin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSalario = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        txtDescripcionTipo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        chkActivo1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de contratos");
        setResizable(false);

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

        jTabbedPane3.addTab("Contrataciones", jScrollPane1);

        tablaTipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripcion", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaTipos);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Tipos de Contrato", jPanel5);

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

        panelTipoContrato.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gestión de Tipos de Contrato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        panelTipoContrato.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Nombre");
        panelTipoContrato.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        txtNombre.setColumns(20);
        panelTipoContrato.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabel3.setText("Tipo");
        panelTipoContrato.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        cmbTipoContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fijo", "Por Hora", "Indefinido", " " }));
        cmbTipoContrato.addActionListener(this::cmbTipoContratoActionPerformed);
        panelTipoContrato.add(cmbTipoContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, -1, -1));

        jLabel4.setText("Descripcion");
        panelTipoContrato.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        txtDescripcion.setColumns(20);
        panelTipoContrato.add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 50));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 7));

        btnActualizarTipo.setBackground(new java.awt.Color(255, 255, 204));
        btnActualizarTipo.setText("Actualizar");
        jPanel2.add(btnActualizarTipo);

        btnEliminarTipo.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarTipo.setText("Eliminar");
        jPanel2.add(btnEliminarTipo);

        btnLimpiarTipo.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiarTipo.setText("Limpiar");
        jPanel2.add(btnLimpiarTipo);

        panelTipoContrato.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, 580, 40));

        chkActivo.setText("Activo");
        panelTipoContrato.add(chkActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, -1, -1));

        jLabel5.setText("Fecha inicio");
        panelTipoContrato.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, -1, -1));
        panelTipoContrato.add(txtFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 140, -1));
        panelTipoContrato.add(txtFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 140, -1));

        jLabel6.setText("Fecha Fin");
        panelTipoContrato.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, -1, -1));

        jLabel7.setText("Salario");
        panelTipoContrato.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, -1, -1));
        panelTipoContrato.add(txtSalario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, 80, -1));

        jTabbedPane2.addTab("Datos", panelTipoContrato);

        jLabel8.setText("Nombre Tipo");

        jLabel9.setText("Descripcion");

        chkActivo1.setText("Activo");
        chkActivo1.addActionListener(this::chkActivo1ActionPerformed);

        btnBorrar.setText("Borrar");
        jPanel4.add(btnBorrar);

        btnEditar.setText("Editar");
        jPanel4.add(btnEditar);

        btnGuardar.setText("Guardar");
        jPanel4.add(btnGuardar);

        btnLimpiar.setText("Limpiar");
        jPanel4.add(btnLimpiar);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtDescripcionTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)))
                .addComponent(chkActivo1)
                .addGap(193, 193, 193))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkActivo1)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtDescripcionTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jTabbedPane2.addTab("Crear Tipo", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(39, Short.MAX_VALUE)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(339, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(405, Short.MAX_VALUE)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTipoContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoContratoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoContratoActionPerformed

    private void chkActivo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivo1ActionPerformed

    private void seleccionarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) return;
        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        int id = (int) model.getValueAt(fila, 0);

        txtNombre.setText(model.getValueAt(fila, 2).toString());
        txtNombre.setEditable(false);
        chkActivo.setSelected("Activo".equals(model.getValueAt(fila, 4)));

        // Seleccionar el tipo de contrato correcto en el combobox
        String tipoContrato = model.getValueAt(fila, 2).toString();
        cmbTipoContrato.setSelectedItem(tipoContrato);

        // Delegar la carga de FechaInicio, FechaFin y Salario al controlador
        if (controller != null) controller.cargarDetallesContratacion(id);
        
        // Poner la observación real en txtDescripcion
        txtDescripcion.setText(model.getValueAt(fila, 3) != null ? model.getValueAt(fila, 3).toString() : "");
    }

    private void limpiarFormularioTipo() {
        txtNombre.setText("");
        txtNombre.setEditable(true);
        cmbTipoContrato.setSelectedIndex(0);
        txtDescripcion.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtSalario.setText("");
        chkActivo.setSelected(true);
        tablaContratos.clearSelection();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarTipo;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarTipo;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiarTipo;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JCheckBox chkActivo1;
    private javax.swing.JComboBox<String> cmbTipoContrato;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panelTipoContrato;
    private javax.swing.JTable tablaContratos;
    private javax.swing.JTable tablaTipos;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDescripcionTipo;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnCerrar() { return btnCerrar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JTable getTablaContratos() { return tablaContratos; }
    public JTable getTablaTipos() { return tablaTipos; }
    public javax.swing.JCheckBox getChkActivo() { return chkActivo; }
    public javax.swing.JComboBox<String> getCmbTipoContrato() { return cmbTipoContrato; }
    public JTextField getTxtFechaInicio() { return txtFechaInicio; }
    public JTextField getTxtFechaFin() { return txtFechaFin; }
    public JTextField getTxtSalario() { return txtSalario; }

    // Getters para la pestaña Crear Tipo
    public JTextField getTxtNombreTipo() { return jTextField1; }
    public JTextField getTxtDescripcionTipo() { return txtDescripcionTipo; }
    public javax.swing.JCheckBox getChkActivo1() { return chkActivo1; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnBorrar() { return btnBorrar; }
}
