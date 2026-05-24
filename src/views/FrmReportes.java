package views;

import controllers.ReporteController;
import models.Planilla;
import utils.ExportadorCSV;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

// @author DialeMRZ
public class FrmReportes extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmReportes.class.getName());
    private ReporteController controller;
    private DefaultTableModel modeloAsistencia;
    private DefaultTableModel modeloBoletas;
    private int[] idsPlanillas;
    private int[] idsEmpleados;

    public FrmReportes() {
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes();
        this.setLocationRelativeTo(null);
    }

    private void configurarComponentes() {
        this.controller = new ReporteController();

        // Modelos de tabla
        modeloAsistencia = new DefaultTableModel(new String[]{"ID", "Empleado", "Entrada", "Salida", "Horas", "Estado"}, 0);
        jTableAsistencia.setModel(modeloAsistencia);
        modeloBoletas = new DefaultTableModel(new String[]{"ID", "Empleado", "Ingresos", "Deducciones", "Líquido"}, 0);
        jTableBoletas.setModel(modeloBoletas);

        // Configurar spinners
        spnDesde.setModel(new javax.swing.SpinnerDateModel());
        spnDesde.setEditor(new javax.swing.JSpinner.DateEditor(spnDesde, "dd/MM/yyyy"));
        spnDesde.setValue(new java.util.Date());
        spnHasta.setModel(new javax.swing.SpinnerDateModel());
        spnHasta.setEditor(new javax.swing.JSpinner.DateEditor(spnHasta, "dd/MM/yyyy"));
        spnHasta.setValue(new java.util.Date());

        // Cargar combos
        var empleados = controller.listarEmpleadosActivos();
        idsEmpleados = new int[empleados.size()];
        int ie = 0;
        for (var e : empleados) {
            cmbEmpleadoAsist.addItem(e.getNombres() + " " + e.getApellidos());
            idsEmpleados[ie++] = e.getIdEmpleado();
        }
        List<Planilla> planillas = controller.listarPlanillas();
        idsPlanillas = new int[planillas.size()];
        int i = 0;
        for (Planilla p : planillas) {
            jComboBoxPlanilla.addItem(p.getMes() + "/" + p.getAnio() + " - " + p.getEstado());
            idsPlanillas[i++] = p.getIdPlanilla();
        }

        // Eventos
        btnGenerarAsistencia.addActionListener(e -> generarReporteAsistencia());
        btnExportarAsistencia.addActionListener(e -> ExportadorCSV.exportarJTable(jTableAsistencia, "asistencia", this));
        btnCargarBoletas.addActionListener(e -> cargarReporteBoletas());
        btnExportarPlanilla.addActionListener(e -> ExportadorCSV.exportarJTable(jTableBoletas, "planilla", this));
    }

    private void generarReporteAsistencia() {
        try {
            if (cmbEmpleadoAsist.getSelectedIndex() < 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un empleado.");
                return;
            }
            LocalDate desde = ((java.util.Date) spnDesde.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate hasta = ((java.util.Date) spnHasta.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

            modeloAsistencia.setRowCount(0);
            int idEmpleado = idsEmpleados[cmbEmpleadoAsist.getSelectedIndex()];
            controller.reporteAsistenciaFormateado(idEmpleado, desde, hasta, new controllers.AsistenciaController())
                .forEach(fila -> modeloAsistencia.addRow(fila));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarReporteBoletas() {
        try {
            int idx = jComboBoxPlanilla.getSelectedIndex();
            if (idx < 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una planilla.");
                return;
            }
            int idPlanilla = idsPlanillas[idx];
            modeloBoletas.setRowCount(0);
            controller.boletasPorPlanillaFormateado(idPlanilla)
                .forEach(fila -> modeloBoletas.addRow(fila));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabAsistencia = new javax.swing.JPanel();
        lblEmpleadoAsist = new javax.swing.JLabel();
        cmbEmpleadoAsist = new javax.swing.JComboBox<>();
        lblDesde = new javax.swing.JLabel();
        spnDesde = new javax.swing.JSpinner();
        lblHasta = new javax.swing.JLabel();
        spnHasta = new javax.swing.JSpinner();
        btnGenerarAsistencia = new javax.swing.JButton();
        btnExportarAsistencia = new javax.swing.JButton();
        jScrollPaneAsistencia = new javax.swing.JScrollPane();
        jTableAsistencia = new javax.swing.JTable();
        tabPlanilla = new javax.swing.JPanel();
        jLabelPlanilla = new javax.swing.JLabel();
        jComboBoxPlanilla = new javax.swing.JComboBox<>();
        btnCargarBoletas = new javax.swing.JButton();
        btnExportarPlanilla = new javax.swing.JButton();
        jScrollPanePlanilla = new javax.swing.JScrollPane();
        jTableBoletas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reportes");

        lblEmpleadoAsist.setText("Empleado:");
        lblDesde.setText("Desde:");
        lblHasta.setText("Hasta:");
        btnGenerarAsistencia.setText("Generar Reporte");
        btnExportarAsistencia.setText("Exportar CSV");
        jTableAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"ID", "Empleado", "Fecha", "Entrada", "Salida", "Horas"}));
        jScrollPaneAsistencia.setViewportView(jTableAsistencia);

        javax.swing.GroupLayout tabAsistenciaLayout = new javax.swing.GroupLayout(tabAsistencia);
        tabAsistencia.setLayout(tabAsistenciaLayout);
        tabAsistenciaLayout.setHorizontalGroup(tabAsistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAsistenciaLayout.createSequentialGroup().addGap(23, 23, 23)
                .addGroup(tabAsistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabAsistenciaLayout.createSequentialGroup()
                        .addComponent(lblEmpleadoAsist).addGap(6, 6, 6)
                        .addComponent(cmbEmpleadoAsist, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(lblDesde).addGap(6, 6, 6)
                        .addComponent(spnDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(lblHasta).addGap(6, 6, 6)
                        .addComponent(spnHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabAsistenciaLayout.createSequentialGroup()
                        .addComponent(btnGenerarAsistencia).addGap(18, 18, 18)
                        .addComponent(btnExportarAsistencia))
                    .addComponent(jScrollPaneAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE)));
        tabAsistenciaLayout.setVerticalGroup(tabAsistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAsistenciaLayout.createSequentialGroup().addGap(20, 20, 20)
                .addGroup(tabAsistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpleadoAsist).addComponent(cmbEmpleadoAsist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDesde).addComponent(spnDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHasta).addComponent(spnHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(tabAsistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerarAsistencia).addComponent(btnExportarAsistencia))
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Asistencia", tabAsistencia);

        jLabelPlanilla.setText("Planilla:");
        btnCargarBoletas.setText("Cargar Boletas");
        btnExportarPlanilla.setText("Exportar CSV");
        jTableBoletas.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"ID", "Empleado", "Per\u00edodo", "Total Ingresos", "Total Deducciones", "L\u00edquido"}));
        jScrollPanePlanilla.setViewportView(jTableBoletas);

        javax.swing.GroupLayout tabPlanillaLayout = new javax.swing.GroupLayout(tabPlanilla);
        tabPlanilla.setLayout(tabPlanillaLayout);
        tabPlanillaLayout.setHorizontalGroup(tabPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlanillaLayout.createSequentialGroup().addGap(23, 23, 23)
                .addGroup(tabPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPlanillaLayout.createSequentialGroup()
                        .addComponent(jLabelPlanilla).addGap(6, 6, 6)
                        .addComponent(jComboBoxPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabPlanillaLayout.createSequentialGroup()
                        .addComponent(btnCargarBoletas).addGap(18, 18, 18).addComponent(btnExportarPlanilla))
                    .addComponent(jScrollPanePlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE)));
        tabPlanillaLayout.setVerticalGroup(tabPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlanillaLayout.createSequentialGroup().addGap(20, 20, 20)
                .addGroup(tabPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPlanilla).addComponent(jComboBoxPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(tabPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCargarBoletas).addComponent(btnExportarPlanilla))
                .addGap(10, 10, 10)
                .addComponent(jScrollPanePlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Planilla", tabPlanilla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap().addComponent(jTabbedPane1).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap().addComponent(jTabbedPane1).addContainerGap()));
        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargarBoletas;
    private javax.swing.JButton btnExportarAsistencia;
    private javax.swing.JButton btnExportarPlanilla;
    private javax.swing.JButton btnGenerarAsistencia;
    private javax.swing.JComboBox<String> cmbEmpleadoAsist;
    private javax.swing.JComboBox<String> jComboBoxPlanilla;
    private javax.swing.JLabel jLabelPlanilla;
    private javax.swing.JScrollPane jScrollPaneAsistencia;
    private javax.swing.JScrollPane jScrollPanePlanilla;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAsistencia;
    private javax.swing.JTable jTableBoletas;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblEmpleadoAsist;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JSpinner spnDesde;
    private javax.swing.JSpinner spnHasta;
    private javax.swing.JPanel tabAsistencia;
    private javax.swing.JPanel tabPlanilla;
    // End of variables declaration//GEN-END:variables
}
