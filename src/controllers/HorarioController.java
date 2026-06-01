package controllers;

import dao.RepositorioJornada;
import models.Jornada;
import utils.SesionGlobal;
import views.FrmGestionHorarios;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.table.DefaultTableModel;

// @author DialeMRZ
public class HorarioController {
    private final RepositorioJornada jornadaDAO;
    private final FrmGestionHorarios vista;
    private boolean editando = false;

    public HorarioController(FrmGestionHorarios vista) {
        this.vista = vista;
        this.jornadaDAO = new RepositorioJornada();
    }

    public void guardarJornada() {
        if (!SesionGlobal.puedeOperar("Horarios")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            String entradaTxt = vista.getTxtEntrada().getText().trim();
            String salidaTxt = vista.getTxtSalida().getText().trim();
            String toleranciaTxt = vista.getTxtTolerancia().getText().trim();
            if (nombre.isEmpty() || entradaTxt.isEmpty() || salidaTxt.isEmpty() || toleranciaTxt.isEmpty()) {
                vista.mostrarMensaje("advertencia", "Complete todos los campos.");
                return;
            }
            LocalTime entrada = LocalTime.parse(entradaTxt);
            LocalTime salida = LocalTime.parse(salidaTxt);
            int tolerancia = Integer.parseInt(toleranciaTxt);
            if (tolerancia < 0) {
                vista.mostrarMensaje("advertencia", "La tolerancia no puede ser negativa.");
                return;
            }
            if (salida.isBefore(entrada)) {
                vista.mostrarMensaje("advertencia", "La salida debe ser después de la entrada.");
                return;
            }
            if (editando) {
                int fila = vista.getTblHorarios().getSelectedRow();
                if (fila == -1) { editando = false; vista.getBtnGuardar().setText("Guardar"); return; }
                DefaultTableModel model = (DefaultTableModel) vista.getTblHorarios().getModel();
                int id = ((Number) model.getValueAt(fila, 0)).intValue();
                if (existeHorario(nombre)) {
                    boolean esOtro = listar().stream()
                        .anyMatch(j -> j.getNombre().equalsIgnoreCase(nombre) && j.getIdJornada() != id);
                    if (esOtro) {
                        vista.mostrarMensaje("advertencia", "Ya existe otra jornada con el nombre '" + nombre + "'.");
                        return;
                    }
                }
                if (vista.mostrarConfirmacion("¿Actualizar '" + nombre + "'?")) {
                    if (jornadaDAO.existeTraslape(entrada, salida, id)) {
                        vista.mostrarMensaje("advertencia", "La jornada se traslapa con otra jornada activa existente.");
                        return;
                    }
                    jornadaDAO.update(new Jornada(id, nombre, entrada, salida, tolerancia));
                    vista.mostrarMensaje("exito", "Horario actualizado correctamente.");
                }
            } else {
                if (existeHorario(nombre)) {
                    vista.mostrarMensaje("advertencia", "Ya existe una jornada '" + nombre + "'.");
                    return;
                }
                if (jornadaDAO.existeTraslape(entrada, salida)) {
                    vista.mostrarMensaje("advertencia", "La jornada se traslapa con otra jornada activa existente.");
                    return;
                }
                jornadaDAO.create(new Jornada(0, nombre, entrada, salida, tolerancia));
                vista.mostrarMensaje("exito", "Horario guardado correctamente.");
            }
            cargarTabla();
            limpiar();
        } catch (DateTimeParseException e) {
            vista.mostrarMensaje("advertencia", "Use formato HH:mm (ej: 08:00)");
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("advertencia", "La tolerancia debe ser un número entero.");
        } catch (Exception e) {
            vista.mostrarMensaje("error", "Error: " + e.getMessage());
        }
    }

    public void eliminarJornada() {
        if (!SesionGlobal.puedeOperar("Horarios")) {
            vista.mostrarMensaje("error", "No tiene permisos para realizar esta operación.");
            return;
        }
        try {
            int fila = vista.getTblHorarios().getSelectedRow();
            if (fila == -1) { vista.mostrarMensaje("advertencia", "Seleccione una jornada."); return; }
            DefaultTableModel model = (DefaultTableModel) vista.getTblHorarios().getModel();
            int id = ((Number) model.getValueAt(fila, 0)).intValue();
            String nombre = (String) model.getValueAt(fila, 1);
            if (vista.mostrarConfirmacion("¿Eliminar '" + nombre + "'?")) {
                jornadaDAO.delete(id);
                vista.mostrarMensaje("exito", "Horario eliminado correctamente.");
                cargarTabla();
                limpiar();
            }
        } catch (Exception e) {
            vista.mostrarMensaje("error", "Error al eliminar jornada: " + e.getMessage());
        }
    }

    public void seleccionarJornada() {
        int fila = vista.getTblHorarios().getSelectedRow();
        if (fila == -1) return;
        DefaultTableModel model = (DefaultTableModel) vista.getTblHorarios().getModel();
        vista.getTxtNombre().setText(String.valueOf(model.getValueAt(fila, 1)));
        vista.getTxtEntrada().setText(String.valueOf(model.getValueAt(fila, 2)));
        vista.getTxtSalida().setText(String.valueOf(model.getValueAt(fila, 3)));
        vista.getTxtTolerancia().setText(String.valueOf(model.getValueAt(fila, 4)));
        editando = true;
        vista.getBtnGuardar().setText("Actualizar");
    }

    public void cargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID"); modelo.addColumn("Nombre"); modelo.addColumn("Entrada");
        modelo.addColumn("Salida"); modelo.addColumn("Tolerancia");
        jornadaDAO.findAll().forEach(j -> modelo.addRow(new Object[]{
            j.getIdJornada(), j.getNombre(), j.getHoraEntradaEsperada(), j.getHoraSalidaEsperada(), j.getMargenToleranciaMinutos()}));
        vista.getTblHorarios().setModel(modelo);
    }

    public void limpiar() {
        vista.getTxtNombre().setText(""); vista.getTxtEntrada().setText("");
        vista.getTxtSalida().setText(""); vista.getTxtTolerancia().setText("");
        vista.getTblHorarios().clearSelection(); editando = false;
        vista.getBtnGuardar().setText("Guardar"); vista.getTxtNombre().requestFocus();
    }

    public java.util.List<Jornada> listar() { return jornadaDAO.findAll(); }

    public boolean existeHorario(String nombre) {
        return jornadaDAO.findAll().stream().anyMatch(j -> j.getNombre().equalsIgnoreCase(nombre));
    }

    public String[] listarNombres() {
        return jornadaDAO.findAll().stream().map(Jornada::getNombre).toArray(String[]::new);
    }
}
