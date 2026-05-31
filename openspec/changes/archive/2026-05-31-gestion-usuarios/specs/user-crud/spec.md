# User CRUD Specification

## Purpose

Admin-only CRUD for system user accounts (username, password, role, employee association).

## Requirements

### R1: User List

MUST display all users in JTable: ID, Username, Rol, Employee (full name), Status.

#### Scenario: Admin views user list

- GIVEN Admin on FrmGestionUsuarios
- WHEN form loads
- THEN JTable shows all users with 5 columns, Status as Activo/Inactivo

### R2: Create User

MUST allow creating users: username (unique), password (plain text), role (Admin/RRHH/Empleado), and an unassigned employee.

#### Scenario: Admin creates user

- GIVEN create form with cmbEmpleado showing employees without user
- WHEN Admin fills all fields and submits
- THEN user created with Activo=1; shown in JTable

#### Scenario: Duplicate username

- GIVEN existing user "jperez"
- WHEN Admin creates another user with username "jperez"
- THEN error shown; user NOT created

#### Scenario: Employee filter

- GIVEN an employee already linked to a user
- WHEN Admin opens create form
- THEN that employee is NOT in the combo box

### R3: Edit User

MUST allow editing username, role, and employee association. Password MUST NOT be editable.

#### Scenario: Role change

- GIVEN user with Rol="Empleado"
- WHEN Admin changes to "RRHH"
- THEN role updated; JTable reflects change

#### Scenario: Occupied employee

- GIVEN "Carlos Martínez" assigned to user "cmartinez"
- WHEN Admin assigns him to another user
- THEN system rejects: "El empleado ya tiene un usuario asignado"

### R4: Deactivate/Reactivate

MUST allow deactivating (Activo=0) and reactivating (Activo=1). MUST NOT self-deactivate.

#### Scenario: Admin deactivates

- GIVEN active user not the session user
- WHEN Admin clicks Desactivar
- THEN Activo=0; JTable shows "Inactivo"

#### Scenario: Self-deactivation blocked

- GIVEN session user "admin"
- WHEN Admin selects own row and clicks Desactivar
- THEN system shows "No puedes desactivarte a ti mismo"; user stays active

#### Scenario: Reactivate

- GIVEN inactive user
- WHEN Admin clicks Reactivar
- THEN Activo=1; JTable shows "Activo"

### R5: Admin-Only Access

MUST restrict to Admin. Both UI and controller MUST enforce.

#### Scenario: Non-Admin blocked

- GIVEN RRHH user
- WHEN abrirGestionUsuarios() is invoked
- THEN controller denies access; form NOT opened

### R6: Password Storage (Debt)

Passwords SHALL be in plain text. Documented debt for future fix.

#### Scenario: Plain text in DB

- GIVEN user created with password "abc123"
- WHEN querying Usuario table
- THEN Password column contains "abc123"
