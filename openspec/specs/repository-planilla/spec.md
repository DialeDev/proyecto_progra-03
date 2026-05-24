# Repository Transactional Planilla Specification

## Purpose

Move the transaction boundary (connection management, commit, rollback) from PlanillaController into RepositorioPlanilla, so the controller only orchestrates business logic.

## Requirements

### R-RP1: Repository handles transaction lifecycle

`RepositorioPlanilla` MUST expose:

```java
String crearPlanillaConBoletas(Planilla planilla, List<Empleado> empleados, int mes, int anio)
```

This method MUST handle `Conexion.getConnection()`, `setAutoCommit(false)`, planilla INSERT via `crear(conn, planilla)`, boleta generation via `GeneradorBoletas.generarYGuardarBoleta(conn, ...)`, `commit()`, `rollback()` on failure, and `close()` — all internally.

The operation MUST be atomic: if any step fails, ALL changes roll back.

#### Scenario: All empleados have active contracts

- GIVEN a list of active employees each with a current contract
- WHEN `crearPlanillaConBoletas(planilla, empleados, mes, anio)` is called
- THEN a planilla row is inserted
- AND one boleta per employee is generated
- AND the method returns a success message counting boletas created
- AND the connection is committed and closed

#### Scenario: Database error mid-operation

- GIVEN the database becomes unavailable after planilla INSERT but before all boletas
- WHEN `crearPlanillaConBoletas(...)` executes
- THEN a `rollback()` is issued reverting the planilla INSERT
- AND the method returns an error message with the exception text
- AND the connection is closed

#### Scenario: No active employees

- GIVEN an empty list of active employees
- WHEN `crearPlanillaConBoletas(planilla, emptyList, ...)` is called
- THEN the method returns early with "No hay empleados activos para generar planilla."
- AND no database write occurs

### R-RP2: Controller has no JDBC imports

`PlanillaController` MUST NOT import `java.sql.Connection`, `java.sql.SQLException`, or `dao.Conexion`.

`PlanillaController` MUST NOT call `Conexion.getConnection()`.

#### Scenario: Clean imports

- GIVEN `PlanillaController.java` after refactor
- WHEN inspected
- THEN no `java.sql` imports exist
- AND no reference to `Conexion` exists in the file
- AND the controller compiles without errors

### R-RP3: Duplicate planilla detection preserved

A planilla for the same month/year MUST NOT be created twice. The check via `planillaDAO.existePorMesYAnio(mes, anio)` MUST execute BEFORE the transactional method.

#### Scenario: Duplicate month/year rejected

- GIVEN a planilla already exists for month 3, year 2026
- WHEN `generarNuevaPlanilla(3, 2026)` is called
- THEN the controller returns "Ya existe una planilla para 3/2026."
- AND the repository method is NOT called
