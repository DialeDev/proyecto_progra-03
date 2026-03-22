package dao.interfaces;

import java.util.List;
import models.Empleado;

/**
 *
 * @author DialeMRZ
 */

public interface IEmpleadoDAO {
    void create(Empleado empleado);
    Empleado findById(int idEmpleado);
    List<Empleado> findAll();
    void update(Empleado empleado);
    void delete(int idEmpleado); 
}
