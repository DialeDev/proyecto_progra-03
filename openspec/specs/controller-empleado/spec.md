# Controller-Empleado Department Data Specification

## Purpose

Eliminate the cross-controller dependency in FrmGestionEmpleados by having EmpleadoController provide department data instead of the view instantiating DepartamentoController directly.

## Requirements

### R-CE1: EmpleadoController provides departments

`EmpleadoController` MUST expose:

```java
public List<Departamento> obtenerDepartamentos()
```

This method delegates to `RepositorioEmpleado` (or `RepositorioDepartamento` if one exists) and returns all departments for populating the ComboBox.

#### Scenario: Departments exist

- GIVEN the database has 5 departments
- WHEN `empleadoController.obtenerDepartamentos()` is called
- THEN a List with 5 Departamento objects is returned
- AND the view iterates to populate `cmbDepartamento`

#### Scenario: No departments in database

- GIVEN the departments table is empty
- WHEN `empleadoController.obtenerDepartamentos()` is called
- THEN an empty List is returned
- AND the ComboBox remains empty

### R-CE2: View does not instantiate DepartamentoController

`FrmGestionEmpleados` MUST NOT import or instantiate `controllers.DepartamentoController`.

The view MUST call `this.controller.obtenerDepartamentos()` (where `controller` is the `EmpleadoController` instance) to populate `cmbDepartamento`.

#### Scenario: View loads with correct data

- GIVEN the FrmGestionEmpleados constructor runs
- WHEN `configurarComponentes()` calls `cargarDepartamentos()`
- THEN `empleadoController.obtenerDepartamentos()` is invoked
- AND the ComboBox shows the same department list as before

#### Scenario: Compile-time check

- GIVEN `FrmGestionEmpleados.java` after refactor
- WHEN inspected
- THEN `import controllers.DepartamentoController` MUST NOT appear
- AND the file compiles without errors
