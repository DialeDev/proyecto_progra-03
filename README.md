# Sistema de Control de Asistencia y Generación de Planillas de Pago

Este repositorio contiene el código fuente del proyecto de Programación III (UTEC). Es un sistema de gestión de recursos humanos que incluye control de asistencia, administración de contratos y generación de planillas de pago.

Este documento sirve como manual para los miembros del equipo y como contexto arquitectónico para cualquier asistente de Inteligencia Artificial.

---

## 1. Instrucciones de Ejecución

El proyecto tiene un único punto de entrada seguro.
**NUNCA** ejecutes las pantallas (JFrames) de forma individual dando clic derecho y "Run File".

Para iniciar la aplicación:
1. Abre NetBeans.
2. Ve al archivo `src/main/App.java`.
3. Ejecuta ese archivo. Este se encargará de cargar la configuración visual y abrir la pantalla de Login correctamente.

**Credenciales por defecto:**
- Usuario: `admin`
- Password: `admin123`

---

## 2. Arquitectura del Proyecto

El proyecto sigue el patrón **MVC (Model-View-Controller)** con una capa de **Repositorios** para el acceso a datos.

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    View     │────▶│ Controller  │────▶│ Repositorio │
│  (JFrames)  │◀────│  (Lógica)   │◀────│   (SQL)     │
└─────────────┘     └─────────────┘     └─────────────┘
                           │
                    ┌──────┴──────┐
                    │    Model    │
                    │   (POJOs)   │
                    └─────────────┘
```

### Flujo de datos
1. Usuario interactúa con la View (JFrame)
2. La View delega al Controller mediante ActionListener
3. Controller valida datos y crea objetos Model
4. Controller llama al Repositorio para persistir
5. Controller actualiza la View (tablas, mensajes)

---

## 3. Estructura de Carpetas

Todo el código debe ir estrictamente dentro de estos paquetes en la carpeta `src`.

```
src/
├── main/
│   └── App.java                      # Punto de entrada único
│
├── models/                           # Clases de datos (POJOs)
│   ├── Empleado.java                 # Datos del empleado
│   ├── Usuario.java                  # Credenciales y rol
│   ├── Contrato.java                 # Tipos de contrato
│   ├── Contratacion.java             # Historial laboral
│   ├── Jornada.java                  # Horario laboral
│   ├── RegistroAsistencia.java       # Marcaciones
│   ├── Planilla.java                 # Cabecera de planilla
│   ├── BoletaPago.java               # Detalle de nómina
│   └── BoletaDetalle.java            # Conceptos de pago
│
├── utils/                            # Utilidades globales
│   └── SesionGlobal.java             # Sesión del usuario actual
│
├── dao/                              # Capa de acceso a datos
│   ├── Conexion.java                 # Conexión a SQL Server
│   ├── RepositorioEmpleado.java      # CRUD Empleados
│   ├── RepositorioUsuario.java       # CRUD Usuarios
│   ├── RepositorioContratacion.java  # CRUD Contrataciones
│   ├── RepositorioAsistencia.java    # CRUD Asistencias
│   └── RepositorioPlanilla.java      # CRUD Planillas
│
├── controllers/                      # Lógica de negocio
│   ├── AuthController.java           # Login y autenticación
│   ├── DashboardController.java      # Navegación principal
│   ├── EmpleadoController.java       # Gestión de empleados
│   ├── PlanillaController.java       # Generación de planillas
│   ├── ContratoController.java       # Gestión de contratos
│   ├── RegistroContratoController.java
│   ├── HorarioController.java        # Gestión de horarios
│   └── AsistenciaController.java     # Control de asistencia
│
└── views/                            # Interfaces visuales (JFrames)
    ├── FrmLogin.java                 # Pantalla de login
    ├── FrmDashboard.java             # Menú principal
    ├── FrmGestionEmpleados.java      # CRUD Empleados
    ├── FrmGestionContratos.java      # Administración de contratos
    ├── FrmRegistroContratos.java     # Nuevo contrato
    ├── FrmGestionHorarios.java       # Administración de horarios
    ├── FrmMarcacion.java             # Registro de asistencia
    └── FrmPlanillas.java             # Generación de planillas
