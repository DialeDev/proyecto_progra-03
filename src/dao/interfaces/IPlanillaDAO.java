package dao.interfaces;

import models.Planilla;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public interface IPlanillaDAO {
    void create(Planilla planilla);
    Planilla findById(int idPlanilla);
    List<Planilla> findAll();
    void updateEstado(int idPlanilla, Planilla.EstadoPlanilla nuevoEstado);
}
