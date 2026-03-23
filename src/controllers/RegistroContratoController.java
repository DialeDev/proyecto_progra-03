package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import views.FrmRegistroContratos;
import views.FrmGestionContratos;

public class RegistroContratoController implements ActionListener {

    private FrmRegistroContratos vistaRegistro;
    private FrmGestionContratos vistaPrincipal; // Agregamos la vista de la tabla

    // Modificamos el constructor para recibir ambas vistas
    public RegistroContratoController(FrmRegistroContratos vistaRegistro, FrmGestionContratos vistaPrincipal) {
        this.vistaRegistro = vistaRegistro;
        this.vistaPrincipal = vistaPrincipal;
        
        this.vistaRegistro.getBtnGuardar().addActionListener(this);
        this.vistaRegistro.getBtnCancelar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vistaRegistro.getBtnGuardar()) {
            

            String empleado = vistaRegistro.getTxtEmpleado().getText();
            String tipo = vistaRegistro.getTxtTipo().getText();


            // 2. Obtener la tabla de la ventana principal
            DefaultTableModel modelo = (DefaultTableModel) vistaPrincipal.getTablaContratos().getModel();
            

            modelo.addRow(new Object[]{"N/A", empleado, tipo, "Hoy", "Mañana", "$1000", "Sin obs"});
            
            vistaRegistro.dispose();
            
        } else if (e.getSource() == vistaRegistro.getBtnCancelar()) {
            vistaRegistro.dispose();
        }
    }
}
