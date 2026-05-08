package views;

import controllers.AsistenciaController;
import models.Empleado;
import models.RegistroAsistencia;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
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
    // [PRODUCCIÓN] private javax.swing.JLabel lblReloj;
    // private javax.swing.JComboBox<String> cmbEmpleado;
    // private javax.swing.JLabel lblSeleccionEmpleado;
    // private int[] idsEmpleados;
    // [PRODUCCIÓN] private Timer relojTimer;

    private javax.swing.JSpinner spnFecha;
    private javax.swing.JSpinner spnHora;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblIdEmpleado;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel lblNombreEmpleado;
    private int idEmpleadoVerificado = -1;

    public FrmMarcacion() {
        this.asistenciaController = new AsistenciaController();
        initComponents();
        configurarComponentes();
        // [PRODUCCIÓN] cargarEmpleados();
        // [PRODUCCIÓN] iniciarReloj();
        cargarMarcacionesDelDia();
    }

    private void configurarComponentes() {
        modeloLista = new DefaultListModel<>();
        jList1.setModel(modeloLista);
        jList1.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jButton1.setText("Marcar Entrada");
        jButton2.setText("Marcar Salida");
        jButton3.setText("Refrescar");

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

        // Configurar boton Verificar
        btnVerificar.addActionListener(e -> verificarEmpleado());

        jButton1.addActionListener(e -> marcarEntrada());
        jButton2.addActionListener(e -> marcarSalida());
        jButton3.addActionListener(e -> { cargarMarcacionesDelDia(); });
    }

    private void verificarEmpleado() {
        String texto = txtIdEmpleado.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de empleado.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(texto);
            Empleado emp = asistenciaController.buscarEmpleadoPorId(id);
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado con ID: " + id, "Error", JOptionPane.WARNING_MESSAGE);
                lblNombreEmpleado.setText("");
                idEmpleadoVerificado = -1;
            } else {
                lblNombreEmpleado.setText("Nombre: " + emp.getNombres() + " " + emp.getApellidos());
                idEmpleadoVerificado = id;
                cargarMarcacionesDelDia();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un numero valido.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al verificar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        LocalDate fecha = new java.sql.Date(fechaDate.getTime()).toLocalDate();
        LocalTime hora = new java.sql.Time(horaDate.getTime()).toLocalTime();
        return LocalDateTime.of(fecha, hora);
    }

    private void marcarEntrada() {
        if (idEmpleadoVerificado == -1) { JOptionPane.showMessageDialog(this, "Verifique un empleado primero.", "Error", JOptionPane.WARNING_MESSAGE); return; }
        try {
            LocalDateTime fechaHora = getFechaHoraDesdeSpinners();
            RegistroAsistencia resultado = asistenciaController.marcarEntrada(idEmpleadoVerificado, fechaHora);
            if (resultado == null) {
                JOptionPane.showMessageDialog(this, "El empleado ya tiene entrada activa.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Entrada: " + resultado.getMarcaEntrada().format(DateTimeFormatter.ofPattern("HH:mm:ss")), "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void marcarSalida() {
        if (idEmpleadoVerificado == -1) { JOptionPane.showMessageDialog(this, "Verifique un empleado primero.", "Error", JOptionPane.WARNING_MESSAGE); return; }
        try {
            LocalDateTime fechaHora = getFechaHoraDesdeSpinners();
            if (asistenciaController.marcarSalida(idEmpleadoVerificado, fechaHora)) {
                JOptionPane.showMessageDialog(this, "Salida registrada.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            } else { JOptionPane.showMessageDialog(this, "No hay entrada activa.", "Error", JOptionPane.WARNING_MESSAGE); }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void cargarMarcacionesDelDia() {
        modeloLista.clear();
        if (idEmpleadoVerificado == -1) { modeloLista.addElement("=== Verifique un empleado ==="); return; }
        try {
            LocalDate fechaSel = ((java.sql.Date) new java.sql.Date(((Date) spnFecha.getValue()).getTime())).toLocalDate();
            List<RegistroAsistencia> asistencias = asistenciaController.obtenerAsistenciasDelDia(idEmpleadoVerificado, fechaSel);
            if (asistencias.isEmpty()) { modeloLista.addElement("=== Sin marcaciones en esta fecha ==="); return; }
            DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm:ss");
            modeloLista.addElement("=== MARCACIONES DEL " + fechaSel.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ===");
            modeloLista.addElement("");
            asistencias.forEach(r -> {
                String entrada = r.getMarcaEntrada().format(hf);
                String salida = r.getMarcaSalida() != null ? r.getMarcaSalida().format(hf) : "--- (activo)";
                String horas = r.getMarcaSalida() != null ? String.format("%.2f hrs", asistenciaController.calcularHorasTrabajadas(r)) : "En curso";
                modeloLista.addElement("Entrada: " + entrada);
                modeloLista.addElement("Salida:  " + salida);
                modeloLista.addElement("Total:   " + horas);
                modeloLista.addElement("------------------------");
            });
        } catch (Exception e) { modeloLista.addElement("Error: " + e.getMessage()); }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();

        // [PRODUCCIÓN] lblReloj = new javax.swing.JLabel();
        // lblSeleccionEmpleado = new javax.swing.JLabel();
        // cmbEmpleado = new javax.swing.JComboBox<>();

        spnFecha = new javax.swing.JSpinner();
        spnHora = new javax.swing.JSpinner();
        lblFechaHora = new javax.swing.JLabel();
        lblIdEmpleado = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        btnVerificar = new javax.swing.JButton();
        lblNombreEmpleado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Marcacion");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 10));

        jButton1.setText("Marcar entrada");
        jButton2.setText("Marcar salida");

        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Almuerzo");

        // [PRODUCCIÓN] lblReloj.setFont(new java.awt.Font("Segoe UI", 1, 16));
        // [PRODUCCIÓN] lblReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // [PRODUCCIÓN] lblReloj.setText(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

        // lblSeleccionEmpleado.setText("Empleado:");
        // cmbEmpleado.setPreferredSize(new java.awt.Dimension(200, 30));

        lblFechaHora.setText("Fecha y Hora:");
        lblIdEmpleado.setText("ID Empleado:");
        txtIdEmpleado.setPreferredSize(new java.awt.Dimension(80, 30));
        btnVerificar.setText("Verificar");
        lblNombreEmpleado.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFechaHora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnHora, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIdEmpleado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVerificar))
                    .addComponent(lblNombreEmpleado)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaHora)
                    .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdEmpleado)
                    .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerificar))
                .addGap(18)
                .addComponent(lblNombreEmpleado)
                .addGap(18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmMarcacion().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
}
