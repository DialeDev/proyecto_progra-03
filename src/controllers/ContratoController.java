package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import views.FrmGestionContratos;

public class ContratoController implements ActionListener {

    private FrmGestionContratos vista;
    private DefaultTableModel modeloTabla;
    // private ContratoDAO dao; // Descomenta esto cuando tengas tu DAO listo

    public ContratoController(FrmGestionContratos vista) {
        this.vista = vista;
        // this.dao = new ContratoDAOImpl(); // Inicializa tu DAO aquí
        System.out.println("¡EL CONTROLADOR DE CONTRATOS ESTÁ VIVO!");
        // 1. Añadimos los "Action Listeners" para que el controlador escuche los clics
        // Asegúrate de tener los "Getters" creados en tu vista para estos botones
        this.vista.getBtnNuevo().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnCerrar().addActionListener(this);
        
        // 2. Preparamos la tabla
        iniciarTabla();
        
        // 3. Cargamos los datos iniciales
        cargarTabla();
    }

    private void iniciarTabla() {
        modeloTabla = new DefaultTableModel();
        
        // Añadimos las columnas exactamente como las tienes en tu diseño
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Empleado");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Inicio");
        modeloTabla.addColumn("Fin");
        modeloTabla.addColumn("Salario");
        modeloTabla.addColumn("Observaciones");

        // Asignamos el modelo a la tabla de la vista
        // Asume que creaste el getter getTablaContratos() en tu FrmGestionContratos
        vista.getTablaContratos().setModel(modeloTabla);
    }

    private void cargarTabla() {
        // Limpiamos la tabla antes de cargar
        modeloTabla.setRowCount(0);
        

    }

@Override
    public void actionPerformed(ActionEvent e) {
        // Detectamos qué botón fue presionado
        
        if (e.getSource() == vista.getBtnNuevo()) {
            
            // --- AQUÍ ESTÁ EL CAMBIO ---
            // 1. Instanciamos la NUEVA vista que acabas de crear
            views.FrmRegistroContratos vistaRegistro = new views.FrmRegistroContratos();
            
            // 2. La centramos en la pantalla
            controllers.RegistroContratoController controladorRegistro = new controllers.RegistroContratoController(vistaRegistro,vista);
            
            vistaRegistro.setLocationRelativeTo(null);
            vistaRegistro.setVisible(true);
            
        } else if (e.getSource() == vista.getBtnEliminar()) {
            // Lógica para eliminar el contrato seleccionado
            eliminarContrato();
            
        } else if (e.getSource() == vista.getBtnCerrar()) {
            // Lógica para cerrar la ventana
            vista.dispose();
        }
    }

    private void eliminarContrato() {
        int filaSeleccionada = vista.getTablaContratos().getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un contrato de la tabla para eliminarlo.");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este contrato?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Obtenemos el ID de la fila seleccionada (columna 0)
            int idContrato = Integer.parseInt(vista.getTablaContratos().getValueAt(filaSeleccionada, 0).toString());
            
            JOptionPane.showMessageDialog(vista, "Contrato " + idContrato + " eliminado (Simulación)");
            
            // Recargamos la tabla para que desaparezca el registro
            cargarTabla();
        }
    }
}
