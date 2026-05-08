package views;

// @author DialeMRZ
public class FrmDashboard extends javax.swing.JFrame {
    private controllers.DashboardController controller;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDashboard.class.getName());

    public FrmDashboard() {
        initComponents();
        this.getContentPane().setBackground(java.awt.Color.WHITE);
        this.setLocationRelativeTo(null);
        this.controller = new controllers.DashboardController(this);
        if (utils.SesionGlobal.haySesionActiva()) {
            lblBienvenida.setText("Bienvenid@ al Sistema de Planilla");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblBienvenida = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itmEmpleado = new javax.swing.JMenuItem();
        itmAsistencia = new javax.swing.JMenuItem();
        itmPlanillas = new javax.swing.JMenuItem();
        itmContratos = new javax.swing.JMenuItem();
        itmHorarios = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        itmCerrarSesion = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Planillas");
        lblBienvenida.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Seleccione una opción del menú para comenzar");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Cargar imagen del dashboard con null-check para evitar NullPointerException
        var imgUrl = getClass().getResource("/img/human_resources_search_employees_icon_187488.png");
        jLabel2.setIcon(imgUrl != null ? new javax.swing.ImageIcon(imgUrl) : null);
        jMenu1.setText("Gestión");
        itmEmpleado.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmEmpleado.setText("Empleados");
        itmEmpleado.addActionListener(this::itmEmpleadoActionPerformed);
        jMenu1.add(itmEmpleado);
        itmAsistencia.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmAsistencia.setText("Marcación");
        itmAsistencia.addActionListener(this::itmAsistenciaActionPerformed);
        jMenu1.add(itmAsistencia);
        itmPlanillas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmPlanillas.setText("Planillas");
        itmPlanillas.addActionListener(this::itmPlanillasActionPerformed);
        jMenu1.add(itmPlanillas);
        itmContratos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmContratos.setText("Contratos");
        itmContratos.addActionListener(this::itmContratosActionPerformed);
        jMenu1.add(itmContratos);
        itmHorarios.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmHorarios.setText("Horarios");
        itmHorarios.addActionListener(this::itmHorariosActionPerformed);
        jMenu1.add(itmHorarios);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Sistema");
        itmCerrarSesion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmCerrarSesion.setText("Cerrar Sesión");
        itmCerrarSesion.addActionListener(this::itmCerrarSesionActionPerformed);
        jMenu2.add(itmCerrarSesion);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addGap(150, 150, 150).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup().addGap(73, 73, 73).addComponent(lblBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(42, 42, 42).addComponent(lblBienvenida)
                .addGap(18, 18, 18).addComponent(jLabel2).addGap(18, 18, 18).addComponent(jLabel1).addContainerGap(39, Short.MAX_VALUE)));
        pack();
    }

    private void itmEmpleadoActionPerformed(java.awt.event.ActionEvent evt) { controller.abrirGestionEmpleados(); }
    private void itmAsistenciaActionPerformed(java.awt.event.ActionEvent evt) { controller.abrirMarcacion(); }
    private void itmPlanillasActionPerformed(java.awt.event.ActionEvent evt) { controller.abrirPlanillas(); }
    private void itmContratosActionPerformed(java.awt.event.ActionEvent evt) { controller.abrirGestionContratos(); }
    private void itmHorariosActionPerformed(java.awt.event.ActionEvent evt) { controller.abrirGestionHorarios(); }
    private void itmCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) { controller.cerrarSesion(); }

    // Variables declaration - do not modify
    private javax.swing.JMenuItem itmAsistencia;
    private javax.swing.JMenuItem itmCerrarSesion;
    private javax.swing.JMenuItem itmContratos;
    private javax.swing.JMenuItem itmEmpleado;
    private javax.swing.JMenuItem itmHorarios;
    private javax.swing.JMenuItem itmPlanillas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lblBienvenida;
}
