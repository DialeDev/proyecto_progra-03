package dao.memory;

import dao.interfaces.IPlanillaDAO;
import models.Planilla;
import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

/**
 *
 * @author DialeMRZ
 */

public class PlanillaDAOMemory implements IPlanillaDAO {
    
    //db temporal
    private final List<Planilla> planillasDB =new ArrayList<>();
    
    //simulador identity
    private int contadorId = 1;

    @Override
    public void crear(Planilla planilla) {
        planilla.setIdPlanilla(contadorId++);
        planillasDB.add(planilla);
    }

    @Override
    public List<Planilla> obtenerTodas() {
        return List.copyOf(planillasDB);
    }

    @Override
    public Optional<Planilla>obtenerPorId(Integer Id){
        return planillasDB.stream()
                .filter(p->p.getIdPlanilla().equals(Id))
                .findFirst();
    }
    
    @Override
    public boolean actualizarEstado(Integer id, Planilla.EstadoPlanilla nuevoEstado){
        return obtenerPorId(id).map(planilla->{
            planilla.setEstado(nuevoEstado);
            return true;
        }).orElse(false);
    }
}