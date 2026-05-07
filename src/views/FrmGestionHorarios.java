/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import controllers.HorarioController;
import models.Jornada;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author DialeMRZ
 */
public class FrmGestionHorarios extends javax.swing.JFrame {

    private HorarioController controller;
    private boolean editando = false;
    // Boton Eliminar agregado programaticamente
    private java.awt.Button btnEliminar;

    public FrmGestionHorarios() {
        this.controller = new HorarioController();
        initComponents();

        // Configurar boton Eliminar y agregarlo al panel
        btnEliminar = new java.awt.Button("Eliminar");
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnEliminar.addActionListener(e -> eliminarJornada());

        // Agregar btnEliminar al jPanel1 que esta arriba (junto a los otros botones)
        // Usamos GroupLayout para agregarlo al jPanel1
        javax.swing.GroupLayout jPanel1Layout = (javax.swing.GroupLayout) jPanel1.getLayout();
        // Recreamos el layout de jPanel1 incluyendo btnEliminar
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnEliminar)
                    .addComponent(btnCerrar))
                .addGap(20, 20, 20))
        );

        // Listener para seleccionar fila en la tabla
        JTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarJornada();
            }
        });

        cargarTabla();
    }

    //  GUARDAR (tambien funciona como Actualizar si se selecciono una fila)
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

            if (salida.isBefore(entrada)) {
                JOptionPane.showMessageDialog(this, "La hora de salida debe ser después de la hora de entrada.");
                return;
            }

            if (editando) {
                // Modo ACTUALIZAR
                int fila = JTable.getSelectedRow();
                if (fila == -1) {
                    editando = false;
                    btnGuardar.setLabel("Guardar");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) JTable.getModel();
                int idJornada = (int) model.getValueAt(fila, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de actualizar la jornada '" + nombre + "'?",
                    "Confirmar Actualización", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Jornada j = new Jornada(idJornada, nombre, entrada, salida, tolerancia);
                    String mensaje = controller.actualizar(j);
                    JOptionPane.showMessageDialog(this, mensaje);
                }
            } else {
                // Modo GUARDAR nuevo
                if (controller.existeHorario(nombre)) {
                    JOptionPane.showMessageDialog(this,
                        "Ya existe una jornada con el nombre '" + nombre + "'.");
                    return;
                }
                Jornada j = new Jornada(0, nombre, entrada, salida, tolerancia);
                String mensaje = controller.guardar(j);
                JOptionPane.showMessageDialog(this, mensaje);
            }

            cargarTabla();
            limpiar();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Error: use formato HH:mm (ej: 08:00)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: la tolerancia debe ser un numero entero.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    //  ELIMINAR
    private void eliminarJornada() {
        int fila = JTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una jornada de la tabla para eliminar.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) JTable.getModel();
        int idJornada = (int) model.getValueAt(fila, 0);
        String nombre = (String) model.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Esta seguro de eliminar la jornada '" + nombre + "'?",
            "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String mensaje = controller.eliminar(idJornada);
                JOptionPane.showMessageDialog(this, mensaje);
                cargarTabla();
                limpiar();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    //  SELECCIONAR JORNADA DE LA TABLA (para editar)
    private void seleccionarJornada() {
        if (JTable.getSelectedRow() == -1) return;

        DefaultTableModel model = (DefaultTableModel) JTable.getModel();
        int fila = JTable.getSelectedRow();
        txtNombre.setText(model.getValueAt(fila, 1).toString());
        txtEntrada.setText(model.getValueAt(fila, 2).toString());
        txtSalida.setText(model.getValueAt(fila, 3).toString());
        txtTolerancia.setText(model.getValueAt(fila, 4).toString());

        editando = true;
        btnGuardar.setLabel("Actualizar");
    }

    //  CARGAR TABLA
    private void cargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Entrada");
        modelo.addColumn("Salida");
        modelo.addColumn("Tolerancia");

        for (Jornada j : controller.listar()) {
            modelo.addRow(new Object[]{
                j.getIdJornada(),
                j.getNombre(),
                j.getHoraEntradaEsperada(),
                j.getHoraSalidaEsperada(),
                j.getMargenToleranciaMinutos()
            });
        }

        JTable.setModel(modelo);
    }

    //  LIMPIAR CAMPOS
    private void limpiar() {
        txtNombre.setText("");
        txtEntrada.setText("");
        txtSalida.setText("");
        txtTolerancia.setText("");
        JTable.clearSelection();
        editando = false;
        btnGuardar.setLabel("Guardar");
        txtNombre.requestFocus();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        txtNombre = new javax.swing.JTextField();
        txtTolerancia = new javax.swing.JTextField();
        txtEntrada = new javax.swing.JTextField();
        txtSalida = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        btnGuardar = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        label1.setText("Nombre");
        label2.setText("Salida");
        label3.setText("Tolerancia");
        label4.setText("Entrada");

        btnGuardar.setLabel("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        JTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(JTable);

        // Configurar jPanel1 (barra superior)
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("GESTION HORARIOS");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(this::btnCerrarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
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
                    .addComponent(btnCerrar))
                .addGap(20, 20, 20))
        );

        // Layout principal
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
                        .addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(240, 240, 240)))
                        .addGap(45, 45, 45))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        ));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTolerancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        guardarJornada();
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        limpiar();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmGestionHorarios().setVisible(true));
    }

    // Variables declaration - do not modify (NetBeans compatibility)
    private javax.swing.JTable JTable;
    private java.awt.Button btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private javax.swing.JTextField txtEntrada;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSalida;
    private javax.swing.JTextField txtTolerancia;
}
