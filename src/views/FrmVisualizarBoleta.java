package views;

import dao.RepositorioEmpleado;
import models.BoletaDetalle;
import models.BoletaPago;
import javax.swing.table.DefaultTableModel;

public class FrmVisualizarBoleta extends javax.swing.JFrame {
    private DefaultTableModel modeloDetalles;

    public FrmVisualizarBoleta(BoletaPago boleta) {
        initComponents();
        utils.UIStyles.applyModernStyle(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icono.png")).getImage());
        configurarComponentes(boleta);
    }

    private void configurarComponentes(BoletaPago boleta) {
        modeloDetalles = new DefaultTableModel(new String[]{"Concepto", "Tipo", "Monto"}, 0);
        jTableDetalles.setModel(modeloDetalles);

        var emp = new RepositorioEmpleado().findById(boleta.idEmpleado());
        txtEmpleado.setText(emp.map(e -> e.getNombres() + " " + e.getApellidos())
            .orElse("ID: " + boleta.idEmpleado()));
        txtSueldoBase.setText(String.format("$%.2f", boleta.sueldoBaseAplicado()));
        txtHorasOrd.setText(String.format("%.2f", boleta.horasOrdinariasCalculadas()));
        txtHorasExtra.setText(String.format("D: %.2f / N: %.2f",
                boleta.horasExtraDiurnas(), boleta.horasExtraNocturnas()));
        txtTotalIngresos.setText(String.format("$%.2f", boleta.totalIngresos()));
        txtTotalDeducciones.setText(String.format("$%.2f", boleta.totalDeducciones()));
        txtLiquidoPagar.setText(String.format("$%.2f", boleta.liquidoAPagar()));

        boleta.detalles().forEach(d -> modeloDetalles.addRow(new Object[]{
            d.concepto(), d.tipo(), String.format("$%.2f", d.monto())
        }));

        btnCerrar.addActionListener(e -> dispose());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabelEmpleado = new javax.swing.JLabel();
        txtEmpleado = new javax.swing.JTextField();
        jLabelSueldo = new javax.swing.JLabel();
        txtSueldoBase = new javax.swing.JTextField();
        jLabelHorasOrd = new javax.swing.JLabel();
        txtHorasOrd = new javax.swing.JTextField();
        jLabelHorasExtra = new javax.swing.JLabel();
        txtHorasExtra = new javax.swing.JTextField();
        jLabelTotalIngresos = new javax.swing.JLabel();
        txtTotalIngresos = new javax.swing.JTextField();
        jLabelTotalDeducciones = new javax.swing.JLabel();
        txtTotalDeducciones = new javax.swing.JTextField();
        jLabelLiquido = new javax.swing.JLabel();
        txtLiquidoPagar = new javax.swing.JTextField();
        jScrollPaneDetalles = new javax.swing.JScrollPane();
        jTableDetalles = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Boleta de Pago");
        setResizable(false);

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelTitulo.setText("Detalle de Boleta de Pago");

        jLabelEmpleado.setText("Empleado:");

        txtEmpleado.setEditable(false);

        jLabelSueldo.setText("Sueldo Base:");

        txtSueldoBase.setEditable(false);

        jLabelHorasOrd.setText("Horas Ordinarias:");

        txtHorasOrd.setEditable(false);

        jLabelHorasExtra.setText("Horas Extra:");

        txtHorasExtra.setEditable(false);

        jLabelTotalIngresos.setText("Total Ingresos:");

        txtTotalIngresos.setEditable(false);
        txtTotalIngresos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabelTotalDeducciones.setText("Total Deducciones:");

        txtTotalDeducciones.setEditable(false);
        txtTotalDeducciones.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabelLiquido.setText("Líquido a Pagar:");

        txtLiquidoPagar.setEditable(false);
        txtLiquidoPagar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtLiquidoPagar.setForeground(new java.awt.Color(0, 0, 255));

        jTableDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Concepto", "Tipo", "Monto"
            }
        ));
        jScrollPaneDetalles.setViewportView(jTableDetalles);

        btnCerrar.setText("Cerrar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelEmpleado)
                                .addGap(6, 6, 6)
                                .addComponent(txtEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelSueldo)
                                .addGap(6, 6, 6)
                                .addComponent(txtSueldoBase, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelHorasOrd)
                                .addGap(6, 6, 6)
                                .addComponent(txtHorasOrd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelHorasExtra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHorasExtra, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelTotalIngresos)
                                .addGap(6, 6, 6)
                                .addComponent(txtTotalIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelTotalDeducciones)
                                .addGap(6, 6, 6)
                                .addComponent(txtTotalDeducciones, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelLiquido)
                                .addGap(6, 6, 6)
                                .addComponent(txtLiquidoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPaneDetalles))
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitulo)
                    .addComponent(btnCerrar))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmpleado)
                    .addComponent(txtEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSueldo)
                    .addComponent(txtSueldoBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHorasOrd)
                    .addComponent(txtHorasOrd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHorasExtra)
                    .addComponent(txtHorasExtra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalIngresos)
                    .addComponent(txtTotalIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTotalDeducciones)
                    .addComponent(txtTotalDeducciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLiquido)
                    .addComponent(txtLiquidoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel jLabelEmpleado;
    private javax.swing.JLabel jLabelHorasExtra;
    private javax.swing.JLabel jLabelHorasOrd;
    private javax.swing.JLabel jLabelLiquido;
    private javax.swing.JLabel jLabelSueldo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotalDeducciones;
    private javax.swing.JLabel jLabelTotalIngresos;
    private javax.swing.JScrollPane jScrollPaneDetalles;
    private javax.swing.JTable jTableDetalles;
    private javax.swing.JTextField txtEmpleado;
    private javax.swing.JTextField txtHorasExtra;
    private javax.swing.JTextField txtHorasOrd;
    private javax.swing.JTextField txtLiquidoPagar;
    private javax.swing.JTextField txtSueldoBase;
    private javax.swing.JTextField txtTotalDeducciones;
    private javax.swing.JTextField txtTotalIngresos;
    // End of variables declaration//GEN-END:variables
}
