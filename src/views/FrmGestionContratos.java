/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import dao.RepositorioContrato;
import dao.RepositorioContratacion;
import dao.RepositorioEmpleado;
import models.Contrato;
import models.Contrato.TipoContrato;
import models.Contratacion;
import models.Empleado;

import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

/**
 * Formulario para gestionar tipos de contrato (catálogo) y contrataciones.
 * @author WinterOS
 */
public class FrmGestionContratos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmGestionContratos.class.getName());

    private final RepositorioContrato contratoDAO;
    private final RepositorioContratacion contratacionDAO;
    private final RepositorioEmpleado empleadoDAO;

    // --- Componentes para CRUD de Tipos de Contrato ---
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

    /**
     * Creates new form FrmGestionContratos
     */
    public FrmGestionContratos() {
        this.contratoDAO = new RepositorioContrato();
        this.contratacionDAO = new RepositorioContratacion();
        this.empleadoDAO = new RepositorioEmpleado();

        initComponents();

        // Inicializar controles para gestión de tipos de contrato
        inicializarPanelTiposContrato();

        // Cargar datos iniciales
        cargarTiposContrato();
    }

    /**
     * Inicializa los componentes del panel de gestión de tipos de contrato.
     */
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

        // Estilos
        btnGuardarTipo.setBackground(new java.awt.Color(204, 255, 204));
        btnGuardarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnActualizarTipo.setBackground(new java.awt.Color(255, 255, 204));
        btnActualizarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnEliminarTipo.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnLimpiarTipo.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiarTipo.setFont(new java.awt.Font("Segoe UI", 1, 12));

        // Layout del panel
        java.awt.GridBagLayout gbl = new java.awt.GridBagLayout();
        panelTipoContrato.setLayout(gbl);
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(3, 5, 3, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelTipoContrato.add(lblNombreTipo, gbc);
        gbc.gridx = 1;
        panelTipoContrato.add(txtNombreTipo, gbc);

        // Fila 0: Tipo
        gbc.gridx = 2;
        panelTipoContrato.add(lblTipoContrato, gbc);
        gbc.gridx = 3;
        panelTipoContrato.add(cmbTipoContrato, gbc);

        // Fila 1: Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        panelTipoContrato.add(lblDescripcion, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelTipoContrato.add(txtDescripcion, gbc);
        gbc.gridwidth = 1;

        // Fila 2: Botones
        javax.swing.JPanel panelBotones = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnGuardarTipo);
        panelBotones.add(btnActualizarTipo);
        panelBotones.add(btnEliminarTipo);
        panelBotones.add(btnLimpiarTipo);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        panelTipoContrato.add(panelBotones, gbc);
        gbc.gridwidth = 1;

        // Agregar el panel al contenido (después de jScrollPane1)
        javax.swing.GroupLayout layout = (javax.swing.GroupLayout) getContentPane().getLayout();
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(panelTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(panelTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        // Listeners
        btnGuardarTipo.addActionListener(e -> guardarTipoContrato());
        btnActualizarTipo.addActionListener(e -> actualizarTipoContrato());
        btnEliminarTipo.addActionListener(e -> eliminarTipoContrato());
        btnLimpiarTipo.addActionListener(e -> limpiarFormularioTipo());

        // Listener para selección en la tabla
        tablaContratos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarTipoContrato();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
            new Object [][] {},
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
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    //  MÉTODOS PARA CRUD DE TIPOS DE CONTRATO
    // =========================================================================

    /**
     * Carga los tipos de contrato desde la BD y los muestra en la tabla.
     */
    private void cargarTiposContrato() {
        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        model.setRowCount(0);

        try {
            List<Contrato> lista = contratoDAO.findAll();
            for (Contrato c : lista) {
                Object[] fila = new Object[5];
                fila[0] = c.getIdContrato();
                fila[1] = c.getNombre();
                fila[2] = c.getTipo().name();
                fila[3] = c.getDescripcion();
                fila[4] = c.isActivo() ? "Activo" : "Inactivo";
                model.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar tipos de contrato: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda un nuevo tipo de contrato en la BD.
     */
    private void guardarTipoContrato() {
        String nombre = txtNombreTipo.getText().trim();
        String tipoStr = (String) cmbTipoContrato.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del tipo de contrato es obligatorio.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtNombreTipo.requestFocus();
            return;
        }

        TipoContrato tipo;
        try {
            tipo = TipoContrato.valueOf(tipoStr);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "El tipo seleccionado no es válido.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar duplicado
        Contrato existente = contratoDAO.buscarPorNombre(nombre);
        if (existente != null) {
            JOptionPane.showMessageDialog(this,
                "Ya existe un tipo de contrato con el nombre '" + nombre + "'.",
                "Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Contrato nuevo = new Contrato(0, nombre, tipo, descripcion);
            contratoDAO.create(nuevo);
            JOptionPane.showMessageDialog(this, "Tipo de contrato guardado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTiposContrato();
            limpiarFormularioTipo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar tipo de contrato: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un tipo de contrato existente en la BD.
     */
    private void actualizarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un tipo de contrato de la tabla para actualizar.",
                "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        int idContrato = (int) model.getValueAt(fila, 0);

        String nombre = txtNombreTipo.getText().trim();
        String tipoStr = (String) cmbTipoContrato.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del tipo de contrato es obligatorio.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoContrato tipo;
        try {
            tipo = TipoContrato.valueOf(tipoStr);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "El tipo seleccionado no es válido.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de actualizar el tipo de contrato '" + nombre + "'?",
            "Confirmar Actualización",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                Contrato contrato = new Contrato(idContrato, nombre, tipo, descripcion);
                contratoDAO.update(contrato);
                JOptionPane.showMessageDialog(this, "Tipo de contrato actualizado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTiposContrato();
                limpiarFormularioTipo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar tipo de contrato: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina (desactiva) un tipo de contrato de la BD.
     */
    private void eliminarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un tipo de contrato de la tabla para eliminar.",
                "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        int idContrato = (int) model.getValueAt(fila, 0);
        String nombre = (String) model.getValueAt(fila, 1);

        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el tipo de contrato '" + nombre + "'?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                contratoDAO.delete(idContrato);
                JOptionPane.showMessageDialog(this, "Tipo de contrato eliminado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTiposContrato();
                limpiarFormularioTipo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar tipo de contrato: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Carga los datos del tipo de contrato seleccionado en los campos del formulario.
     */
    private void seleccionarTipoContrato() {
        int fila = tablaContratos.getSelectedRow();
        if (fila == -1) return;

        DefaultTableModel model = (DefaultTableModel) tablaContratos.getModel();
        txtNombreTipo.setText(model.getValueAt(fila, 1).toString());
        String tipoStr = model.getValueAt(fila, 2).toString();
        cmbTipoContrato.setSelectedItem(tipoStr);
        txtDescripcion.setText(model.getValueAt(fila, 3) != null ? model.getValueAt(fila, 3).toString() : "");
    }

    /**
     * Limpia los campos del formulario de tipo de contrato.
     */
    private void limpiarFormularioTipo() {
        txtNombreTipo.setText("");
        cmbTipoContrato.setSelectedIndex(0);
        txtDescripcion.setText("");
        tablaContratos.clearSelection();
        txtNombreTipo.requestFocus();
    }

    // =========================================================================
    //  EVENTOS DE LA INTERFAZ (compatibilidad con ContratoController)
    // =========================================================================

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        // Este método se mantiene para compatibilidad con el Form Editor
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmGestionContratos().setVisible(true));
    }

    // =========================================================================
    //  GETTERS (para compatibilidad con ContratoController)
    // =========================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaContratos;
    // End of variables declaration//GEN-END:variables

    public static Logger getLogger() {
        return logger;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JTable getTablaContratos() {
        return tablaContratos;
    }
}
