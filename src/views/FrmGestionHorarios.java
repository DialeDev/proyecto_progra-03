package views;

import controllers.HorarioController;
import models.Jornada;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// @author DialeMRZ
public class FrmGestionHorarios extends javax.swing.JFrame {
    private HorarioController controller;
    private boolean editando = false;

    public FrmGestionHorarios() {
        this.controller = new HorarioController();
        initComponents();
        configurarComponentes();
    }

    private void guardarJornada() {
        try {
            String nombre = txtNombre.getText().trim();
            String entradaTxt = txtEntrada.getText().trim();
            String salidaTxt = txtSalida.getText().trim();
            String toleranciaTxt = txtTolerancia.getText().trim();
            if (nombre.isEmpty() || entradaTxt.isEmpty() || salidaTxt.isEmpty() || toleranciaTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            LocalTime entrada = LocalTime.parse(entradaTxt);
            LocalTime salida = LocalTime.parse(salidaTxt);
            int tolerancia = Integer.parseInt(toleranciaTxt);
            if (tolerancia < 0) {
                JOptionPane.showMessageDialog(this, "La tolerancia no puede ser negativa.");
                return;
            }
            if (salida.isBefore(entrada)) {
                JOptionPane.showMessageDialog(this, "La salida debe ser después de la entrada.");
                return;
            }
            if (editando) {
                int fila = tblHorarios.getSelectedRow();
                if (fila == -1) { editando = false; btnGuardar.setText("Guardar"); return; }
                DefaultTableModel model = (DefaultTableModel) tblHorarios.getModel();
                int id = ((Number) model.getValueAt(fila, 0)).intValue();
                // Verificar duplicado excluyendo el registro actual
                if (controller.existeHorario(nombre)) {
                    boolean esOtro = controller.listar().stream()
                        .anyMatch(j -> j.getNombre().equalsIgnoreCase(nombre) && j.getIdJornada() != id);
                    if (esOtro) {
                        JOptionPane.showMessageDialog(this, "Ya existe otra jornada con el nombre '" + nombre + "'.");
                        return;
                    }
                }
                if (JOptionPane.showConfirmDialog(this, "¿Actualizar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, controller.actualizar(new Jornada(id, nombre, entrada, salida, tolerancia)));
                }
            } else {
                if (controller.existeHorario(nombre)) {
                    JOptionPane.showMessageDialog(this, "Ya existe una jornada '" + nombre + "'.");
                    return;
                }
                JOptionPane.showMessageDialog(this, controller.guardar(new Jornada(0, nombre, entrada, salida, tolerancia)));
            }
            cargarTabla();
            limpiar();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use formato HH:mm (ej: 08:00)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La tolerancia debe ser un número entero.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarJornada() {
        try {
            int fila = tblHorarios.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione una jornada."); return; }
            DefaultTableModel model = (DefaultTableModel) tblHorarios.getModel();
            int id = ((Number) model.getValueAt(fila, 0)).intValue();
            String nombre = (String) model.getValueAt(fila, 1);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, controller.eliminar(id));
                cargarTabla();
                limpiar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar jornada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarJornada() {
        if (tblHorarios.getSelectedRow() == -1) return;
        DefaultTableModel model = (DefaultTableModel) tblHorarios.getModel();
        int fila = tblHorarios.getSelectedRow();
        txtNombre.setText(String.valueOf(model.getValueAt(fila, 1)));
        txtEntrada.setText(String.valueOf(model.getValueAt(fila, 2)));
        txtSalida.setText(String.valueOf(model.getValueAt(fila, 3)));
        txtTolerancia.setText(String.valueOf(model.getValueAt(fila, 4)));
        editando = true;
        btnGuardar.setText("Actualizar");
    }

    private void cargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID"); modelo.addColumn("Nombre"); modelo.addColumn("Entrada");
        modelo.addColumn("Salida"); modelo.addColumn("Tolerancia");
        controller.listar().forEach(j -> modelo.addRow(new Object[]{
            j.getIdJornada(), j.getNombre(), j.getHoraEntradaEsperada(), j.getHoraSalidaEsperada(), j.getMargenToleranciaMinutos()}));
        tblHorarios.setModel(modelo);
    }

    private void limpiar() {
        txtNombre.setText(""); txtEntrada.setText(""); txtSalida.setText(""); txtTolerancia.setText("");
        tblHorarios.clearSelection(); editando = false; btnGuardar.setText("Guardar"); txtNombre.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorarios = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtTolerancia = new javax.swing.JTextField();
        txtEntrada = new javax.swing.JTextField();
        txtSalida = new javax.swing.JTextField();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de horarios");
        setResizable(false);

        tblHorarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Entrada", "Salida", "Tolerancia"
            }
        ));
        jScrollPane1.setViewportView(tblHorarios);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Gestion Horarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        txtEntrada.setToolTipText("(HH:MM)");

        txtSalida.setToolTipText("(HH:MM)");

        label1.setText("Nombre");

        label2.setText("Salida");

        label3.setText("Tolerancia (Min.)");

        label4.setText("Entrada");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label3)
                    .addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label4)
                            .addComponent(label2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtSalida))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Acciones", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        btnNuevo.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnGuardar.setBackground(new java.awt.Color(0, 153, 0));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(0, 0, 0));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addGap(35, 35, 35)
                .addComponent(btnEliminar)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarJornada();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarJornada();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiar();
    }//GEN-LAST:event_btnNuevoActionPerformed
    // </editor-fold>

    private void configurarComponentes() {
        tblHorarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarJornada(); }
        });
        cargarTabla();
    }

    //private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) { guardarJornada(); }
    //private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) { limpiar(); }
    //private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) { dispose(); }
    //private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) { eliminarJornada(); }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JTable tblHorarios;
    private javax.swing.JTextField txtEntrada;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSalida;
    private javax.swing.JTextField txtTolerancia;
    // End of variables declaration//GEN-END:variables
}
