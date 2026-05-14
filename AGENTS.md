# AGENTS.md — Sistema de Control de Asistencia y Planillas

## How to Use This Guide

Java Swing application for employee attendance tracking and payroll generation. This file defines AI agent conventions, project-specific skills, and development patterns for the **proyecto_progra-03** repository.

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
| Java | Core programming language (**JDK 21**) |
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
│   │   └── App.java                # Punto de entrada único
│   │
│   ├── models/                   # Clases de datos (POJOs)
│   │   ├── Empleado.java
│   │   ├── Usuario.java
│   │   ├── Contrato.java
│   │   ├── Contratacion.java
│   │   ├── Jornada.java
│   │   ├── RegistroAsistencia.java
│   │   ├── Planilla.java
│   │   ├── BoletaPago.java
│   │   └── BoletaDetalle.java
│   │
│   ├── utils/                    # Utilidades globales
│   │   └── SesionGlobal.java
│   │
│   ├── dao/                      # Capa de acceso a datos
│   │   ├── Conexion.java
│   │   ├── RepositorioEmpleado.java
│   │   ├── RepositorioUsuario.java
│   │   ├── RepositorioContratacion.java
│   │   ├── RepositorioAsistencia.java
│   │   └── RepositorioPlanilla.java
│   │
│   ├── controllers/              # Lógica de negocio
│   │   ├── AuthController.java
│   │   ├── DashboardController.java
│   │   ├── EmpleadoController.java
│   │   ├── PlanillaController.java
│   │   ├── ContratoController.java
│   │   ├── RegistroContratoController.java
│   │   ├── HorarioController.java
│   │   └── AsistenciaController.java
│   │
│   └── views/                    # Interfaces visuales (JFrames)
│       ├── FrmLogin.java
│       ├── FrmDashboard.java
│       ├── FrmGestionEmpleados.java
│       ├── FrmGestionContratos.java
│       ├── FrmRegistroContratos.java
│       ├── FrmGestionHorarios.java
│       ├── FrmMarcacion.java
│       └── FrmPlanillas.java
│
├── lib/                          # Dependencias externas
│   ├── mssql-jdbc-13.4.0.jre11.jar
│   └── mssql-jdbc_auth-13.4.0.x64.dll
│
├── docs/                         # Documentación adicional
├── nbproject/                    # Configuración de NetBeans y Ant
├── .git/                         # Repositorio Git
├── .opencode/                    # Configuración de OpenCode
├── build.xml                     # Script de construcción (Ant)
├── manifest.mf                   # Manifiesto de la aplicación
├── scriptBD.sql                  # Script de inicialización de la base de datos
└── README.md                     # Documentación principal
```

---

## Patterns & Rules

### Architecture

The project follows the **MVC (Model-View-Controller)** pattern with a **Repository layer** for data access:

1. **View (JFrames)**: Captures user events and delegates to Controllers. **Must not** contain business logic, validations, or data persistence.
2. **Controller**: Validates data, creates Model objects, and orchestrates calls to Repositories. **Must not** interact directly with the database.
3. **Repository**: Handles SQL queries and database interactions. **Must not** contain business logic or show error messages.
4. **Model (POJOs)**: Plain Old Java Objects for data representation. **Must not** contain logic.

### Golden Template

Use the **Employee Management module** as the golden template for new features:
- `models/Empleado.java`
- `dao/RepositorioEmpleado.java`
- `controllers/EmpleadoController.java`
- `views/FrmGestionEmpleados.java`

### Rules

1. **Single Entry Point**: Always run the application via `src/main/App.java`. **Never** run JFrames individually.
2. **Separation of Concerns**: Each class has **one responsibility**. Controllers handle logic, Views handle UI, Repositories handle data.
3. **No Direct Database Access**: Views and Controllers **must not** interact directly with the database. Use Repositories.
4. **Error Handling**: Repositories **must not** show error messages. Controllers handle errors and update Views.
5. **Session Management**: Use `utils/SesionGlobal.java` for global session state.
6. **Database Connection**: Configured in `dao/Conexion.java`. Use integrated security for SQL Server.

### NetBeans Form Files (`.form`)

- **Never** edit `.form` files manually. Use the NetBeans designer.
- **Never** mix hand-written code with NetBeans-generated code in `.java` files linked to `.form` files.
- Use the **netbeans-form-safe-edit** skill when modifying Swing UI components.

---

## References

- [README.md](./README.md)
- [.opencode/skills/netbeans-form-safe-edit/SKILL.md](./.opencode/skills/netbeans-form-safe-edit/SKILL.md)