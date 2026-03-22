package dao.interfaces;

import models.RegistroAsistencia;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public interface IAsistenciaDAO {
    void registrar(RegistroAsistencia asistencia);
    
    RegistroAsistencia findEntradaActivaByEmpleado(int idEmpleado); 
    
    void actualizarSalida(RegistroAsistencia asistencia);
    
    List<RegistroAsistencia> findByEmpleadoAndMes(int idEmpleado, int mes, int anio);
}
