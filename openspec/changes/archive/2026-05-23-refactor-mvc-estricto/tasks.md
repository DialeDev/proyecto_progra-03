# Tasks: Refactor MVC Estricto

## Review Workload Forecast

Decision needed before apply: No
Chained PRs recommended: No
Chain strategy: size-exception
400-line budget risk: Low

| Field | Value |
|-------|-------|
| Estimated changed lines | ~180-200 |
| 400-line budget risk | Low |
| 800-line budget risk | Low |
| Chained PRs recommended | No |
| Suggested split | Single PR (size-exception pre-approved) |
| Delivery strategy | single-pr |
| Chain strategy | size-exception |

## Phase 1: Backups (antes de tocar views)

- [x] **B1** Backup .form y .java de las 4 views a modificar | Files: FrmLogin, FrmGestionEmpleados, FrmGestionContratos, FrmRegistroContratos (`.form` + `.java`) → `assets/backups/` | Deps: ninguna | Risk: bajo | Lines: ~0 (copia archivos)

## Phase 2: View messaging methods

- [x] **V1** Agregar `mostrarMensaje(tipo, msg)` a `FrmLogin.java` | Files: src/views/FrmLogin.java | Deps: B1 | Risk: bajo | Lines: ~15
- [x] **V2** Agregar `mostrarMensaje(tipo, msg)` + `mostrarConfirmacion(msg)` a `FrmGestionEmpleados.java` | Files: src/views/FrmGestionEmpleados.java | Deps: B1 | Risk: bajo | Lines: ~20
- [x] **V3** Agregar `mostrarMensaje(tipo, msg)` + `mostrarConfirmacion(msg)` a `FrmGestionContratos.java` | Files: src/views/FrmGestionContratos.java | Deps: B1 | Risk: bajo | Lines: ~20
- [x] **V4** Agregar `mostrarMensaje(tipo, msg)` a `FrmRegistroContratos.java` | Files: src/views/FrmRegistroContratos.java | Deps: B1 | Risk: bajo | Lines: ~15

## Phase 3: Repository transactional method

- [x] **R1** Agregar `crearPlanillaConBoletas(Planilla, List<Empleado>, int, int)` en `RepositorioPlanilla` con Connection, setAutoCommit(false), commit/rollback, close | Files: src/dao/RepositorioPlanilla.java | Deps: ninguna | Risk: alto (transacción manual, probar commit/rollback) | Lines: ~35

## Phase 4: Controller messaging refactor

- [x] **C1** Reemplazar `JOptionPane.showMessageDialog/ConfirmDialog` por `vista.mostrarMensaje/Confirmacion` en `AuthController`, eliminar import javax.swing | Files: src/controllers/AuthController.java | Deps: V1 | Risk: bajo | Lines: ~8
- [x] **C2** Idem en `EmpleadoController` (mayor volumen: ~11 JOptionPane calls) + agregar `obtenerDepartamentos()` | Files: src/controllers/EmpleadoController.java, src/views/FrmGestionEmpleados.java (solo cambio cargarDepartamentos acá) | Deps: V2, R1 (verificar que RepositorioDepartamento existe) | Risk: medio (cambio cruzado empleado + deptos) | Lines: ~20
- [x] **C3** Idem en `ContratoController` (~17 JOptionPane calls) | Files: src/controllers/ContratoController.java | Deps: V3 | Risk: medio (muchos reemplazos, verificar mapeo tipo→constante) | Lines: ~25
- [x] **C4** Idem en `RegistroContratoController` (~8 JOptionPane calls) | Files: src/controllers/RegistroContratoController.java | Deps: V4 | Risk: bajo | Lines: ~12

## Phase 5: PlanillaController JDBC removal

- [x] **P1** Eliminar `java.sql.Connection`, `SQLException`, `dao.Conexion` de `PlanillaController`; delegar transacción a `planillaDAO.crearPlanillaConBoletas()`; eliminar GeneradorBoletas como campo (se usará dentro del repositorio) | Files: src/controllers/PlanillaController.java | Deps: R1 | Risk: alto (cambio transaccional, validar manual) | Lines: ~20

## Phase 6: Cross-controller dependency fix

- [x] **D1** En `FrmGestionEmpleados.cargarDepartamentos()`: reemplazar `new DepartamentoController().listar()` por `controller.obtenerDepartamentos()`; eliminar `import controllers.DepartamentoController` | Files: src/views/FrmGestionEmpleados.java | Deps: C2 | Risk: bajo | Lines: ~5

## Phase 7: Verification

- [x] **T1** Verificar: ningún controller importa `javax.swing.JOptionPane`, ningún controller importa `java.sql`, `FrmGestionEmpleados` no importa `DepartamentoController`, app compila y cada pantalla funciona | Deps: todas las anteriores | Risk: medio (sin tests, validación manual) | Lines: ~0

## Implementation Order

1. **Backups** (B1) — antes de tocar cualquier view
2. **Views** (V1-V4) — agregar métodos de mensaje para que controllers compilen después
3. **Repositorio** (R1) — `crearPlanillaConBoletas`, independiente
4. **Controllers messaging** (C1-C4) — reemplazar JOptionPane, después de sus views respectivas
5. **PlanillaController** (P1) — eliminar JDBC, después de R1
6. **FrmGestionEmpleados cargarDepartamentos** (D1) — después de C2
7. **Verificación** (T1) — validación manual final

**Warning**: C2 modifica EmpleadoController (JOptionPane + obtenerDepartamentos) y FrmGestionEmpleados (cargarDepartamentos). D1 es solo el cambio restante en la misma view — considerar fusionar C2+D1 en un solo paso si no hay commit intermedio.
