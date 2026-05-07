/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import controllers.AsistenciaController;
import dao.RepositorioEmpleado;
import models.Empleado;
import models.RegistroAsistencia;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Formulario para marcación de asistencia (entrada/salida).
 * @author DialeMRZ
 */
public class FrmMarcacion extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmMarcacion.class.getName());

    private final AsistenciaController asistenciaController;
    private final RepositorioEmpleado empleadoDAO;
    private DefaultListModel<String> modeloLista;

    // Componentes agregados programáticamente
    private javax.swing.JLabel lblReloj;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JLabel lblSeleccionEmpleado;
    private int[] idsEmpleados; // Para mapear nombres a IDs

    private Timer relojTimer;

    /**
     * Creates new form FrmMarcacion
     */
    public FrmMarcacion() {
        this.asistenciaController = new AsistenciaController();
        this.empleadoDAO = new RepositorioEmpleado();

        initComponents();

        // Configurar componentes después de initComponents
        configurarComponentes();

        // Cargar empleados en el combo box
        cargarEmpleados();

        // Iniciar el reloj en tiempo real
        iniciarReloj();

        // Cargar marcaciones de hoy
        cargarMarcacionesDelDia();
    }

    /**
     * Configura los componentes de la interfaz después de la inicialización.
     */
    private void configurarComponentes() {
        // Configurar el modelo de lista
        modeloLista = new DefaultListModel<>();
        jList1.setModel(modeloLista);
        jList1.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Configurar el reloj
        lblReloj = new javax.swing.JLabel();
        lblReloj.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblReloj.setText(java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

        // Añadir selector de empleado
        lblSeleccionEmpleado = new javax.swing.JLabel("Empleado:");
        cmbEmpleado = new javax.swing.JComboBox<>();
        cmbEmpleado.setPreferredSize(new java.awt.Dimension(200, 30));

        // Reposicionar componentes usando el layout existente
        javax.swing.GroupLayout layout = (javax.swing.GroupLayout) getContentPane().getLayout();

        // Quitar el jLabel1 original y usar el nuestro
        jLabel1.setText(""); // Ocultar texto original

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(26, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblReloj)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSeleccionEmpleado)
                            .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblReloj)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(lblSeleccionEmpleado)
                        .addGap(3, 3, 3)
                        .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );

        // Configurar botones
        jButton1.setText("Marcar Entrada");
        jButton2.setText("Marcar Salida");
        jButton3.setText("Refrescar");

        // Listeners
        jButton1.addActionListener(e -> marcarEntrada());
        jButton2.addActionListener(e -> marcarSalida());
        jButton3.addActionListener(e -> {
            cargarEmpleados();
            cargarMarcacionesDelDia();
        });
    }

    /**
     * Carga la lista de empleados activos en el combo box.
     */
    private void cargarEmpleados() {
        cmbEmpleado.removeAllItems();
        try {
            List<Empleado> empleados = empleadoDAO.findAll();
            idsEmpleados = new int[empleados.size()];
            int i = 0;
            for (Empleado e : empleados) {
                if (e.isActivo()) {
                    cmbEmpleado.addItem(e.getNombres() + " " + e.getApellidos() + " - " + e.getDui());
                    idsEmpleados[i] = e.getIdEmpleado();
                    i++;
                }
            }
            // Redimensionar el arreglo si hay inactivos
            if (i < idsEmpleados.length) {
                int[] temp = new int[i];
                System.arraycopy(idsEmpleados, 0, temp, 0, i);
                idsEmpleados = temp;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar empleados: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene el ID del empleado seleccionado en el combo box.
     * @return ID del empleado, o -1 si no hay selección
     */
    private int getEmpleadoSeleccionado() {
        int idx = cmbEmpleado.getSelectedIndex();
        if (idx >= 0 && idx < idsEmpleados.length) {
            return idsEmpleados[idx];
        }
        return -1;
    }

    /**
     * Inicia un Timer para actualizar el reloj cada segundo.
     */
    private void iniciarReloj() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        relojTimer = new Timer(1000, e -> {
            LocalDateTime ahora = LocalDateTime.now();
            lblReloj.setText(ahora.format(formatter) + "  -  " + ahora.format(fechaFormatter));
        });
        relojTimer.setRepeats(true);
        relojTimer.start();
    }

    /**
     * Marca la entrada del empleado seleccionado.
     */
    private void marcarEntrada() {
        int idEmpleado = getEmpleadoSeleccionado();
        if (idEmpleado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un empleado para marcar entrada.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            RegistroAsistencia resultado = asistenciaController.marcarEntrada(idEmpleado);
            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                    "El empleado ya tiene una marcación de entrada sin registrar salida.\n"
                    + "Debe marcar la salida primero.",
                    "Entrada duplicada", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Entrada registrada exitosamente:\n"
                    + resultado.getMarcaEntrada().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    "Entrada registrada", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al marcar entrada: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Marca la salida del empleado seleccionado.
     */
    private void marcarSalida() {
        int idEmpleado = getEmpleadoSeleccionado();
        if (idEmpleado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un empleado para marcar salida.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean resultado = asistenciaController.marcarSalida(idEmpleado);
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                    "Salida registrada exitosamente.",
                    "Salida registrada", JOptionPane.INFORMATION_MESSAGE);
                cargarMarcacionesDelDia();
            } else {
                JOptionPane.showMessageDialog(this,
                    "El empleado no tiene una marcación de entrada activa.\n"
                    + "Debe marcar la entrada primero.",
                    "Sin entrada activa", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al marcar salida: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga las marcaciones del día de hoy para el empleado seleccionado.
     */
    private void cargarMarcacionesDelDia() {
        modeloLista.clear();

        int idEmpleado = getEmpleadoSeleccionado();
        if (idEmpleado == -1) {
            modeloLista.addElement("=== Seleccione un empleado ===");
            return;
        }

        try {
            List<RegistroAsistencia> asistencias = asistenciaController.obtenerAsistenciasDelDia(idEmpleado);

            if (asistencias.isEmpty()) {
                modeloLista.addElement("=== Sin marcaciones hoy ===");
                return;
            }

            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            modeloLista.addElement("=== MARCACIONES DE HOY ===");
            modeloLista.addElement("");

            for (RegistroAsistencia r : asistencias) {
                String entrada = r.getMarcaEntrada().format(horaFormatter);
                String salida = r.getMarcaSalida() != null
                    ? r.getMarcaSalida().format(horaFormatter)
                    : "--- (activo)";
                String horas = r.getMarcaSalida() != null
                    ? String.format("%.2f hrs", asistenciaController.calcularHorasTrabajadas(r))
                    : "En curso";

                modeloLista.addElement("Entrada: " + entrada);
                modeloLista.addElement("Salida:  " + salida);
                modeloLista.addElement("Total:   " + horas);
                modeloLista.addElement("------------------------");
            }
        } catch (Exception e) {
            modeloLista.addElement("Error al cargar: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Marcación");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel1.setText("");

        jButton1.setText("Marcar entrada");

        jButton2.setText("Marcar salida");

        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Almuerzo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(26, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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

        java.awt.EventQueue.invokeLater(() -> new FrmMarcacion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
