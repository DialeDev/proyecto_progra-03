# Design: Módulo de Gestión de Usuarios

## Technical Approach

Extender MVC+Repository existente: `FrmGestionUsuarios` → `UsuarioController` → `RepositorioUsuario`. Admin-only con doble guard (botón oculto + controller check), mismo patrón que `FrmGestionPermisos`. Contraseña en texto plano (deuda documentada). Form crear/editar con toggle visibility inline.

## Architecture Decisions

| Decisión | Opciones | Tradeoff | Decisión |
|----------|----------|----------|----------|
| Admin-only vs RBAC | Usar PermisoService vs `rol.equals("Admin")` | FrmGestionPermisos ya usa hardcode; agregar seed y query para un solo rol es overkill | Hardcode: `"Admin".equals(rol)` en controller |
| Nombre empleado en JTable | Campo transient en Usuario vs DTO separado | DTO añade clase; transient field es mínimo cambio, consistente con DefaultTableModel | `nombreEmpleado` String transient en Usuario, poblado vía JOIN en findAll |
| Form crear/editar | Toggle visibility vs JDialog | Dialog es más limpio pero FrmGestionPermisos usa el mismo form inline | Panel con visibility toggle, mismo layout que Permisos |
| SVG icons en botones | FlatSVGIcon + ColorFilter vs PNGs | Dashboard actual usa PNGs de `/img/`; FlatSVGIcon requiere librería FlatLaf pero da color controlado | FlatSVGIcon con ColorFilter #6161FF (consistente con iconos en permisos) |

## Data Flow

```
FrmGestionUsuarios (JTable + form)
    ↓ eventos (clic, selección fila, submit)
UsuarioController (valida, orquesta)
    ↓ delegación
RepositorioUsuario (SQL directo)
    ↓
SQL Server (JOIN Empleado para nombre completo)
    ↑
SesionGlobal.getUsuarioActivo() → controller valida Admin + no autodesactivación
```

## Component Layout — FrmGestionUsuarios

```
┌──────────────────────────────────────────┐
│ [txtBuscar]  [btnBuscar]                 │  ← Top: search/filter
├──────────────────────────────────────────┤
│ ┌─ JScrollPane ────────────────────────┐ │
│ │  tblUsuarios (JTable, 5 columnas)   │ │  ← Center
│ │  ID | Username | Rol | Empleado |   │ │
│ │  Estado                             │ │
│ └─────────────────────────────────────┘ │
├──────────────────────────────────────────┤
│ [Nuevo] [Editar] [Desactivar] [Reactivar]│  ← Bottom: action buttons
├──────────────────────────────────────────┤
│ Username: [text]  Password: [****]      │  ← Form (toggle visibility)
│ Rol: [Admin|RRHH|Empleado]              │
│ Empleado: [combo: sin asignar]          │
│ Activo: [x]  [Guardar] [Cancelar]      │
└──────────────────────────────────────────┘
```

## File Changes

| File | Action | Descripción |
|------|--------|-------------|
| `src/controllers/UsuarioController.java` | **Create** | CRUD + validaciones + constructor(FrmGestionUsuarios) |
| `src/views/FrmGestionUsuarios.java` | **Create** | JFrame con JTable + panel formulario inline (post-init pattern) |
| `src/views/FrmGestionUsuarios.form` | **Create** | NetBeans form design |
| `src/dao/RepositorioUsuario.java` | **Modify** | +findAll(JOIN Empleado), +findById, +update, +reactivar, +findEmpleadosSinUsuario, +existeUsername |
| `src/models/Usuario.java` | **Modify** | +`nombreEmpleado` (transient, solo display, no persiste) |
| `src/views/FrmDashboard.java` | **Modify** | +btnUsuarios en jPanel1, +getBtnUsuarios(), event handler |
| `src/views/FrmDashboard.form` | **Modify** | +botón en layout Grid (NetBeans designer) |
| `src/controllers/DashboardController.java` | **Modify** | +getBtnUsuarios(), +abrirGestionUsuarios(), entry en cargarPermisos |
| `scriptBD.sql` | **Modify** | +INSERT Permiso (Admin, NULL, 'Usuarios', 1, 1) |

## Interfaces & Method Contracts

### RepositorioUsuario — métodos nuevos

```java
List<Usuario> findAll();                    // SELECT u.*, e.Nombres, e.Apellidos + JOIN
Usuario findById(int id);
void update(Usuario u);                     // Username, Rol, IdEmpleado, Activo
void reactivarUsuario(int id);              // UPDATE Activo=1
List<Empleado> findEmpleadosSinUsuario();   // WHERE id NOT IN (SELECT idEmpleado FROM Usuario WHERE activo=1)
boolean existeUsername(String username, Integer excludeId);  // excludeId para edit (opcional)
```

### UsuarioController

```java
public UsuarioController(FrmGestionUsuarios vista);
List<Usuario> listarUsuarios();
String crearUsuario(String username, String password, String rol, int idEmpleado);
String editarUsuario(int id, String username, String rol, int idEmpleado, boolean activo);
String desactivarUsuario(int id);     // valida no autodesactivación vía SesionGlobal
String reactivarUsuario(int id);
List<Empleado> obtenerEmpleadosSinUsuario();
```

### FrmGestionUsuarios — getters públicos (controller access)

```java
JTable getTblUsuarios();
JTextField getTxtUsername();  JPasswordField getTxtPassword();
JComboBox<String> getCmbRol();  JComboBox<String> getCmbEmpleado();
JCheckBox getChkActivo();  JButton getBtnGuardar();
// + mostrarMensaje(String tipo, String msg), mostrarConfirmacion(String msg)
```

## Patrons & Conventions

- **Post-Init**: `inicializarPersonalizaciones()` después de `initComponents()` — configurar TableModel, cargar combos, aplicar FlatSVGIcon
- **FlatSVGIcon**: usar `new FlatSVGIcon("img/faUser.svg", 24, 24).setColorFilter(new ColorFilter(0x6161FF))` en botones
- **Table style**: mismo row height (32), selection color, y font que FrmGestionPermisos
- **Double-click fila**: cargar datos en formulario de edición
- **Password**: solo JPasswordField en creación, oculto en edición
- **Empleado combo**: `findEmpleadosSinUsuario()` al crear; en edición también incluir el empleado actual del usuario

## Testing Strategy

| Layer | Qué probar | Cómo |
|-------|-----------|------|
| Repositorio | findAll JOIN, UPDATE, subquery empleados sin usuario | Prueba manual contra BD (no hay infra de testing) |
| Controller | Validación username único, autodesactivación, campos requeridos | Manual con JOptionPane assertions |
| View | Layout, botones visibles según estado fila, toggle form | Manual — ejecutar app |

## Migration / Rollout

No migration required. Seed INSERT en `scriptBD.sql` es idempotente (IF NOT EXISTS implícito o manual). Usuarios existentes no se modifican.

## Open Questions

- Ninguna. Todas las decisiones están cubiertas por el codebase existente y las specs.
