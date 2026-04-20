package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
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
// 1. Extraemos los datos de las listas desplegables (ComboBox)
            String empleado = vistaRegistro.getCmbEmpleado().getSelectedItem().toString();
            String tipo = vistaRegistro.getCmbTipo().getSelectedItem().toString();
            
            // 2. Extraemos los datos de las cajas de texto normales
            String inicio = vistaRegistro.getTxtInicio().getText();
            String fin = vistaRegistro.getTxtFin().getText();
            String salario = vistaRegistro.getTxtSalario().getText();
            String observaciones = vistaRegistro.getTxtObservaciones().getText();

            // 3. Obtenemos la tabla de la ventana principal
            javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vistaPrincipal.getTablaContratos().getModel();
            
            // 4. Agregamos la fila usando NUESTRAS VARIABLES (¡Adiós al texto de mentiritas!)
            modelo.addRow(new Object[]{"N/A", empleado, tipo, inicio, fin, salario, observaciones});
            
            // 5. Cerramos la ventanita de registro
            vistaRegistro.dispose();
    }
    

}
