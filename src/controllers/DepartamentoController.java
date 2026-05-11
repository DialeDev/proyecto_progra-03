package controllers;

import dao.RepositorioDepartamento;
import models.Departamento;
import java.util.List;
import java.util.Optional;

// @author DialeMRZ
public class DepartamentoController {
    private final RepositorioDepartamento departamentoDAO;

    public DepartamentoController() {
        this.departamentoDAO = new RepositorioDepartamento();
    }

    public String guardar(Departamento d) {
        if (d.getNombre() == null || d.getNombre().trim().isEmpty())
            return "El nombre del departamento es obligatorio.";
        if (departamentoDAO.existePorNombre(d.getNombre()))
            return "Ya existe un departamento con el nombre '" + d.getNombre() + "'.";
        departamentoDAO.create(d);
        return "Departamento guardado correctamente.";
    }

    public List<Departamento> listar() { return departamentoDAO.findAll(); }

    public Optional<Departamento> buscarPorId(int id) { return departamentoDAO.findById(id); }

    public String actualizar(Departamento d) {
        if (d.getNombre() == null || d.getNombre().trim().isEmpty())
            return "El nombre del departamento es obligatorio.";
        // Verificar duplicado excluyendo el propio registro
        if (departamentoDAO.existePorNombre(d.getNombre())) {
            Optional<Departamento> existente = departamentoDAO.findById(d.getIdDepartamento());
            if (existente.isEmpty()) return "El departamento no existe.";
            // Si el nombre pertenece a otro registro, es duplicado
            if (!existente.get().getNombre().equalsIgnoreCase(d.getNombre().trim()))
                return "Ya existe otro departamento con el nombre '" + d.getNombre() + "'.";
        }
        departamentoDAO.update(d);
        return "Departamento actualizado correctamente.";
    }

    public String eliminar(int id) {
        departamentoDAO.delete(id);
        return "Departamento eliminado correctamente.";
    }

    public boolean existeDepartamento(String nombre) {
        return departamentoDAO.existePorNombre(nombre);
    }
}
