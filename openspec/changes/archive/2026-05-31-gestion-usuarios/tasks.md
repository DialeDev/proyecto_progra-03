# Tasks: Módulo de Gestión de Usuarios

## Review Workload Forecast

| Field | Value |
|-------|-------|
| Estimated changed lines | ~700-800 |
| 400-line budget risk | High |
| Chained PRs recommended | No |
| Suggested split | Single PR |
| Delivery strategy | single-pr |
| Chain strategy | size-exception |

Decision needed before apply: No
Chained PRs recommended: No
Chain strategy: size-exception
400-line budget risk: High

### Suggested Work Units

| Unit | Goal | Likely PR | Notes |
|------|------|-----------|-------|
| 1 | Full módulo gestión usuarios | PR 1 (single) | 8 archivos, ~700-800 líneas, base=main |

## Phase 1: Foundation — Repository + Model

- [x] 1.1 Agregar `nombreEmpleado` (String transient) a `src/models/Usuario.java`
- [x] 1.2 Extender `src/dao/RepositorioUsuario.java`: findAll (JOIN Empleado), findById, update, reactivarUsuario, findEmpleadosSinUsuario

## Phase 2: Controller

- [x] 2.1 Crear `src/controllers/UsuarioController.java` con CRUD + validaciones (campos requeridos, no autodesactivación, empleado no ocupado)

## Phase 3: View — FrmGestionUsuarios

- [x] 3.1 El form `src/views/FrmGestionUsuarios` fue creado vía NetBeans designer antes del apply batch
- [x] 3.2 Completar `src/views/FrmGestionUsuarios.java`: constructor + inicializarPersonalizaciones() (TableModel, combos, FlatSVGIcon, eventos, toggle form)

## Phase 4: Dashboard Wiring

- [x] 4.1 btnUsuarios agregado en `FrmDashboard` (NetBeans designer) antes del apply batch
- [x] 4.2 Modificar `FrmDashboard.java`: getBtnUsuarios(), event handler btnUsuariosActionPerformed -> controller
- [x] 4.3 Modificar `DashboardController.java`: abrirGestionUsuarios(), getBtnUsuarios en cargarPermisos()

## Phase 5: Seed Data & Specs

- [x] 5.1 Agregar INSERT Permiso (Admin, NULL, 'Usuarios', 1, 1) a `scriptBD.sql`
- [x] 5.2 Actualizar `openspec/specs/access-control/spec.md`: +módulo Usuarios, 9 módulos seed
