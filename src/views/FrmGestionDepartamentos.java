package views;

import controllers.DepartamentoController;
import models.Departamento;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// @author DialeMRZ
public class FrmGestionDepartamentos extends javax.swing.JFrame {
    private DepartamentoController controller;
    private boolean editando = false;

    public FrmGestionDepartamentos() {
        this.controller = new DepartamentoController();
        initComponents();
        configurarComponentes();
    }

    private void guardarDepartamento() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del departamento es obligatorio.");
                return;
            }
            boolean activo = chkActivo.isSelected();
            if (editando) {
                int fila = tblDepartamentos.getSelectedRow();
                if (fila == -1) { editando = false; btnGuardar.setText("Guardar"); return; }
                DefaultTableModel model = (DefaultTableModel) tblDepartamentos.getModel();
                int id = ((Number) model.getValueAt(fila, 0)).intValue();
                if (JOptionPane.showConfirmDialog(this, "¿Actualizar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, controller.actualizar(new Departamento(id, nombre, activo)));
                }
            } else {
                JOptionPane.showMessageDialog(this, controller.guardar(new Departamento(0, nombre, activo)));
            }
            cargarTabla();
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarDepartamento() {
        try {
            int fila = tblDepartamentos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un departamento."); return; }
            DefaultTableModel model = (DefaultTableModel) tblDepartamentos.getModel();
            int id = ((Number) model.getValueAt(fila, 0)).intValue();
            String nombre = (String) model.getValueAt(fila, 1);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, controller.eliminar(id));
                cargarTabla();
                limpiar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar departamento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarDepartamento() {
        if (tblDepartamentos.getSelectedRow() == -1) return;
        DefaultTableModel model = (DefaultTableModel) tblDepartamentos.getModel();
        int fila = tblDepartamentos.getSelectedRow();
        txtNombre.setText(String.valueOf(model.getValueAt(fila, 1)));
        chkActivo.setSelected(Boolean.parseBoolean(String.valueOf(model.getValueAt(fila, 2))));
        editando = true;
        btnGuardar.setText("Actualizar");
    }

    private void cargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID"); modelo.addColumn("Nombre"); modelo.addColumn("Activo");
        controller.listar().forEach(d -> modelo.addRow(new Object[]{
            d.getIdDepartamento(), d.getNombre(), d.isActivo()}));
        tblDepartamentos.setModel(modelo);
    }

    private void limpiar() {
        txtNombre.setText(""); chkActivo.setSelected(false);
        tblDepartamentos.clearSelection(); editando = false; btnGuardar.setText("Guardar"); txtNombre.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        txtNombre = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        label1 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDepartamentos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        label1.setText("Nombre");

        chkActivo.setText("Activo");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        tblDepartamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String[] {"ID", "Nombre", "Activo"}
        ));
        jScrollPane1.setViewportView(tblDepartamentos);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("GESTION DEPARTAMENTOS");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(this::btnCerrarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnNuevo)
                .addGap(40, 40, 40)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnEliminar)
                    .addComponent(btnCerrar))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkActivo))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    private void configurarComponentes() {
        tblDepartamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarDepartamento(); }
        });
        cargarTabla();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) { guardarDepartamento(); }
    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) { limpiar(); }
    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) { dispose(); }
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) { eliminarDepartamento(); }

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblDepartamentos;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label1;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JCheckBox chkActivo;
    // End of variables declaration//GEN-END:variables
}
