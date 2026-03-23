package dao.interfaces;

import models.Planilla;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author DialeMRZ
 */

public interface IPlanillaDAO {
    //guardar nueva planilla
    void crear(Planilla planilla);
    //listar todo
    List<Planilla> obtenerTodas();
    //obtener planilla existente
    Optional<Planilla>obtenerPorId(Integer id);
    //cambiar estado
    boolean actualizarEstado(Integer id, Planilla.EstadoPlanilla nuevoEstado);
}
