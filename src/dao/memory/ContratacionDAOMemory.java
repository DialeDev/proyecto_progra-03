package dao.memory;

import dao.interfaces.IContratacionDAO;
import models.Contratacion;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class ContratacionDAOMemory implements IContratacionDAO {

    @Override
    public void create(Contratacion contratacion) {
        contratacion.setIdContratacion(DatabaseMock.autoIncrementContratacion++);
        DatabaseMock.tablaContrataciones.add(contratacion);
    }

    @Override
    public Contratacion findContratoActivoByEmpleado(int idEmpleado) {
        for (Contratacion c : DatabaseMock.tablaContrataciones) {
            if (c.getIdEmpleado() == idEmpleado && c.getFechaFin() == null) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Contratacion> findHistorialByEmpleado(int idEmpleado) {
        List<Contratacion> historial = new ArrayList<>();
        for (Contratacion c : DatabaseMock.tablaContrataciones) {
            if (c.getIdEmpleado() == idEmpleado) {
                historial.add(c);
            }
        }
        return historial;
    }

    @Override
    public void update(Contratacion contratacion) {
        for (int i = 0; i < DatabaseMock.tablaContrataciones.size(); i++) {
            if (DatabaseMock.tablaContrataciones.get(i).getIdContratacion() == contratacion.getIdContratacion()) {
                DatabaseMock.tablaContrataciones.set(i, contratacion);
                break;
            }
        }
    }
}
