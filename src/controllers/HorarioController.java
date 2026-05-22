package controllers;

import java.util.List;
import java.util.Optional;
import models.Jornada;
import dao.RepositorioJornada;

// @author DialeMRZ
public class HorarioController {
    private final RepositorioJornada jornadaDAO;

    public HorarioController() { this.jornadaDAO = new RepositorioJornada(); }

    public String guardar(Jornada jornada) {
        if (!validar(jornada)) return "Datos inválidos";
        jornadaDAO.create(jornada);
        return "Horario guardado correctamente";
    }

    public List<Jornada> listar() { return jornadaDAO.findAll(); }

    public Jornada buscarPorId(int id) {
        return jornadaDAO.findById(id).orElse(null);
    }

    public String actualizar(Jornada jornada) {
        if (!validar(jornada)) return "Datos inválidos";
        jornadaDAO.update(jornada);
        return "Horario actualizado correctamente";
    }

    public String eliminar(int id) {
        jornadaDAO.delete(id);
        return "Horario eliminado correctamente";
    }

    private boolean validar(Jornada j) {
        if (j.getNombre() == null || j.getNombre().trim().isEmpty()) return false;
        if (j.getHoraEntradaEsperada() == null || j.getHoraSalidaEsperada() == null) return false;
        if (j.getMargenToleranciaMinutos() < 0) return false;
        return true;
    }

    public boolean existeHorario(String nombre) {
        return jornadaDAO.findAll().stream().anyMatch(j -> j.getNombre().equalsIgnoreCase(nombre));
    }

    public String[] listarNombres() {
        return jornadaDAO.findAll().stream().map(Jornada::getNombre).toArray(String[]::new);
    }
}
