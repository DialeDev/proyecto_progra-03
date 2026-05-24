# Proposal: Refactor MVC Estricto

## Intent

Eliminar 3 violaciones al patrón MVC+Repository detectadas en auditoría: controllers acoplados a JOptionPane (7 archivos), PlanillaController con JDBC directo, y FrmGestionEmpleados instanciando un controller ajeno.

## Scope

### In Scope
- Desacoplar JOptionPane de todos los controllers (7 archivos)
- Mover lógica transaccional de PlanillaController a RepositorioPlanilla
- Delegar carga de departamentos a EmpleadoController
- Backups de .form/.java previo a editar views

### Out of Scope
- Models, utils, services, App.java (ya cumplen)
- Repositories existentes (solo agregar métodos si necesario)
- Tests, refactor de nombres, cambios de comportamiento observable

## Capabilities

### New Capabilities
None — refactor estructural, no altera comportamiento observable.

### Modified Capabilities
None — no hay specs en `openspec/specs/`. Comportamiento se preserva.

## Approach

### Violación #1 — JOptionPane en Controllers
Cada view expone `mostrarMensaje(tipo, mensaje)` que envuelve JOptionPane. Controllers reciben la view y llaman al método sin conocer Swing.

### Violación #2 — JDBC en PlanillaController
Mover transacción (commit/rollback) y llamadas a Conexión a nuevo método en RepositorioPlanilla. Controller solo orquesta.

### Violación #3 — Controller ajeno en View
Agregar `obtenerDepartamentos()` en EmpleadoController que delega al repositorio. FrmGestionEmpleados usa ese método.

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| 7 controllers en `src/controllers/` | Modified | Quitar JOptionPane |
| `src/controllers/PlanillaController.java` | Modified | Quitar JOptionPane + JDBC |
| `src/dao/RepositorioPlanilla.java` | Modified | Nuevo método transaccional |
| `src/views/FrmGestionEmpleados.java` | Modified | Usar EmpleadoController para departamentos |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Romper forms NetBeans al editar views | Low | Backup .form/.java, no tocar .form |
| Cambiar comportamiento (sin tests) | Medium | Validación manual, probar cada pantalla |
| Error en migración transaccional | High | Revisar lógica commit/rollback, probar con data real |

## Rollback Plan

Revertir commit con `git revert HEAD`. Backups de .form/.java como fallback manual.

## Dependencies

Ninguna.

## Success Criteria

- [ ] Ningún controller importa `javax.swing.JOptionPane`
- [ ] `PlanillaController.java` sin imports `java.sql`
- [ ] `FrmGestionEmpleados.java` no instancia `DepartamentoController`
- [ ] App inicia y toda pantalla funciona igual
- [ ] Generación de planillas produce mismo resultado
