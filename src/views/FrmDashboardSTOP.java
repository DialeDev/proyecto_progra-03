package views;

// @author DialeMRZ
public class FrmDashboardSTOP extends javax.swing.JFrame {
    private controllers.DashboardController controller;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDashboardSTOP.class.getName());

    public FrmDashboardSTOP() {
        initComponents();
        configurarComponentes();
        this.getContentPane().setBackground(java.awt.Color.WHITE);
        this.setLocationRelativeTo(null);
        this.controller = new controllers.DashboardController(this);
        if (utils.SesionGlobal.haySesionActiva()) {
            lblBienvenida.setText("Bienvenid@ al Sistema de Planilla");
            lblUsuario.setText(utils.SesionGlobal.getUsuarioActivo().getUsername());
        }
    }

    /**
     * Configura todos los eventos de la UI.
     * Los eventos se asignan en código manual, NO en el .form.
     */
    private void configurarComponentes() {
        // Eventos de tarjetas de acceso rápido
        btnEmpleados.addActionListener(e -> controller.abrirGestionEmpleados());
        btnMarcacion.addActionListener(e -> controller.abrirMarcacion());
        btnPlanillas.addActionListener(e -> controller.abrirPlanillas());
        btnContratos.addActionListener(e -> controller.abrirGestionContratos());
        btnHorarios.addActionListener(e -> controller.abrirGestionHorarios());
        btnReportes.addActionListener(e -> controller.abrirReportes());
        btnDepartamentos.addActionListener(e -> controller.abrirDepartamentos());
        btnAsignacionJornada.addActionListener(e -> controller.abrirAsignacionJornada());
        btnCerrarSesion.addActionListener(e -> controller.cerrarSesion());
    }

    /**
     * Configura la apariencia de un botón tipo tarjeta.
     * Llamado desde initComponents() para mantener el código GEN limpio.
     */
    private void configurarTarjeta(javax.swing.JButton btn, String texto, String rutaIcono) {
        btn.setFont(new java.awt.Font("Segoe UI", 0, 11));
        btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn.setText(texto);
        btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        btn.setPreferredSize(new java.awt.Dimension(120, 100));
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setBackground(java.awt.Color.WHITE);
        // Cargar icono escalado con null-check para evitar NullPointerException
        var url = getClass().getResource(rutaIcono);
        if (url != null) {
            btn.setIcon(new javax.swing.ImageIcon(
                new javax.swing.ImageIcon(url).getImage().getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH)));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlHeader = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        pnlBienvenida = new javax.swing.JPanel();
        lblBienvenida = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        pnlCards = new javax.swing.JPanel();
        btnEmpleados = new javax.swing.JButton();
        btnMarcacion = new javax.swing.JButton();
        btnPlanillas = new javax.swing.JButton();
        btnContratos = new javax.swing.JButton();
        btnHorarios = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();
        btnDepartamentos = new javax.swing.JButton();
        btnAsignacionJornada = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Planillas");
        setResizable(false);

        // ---- Header superior oscuro ----
        pnlHeader.setBackground(new java.awt.Color(108, 91, 96));
        pnlHeader.setPreferredSize(new java.awt.Dimension(680, 40));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setText("Sistema de Planillas");
        lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));

        btnCerrarSesion.setBackground(new java.awt.Color(108, 91, 96));
        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 0, 12));
        btnCerrarSesion.setForeground(java.awt.Color.WHITE);
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        // Cargar icono de cerrar sesión con null-check
        var urlCerrar = getClass().getResource("/img/mbrilogout_99583.png");
        if (urlCerrar != null) {
            btnCerrarSesion.setIcon(new javax.swing.ImageIcon(
                new javax.swing.ImageIcon(urlCerrar).getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
        }

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHeaderLayout.createSequentialGroup()
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHeaderLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTitulo)
                        .addComponent(btnCerrarSesion)))
        );

        // ---- Área de bienvenida ----
        pnlBienvenida.setBackground(java.awt.Color.WHITE);

        lblBienvenida.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBienvenida.setText("Bienvenid@ al Sistema de Planilla");

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 0, 16));
        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSub.setForeground(new java.awt.Color(102, 102, 102));
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("Seleccione un módulo para comenzar");

        javax.swing.GroupLayout pnlBienvenidaLayout = new javax.swing.GroupLayout(pnlBienvenida);
        pnlBienvenida.setLayout(pnlBienvenidaLayout);
        pnlBienvenidaLayout.setHorizontalGroup(
            pnlBienvenidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBienvenidaLayout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(pnlBienvenidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(lblBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSub, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(50, Short.MAX_VALUE))
        );
        pnlBienvenidaLayout.setVerticalGroup(
            pnlBienvenidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBienvenidaLayout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(lblBienvenida)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblUsuario)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(lblSub)
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        // ---- Tarjetas de acceso rápido ----
        pnlCards.setBackground(java.awt.Color.WHITE);
        pnlCards.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Configurar cada botón con estilo de tarjeta (icono + texto)
        configurarTarjeta(btnEmpleados, "Empleados", "/img/human_resources_search_employees_icon_187488.png");
        configurarTarjeta(btnMarcacion, "Marcación", "/img/timeclock_tiemp_3924.png");
        configurarTarjeta(btnPlanillas, "Planillas", "/img/dollar_salary_income_growth_bag_profit_money_icon_232624.png");
        configurarTarjeta(btnContratos, "Contratos", "/img/contract_icon-icons.com_64813.png");
        configurarTarjeta(btnHorarios, "Horarios", "/img/calendar-with-a-clock-time-tools_icon-icons.com_56831.png");
        configurarTarjeta(btnReportes, "Reportes", "/img/modules_apps_icon_188198.png");
        configurarTarjeta(btnDepartamentos, "Departamentos", "/img/emblemsystem_93495.png");
        configurarTarjeta(btnAsignacionJornada, "Asignación Jornada", "/img/emblemsystem_93495.png");

        pnlCards.setLayout(new java.awt.GridLayout(2, 4, 10, 10));
        pnlCards.add(btnEmpleados);
        pnlCards.add(btnMarcacion);
        pnlCards.add(btnPlanillas);
        pnlCards.add(btnContratos);
        pnlCards.add(btnHorarios);
        pnlCards.add(btnReportes);
        pnlCards.add(btnDepartamentos);
        pnlCards.add(btnAsignacionJornada);

        // ---- Layout principal del JFrame ----
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(pnlBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20)
                    .addComponent(pnlBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10, 10, 10)
                    .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }//GEN-END:initComponents
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignacionJornada;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnContratos;
    private javax.swing.JButton btnDepartamentos;
    private javax.swing.JButton btnEmpleados;
    private javax.swing.JButton btnHorarios;
    private javax.swing.JButton btnMarcacion;
    private javax.swing.JButton btnPlanillas;
    private javax.swing.JButton btnReportes;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnlBienvenida;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables
}