```

---

## 4. La Plantilla Maestra (Golden Template)

El módulo de **Gestión de Empleados** es la plantilla perfecta. Si necesitas programar algo nuevo, estudia este módulo:

1. **Model (`models/Empleado.java`):** Solo datos + getters/setters
2. **Repositorio (`dao/RepositorioEmpleado.java`):** Queries SQL a la base de datos
3. **Controller (`controllers/EmpleadoController.java`):** Validaciones + orquestación
4. **View (`views/FrmGestionEmpleados.java`):** UI "tonta" que solo captura eventos

---

## 5. Reglas de Oro

**NUNCA** rompas estas reglas:

1. **Views "tontas":** Los JFrames solo capturan eventos de usuario. Prohibido hacer validaciones, cálculos o guardar datos en el código de los botones.

2. **Controllers validadores:** El Controller recibe datos de la View, valida (ej: campos vacíos), y llama al Repositorio. Nunca la View llama directo al Repositorio.

3. **Repositorios silenciosos:** Solo guardan y leen datos. No muestran mensajes de error nivalidan reglas de negocio.

4. **Una responsabilidad:** Cada clase hace UNA cosa. Controller = lógica, View = UI, Model = datos.

---

## 6. Base de Datos

### Requisitos
- SQL Server 2022
- Autenticación de Windows (Integrated Security)

### Configuración
La conexión está configurada en `src/dao/Conexion.java`:
```java
jdbc:sqlserver://localhost:1433;databaseName=AsistenciaPlanillasDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;
```

### Inicialización
1. Ejecutar `scriptBD.sql` en SQL Server Management Studio
2. El script crea la base de datos, tablas y datos de prueba automáticamente
3. Usuario por defecto: `admin` / `admin123`

### Dependencias
El driver JDBC está en la carpeta `lib/`:
- `mssql-jdbc-13.4.0.jre11.jar`
- `mssql-jdbc_auth-13.4.0.x64.dll` (para autenticación Windows)

---

## 7. Requisitos Técnicos

### Java
- **Versión:** JDK 25 
- **Nota:** El proyecto usa funcionalidades de Java 17+ y JDBC driver compatible

### SQL Server
- **Versión:** SQL Server 2022 o Express
- **Puerto:** 1433 (TCP/IP debe estar habilitado)

---

## 8. Troubleshooting

### Error: "TCP/IP connection to host localhost, port 1433 has failed"

**Síntoma:**
```
The TCP/IP connection to the host localhost, port 1433 has failed. 
Error: "Connection refused: getsockopt"
```

**Causa:** SQL Server tiene el protocolo TCP/IP deshabilitado o usa un puerto dinámico.

**Solución:**

1. **Habilitar TCP/IP:**
   - Abre **SQL Server Configuration Manager**
   - Ve a **SQL Server Network Configuration** → **Protocols for MSSQLSERVER** (o SQLEXPRESS)
   - Click derecho en **TCP/IP** → **Enable**
   - Reinicia el servicio de SQL Server

2. **Configurar puerto estático (importante para SQL Express):**
   - En **SQL Server Configuration Manager**
   - **Protocols for SQLEXPRESS** → Doble click en **TCP/IP**
   - Pestaña **IP Addresses** → sección **IPAll**
   - **Borra** el valor de **TCP Dynamic Ports**
   - Escribe **1433** en **TCP Port**
   - Aplica y reinicia SQL Server

3. **Verificar puerto con PowerShell:**
   ```powershell
   Get-NetTCPConnection -LocalPort 1433 -State Listen
   ```

### Warning: "Restricted method in java.lang.System has been called"

**Síntoma:**
```
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::loadLibrary has been called by com.microsoft.sqlserver.jdbc.AuthenticationJNI
```

**Causa:** Java 17+ protege el acceso a librerías nativas. Es un warning inofensivo.

---

## 9. Módulos del Sistema

| Módulo | Descripción | Estado |
|--------|-------------|--------|
| Login | Autenticación de usuarios | ✅ Completo |
| Dashboard | Menú principal | ✅ Completo |
| Gestión Empleados | CRUD completo | ✅ Completo |
| Gestión Contratos | Catálogo de tipos de contrato | ✅ Completo |
| Registro Contratos | Alta de empleados | 🟡 Parcial |
| Gestión Horarios | Jornadas laborales | 🟡 Parcial |
| Marcación | Registro de asistencia | 🔴 Incompleto |
| Planillas | Generación de nómina | ✅ Completo |

---

## 10. Créditos

Desarrollado por estudiantes de Programación III - UTEC

**Tecnologías:**
- Java 25 (Swing)
- SQL Server 2022
- JDBC Driver (mssql-jdbc 13.4.0)
- NetBeans IDE