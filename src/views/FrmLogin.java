package views;

import controllers.AuthController;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

// @author DialeMRZ
public class FrmLogin extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmLogin.class.getName());
    private AuthController controller;

    public FrmLogin() {
        initComponents();
        configurarComponentes();
        this.setLocationRelativeTo(null);
        this.controller = new AuthController(this);
    }

    private void configurarComponentes() {
        
        btnMostrarPassword.addActionListener(e -> {
            
            if (btnMostrarPassword.isSelected()) {
                
                txtPassword.setEchoChar((char) 0);
                btnMostrarPassword.setIcon(new ImageIcon("src\\img\\ojo-cruzado.png"));
                
            } else {
                
                txtPassword.setEchoChar('*');
                btnMostrarPassword.setIcon(new ImageIcon("src\\img\\ojo.png"));
                
            }
        });
        
    }

    public JPasswordField getTxtPassword() { return txtPassword; }
    public JTextField getTxtUsuario() { return txtUsuario; }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblContrasena = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnMostrarPassword = new javax.swing.JToggleButton();
        txtPassword = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        jLabel1.setText("Usuario");

        lblContrasena.setText("Contraseña");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Iniciar Sesión");

        btnMostrarPassword.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnMostrarPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojo.png"))); // NOI18N

        btnIngresar.setText("Iniciar Sesión");
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(lblContrasena)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMostrarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(49, 49, 49))))
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblContrasena)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarPassword))
                .addGap(18, 18, 18)
                .addComponent(btnIngresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiar)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
        txtPassword.setText("");
        txtUsuario.setText("");
        
        
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        
        try {
            
            controller.procesarLogin();
            
        } catch (Exception e) {
            // Manejo global: captura cualquier excepción no controlada y la muestra al usuario
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error al procesar el inicio de sesión: " + e.getMessage(),
                "Error del Sistema", javax.swing.JOptionPane.ERROR_MESSAGE);
            logger.severe("Error en btnIngresarActionPerformed: " + e.getMessage());
        }
    }//GEN-LAST:event_btnIngresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JToggleButton btnMostrarPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblContrasena;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
