package views;

import controllers.AsistenciaController;
import models.Empleado;
import models.RegistroAsistencia;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
// [PRODUCCIÓN] import javax.swing.Timer;
import javax.swing.SpinnerDateModel;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

// @author DialeMRZ
public class FrmMarcacion extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmMarcacion.class.getName());
    private final AsistenciaController asistenciaController;
    private DefaultListModel<String> modeloLista;
    private DefaultComboBoxModel<String> modeloCombo;
    private List<Empleado> empleadosActivos;
    // [PRODUCCIÓN] private javax.swing.JLabel lblReloj;
    // private javax.swing.JComboBox<String> cmbEmpleado;
    // private javax.swing.JLabel lblSeleccionEmpleado;
    // private int[] idsEmpleados;
    // [PRODUCCIÓN] private Timer relojTimer;

    private int idEmpleadoVerificado = -1;
    private java.util.List<RegistroAsistencia> asistenciasActuales = java.util.Collections.emptyList();

    public FrmMarcacion() {
        this.asistenciaController = new AsistenciaController();
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes();
        // [PRODUCCIÓN] cargarEmpleados();
        // [PRODUCCIÓN] iniciarReloj();
        cargarMarcacionesDelDia();
    }

    private void configurarComponentes() {
        modeloLista = new DefaultListModel<>();
        jList1.setModel(modeloLista);
        jList1.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Cargar empleados activos en combobox
        cargarEmpleados();

        // Configurar spinner de fecha
        SpinnerDateModel fechaModel = new SpinnerDateModel();
        spnFecha.setModel(fechaModel);
        spnFecha.setEditor(new javax.swing.JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));
        spnFecha.setValue(new Date());

        // Configurar spinner de hora
        SpinnerDateModel horaModel = new SpinnerDateModel();
        spnHora.setModel(horaModel);
        spnHora.setEditor(new javax.swing.JSpinner.DateEditor(spnHora, "HH:mm"));
        spnHora.setValue(new Date());

        // Configurar boton Verificar (ahora es seleccionar del combo)
        btnVerificar.addActionListener(e -> verificarEmpleadoDelCombo());

        jButton1.addActionListener(e -> marcarEntrada());
        jButton2.addActionListener(e -> marcarSalida());
        jButton3.addActionListener(e -> cargarMarcacionesDelDia());
        jButton4.addActionListener(e -> eliminarMarcacionSeleccionada());
    }

    private void cargarEmpleados() {
        try {
            empleadosActivos = asistenciaController.listarEmpleadosActivos();
            modeloCombo = new DefaultComboBoxModel<>();
            
            for (Empleado emp : empleadosActivos) {
                String nombreCompleto = emp.getNombres() + " " + emp.getApellidos();
                modeloCombo.addElement(nombreCompleto);
            }
            
            cmbEmpleado.setModel(modeloCombo);
            cmbEmpleado.setSelectedIndex(-1); // Sin selección inicial
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarEmpleadoDelCombo() {
        int idx = cmbEmpleado.getSelectedIndex();
        if (idx < 0 || idx >= empleadosActivos.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado del listado.", "Error", JOptionPane.WARNING_MESSAGE);
            idEmpleadoVerificado = -1;
            lblNombreEmpleado.setText("");
            return;
        }
        
        Empleado empSeleccionado = empleadosActivos.get(idx);
        lblNombreEmpleado.setText("Nombre: " + empSeleccionado.getNombres() + " " + empSeleccionado.getApellidos());
        idEmpleadoVerificado = empSeleccionado.getIdEmpleado();
        cargarMarcacionesDelDia();
    }

    // [PRODUCCIÓN] private void cargarEmpleados() {
    // [PRODUCCIÓN]     cmbEmpleado.removeAllItems();
    // [PRODUCCIÓN]     try {
    // [PRODUCCIÓN]         List<Empleado> empleados = asistenciaController.listarEmpleadosActivos();
    // [PRODUCCIÓN]         idsEmpleados = new int[empleados.size()];
    // [PRODUCCIÓN]         int i = 0;
    // [PRODUCCIÓN]         for (Empleado e : empleados) {
    // [PRODUCCIÓN]             cmbEmpleado.addItem(e.getNombres() + " " + e.getApellidos() + " - " + e.getDui());
    // [PRODUCCIÓN]             idsEmpleados[i++] = e.getIdEmpleado();
    // [PRODUCCIÓN]         }
    // [PRODUCCIÓN]     } catch (Exception e) {
    // [PRODUCCIÓN]         JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    // [PRODUCCIÓN]     }
    // [PRODUCCIÓN] }

    // [PRODUCCIÓN] private int getEmpleadoSeleccionado() {
    // [PRODUCCIÓN]     int idx = cmbEmpleado.getSelectedIndex();
    // [PRODUCCIÓN]     return (idx >= 0 && idx < idsEmpleados.length) ? idsEmpleados[idx] : -1;
    // [PRODUCCIÓN] }

    // [PRODUCCIÓN] private void iniciarReloj() {
    // [PRODUCCIÓN]     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    // [PRODUCCIÓN]     DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // [PRODUCCIÓN]     relojTimer = new Timer(1000, e -> {
    // [PRODUCCIÓN]         LocalDateTime ahora = LocalDateTime.now();
    // [PRODUCCIÓN]         lblReloj.setText(ahora.format(formatter) + "  -  " + ahora.format(fechaFormatter));
    // [PRODUCCIÓN]     });
    // [PRODUCCIÓN]     relojTimer.setRepeats(true);
    // [PRODUCCIÓN]     relojTimer.start();
    // [PRODUCCIÓN] }

    private LocalDateTime getFechaHoraDesdeSpinners() {
        Date fechaDate = (Date) spnFecha.getValue();
        Date horaDate = (Date) spnHora.getValue();
        if (fechaDate == null || horaDate == null) {
            JOptionPane.showMessageDialog(this, "Fecha u hora no válida en los spinners.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        LocalDate fecha = new java.sql.Date(fechaDate.getTime()).toLocalDate();
        LocalTime hora = new java.sql.Time(horaDate.getTime()).toLocalTime();
        return LocalDateTime.of(fecha, hora);
    }

    private void marcarEntrada() {
        if (idEmpleadoVerificado == -1) { JOptionPane.showMessageDialog(this, "Verifique un empleado primero.", "Error", JOptionPane.WARNING_MESSAGE); return; }
        try {
            LocalDateTime fechaHora = getFechaHoraDesdeSpinners();
            if (fechaHora == null) return;
            RegistroAsistencia resultado = asistenciaController.marcarEntrada(idEmpleadoVerificado, fechaHora);
            if (resultado == null) {
                JOptionPane.showMessageDialog(this, "El empleado ya tiene entrada activa.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Entrada: " + resultado.getMarcaEntrada().format(DateTimeFormatter.ofPattern("HH:mm:ss")), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void marcarSalida() {
        if (idEmpleadoVerificado == -1) { JOptionPane.showMessageDialog(this, "Verifique un empleado primero.", "Error", JOptionPane.WARNING_MESSAGE); return; }
        try {
            LocalDateTime fechaHora = getFechaHoraDesdeSpinners();
            if (fechaHora == null) return;
            if (asistenciaController.marcarSalida(idEmpleadoVerificado, fechaHora)) {
                JOptionPane.showMessageDialog(this, "Salida registrada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            } else { JOptionPane.showMessageDialog(this, "No hay entrada activa.", "Error", JOptionPane.WARNING_MESSAGE); }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void cargarMarcacionesDelDia() {
        modeloLista.clear();
        if (idEmpleadoVerificado == -1) { modeloLista.addElement("=== Verifique un empleado ==="); return; }
        try {
            LocalDate fechaSel = new java.sql.Date(((Date) spnFecha.getValue()).getTime()).toLocalDate();
            asistenciasActuales = asistenciaController.obtenerAsistenciasDelDia(idEmpleadoVerificado, fechaSel);
            if (asistenciasActuales.isEmpty()) { modeloLista.addElement("=== Sin marcaciones en esta fecha ==="); return; }
            DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm:ss");
            modeloLista.addElement("=== MARCACIONES DEL " + fechaSel.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ===");
            modeloLista.addElement("");
            asistenciasActuales.forEach(r -> {
                String entrada = r.getMarcaEntrada().format(hf);
                String salida = r.getMarcaSalida() != null ? r.getMarcaSalida().format(hf) : "--- (activo)";
                String horas = r.getMarcaSalida() != null ? String.format("%.2f hrs", asistenciaController.calcularHorasTrabajadas(r)) : "En curso";
                modeloLista.addElement("Entrada: " + entrada);
                modeloLista.addElement("Salida:  " + salida);
                modeloLista.addElement("Total:   " + horas);
                modeloLista.addElement("------------------------");
            });
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error al cargar marcaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void eliminarMarcacionSeleccionada() {
        int idx = jList1.getSelectedIndex();
        if (idx < 0 || idx >= asistenciasActuales.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione una marcacion de la lista.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        RegistroAsistencia seleccionada = asistenciasActuales.get(idx);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Eliminar marcacion del " + seleccionada.getMarcaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "?",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            asistenciaController.eliminarAsistencia(seleccionada.getIdRegistro());
            JOptionPane.showMessageDialog(this, "Marcacion eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarMarcacionesDelDia();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        spnFecha = new javax.swing.JSpinner();
        spnHora = new javax.swing.JSpinner();
        lblFechaHora = new javax.swing.JLabel();
        lblIdEmpleado = new javax.swing.JLabel();
        btnVerificar = new javax.swing.JButton();
        lblNombreEmpleado = new javax.swing.JLabel();
        cmbEmpleado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Marcacion");
        setResizable(false);

        jButton1.setText("Marcar entrada");

        jButton2.setText("Marcar salida");

        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Refrescar");

        jButton4.setText("Eliminar");

        lblFechaHora.setText("Fecha y Hora:");

        lblIdEmpleado.setText("Seleccionar Empleado");

        btnVerificar.setText("Verificar");

        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFechaHora)
                        .addGap(18, 18, 18)
                        .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(spnHora, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIdEmpleado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVerificar))
                    .addComponent(lblNombreEmpleado)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaHora)
                    .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdEmpleado)
                    .addComponent(btnVerificar)
                    .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(lblNombreEmpleado)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVerificar;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblIdEmpleado;
    private javax.swing.JLabel lblNombreEmpleado;
    private javax.swing.JSpinner spnFecha;
    private javax.swing.JSpinner spnHora;
    // End of variables declaration//GEN-END:variables
}
