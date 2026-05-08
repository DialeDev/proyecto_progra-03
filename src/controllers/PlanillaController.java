package controllers;

import dao.RepositorioPlanilla;
import models.Planilla;
import models.Planilla.EstadoPlanilla;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// @author DialeMRZ
public class PlanillaController {
    private final RepositorioPlanilla planillaDAO;

    public PlanillaController() { this.planillaDAO = new RepositorioPlanilla(); }

    public void generarNuevaPlanilla(int mes, int anio) {
        planillaDAO.crear(new Planilla(0, mes, anio, LocalDate.now(), EstadoPlanilla.BORRADOR));
    }

    public List<Planilla> listarTodas() { return planillaDAO.obtenerTodas(); }

    public List<Planilla> filtrarPorEstado(EstadoPlanilla estado) {
        return planillaDAO.obtenerTodas().stream().filter(p -> p.getEstado() == estado).toList();
    }

    public Optional<Planilla> buscarPlanilla(Integer id) { return planillaDAO.obtenerPorId(id); }

    public boolean emitirPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.EMITIDA); }

    public boolean anularPlanilla(Integer id) { return planillaDAO.actualizarEstado(id, EstadoPlanilla.ANULADA); }
}
