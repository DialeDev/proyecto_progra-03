# Apply Progress: Refactor MVC Estricto

## Change
Refactor MVC Estricto — eliminar 3 violaciones MVC+Repository

## Mode
Standard (no tests/strict_tdd: false)

## Delivery
- **Strategy**: single-pr
- **size:exception**: pre-approved
- **Budget impact**: ~200 lines, well within 800-line threshold

---

## Completed Tasks

### Phase 1: Backups
- [x] **B1** Backup .form y .java: FrmLogin, FrmGestionEmpleados, FrmGestionContratos, FrmRegistroContratos → `assets/backups/` (8 archivos con timestamp `20260523_195544`)

### Phase 2: Views — Messaging methods
- [x] **V1** `FrmLogin.java` — agregado `mostrarMensaje(String tipo, String mensaje)`
- [x] **V2** `FrmGestionEmpleados.java` — agregado `mostrarMensaje()` + `mostrarConfirmacion(String)`
- [x] **V3** `FrmGestionContratos.java` — agregado `mostrarMensaje()` + `mostrarConfirmacion(String)`
- [x] **V4** `FrmRegistroContratos.java` — agregado `mostrarMensaje(String tipo, String mensaje)`

### Phase 3: Repository
- [x] **R1** `RepositorioPlanilla.java` — agregado `crearPlanillaConBoletas(Planilla, List<Empleado>, int, int)` con Connection, setAutoCommit(false), commit/rollback/close, instancia interna de GeneradorBoletas

### Phase 4: Controllers — JOptionPane removal
- [x] **C1** `AuthController.java` — 3 JOptionPane calls → vista.mostrarMensaje(), eliminado import javax.swing.JOptionPane
- [x] **C2** `EmpleadoController.java` — 11 JOptionPane calls → vista.mostrarMensaje/Confirmacion(), agregado `obtenerDepartamentos()` via RepositorioDepartamento.findAll(), eliminado import javax.swing.JOptionPane
- [x] **C3** `ContratoController.java` — 18 JOptionPane calls → vista.mostrarMensaje/Confirmacion(), eliminado import javax.swing.JOptionPane
- [x] **C4** `RegistroContratoController.java` — 8 JOptionPane calls → vistaRegistro.mostrarMensaje(), eliminado import javax.swing.JOptionPane

### Phase 5: PlanillaController JDBC removal
- [x] **P1** `PlanillaController.java` — eliminados imports java.sql.Connection, SQLException, dao.Conexion, services.GeneradorBoletas; eliminado campo generador; delegada transacción a planillaDAO.crearPlanillaConBoletas()

### Phase 6: Cross-controller fix
- [x] **D1** `FrmGestionEmpleados.java` — reemplazado `new DepartamentoController().listar()` por `controller.obtenerDepartamentos()`, eliminado import controllers.DepartamentoController

### Phase 7: Verification
- [x] **T1** Verificación de imports:
  - ✅ Ningún controller importa `javax.swing.JOptionPane`
  - ✅ Ningún controller importa `java.sql.*`
  - ✅ Ningún controller importa `dao.Conexion`
  - ✅ FrmGestionEmpleados no importa `controllers.DepartamentoController`
  - ⚠️ Compilación no verificada (no hay JDK en el entorno) — verificar con `ant build` en NetBeans

---

## Files Changed

| File | Action | What Was Done |
|------|--------|---------------|
| `src/views/FrmLogin.java` | Modified | +mostrarMensaje(), +import JOptionPane |
| `src/views/FrmGestionEmpleados.java` | Modified | +mostrarMensaje(), +mostrarConfirmacion(), cargarDepartamentos() usa controller.obtenerDepartamentos(), -import DepartamentoController, +import JOptionPane |
| `src/views/FrmGestionContratos.java` | Modified | +mostrarMensaje(), +mostrarConfirmacion() |
| `src/views/FrmRegistroContratos.java` | Modified | +mostrarMensaje() |
| `src/controllers/AuthController.java` | Modified | JOptionPane → vista.mostrarMensaje(), -import JOptionPane |
| `src/controllers/EmpleadoController.java` | Modified | JOptionPane → vista.mostrarMensaje/Confirmacion(), +obtenerDepartamentos(), +departamentoDAO field, -import JOptionPane, +import RepositorioDepartamento |
| `src/controllers/ContratoController.java` | Modified | JOptionPane → vista.mostrarMensaje/Confirmacion(), -import JOptionPane |
| `src/controllers/RegistroContratoController.java` | Modified | JOptionPane → vistaRegistro.mostrarMensaje(), -import JOptionPane |
| `src/controllers/PlanillaController.java` | Modified | -imports JDBC/Conexion/GeneradorBoletas, -campo generador, delega a planillaDAO.crearPlanillaConBoletas() |
| `src/dao/RepositorioPlanilla.java` | Modified | +crearPlanillaConBoletas() con transacción atómica, +imports Empleado/GeneradorBoletas |
| `assets/backups/` | Created | 8 archivos backup (.form.bak + .java.bak para 4 views) |

---

## Deviations from Design

None — implementation matches design.md exactly.

### Notes
- `FrmGestionContratos.java` conserva JOptionPane en su `configurarComponentes()` para guardias precontroller (está en la view, es correcto según la spec que dice "la view OWNS JOptionPane")
- `FrmRegistroContratos.java` sigue teniendo JOptionPane en sus métodos `cargarEmpleados()` y `cargarTiposContrato()` — también es correcto pues son métodos internos de la view

## Issues Found

None during implementation. Código actual leído y respetado en cada cambio. No se tocaron archivos `.form`.

## Remaining Tasks

None — todas las 13 tareas completadas.

---

## Workload / PR Boundary

- **Mode**: single-pr (size:exception pre-aprobado)
- **Current work unit**: refactor-mvc-estricto completo (13 tareas)
- **Boundary**: B1 → T1 (full change)
- **Estimated review budget impact**: ~200 líneas (bien dentro del budget de 800)
- **Chain strategy**: size-exception

## Status
**13/13 tasks complete**. Ready for verify.
