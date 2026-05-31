# Proposal: Módulo de Gestión de Usuarios

## Intent

No existe UI para administrar usuarios del sistema. Solo se crean via scriptBD.sql — no hay forma de listar, editar roles, desactivar ni reactivar desde la app. Esto obliga a DBAs a tocar la BD directamente. Se necesita una interfaz Admin-only con CRUD completo.

## Scope

### In Scope
- `FrmGestionUsuarios` con JTable (listar), formulario crear/editar, botón desactivar/reactivar, búsqueda por username
- `UsuarioController` con validaciones (username único, campos requeridos) que delega a repositorio
- Métodos faltantes en `RepositorioUsuario`: findAll, findById, update, reactivarUsuario
- Botón "Usuarios" en FrmDashboard + wiring en DashboardController
- Seed permissions para módulo "Usuarios" (Admin: ver+operar) en scriptBD.sql
- Módulo "Usuarios" en el sistema RBAC

### Out of Scope
- Password hashing (BCrypt/Argon2) — texto plano, deuda técnica documentada
- Vincular creación de empleado con usuario automáticamente
- Mostrar usuario asociado en FrmGestionEmpleados
- Integrar permisos de usuarios en controllers individuales (nueva issue)

## Capabilities

### New Capabilities
- `user-crud`: CRUD completo de usuarios del sistema. Crear, editar, buscar, desactivar/reactivar. Admin-only.

### Modified Capabilities
- `access-control`: Agregar módulo "Usuarios" al sistema RBAC — seed permissions, nuevo botón en Dashboard, actualizar spec existente.

## Approach

Seguir patrón de `FrmGestionPermisos` + `PermisoController`: Admin-only, doble guard (botón oculto + controller guard). UsuarioController valida y delega a RepositorioUsuario. JFrame con JTable + panel formulario (misma estructura que FrmGestionEmpleados). Botón Dashboard via NetBeans designer (.form + initComponents). Contraseña en texto plano (deuda documentada).

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| `src/controllers/UsuarioController.java` | New | CRUD + validaciones |
| `src/views/FrmGestionUsuarios.java` | New | JFrame tabla + formulario |
| `src/views/FrmGestionUsuarios.form` | New | NetBeans form design |
| `src/dao/RepositorioUsuario.java` | Modified | +findAll, findById, update, reactivar |
| `src/views/FrmDashboard.java` | Modified | +botón Usuarios |
| `src/views/FrmDashboard.form` | Modified | +botón en layout |
| `src/controllers/DashboardController.java` | Modified | +abrirGestionUsuarios, getBtnUsuarios |
| `scriptBD.sql` | Modified | +seed permissions Usuarios |
| `openspec/specs/access-control/spec.md` | Modified | +módulo Usuarios en spec |

## Decisions

| Decision | Rationale |
|----------|-----------|
| Admin-only | Igual que Permisos — solo admins gestionan usuarios |
| Texto plano password | Sin hashing. Deuda técnica documentada explícitamente. |
| Single PR | Presupuesto 800 líneas, ~8 archivos (~500-700 LOC) |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Romper .form al editar Dashboard | Low | Usar NetBeans designer, backup .form previo |
| Olvidar seed permissions | Medium | Verificar scriptBD.sql antes de commit |

## Rollback Plan

`git revert HEAD`. Si se modificó .form, restaurar desde backup manual.

## Dependencies

Ninguna.

## Success Criteria

- [ ] Admin puede listar, crear, editar, desactivar y reactivar usuarios
- [ ] RRHH/Empleado NO ven el botón ni acceden vía controller
- [ ] Username único validado antes de insert/update
- [ ] scriptBD.sql incluye seed permissions para Admin
- [ ] App inicia y todas las pantallas existentes funcionan
