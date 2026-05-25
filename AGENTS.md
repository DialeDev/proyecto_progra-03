# AGENTS.md — Sistema de Control de Asistencia y Planillas

## How to Use This Guide

Java Swing desktop application for employee attendance tracking, contract management, and payroll generation with role-based access control (RBAC). This file defines AI agent conventions, project architecture, and development patterns for the **proyecto_progra-03** repository.

---

## Available Skills

| Skill | Description | Location |
|-------|-------------|----------|
| **netbeans-form-safe-edit** | Safely edit Java Swing JFrameForms in NetBeans without corrupting `.form` files. Enforces strict separation between hand-editable `.java` code and NetBeans-generated `.form` XML. | `./.opencode/skills/netbeans-form-safe-edit/` |
| **branch-pr** | Create Gentle AI pull requests with issue-first checks. | `./.opencode/skills/branch-pr/` |
| **chained-pr** | Split oversized changes into chained PRs that protect review focus. | `./.opencode/skills/chained-pr/` |
| **work-unit-commits** | Plan commits as reviewable work units for implementation, chained PRs, or keeping tests/docs with code. | `./.opencode/skills/work-unit-commits/` |

---

## Auto-invoke Skills

| Action | Skill |
|--------|-------|
| Editing `.form` files or Swing UI components in NetBeans | **netbeans-form-safe-edit** |
| Creating or updating pull requests | **branch-pr** |
| Changes exceeding 400 lines or requiring stacked PRs | **chained-pr** |
| Implementing features or splitting commits for review | **work-unit-commits** |

---

## Stack

| Technology | Purpose |
|------------|---------|
| Java | Core programming language |
| Swing | Desktop UI framework for Java |
| SQL Server | Relational database for data persistence |
| JDBC | Java Database Connectivity for SQL Server |
| NetBeans IDE | Integrated Development Environment |
| Ant | Build automation tool |

---

## DevStack

| Technology | Purpose |
|------------|---------|
| NetBeans | Primary IDE for Swing development |
| SQL Server Management Studio | Database management and query execution |
| Git | Version control and collaboration |
| Ant | Build automation (via NetBeans) |

---

## Project Structure

```
proyecto_progra-03/
├── src/
│   ├── main/
│   │   ├── App.java                    # Single entry point
│   │   └── PruebaConexion.java         # Connection test harness
│   │
│   ├── models/                         # POJOs (data only, no logic)
│   │   ├── Empleado.java
│   │   ├── Usuario.java
│   │   ├── Contrato.java
│   │   ├── Contratacion.java
│   │   ├── Jornada.java
│   │   ├── Departamento.java
│   │   ├── AsignacionJornada.java
│   │   ├── RegistroAsistencia.java
│   │   ├── Planilla.java
│   │   ├── BoletaPago.java
│   │   ├── BoletaDetalle.java
│   │   └── Permiso.java                # RBAC: rol, modulo, depto scope, flags
│   │
│   ├── utils/                          # Global utilities
│   │   ├── SesionGlobal.java           # Active user session
│   │   ├── UIStyles.java               # Centralized UI theming
│   │   └── ExportadorCSV.java          # CSV export utility
│   │
│   ├── dao/                            # Data access (SQL only, no business logic)
│   │   ├── Conexion.java               # SQL Server connection
│   │   ├── RepositorioEmpleado.java
│   │   ├── RepositorioUsuario.java
│   │   ├── RepositorioContrato.java
│   │   ├── RepositorioContratacion.java
│   │   ├── RepositorioJornada.java
│   │   ├── RepositorioDepartamento.java
│   │   ├── RepositorioAsignacionJornada.java
│   │   ├── RepositorioAsistencia.java
│   │   ├── RepositorioPlanilla.java
│   │   ├── RepositorioBoletaPago.java
│   │   ├── RepositorioBoletaDetalle.java
│   │   └── RepositorioPermiso.java     # RBAC queries
│   │
│   ├── services/                       # Business logic layer (stateless)
│   │   ├── PermisoService.java         # RBAC cache + tieneAcceso() + Admin bypass
│   │   ├── GeneradorBoletas.java       # Payroll slip generation
│   │   ├── CalculadoraHorasExtras.java # Overtime calculation
│   │   ├── CalculadoraDeduccionesLey.java # Legal deductions
│   │   └── ExportadorCSV.java          # Data export
│   │
│   ├── controllers/                    # Orchestrators (validate + delegate)
│   │   ├── AuthController.java
│   │   ├── DashboardController.java    # Module navigation + RBAC guard
│   │   ├── EmpleadoController.java
│   │   ├── ContratoController.java
│   │   ├── RegistroContratoController.java
│   │   ├── HorarioController.java
│   │   ├── DepartamentoController.java
│   │   ├── AsignacionJornadaController.java
│   │   ├── AsistenciaController.java
│   │   ├── PlanillaController.java
│   │   ├── ReporteController.java
│   │   └── PermisoController.java      # Permission CRUD
│   │
│   └── views/                          # JFrames (UI only, no logic)
│       ├── FrmLogin.java
│       ├── FrmDashboard.java           # Main menu with role-filtered buttons
│       ├── FrmGestionEmpleados.java
│       ├── FrmGestionContratos.java
│       ├── FrmRegistroContratos.java
│       ├── FrmGestionHorarios.java
│       ├── FrmMarcacion.java
│       ├── FrmPlanillas.java
│       ├── FrmGestionDepartamentos.java
│       ├── FrmAsignacionJornada.java
│       ├── FrmReportes.java
│       ├── FrmVisualizarBoleta.java
│       └── FrmGestionPermisos.java     # Permission management UI
│
├── lib/                                # External dependencies
│   ├── mssql-jdbc-13.4.0.jre11.jar
│   └── mssql-jdbc_auth-13.4.0.x64.dll
│
├── openspec/                           # SDD artifacts (archive + specs)
│   ├── specs/
│   │   └── access-control/
│   │       └── spec.md                # RBAC source-of-truth spec
│   └── changes/
│       └── archive/                   # Completed change folders
│
├── docs/
├── nbproject/
├── .git/
├── .opencode/
├── build.xml
├── manifest.mf
├── scriptBD.sql                       # DB schema + seed data
└── README.md
```

