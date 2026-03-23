package controllers;
import dao.interfaces.IPlanillaDAO;
import dao.memory.PlanillaDAOMemory;
import models.Planilla;
import models.Planilla.EstadoPlanilla;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author DialeMRZ
 */
public class PlanillaController {
    
    private final IPlanillaDAO planillaDAO;

    public PlanillaController() {
        this.planillaDAO = new PlanillaDAOMemory();
    }
    
    //creacion planilla en borrador
    public void generarNuevaPlanilla(int mes, int anio) {
        Planilla nuevaPlanilla = new Planilla(
            0,
            mes, 
            anio, 
            LocalDate.now(), 
            EstadoPlanilla.BORRADOR
        );
        planillaDAO.crear(nuevaPlanilla);
        System.out.println("Planilla del mes " + mes + " generada con éxito.");
    }

    //historial
    public List<Planilla> listarTodas() {
        return planillaDAO.obtenerTodas();
    }

    //listado por estado
    public List<Planilla> filtrarPorEstado(EstadoPlanilla estadoBuscado) {
        return planillaDAO.obtenerTodas().stream()
                .filter(planilla -> planilla.getEstado() == estadoBuscado)
                .toList(); // Empaqueta el resultado en una nueva lista
    }

    //buscar por id
    public Optional<Planilla> buscarPlanilla(Integer id) {
        return planillaDAO.obtenerPorId(id);
    }

    //actualizar estado
    public boolean emitirPlanilla(Integer id) {
        return planillaDAO.actualizarEstado(id, EstadoPlanilla.EMITIDA);
    }
    
    public boolean anularPlanilla(Integer id) {
        return planillaDAO.actualizarEstado(id, EstadoPlanilla.ANULADA);
    }
}
