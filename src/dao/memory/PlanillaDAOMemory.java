package dao.memory;

import dao.interfaces.IPlanillaDAO;
import models.Planilla;
import java.util.ArrayList; 
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class PlanillaDAOMemory implements IPlanillaDAO {

    private static int autoIncrement = 1;

    @Override
    public void create(Planilla planilla) {
        planilla.setIdPlanilla(autoIncrement++);
        DatabaseMock.tablaPlanillas.add(planilla);
    }

    @Override
    public Planilla findById(int idPlanilla) {
        for (Planilla p : DatabaseMock.tablaPlanillas) {
            if (p.getIdPlanilla() == idPlanilla) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Planilla> findAll() {
        return DatabaseMock.tablaPlanillas;
    }

    @Override
    public void updateEstado(int idPlanilla, Planilla.EstadoPlanilla nuevoEstado) {
        Planilla p = findById(idPlanilla);
        if (p != null) {
            p.setEstado(nuevoEstado);
        }
    }
}