package controllers;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;

import models.Jornada;
import dao.memory.DatabaseMock;

/**
 *
 * @author DialeMRZ
 */

public class HorarioController {

    //GUARDAR
    public String guardar(Jornada jornada) {

        if (!validar(jornada)) {
            return "Datos inválidos";
        }

        jornada.setIdJornada(DatabaseMock.tablaJornadas.size() + 1);
        DatabaseMock.tablaJornadas.add(jornada);

        return "Horario guardado correctamente";
    }

    // LISTAR
    public List<Jornada> listar() {
        return DatabaseMock.tablaJornadas;
    }

    // BUSCAR POR ID
    public Jornada buscarPorId(int id) {
        for (Jornada j : DatabaseMock.tablaJornadas) {
            if (j.getIdJornada() == id) {
                return j;
            }
        }
        return null;
    }

    //ACTUALIZAR
    public String actualizar(Jornada jornada) {

        if (!validar(jornada)) {
            return "Datos inválidos";
        }

        for (Jornada j : DatabaseMock.tablaJornadas) {
            if (j.getIdJornada() == jornada.getIdJornada()) {
                j.setNombre(jornada.getNombre());
                j.setHoraEntradaEsperada(jornada.getHoraEntradaEsperada());
                j.setHoraSalidaEsperada(jornada.getHoraSalidaEsperada());
                j.setMargenToleranciaMinutos(jornada.getMargenToleranciaMinutos());
                return "Horario actualizado correctamente";
            }
        }

        return "Horario no encontrado";
    }

    //ELIMINAR
    public String eliminar(int id) {

        for (Jornada j : DatabaseMock.tablaJornadas) {
            if (j.getIdJornada() == id) {
                DatabaseMock.tablaJornadas.remove(j);
                return "Horario eliminado correctamente";
            }
        }

        return "Horario no encontrado";
    }

    // VALIDACIONES
    private boolean validar(Jornada j) {

        if (j.getNombre() == null || j.getNombre().trim().isEmpty()) {
            return false;
        }

        if (j.getHoraEntradaEsperada() == null) {
            return false;
        }

        if (j.getHoraSalidaEsperada() == null) {
            return false;
        }

        if (j.getMargenToleranciaMinutos() < 0) {
            return false;
        }

        // Validar que salida sea después de entrada
        if (j.getHoraSalidaEsperada().isBefore(j.getHoraEntradaEsperada())) {
            return false;
        }

        return true;
    }

    // VALIDAR DUPLICADOS
    public boolean existeHorario(String nombre) {
        for (Jornada j : DatabaseMock.tablaJornadas) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // LISTAR PARA COMBOBOX
    public String[] listarNombres() {
        List<String> nombres = new ArrayList<>();

        for (Jornada j : DatabaseMock.tablaJornadas) {
            nombres.add(j.getNombre());
        }

        return nombres.toArray(new String[0]);
    }
}