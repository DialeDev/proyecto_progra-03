package dao.memory;

import java.util.ArrayList;
import java.util.List;
import models.*;

/**
 *
 * @author DialeMRZ
 */

public class DatabaseMock {
    public static List<Empleado> tablaEmpleados = new ArrayList<>();
    public static List<Contrato> tablaContratos = new ArrayList<>();
    public static List<Contratacion> tablaContrataciones = new ArrayList<>();
    public static List<RegistroAsistencia> tablaAsistencias = new ArrayList<>();
    public static List<Usuario> tablaUsuarios = new ArrayList<>();
    public static List<Planilla> tablaPlanillas = new ArrayList<>();
    public static List<Jornada> tablaJornadas = new ArrayList<>();
    
    public static int autoIncrementEmpleado = 1;
    public static int autoIncrementContratacion = 1;

}
