# Design: Refactor MVC Estricto

## Technical Approach

Eliminar 3 violaciones MVC migrando el manejo de mensajes a las vistas (violación #1), moviendo la transacción de planillas al repositorio (violación #2), y reemplazando la dependencia cruzada de controllers por un método proveedor de datos (#3). Sin cambios funcionales — el refactor es puramente estructural.

---

## 1. Controller-View Messaging Pattern

### Interfaz implícita (sin interfaz formal — cada view implementa su propio método)

Cada view expone dos métodos:

```java
// En cada FrmXxx.java
public void mostrarMensaje(String tipo, String mensaje) {
    int constant = switch (tipo) {
        case "exito" -> JOptionPane.INFORMATION_MESSAGE;
        case "error" -> JOptionPane.ERROR_MESSAGE;
        case "advertencia" -> JOptionPane.WARNING_MESSAGE;
        case "info" -> JOptionPane.INFORMATION_MESSAGE;
        default -> JOptionPane.INFORMATION_MESSAGE;
    };
    JOptionPane.showMessageDialog(this, mensaje, getTitle(), constant);
}

public boolean mostrarConfirmacion(String mensaje) {
    return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
}
```

### Comunicación Controller → View

```
Controller ── llama ──→ vista.mostrarMensaje("exito", "texto")
Controller ── llama ──→ vista.mostrarConfirmacion("¿Seguro?")
```

Los controllers YA reciben `vista` por constructor. Se reemplaza:
- `JOptionPane.showMessageDialog(vista, ...)` → `vista.mostrarMensaje(...)`
- `JOptionPane.showConfirmDialog(vista, ...)` → `vista.mostrarConfirmacion(...)`
- Se elimina `import javax.swing.JOptionPane` de cada controller

### Views afectadas

| View | Controller | Agrega mostrarMensaje | Agrega mostrarConfirmacion |
|------|-----------|----------------------|---------------------------|
| FrmLogin | AuthController | ✅ | ❌ (no usa) |
| FrmGestionEmpleados | EmpleadoController | ✅ | ✅ |
| FrmGestionContratos | ContratoController | ✅ | ✅ |
| FrmRegistroContratos | RegistroContratoController | ✅ | ❌ (no usa) |

---

## 2. Repository Transaction Pattern

### Firma del nuevo método en `RepositorioPlanilla`

```java
public String crearPlanillaConBoletas(Planilla planilla,
    List<Empleado> empleadosActivos, int mes, int anio)
```

### Comportamiento interno

```
RepositorioPlanilla.crearPlanillaConBoletas():
  1. Conexion.getConnection()
  2. conn.setAutoCommit(false)
  3. int idPlanilla = crear(conn, planilla)         // INSERT planilla
  4. GeneradorBoletas gen = new GeneradorBoletas()   // instancia interna
  5. for each empleado:
       gen.generarYGuardarBoleta(conn, idPlanilla, emp.getIdEmpleado(), mes, anio)
  6. conn.commit()
  7. Retorna "Planilla creada. Boletas generadas: N"
  8. catch(Exception) → conn.rollback() → retorna "Error: " + e.getMessage()
  9. finally → conn.close()
```

### Cambios en `PlanillaController`

```java
// Antes: maneja Connection, commit, rollback
public String generarNuevaPlanilla(int mes, int anio) { ... }

// Después: solo orquestación sin JDBC
public String generarNuevaPlanilla(int mes, int anio) {
    List<Empleado> activos = empleadoDAO.findAllActivos();
    if (activos.isEmpty()) return "No hay empleados activos...";
    if (planillaDAO.existePorMesYAnio(mes, anio))
        return "Ya existe una planilla para " + mes + "/" + anio + "...";
    // Pre-validar contratos activos (se mantiene en controller)
    List<String> errores = ...;
    if (!errores.isEmpty()) return ...;
    // Delegar todo lo transaccional al repositorio
    return planillaDAO.crearPlanillaConBoletas(
        new Planilla(0, mes, anio, LocalDate.now(), EstadoPlanilla.BORRADOR),
        activos, mes, anio);
}
```

Se eliminan imports: `java.sql.Connection`, `java.sql.SQLException`, `dao.Conexion`.

### Manejo de errores

`crearPlanillaConBoletas` retorna String (mensaje de error o éxito) — mismo patrón que `generarNuevaPlanilla` ya usa. Sin excepciones lanzadas al controller para mantener consistencia.

---

## 3. Controller Data Provider Pattern

### Nuevo método en `EmpleadoController`

```java
public List<Departamento> obtenerDepartamentos() {
    return new RepositorioDepartamento().findAll();
}
```

### Cambio en `FrmGestionEmpleados`

```diff
- import controllers.DepartamentoController;
...
  private void cargarDepartamentos() {
-     DepartamentoController deptoCtrl = new DepartamentoController();
-     deptoCtrl.listar().forEach(d -> cmbDepartamento.addItem(d));
+     controller.obtenerDepartamentos().forEach(d -> cmbDepartamento.addItem(d));
      cmbDepartamento.setSelectedIndex(-1);
  }
```

Sin interfaz adicional — el controller ya tiene la vista en su constructor y el método se llama después de construir el controller (actualmente en `configurarComponentes()`).

---

## 4. File Changes

| File | Acción | Descripción |
|------|--------|-------------|
| `src/views/FrmLogin.java` | Modificar | Agregar `mostrarMensaje()` |
| `src/views/FrmGestionEmpleados.java` | Modificar | Agregar `mostrarMensaje()`, `mostrarConfirmacion()`, cambiar `cargarDepartamentos()` |
| `src/views/FrmGestionContratos.java` | Modificar | Agregar `mostrarMensaje()`, `mostrarConfirmacion()` |
| `src/views/FrmRegistroContratos.java` | Modificar | Agregar `mostrarMensaje()` |
| `src/controllers/EmpleadoController.java` | Modificar | Reemplazar JOptionPane → vista.mostrarMensaje/Confirmacion, agregar `obtenerDepartamentos()`, eliminar import JOptionPane |
| `src/controllers/AuthController.java` | Modificar | Reemplazar JOptionPane → vista.mostrarMensaje, eliminar import JOptionPane |
| `src/controllers/ContratoController.java` | Modificar | Reemplazar JOptionPane → vista.mostrarMensaje/Confirmacion, eliminar import JOptionPane |
| `src/controllers/RegistroContratoController.java` | Modificar | Reemplazar JOptionPane → vistaRegistro.mostrarMensaje, eliminar import JOptionPane |
| `src/controllers/PlanillaController.java` | Modificar | Eliminar JDBC directo, delegar transacción a repositorio, eliminar imports java.sql y dao.Conexion |
| `src/dao/RepositorioPlanilla.java` | Modificar | Agregar `crearPlanillaConBoletas()` |
| **Total: 10 archivos, 0 nuevos, 10 modificados** | | |

---

## 5. Implementation Order

1. **Views primero** — agregar `mostrarMensaje()` y `mostrarConfirmacion()` a las 4 views. Sin esto, los controllers no compilan después del cambio.
2. **RepositorioPlanilla** — agregar `crearPlanillaConBoletas()`. Independiente, testeable.
3. **Controllers (JOptionPane)** — refactor EmpleadoController, AuthController, ContratoController, RegistroContratoController. Un commit por controller.
4. **PlanillaController** — eliminar JDBC, delegar a repositorio.
5. **FrmGestionEmpleados.cargarDepartamentos()** — cambiar a `controller.obtenerDepartamentos()`.

Por qué este orden: views primero evitan compilación rota. Repositorio segundo porque PlanillaController depende de él. El cambio de departamentos es el más simple y va al final.

---

## 6. Rollback Plan

1. `git stash` si no hay commits intermedios, o `git revert <commit>` por cada commit individual.
2. Si se commitearon los cambios de views pero no controllers, hacer `git revert` de los commits de views y luego `git reset` de los controllers — las views sin controllers no causan error de compilación porque el método agregado es nuevo y no obligatorio.
3. Para `RepositorioPlanilla.crearPlanillaConBoletas()`: es un método nuevo, no se llama desde ningún lado hasta que PlanillaController cambia. Revertir el commit del repositorio antes que el del controller.
4. Verificar post-rollback: `git log` para confirmar que HEAD está en el commit pre-refactor, y compilar con Ant para verificar que no hay errores.

**Riesgo más alto**: migración transaccional en `crearPlanillaConBoletas()` — validar manualmente que commit/rollback funcionan antes de integrar.
