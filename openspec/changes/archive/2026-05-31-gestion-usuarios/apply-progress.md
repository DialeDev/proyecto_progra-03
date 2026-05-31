# Apply Progress: Módulo de Gestión de Usuarios

**Date**: 2026-05-31
**Mode**: Standard (strict_tdd: false)
**Delivery strategy**: single-pr (size-exception)

## Summary

Full implementation of the `gestion-usuarios` change: CRUD de usuarios del sistema con interfaz Admin-only, siguiendo el patrón MVC + Repository existente (FrmGestionPermisos como referencia).

## Completed Tasks

### Phase 1: Foundation — Repository + Model
- [x] 1.1 `nombreEmpleado` (String transient) agregado a `Usuario.java`
- [x] 1.2 `RepositorioUsuario.java` extendido con: findAll (JOIN Empleado), findById, update, reactivarUsuario, findEmpleadosSinUsuario

### Phase 2: Controller
- [x] 2.1 `UsuarioController.java` creado con CRUD + validaciones (campos requeridos, no autodesactivación vía SesionGlobal)

### Phase 3: View — FrmGestionUsuarios
- [x] 3.1 Form creado vía NetBeans designer antes de este apply batch
- [x] 3.2 `FrmGestionUsuarios.java` completado: constructor, inicializarPersonalizaciones(), TableModel, combos, FlatSVGIcon, row selection, event handlers, toggle form, mensajes

### Phase 4: Dashboard Wiring
- [x] 4.1 btnUsuarios en FrmDashboard (diseñado en NetBeans antes del apply batch)
- [x] 4.2 `FrmDashboard.java`: getBtnUsuarios(), btnUsuariosActionPerformed → controller.abrirGestionUsuarios()
- [x] 4.3 `DashboardController.java`: abrirGestionUsuarios(), btnUsuarios en cargarPermisos()

### Phase 5: Seed Data & Specs
- [x] 5.1 INSERT Permiso (Admin, NULL, 'Usuarios', 1, 1) en `scriptBD.sql`
- [x] 5.2 `openspec/specs/access-control/spec.md` actualizado: 9 módulos, Usuarios module + escenarios

## Files Changed

| File | Action | What Was Done |
|------|--------|---------------|
| `src/models/Usuario.java` | Modified | +nombreEmpleado (String transient) |
| `src/dao/RepositorioUsuario.java` | Modified | +findAll, +findById, +update, +reactivarUsuario, +findEmpleadosSinUsuario + mappers |
| `src/controllers/UsuarioController.java` | Created | CRUD + validaciones (no autodesactivación, empleado sin usuario) |
| `src/views/FrmGestionUsuarios.java` | Modified | Constructor, inicializarPersonalizaciones(), event handlers, safe getters, helpers |
| `src/views/FrmDashboard.java` | Modified | +getBtnUsuarios(), btnUsuarios handler, btnUsuarios en styleModernUI |
| `src/controllers/DashboardController.java` | Modified | +abrirGestionUsuarios(), +btnUsuarios en cargarPermisos |
| `src/utils/UIStyles.java` | Modified | +setSVGIcon(JButton, String) helper |
| `scriptBD.sql` | Modified | +INSERT Permiso (Admin, NULL, 'Usuarios', 1, 1) |
| `openspec/specs/access-control/spec.md` | Modified | +Usuarios module, 9 módulos, escenarios R7 |
| `openspec/changes/gestion-usuarios/tasks.md` | Modified | Tareas marcadas [x] |
| `src/img/faUser.svg` | Created | FlatSVGIcon para iconos |
| `src/img/faSave.svg` | Created | FlatSVGIcon para iconos |
| `src/img/faPlus.svg` | Created | FlatSVGIcon para iconos |
| `src/img/faBan.svg` | Created | FlatSVGIcon para iconos |
| `src/img/faUndo.svg` | Created | FlatSVGIcon para iconos |

## Deviations from Design

1. **Sin `existeUsername` en repositorio**: el design.md menciona validación de username único vía `existeUsername(String, Integer excludeId)`, pero el usuario de negocio ya está garantizado único por la UNIQUE CONSTRAINT en la BD (Username). La validación de unicidad se maneja en la BD (error al insertar username duplicado). Esto es aceptable dado que el spec original del usuario no listaba `existeUsername`.
2. **txtPassword es JTextField (no JPasswordField)**: por solicitud explícita del usuario — "txtPassword hay que dejarlo como JTextField, ya funciona". el design.md indicaba JPasswordField pero se respeta la decisión del usuario.
3. **Controller devuelve void (no String)**: el usuario especificó void para métodos del controller; el design.md indicaba return String para mensajes. Se siguió la especificación del usuario.
4. **No se agregó buscador/búsqueda**: el design.md sketch mostraba txtBuscar filtrando la tabla. No estaba en los requisitos del usuario, se omite para mantener scope.

## Issues Found

- **SVG icons locales**: Los 5 archivos SVG se crearon en `src/img/`. FlatLaf 3.7.1 incluye FlatSVGIcon; si la versión no lo soporta, los botones funcionan sin iconos (solo texto).
- **No hay compilación disponible**: `javac` y `ant` no están instalados en este entorno. No se pudo verificar la compilación. Se recomienda `ant compile` manual.

## Workload / PR Boundary

- Mode: size-exception (single PR, ~800 líneas estimadas)
- Current work unit: Full módulo gestión usuarios (único batch)
- Boundary: base=main, no chained PRs necesarios
- Estimated review budget impact: ~700-800 lines (justificado como size-exception)

## Status

11/11 tasks complete. Ready for verify.
