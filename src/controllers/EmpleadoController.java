package controllers;

import dao.interfaces.IEmpleadoDAO;
import dao.memory.EmpleadoDAOMemory;
import models.Empleado;
import views.FrmGestionEmpleados;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class EmpleadoController {

    private final IEmpleadoDAO empleadoDAO;
    private final FrmGestionEmpleados vista;
    private DefaultTableModel modeloTabla;

    public EmpleadoController(FrmGestionEmpleados vista) {
        this.vista = vista;
        // Instansiamos el DAO para manejar la persistencia (en este caso, en memoria)
        this.empleadoDAO = new EmpleadoDAOMemory(); 
        
        // Inicializamos la vista al momento de construir el controlador
        initView();
    }

    // Configura el estado inicial de la pantalla 
    private void initView() {
        // 1. Configurar la estructura de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DUI");
        modeloTabla.addColumn("Nombres");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Estado");
        
        // Asignamos el modelo a la tabla visual del JFrame
        vista.getTblEmpleados().setModel(modeloTabla);

        // 2. Cargar datos si los hay
        cargarTabla();
    }

  
     //Extrae los datos de la vista, los valida y los envía al DAO.
    
    public void guardarEmpleado() {
        // 1. Extracción (La vista usa Getters)
        String nombres = vista.getTxtNombres().getText().trim();
        String apellidos = vista.getTxtApellidos().getText().trim();
        String dui = vista.getTxtDui().getText().trim();
        // Asumimos un departamento por defecto para el prototipo 
        int idDepartamento = 1; 

        // 2. Validación
        if (nombres.isEmpty() || apellidos.isEmpty() || dui.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", 
                                          "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación de formato básico de DUI 
        if (!dui.matches("\\d{8}-\\d")) {
            JOptionPane.showMessageDialog(vista, "El formato del DUI es incorrecto (ej: 12345678-9).", 
                                          "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Creación del modelo 
        Empleado nuevoEmpleado = new Empleado(
            0, // El ID se auto-generará en el DAO
            idDepartamento, 
            nombres, 
            apellidos, 
            dui
        );

        // 4. Persistencia
        empleadoDAO.create(nuevoEmpleado);

        // 5. Actualización de la UI
        JOptionPane.showMessageDialog(vista, "Empleado registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
        cargarTabla(); // Refresca la cuadrícula
    }

    
    // Pide la lista al DAO y repinta la tabla.
  
    private void cargarTabla() {
        // Limpiar filas anteriores para no duplicar datos
        modeloTabla.setRowCount(0);

        List<Empleado> lista = empleadoDAO.findAll();

        // Convertir cada objeto Empleado en una fila (Arreglo de Objetos)
        for (Empleado e : lista) {
            Object[] fila = new Object[5];
            fila[0] = e.getIdEmpleado();
            fila[1] = e.getDui();
            fila[2] = e.getNombres();
            fila[3] = e.getApellidos();
            fila[4] = e.isActivo() ? "Activo" : "Inactivo";
            
            modeloTabla.addRow(fila);
        }
    }

    
    // Devuelve la pantalla a su estado original.
    
    public void limpiarFormulario() {
        vista.getTxtNombres().setText("");
        vista.getTxtApellidos().setText("");
        vista.getTxtDui().setText("");
        vista.getTblEmpleados().clearSelection();
        modeloTabla.setRowCount(0);
        vista.getTxtNombres().requestFocus();
    }


   
      //Captura el empleado seleccionado en la tabla, pide confirmación 
      //y realiza un borrado lógico (desactivación).
     
    public void desactivarEmpleado() {
        // 1. Obtener el índice de la fila seleccionada en la vista
        int filaSeleccionada = vista.getTblEmpleados().getSelectedRow();

        // 2. Validación: ¿Seleccionó algo?
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un empleado de la tabla para desactivarlo.", 
                                          "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Extracción de la Llave Primaria (ID) desde el modelo de la tabla
        // Sabemos que el ID está en la columna 0, según nuestro método initView()
        int idEmpleado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreEmpleado = (String) modeloTabla.getValueAt(filaSeleccionada, 2); // Columna de Nombres

        // 4. Confirmación de seguridad 
        int respuesta = JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro que desea dar de baja al empleado " + nombreEmpleado + "?", 
                "Confirmar Desactivación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            // 5. Persistencia 
            empleadoDAO.delete(idEmpleado);

            // 6. Actualización de la UI
            JOptionPane.showMessageDialog(vista, "Empleado desactivado exitosamente.", 
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla(); // Refresca la tabla para mostrar el nuevo estado "Inactivo"
            limpiarFormulario(); // Limpia las cajas de texto por si habían datos escritos
        }
    }

}
