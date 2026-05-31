# Delta for Access Control

## MODIFIED Requirements

### R2: Default Seed Permissions

9 modules seeded (was 8). Admin gets Usuarios.
(Previously: 8 modules)

| Rol | Modules with PuedeVer = 1 |
|-----|--------------------------|
| Admin | All 9 (incl. Usuarios) |
| RRHH | Empleados, Contratos, Planillas, Reportes |
| Empleado | Marcacion only |

#### Scenario: Admin full access

- GIVEN freshly seeded DB
- WHEN Admin permissions queried
- THEN all 9 modules have ver+operar

#### Scenario: Empleado restricted

- GIVEN freshly seeded DB
- WHEN Empleado permissions queried
- THEN only Marcacion has PuedeVer=1

### R4: Dashboard Module Filtering

Hides buttons per permissions. 9 modules total.
(Previously: 8 modules)

#### Scenario: RRHH sees 4 modules

- GIVEN RRHH user
- WHEN FrmDashboard loads
- THEN Empleados, Contratos, Planillas, Reportes visible
- AND 5 hidden: Marcacion, Horarios, Departamentos, AsignacionJornada, Usuarios

#### Scenario: Empleado sees only marcacion

- GIVEN Empleado user
- WHEN FrmDashboard loads
- THEN only Marcacion visible

#### Scenario: Usuarios hidden for non-Admin

- GIVEN RRHH user without Usuarios permiso
- WHEN FrmDashboard loads
- THEN Usuarios button is NOT visible

## ADDED Requirements

### R7: Usuarios RBAC Module

MUST recognize "Usuarios" as a module name (case-sensitive). Seed: Admin with ver+operar.

#### Scenario: Admin seed

- GIVEN freshly seeded DB
- WHEN querying Permiso WHERE Modulo='Usuarios' AND Rol='Admin'
- THEN PuedeVer=1 and PuedeOperar=1

#### Scenario: No RRHH seed

- GIVEN freshly seeded DB
- WHEN querying Permiso WHERE Modulo='Usuarios' AND Rol='RRHH'
- THEN no row returned
