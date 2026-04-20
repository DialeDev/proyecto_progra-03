package controllers;

import java.util.List;
import java.util.ArrayList;

import models.Jornada;
import dao.RepositorioJornada;

/**
 *
 * @author DialeMRZ
 */

public class HorarioController {

    private final RepositorioJornada jornadaDAO;

    public HorarioController() {
        this.jornadaDAO = new RepositorioJornada();
    }

    //GUARDAR
    public String guardar(Jornada jornada) {

        if (!validar(jornada)) {
            return "Datos inválidos";
        }

        jornadaDAO.create(jornada);

        return "Horario guardado correctamente";
    }

    // LISTAR
    public List<Jornada> listar() {
        return jornadaDAO.findAll();
    }

    // BUSCAR POR ID
    public Jornada buscarPorId(int id) {
        return jornadaDAO.findById(id);
    }

    //ACTUALIZAR
    public String actualizar(Jornada jornada) {

        if (!validar(jornada)) {
            return "Datos inválidos";
        }

        jornadaDAO.update(jornada);

        return "Horario actualizado correctamente";
    }

    //ELIMINAR
    public String eliminar(int id) {
        jornadaDAO.delete(id);
        return "Horario eliminado correctamente";
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
        List<Jornada> jornadas = jornadaDAO.findAll();
        for (Jornada j : jornadas) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // LISTAR PARA COMBOBOX
    public String[] listarNombres() {
        List<Jornada> jornadas = jornadaDAO.findAll();
        List<String> nombres = new ArrayList<>();

        for (Jornada j : jornadas) {
            nombres.add(j.getNombre());
        }

        return nombres.toArray(new String[0]);
    }
}