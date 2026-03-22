package dao.memory;

import dao.interfaces.IEmpleadoDAO;
import models.Empleado;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class EmpleadoDAOMemory implements IEmpleadoDAO {

    @Override
    public void create(Empleado empleado) {
        empleado.setIdEmpleado(DatabaseMock.autoIncrementEmpleado++);
        DatabaseMock.tablaEmpleados.add(empleado);
    }

    @Override
    public Empleado findById(int idEmpleado) {
        for (Empleado e : DatabaseMock.tablaEmpleados) {
            if (e.getIdEmpleado() == idEmpleado) {
                return e;
            }
        }
        return null; 
    }

    @Override
    public List<Empleado> findAll() {
        return DatabaseMock.tablaEmpleados; 
    }

    @Override
    public void update(Empleado empleadoActualizado) {
        for (int i = 0; i < DatabaseMock.tablaEmpleados.size(); i++) {
            if (DatabaseMock.tablaEmpleados.get(i).getIdEmpleado() == empleadoActualizado.getIdEmpleado()) {
                DatabaseMock.tablaEmpleados.set(i, empleadoActualizado);
                break;
            }
        }
    }

    @Override
    public void delete(int idEmpleado) {
        Empleado e = findById(idEmpleado);
        if (e != null) {
            e.setActivo(false); 
        }
    }
}
