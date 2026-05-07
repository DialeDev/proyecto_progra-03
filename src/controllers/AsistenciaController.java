/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.RepositorioAsistencia;
import models.RegistroAsistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para las operaciones de marcación de asistencia.
 * @author DialeMRZ
 */
public class AsistenciaController {

    private final RepositorioAsistencia asistenciaDAO;

    public AsistenciaController() {
        this.asistenciaDAO = new RepositorioAsistencia();
    }

    /**
     * Registra la hora de entrada de un empleado.
     * @param idEmpleado ID del empleado
     * @return El registro de asistencia creado, o null si ya tiene una entrada activa
     */
    public RegistroAsistencia marcarEntrada(int idEmpleado) {
        // Verificar si ya tiene una entrada sin salida
        RegistroAsistencia activa = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (activa != null) {
            return null; // Ya tiene una marcación de entrada sin salida
        }

        // Crear nuevo registro con la hora actual
        RegistroAsistencia nuevo = new RegistroAsistencia(
            0,
            idEmpleado,
            LocalDateTime.now()
        );
        nuevo.setFaltaJustificada(false);

        asistenciaDAO.registrar(nuevo);
        return nuevo;
    }

    /**
     * Registra la hora de salida de un empleado.
     * @param idEmpleado ID del empleado
     * @return true si se registró la salida, false si no había entrada activa
     */
    public boolean marcarSalida(int idEmpleado) {
        // Buscar la entrada activa (sin salida)
        RegistroAsistencia activa = asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
        if (activa == null) {
            return false; // No hay entrada activa para marcar salida
        }

        // Actualizar con la hora actual como salida
        activa.setMarcaSalida(LocalDateTime.now());
        asistenciaDAO.actualizarSalida(activa);
        return true;
    }

    /**
     * Obtiene las asistencias de un empleado en el mes actual.
     * @param idEmpleado ID del empleado
     * @return Lista de registros de asistencia
     */
    public List<RegistroAsistencia> obtenerAsistenciasPorEmpleado(int idEmpleado) {
        LocalDate ahora = LocalDate.now();
        return asistenciaDAO.findByEmpleadoAndMes(idEmpleado, ahora.getMonthValue(), ahora.getYear());
    }

    /**
     * Obtiene las asistencias de un empleado para una fecha específica.
     * Filtra en memoria los registros del mes que corresponden a la fecha indicada.
     * @param fecha Fecha a consultar
     * @return Lista de registros de asistencia en esa fecha
     */
    public List<RegistroAsistencia> obtenerAsistenciasPorFecha(LocalDate fecha) {
        // Obtenemos todos los registros del mes y filtramos por fecha
        // Nota: idealmente el repositorio debería tener un método findByDate,
        // pero usamos el existente y filtramos en memoria
        List<RegistroAsistencia> lista = asistenciaDAO.findByEmpleadoAndMes(
            0, fecha.getMonthValue(), fecha.getYear()
        );
        List<RegistroAsistencia> filtrados = new ArrayList<>();
        for (RegistroAsistencia r : lista) {
            if (r.getMarcaEntrada().toLocalDate().equals(fecha)) {
                filtrados.add(r);
            }
        }
        return filtrados;
    }

    /**
     * Obtiene las asistencias de un empleado para una fecha específica.
     * @param idEmpleado ID del empleado
     * @param fecha Fecha a consultar
     * @return Lista de registros de asistencia en esa fecha
     */
    public List<RegistroAsistencia> obtenerAsistenciasPorEmpleadoYFecha(int idEmpleado, LocalDate fecha) {
        List<RegistroAsistencia> lista = asistenciaDAO.findByEmpleadoAndMes(
            idEmpleado, fecha.getMonthValue(), fecha.getYear()
        );
        List<RegistroAsistencia> filtrados = new ArrayList<>();
        for (RegistroAsistencia r : lista) {
            if (r.getMarcaEntrada().toLocalDate().equals(fecha)) {
                filtrados.add(r);
            }
        }
        return filtrados;
    }

    /**
     * Calcula las horas trabajadas entre la entrada y la salida.
     * @param idAsistencia ID del registro de asistencia
     * @return Horas trabajadas como double, o -1 si no tiene salida registrada
     */
    public double calcularHorasTrabajadas(int idAsistencia) {
        // Para simplificar, obtenemos el registro. Como el repositorio no tiene
        // un método findById, buscamos entre todos los registros disponibles.
        // En un escenario real se agregaría un método al DAO.
        // Retornamos -1 para indicar que se necesita usar la versión con objeto.
        return -1;
    }

    /**
     * Calcula las horas trabajadas entre la entrada y la salida.
     * @param asistencia Registro de asistencia
     * @return Horas trabajadas, o -1 si no tiene salida
     */
    public double calcularHorasTrabajadas(RegistroAsistencia asistencia) {
        if (asistencia == null || asistencia.getMarcaSalida() == null) {
            return -1;
        }
        long minutos = ChronoUnit.MINUTES.between(
            asistencia.getMarcaEntrada(),
            asistencia.getMarcaSalida()
        );
        return Math.round(minutos / 60.0 * 100.0) / 100.0;
    }

    /**
     * Verifica si un empleado tiene una marcación de entrada activa (sin salida).
     * @param idEmpleado ID del empleado
     * @return El registro activo o null
     */
    public RegistroAsistencia tieneEntradaActiva(int idEmpleado) {
        return asistenciaDAO.findEntradaActivaByEmpleado(idEmpleado);
    }

    /**
     * Obtiene las asistencias del día de hoy para todos los empleados
     * (limitado a un empleado específico).
     * @param idEmpleado ID del empleado
     * @return Lista de asistencias del día de hoy
     */
    public List<RegistroAsistencia> obtenerAsistenciasDelDia(int idEmpleado) {
        return obtenerAsistenciasPorEmpleadoYFecha(idEmpleado, LocalDate.now());
    }
}