---

## Commands

| Command | Scope | Description |
|---------|-------|-------------|
| `ant run` | Project | Build and run via NetBeans Ant |
| `ant clean` | Project | Clean build artifacts |
| `ant jar` | Project | Package distributable JAR |
| App.java | Entry point | Run via NetBeans (right-click → Run File) |

---

## Patterns & Rules

### Architecture

The project uses **MVC (Model-View-Controller)** with a **Repository** layer and a dedicated **Service** layer for RBAC:

```
View (JFrame) → Controller (validate + orchestrate) → Repository (SQL)
                                  ↓
                          Service (RBAC, calculations)
                                  ↓
                          Model (POJO)
```

### MVC Layering Rules

1. **View (JFrames)**: Capture user events and delegate to Controllers. **Must not** contain business logic, validations, or data persistence.
2. **Controller**: Validate data, create Model objects, orchestrate Repositories and Services. **Must not** interact directly with the database.
3. **Repository**: Execute SQL queries only. **Must not** contain business logic or display UI messages.
4. **Service**: Stateless business logic (permission checks, calculations). **Must not** interact with the database directly.
5. **Model (POJOs)**: Plain data containers. **Must not** contain logic.

### RBAC (Role-Based Access Control)

The system has **3 roles** with department-scoped permissions:

| Role | Access Level |
|------|-------------|
| **Admin** | Bypasses all permission checks (hardcoded in PermisoService). Full access to all 9 modules + Permission management. |
| **RRHH** | Restricted by Permiso table. Has visibility into 4 modules by default. |
| **Empleado** | Restricted by Permiso table. Only sees own Marcacion by default. |

**Implementation:**

- `Permiso` table with nullable `IdDepartamento` (NULL = global permission, value = department-scoped).
- `PermisoService` loads all permissions into a `Map` cache at Dashboard init.
- `DashboardController` calls `tieneAcceso(usuario, modulo, accion)` before each `abrirXxx()` — prevents URL/button bypass.
- Admin bypass is hardcoded in `PermisoService.tieneAcceso()` to avoid a DB query.
- **Dual-layer enforcement**: UI hiding (buttons hidden in FrmDashboard) + controller guard (abrirXxx checks).

### Database

- **Server**: SQL Server via Windows Integrated Security.
- **Connection**: `src/dao/Conexion.java` — JDBC URL with `integratedSecurity=true`.
- **Schema**: `scriptBD.sql` — creates `AsistenciaPlanillasDB`, all tables, and seed data (idempotent).
- **Seed users**: `admin/admin123` (Admin), `mgarcia/123456` (RRHH), `cmartinez/123456` (Empleado, Ingeniería), `plopez/123456` (Empleado, Administración).

### Critical Rules

1. **Single Entry Point**: Always run via `src/main/App.java`. **Never** run JFrames individually.
2. **No Direct DB Access**: Views and Controllers **must not** use JDBC directly. Use Repositories.
3. **Error Handling**: Repositories **must not** show error messages. Controllers handle errors and update Views.
4. **NetBeans Form Files (`.form`)**: **Never** edit `.form` files manually. Use the NetBeans designer.
5. **Post-Init Pattern**: Custom table models, combo box population, and renderers MUST live in a separate method called **after** `initComponents()` (e.g., `inicializarPersonalizaciones()`). NetBeans regenerates `initComponents()` on each designer save; the post-init method survives untouched.

---

## References

- [README.md](./README.md)
- [openspec/specs/access-control/spec.md](./openspec/specs/access-control/spec.md)
- [.opencode/skills/netbeans-form-safe-edit/SKILL.md](./.opencode/skills/netbeans-form-safe-edit/SKILL.md)
