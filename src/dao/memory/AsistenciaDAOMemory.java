package dao.memory;

import dao.interfaces.IAsistenciaDAO;
import models.RegistroAsistencia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DialeMRZ
 */

public class AsistenciaDAOMemory implements IAsistenciaDAO {

    @Override
    public void registrar(RegistroAsistencia asistencia) {
        DatabaseMock.tablaAsistencias.add(asistencia);
    }

    @Override
    public RegistroAsistencia findEntradaActivaByEmpleado(int idEmpleado) {
        for (RegistroAsistencia r : DatabaseMock.tablaAsistencias) {
            if (r.getIdEmpleado() == idEmpleado && r.getMarcaSalida() == null) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void actualizarSalida(RegistroAsistencia asistenciaActualizada) {
        for (int i = 0; i < DatabaseMock.tablaAsistencias.size(); i++) {
            if (DatabaseMock.tablaAsistencias.get(i).getIdRegistro() == asistenciaActualizada.getIdRegistro()) {
                DatabaseMock.tablaAsistencias.set(i, asistenciaActualizada);
                break;
            }
        }
    }

    @Override
    public List<RegistroAsistencia> findByEmpleadoAndMes(int idEmpleado, int mes, int anio) {
        List<RegistroAsistencia> registrosDelMes = new ArrayList<>();
        for (RegistroAsistencia r : DatabaseMock.tablaAsistencias) {
            if (r.getIdEmpleado() == idEmpleado && 
                r.getMarcaEntrada().getMonthValue() == mes && 
                r.getMarcaEntrada().getYear() == anio) {
                registrosDelMes.add(r);
            }
        }
        return registrosDelMes;
    }
}
