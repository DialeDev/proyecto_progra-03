# Controller-View Messaging Specification

## Purpose

Define how controllers communicate operation results to views without importing Swing components, preserving the exact message text users see.

## Requirements

### R-VC1: View exposes message method

Each view wired to a controller MUST expose `void mostrarMensaje(String tipo, String mensaje)`.

The view MUST own `javax.swing.JOptionPane.showMessageDialog(...)`. Valid `tipo`: `"exito"`, `"error"`, `"advertencia"`, `"info"`.

| tipo | JOptionPane constant |
|------|---------------------|
| exito | INFORMATION_MESSAGE |
| error | ERROR_MESSAGE |
| advertencia | WARNING_MESSAGE |
| info | INFORMATION_MESSAGE |

#### Scenario: Success after save

- GIVEN the controller completed `empleadoDAO.create(...)` without exceptions
- WHEN the controller calls `vista.mostrarMensaje("exito", "Empleado registrado.")`
- THEN a JOptionPane with INFORMATION_MESSAGE shows "Empleado registrado."

#### Scenario: Database error

- GIVEN the repository threw an SQLException
- WHEN the controller calls `vista.mostrarMensaje("error", "Error: " + e.getMessage())`
- THEN a JOptionPane with ERROR_MESSAGE shows the error text

#### Scenario: Validation warning

- GIVEN the user left required fields empty
- WHEN the controller calls `vista.mostrarMensaje("advertencia", "Todos los campos son obligatorios.")`
- THEN a JOptionPane with WARNING_MESSAGE shows the validation text

### R-VC2: Controller does not import Swing

Controllers MUST NOT import `javax.swing.JOptionPane` or `javax.swing.*`.

The controller MUST receive the view via constructor or setter and delegate all user messaging to `vista.mostrarMensaje(...)`.

#### Scenario: Compile-time check

- GIVEN the controller source after refactor
- WHEN compiled
- THEN `javax.swing.JOptionPane` MUST NOT appear in import statements
- AND the controller compiles without errors

### R-VC3: Messages are preserved verbatim

All user-facing message strings MUST be identical before and after refactor. The refactor changes HOW messages are dispatched, not WHAT messages are shown.

#### Scenario: Same UI text

- GIVEN original code showed "Credenciales incorrectas o usuario inactivo."
- WHEN the refactored code runs
- THEN `mostrarMensaje` receives the identical string
