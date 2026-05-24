package views;

import controllers.AsignacionJornadaController;
import models.AsignacionJornada;
import models.Empleado;
import models.Jornada;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

// @author DialeMRZ
public class FrmAsignacionJornada extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmAsignacionJornada.class.getName());
    private AsignacionJornadaController controller;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int idEnEdicion = 0;
    private List<Empleado> empleados;
    private List<Jornada> jornadas;

    public FrmAsignacionJornada() {
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes();
        this.setLocationRelativeTo(null);
    }

    private void configurarComponentes() {
        this.controller = new AsignacionJornadaController();

        // Configurar spinners
        jSpinnerFechaInicio.setModel(new javax.swing.SpinnerDateModel());
        jSpinnerFechaInicio.setEditor(new javax.swing.JSpinner.DateEditor(jSpinnerFechaInicio, "dd/MM/yyyy"));
        jSpinnerFechaInicio.setValue(new java.util.Date());

        jSpinnerFechaFin.setModel(new javax.swing.SpinnerDateModel());
        jSpinnerFechaFin.setEditor(new javax.swing.JSpinner.DateEditor(jSpinnerFechaFin, "dd/MM/yyyy"));
        jSpinnerFechaFin.setValue(new java.util.Date());

        // Modelo tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Empleado", "Jornada", "F. Inicio", "F. Fin", "Activa"}, 0);
        jTableAsignaciones.setModel(modeloTabla);

        // Cargar combos
        cargarCombos();

        // Eventos
        btnGuardar.addActionListener(e -> guardarAsignacion());
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarAsignacion());
        btnCerrar.addActionListener(e -> dispose());
        jTableAsignaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) { seleccionarAsignacion(); }
        });

        cargarTabla();
    }

    private void cargarCombos() {
        jComboBoxEmpleado.removeAllItems();
        empleados = controller.listarEmpleadosActivos();
        for (Empleado e : empleados) {
            jComboBoxEmpleado.addItem(e.getNombres() + " " + e.getApellidos());
        }

        jComboBoxJornada.removeAllItems();
        jornadas = controller.listarJornadasActivas();
        for (Jornada j : jornadas) {
            jComboBoxJornada.addItem(j.getNombre());
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        controller.listarConNombres().forEach(fila -> modeloTabla.addRow(fila));
    }

    private void guardarAsignacion() {
        try {
            int idxEmp = jComboBoxEmpleado.getSelectedIndex();
            int idxJor = jComboBoxJornada.getSelectedIndex();
            if (idxEmp < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado.");
                return;
            }
            if (idxJor < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una jornada.");
                return;
            }
            LocalDate inicio = ((java.util.Date) jSpinnerFechaInicio.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fin = ((java.util.Date) jSpinnerFechaFin.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

            AsignacionJornada a = new AsignacionJornada(
                editando ? idEnEdicion : 0,
                empleados.get(idxEmp).getIdEmpleado(),
                jornadas.get(idxJor).getIdJornada(),
                inicio, fin, chkActiva.isSelected()
            );

            String msg = editando ? controller.actualizar(a) : controller.guardar(a);
            JOptionPane.showMessageDialog(this, msg);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAsignacion() {
        try {
            int fila = jTableAsignaciones.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una asignacion.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar asignacion?", "Confirmar",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, controller.eliminar(id));
                cargarTabla();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarAsignacion() {
        int fila = jTableAsignaciones.getSelectedRow();
        if (fila == -1) return;
        idEnEdicion = (int) modeloTabla.getValueAt(fila, 0);
        String empleadoNombre = (String) modeloTabla.getValueAt(fila, 1);
        String jornadaNombre = (String) modeloTabla.getValueAt(fila, 2);

        for (int i = 0; i < jComboBoxEmpleado.getItemCount(); i++) {
            if (jComboBoxEmpleado.getItemAt(i).equals(empleadoNombre)) {
                jComboBoxEmpleado.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < jComboBoxJornada.getItemCount(); i++) {
            if (jComboBoxJornada.getItemAt(i).equals(jornadaNombre)) {
                jComboBoxJornada.setSelectedIndex(i);
                break;
            }
        }

        java.sql.Date fechaIni = (java.sql.Date) modeloTabla.getValueAt(fila, 3);
        if (fechaIni != null) jSpinnerFechaInicio.setValue(fechaIni);
        Object fechaFinObj = modeloTabla.getValueAt(fila, 4);
        if (fechaFinObj instanceof java.util.Date) {
            jSpinnerFechaFin.setValue(fechaFinObj);
        }
        chkActiva.setSelected((boolean) modeloTabla.getValueAt(fila, 5));
        editando = true;
        btnGuardar.setText("Actualizar");
    }

    private void limpiarFormulario() {
        jComboBoxEmpleado.setSelectedIndex(-1);
        jComboBoxJornada.setSelectedIndex(-1);
        jSpinnerFechaInicio.setValue(new java.util.Date());
        jSpinnerFechaFin.setValue(new java.util.Date());
        chkActiva.setSelected(true);
        jTableAsignaciones.clearSelection();
        editando = false;
        idEnEdicion = 0;
        btnGuardar.setText("Guardar");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabelEmpleado = new javax.swing.JLabel();
        jComboBoxEmpleado = new javax.swing.JComboBox<>();
        jLabelJornada = new javax.swing.JLabel();
        jComboBoxJornada = new javax.swing.JComboBox<>();
        jLabelFechaInicio = new javax.swing.JLabel();
        jSpinnerFechaInicio = new javax.swing.JSpinner();
        jLabelFechaFin = new javax.swing.JLabel();
        jSpinnerFechaFin = new javax.swing.JSpinner();
        chkActiva = new javax.swing.JCheckBox();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAsignaciones = new javax.swing.JTable();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asignaci\u00f3n de Jornadas");

        jLabelEmpleado.setText("Empleado:");
        jLabelJornada.setText("Jornada:");
        jLabelFechaInicio.setText("Fecha Inicio:");
        jLabelFechaFin.setText("Fecha Fin (opcional):");
        chkActiva.setSelected(true);
        chkActiva.setText("Activa");
        btnGuardar.setText("Guardar");

        jTableAsignaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"ID", "Empleado", "Jornada", "Fecha Inicio", "Fecha Fin", "Activa"}));
        jScrollPane1.setViewportView(jTableAsignaciones);

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabelTitulo.setText("Gesti\u00f3n de Asignaci\u00f3n de Jornadas");
        btnNuevo.setText("Nuevo");
        btnCerrar.setText("Cerrar");
        btnEliminar.setText("Eliminar");

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup().addGap(51, 51, 51)
                .addComponent(btnNuevo).addGap(40, 40, 40).addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnCerrar).addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        jPanelTituloLayout.setVerticalGroup(jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup().addGap(22, 22, 22)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo).addComponent(btnEliminar).addComponent(btnCerrar)).addGap(20, 20, 20)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelEmpleado).addGap(6, 6, 6)
                        .addComponent(jComboBoxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(jLabelJornada).addGap(6, 6, 6)
                        .addComponent(jComboBoxJornada, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFechaInicio).addGap(6, 6, 6)
                        .addComponent(jSpinnerFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(jLabelFechaFin).addGap(6, 6, 6)
                        .addComponent(jSpinnerFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(chkActiva))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmpleado).addComponent(jComboBoxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelJornada).addComponent(jComboBoxJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFechaInicio).addComponent(jSpinnerFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFechaFin).addComponent(jSpinnerFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkActiva))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE)));
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkActiva;
    private javax.swing.JComboBox<String> jComboBoxEmpleado;
    private javax.swing.JComboBox<String> jComboBoxJornada;
    private javax.swing.JLabel jLabelEmpleado;
    private javax.swing.JLabel jLabelFechaFin;
    private javax.swing.JLabel jLabelFechaInicio;
    private javax.swing.JLabel jLabelJornada;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerFechaFin;
    private javax.swing.JSpinner jSpinnerFechaInicio;
    private javax.swing.JTable jTableAsignaciones;
    // End of variables declaration//GEN-END:variables
}
