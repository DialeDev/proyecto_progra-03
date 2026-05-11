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
        txtNombre = new javax.swing.JTextField();
        txtTolerancia = new javax.swing.JTextField();
        txtEntrada = new javax.swing.JTextField();
        txtSalida = new javax.swing.JTextField();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorarios = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        label1.setText("Nombre"); label2.setText("Salida"); label3.setText("Tolerancia"); label4.setText("Entrada");
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);
        tblHorarios.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
            {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null},
            {null, null, null, null, null}, {null, null, null, null, null}},
            new String[] {"ID", "Nombre", "Entrada", "Salida", "Tolerancia"}));
        jScrollPane1.setViewportView(tblHorarios);
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("GESTION HORARIOS");
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(this::btnCerrarActionPerformed);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(51, 51, 51).addComponent(btnNuevo)
                .addGap(40, 40, 40).addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE).addComponent(btnCerrar).addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnNuevo).addComponent(btnEliminar).addComponent(btnCerrar)).addGap(20, 20, 20)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(240, 240, 240)))
                            .addGap(45, 45, 45))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(166, 166, 166))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE)));
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    private void configurarComponentes() {
        tblHorarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarJornada(); }
        });
        cargarTabla();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) { guardarJornada(); }
    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) { limpiar(); }
    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) { dispose(); }
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) { eliminarJornada(); }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmGestionHorarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblHorarios;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JTextField txtEntrada;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSalida;
    private javax.swing.JTextField txtTolerancia;
    // End of variables declaration//GEN-END:variables
}
