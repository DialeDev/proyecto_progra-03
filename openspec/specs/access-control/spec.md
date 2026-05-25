# Access Control Specification

## Purpose

Control access to 8 Dashboard modules by user `Rol` and `IdDepartamento`. Enforces at UI (button visibility) and Controller (direct access) levels.

## Requirements

### R1: Permiso Entity

Stores permissions with: IdPermiso, Rol (VARCHAR), IdDepartamento (INT nullable), Modulo (VARCHAR), PuedeVer (BIT), PuedeOperar (BIT).

#### Scenario: Permission applies to all departments

- GIVEN a Permiso row with IdDepartamento = NULL
- WHEN queried for any user with matching Rol
- THEN the permission applies regardless of the user's department

#### Scenario: Permission scoped to one department

- GIVEN a Permiso row with IdDepartamento = 2
- WHEN queried for a user in departamento 2
- THEN the permission applies
- AND queried for a user in a different departamento
- THEN it does NOT apply

### R2: Default Seed Permissions

Seeds Permiso on initialization for all 3 roles and 8 modules.

| Rol | Modules with PuedeVer = 1 |
|-----|--------------------------|
| Admin | All 8 modules |
| RRHH | Empleados, Contratos, Planillas, Reportes |
| Empleado | Marcacion only |

#### Scenario: Admin full access

- GIVEN a freshly seeded database
- WHEN an Admin user's permissions are queried
- THEN all 8 modules have PuedeVer = 1 and PuedeOperar = 1

#### Scenario: Empleado restricted access

- GIVEN a freshly seeded database
- WHEN an Empleado user's permissions are queried
- THEN only Marcacion has PuedeVer = 1
- AND all other modules have PuedeVer = 0

### R3: Permission Check Service

Provides `PermisoService.tieneAcceso(Usuario, String modulo, String accion)` returning boolean. Admin bypasses the permission table.

#### Scenario: Admin bypasses permission table

- GIVEN an active user with Rol = "Admin"
- WHEN `tieneAcceso` is called for any module
- THEN it returns true without querying the Permiso table

#### Scenario: User with permission granted access

- GIVEN an active user with PuedeVer = 1 for Planillas
- WHEN `tieneAcceso(usuario, "Planillas", "ver")` is called
- THEN it returns true

#### Scenario: User without permission denied

- GIVEN an active user with no matching Permiso row for Horarios
- WHEN `tieneAcceso` is called
- THEN it returns false

#### Scenario: Inactive user always denied

- GIVEN a user with activo = false
- WHEN `tieneAcceso` is called for any module
- THEN it returns false regardless of permission data

### R4: Dashboard Module Filtering

Hides module buttons on `FrmDashboard` where user lacks PuedeVer.

#### Scenario: RRHH sees permitted modules

- GIVEN an RRHH user with PuedeVer = 1 for Empleados, Contratos, Planillas, Reportes
- WHEN FrmDashboard loads
- THEN those 4 buttons are visible
- AND the remaining 4 buttons (Marcacion, Horarios, Departamentos, AsignacionJornada) are hidden

#### Scenario: Empleado sees only marcacion

- GIVEN an Empleado user
- WHEN FrmDashboard loads
- THEN only the Marcacion button is visible
- AND all other module buttons are hidden

### R5: Controller Access Guard

Controllers MUST validate via `PermisoService` before displaying forms. Direct access bypass MUST be rejected.

#### Scenario: Direct URL access blocked

- GIVEN an RRHH user without Permiso for Horarios
- WHEN the user directly invokes `HorarioController.abrirGestion()`
- THEN the controller denies access and shows "Acceso denegado"
- AND FrmGestionHorarios is NOT opened

#### Scenario: Admin bypasses controller guard

- GIVEN an Admin user
- WHEN the user invokes any controller action
- THEN the controller grants access without permission validation

### R6: Permission Management UI

Provides `FrmGestionPermisos` to manage the Permiso table. Only Rol = "Admin" can access it.

#### Scenario: Admin manages permissions

- GIVEN an Admin user
- WHEN the user opens FrmGestionPermisos
- THEN the user can view, add, edit, and delete permission rows

#### Scenario: Non-admin access rejected

- GIVEN an RRHH user
- WHEN they attempt to open FrmGestionPermisos
- THEN access is denied with "Acceso denegado"
- AND the permission management form is NOT opened
