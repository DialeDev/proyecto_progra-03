package controllers;

import dao.RepositorioAsistencia;
import dao.RepositorioEmpleado;
import models.Empleado;
import models.RegistroAsistencia;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class AsistenciaController {
    private final RepositorioAsistencia asistenciaDAO;
    private final RepositorioEmpleado empleadoDAO;

    public AsistenciaController() {
        this.asistenciaDAO = new RepositorioAsistencia();
        this.empleadoDAO = new RepositorioEmpleado();
    }

    // Delegar empleados al controlador
    public List<Empleado> listarEmpleadosActivos() {
        return empleadoDAO.findAll().stream().filter(Empleado::isActivo).toList();
    }

    public Empleado buscarEmpleadoPorId(int id) {
        return empleadoDAO.findById(id).orElse(null);
    }

    public RegistroAsistencia marcarEntrada(int idEmpleado) {
        Optional<RegistroAsistencia> activa = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (activa.isPresent()) return null;
        RegistroAsistencia nuevo = new RegistroAsistencia(0, idEmpleado, LocalDateTime.now());
        nuevo.setFaltaJustificada(false);
        asistenciaDAO.registrar(nuevo);
        return nuevo;
    }

    public boolean marcarSalida(int idEmpleado) {
        Optional<RegistroAsistencia> opt = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (opt.isEmpty()) return false;
        RegistroAsistencia activa = opt.get();
        activa.setMarcaSalida(LocalDateTime.now());
        asistenciaDAO.actualizarSalida(activa);
        return true;
    }

    public RegistroAsistencia marcarEntrada(int idEmpleado, LocalDateTime fechaHora) {
        Optional<RegistroAsistencia> activa = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (activa.isPresent()) return null;
        RegistroAsistencia nuevo = new RegistroAsistencia(0, idEmpleado, fechaHora);
        nuevo.setFaltaJustificada(false);
        asistenciaDAO.registrar(nuevo);
        return nuevo;
    }

    public boolean marcarSalida(int idEmpleado, LocalDateTime fechaHora) {
        Optional<RegistroAsistencia> opt = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (opt.isEmpty()) return false;
        RegistroAsistencia activa = opt.get();
        if (fechaHora.isBefore(activa.getMarcaEntrada())) {
            throw new IllegalArgumentException("La hora de salida no puede ser anterior a la hora de entrada.");
        }
        activa.setMarcaSalida(fechaHora);
        asistenciaDAO.actualizarSalida(activa);
        return true;
    }

    public List<RegistroAsistencia> obtenerAsistenciasDelDia(int idEmpleado) {
        return obtenerAsistenciasPorEmpleadoYFecha(idEmpleado, LocalDate.now());
    }

    public List<RegistroAsistencia> obtenerAsistenciasDelDia(int idEmpleado, LocalDate fecha) {
        return obtenerAsistenciasPorEmpleadoYFecha(idEmpleado, fecha);
    }

    public List<RegistroAsistencia> obtenerAsistenciasPorEmpleado(int idEmpleado) {
        LocalDate ahora = LocalDate.now();
        return asistenciaDAO.findByEmpleadoAndMes(idEmpleado, ahora.getMonthValue(), ahora.getYear());
    }

    public double calcularHorasTrabajadas(RegistroAsistencia asistencia) {
        if (asistencia == null || asistencia.getMarcaSalida() == null) return -1;
        long minutos = ChronoUnit.MINUTES.between(asistencia.getMarcaEntrada(), asistencia.getMarcaSalida());
        return Math.round(minutos / 60.0 * 100.0) / 100.0;
    }

    public void eliminarAsistencia(long idRegistro) {
        asistenciaDAO.eliminar(idRegistro);
    }

    public RegistroAsistencia tieneEntradaActiva(int idEmpleado) {
        return asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado).orElse(null);
    }

    private List<RegistroAsistencia> obtenerAsistenciasPorEmpleadoYFecha(int idEmpleado, LocalDate fecha) {
        return asistenciaDAO.findByEmpleadoAndMes(idEmpleado, fecha.getMonthValue(), fecha.getYear()).stream()
            .filter(r -> r.getMarcaEntrada().toLocalDate().equals(fecha)).toList();
    }
}
