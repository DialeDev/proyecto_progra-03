package dao.interfaces;

import java.util.List;
import models.Contratacion;

/**
 *
 * @author DialeMRZ
 */

public interface IContratacionDAO {
    void create(Contratacion contratacion);
    Contratacion findContratoActivoByEmpleado(int idEmpleado);
    List<Contratacion> findHistorialByEmpleado(int idEmpleado);
    void update(Contratacion contratacion); 
}
