# Sistema de Control de Asistencia y Planillas

Una aplicación de escritorio en Java Swing para la gestión de recursos humanos: control de asistencia, administración de contratos, generación de planillas de pago y control de acceso por roles.

Desarrollado como proyecto de **Programación III — UTEC**.

---

## Tabla de Contenidos

- [Cómo empezar](#cómo-empezar)
- [Roles y Permisos](#roles-y-permisos)
- [Módulos del Sistema](#módulos-del-sistema)
- [Arquitectura](#arquitectura)
- [Base de Datos](#base-de-datos)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Solución de Problemas](#solución-de-problemas)
- [Créditos](#créditos)

---

## Cómo empezar

### Requisitos

- Java JDK 17 o superior
- SQL Server 2022 (o Express)
- NetBeans IDE (recomendado)

### Paso a paso

1. **Crear la base de datos**

   Abrí `scriptBD.sql` en SQL Server Management Studio y ejecutalo. Crea todo: la base `AsistenciaPlanillasDB`, las tablas y los datos de prueba.

2. **Abrir el proyecto en NetBeans**

   File → Open Project → seleccioná la carpeta del proyecto.

3. **Ejecutar**

   Hacé clic derecho en `src/main/App.java` → **Run File**. **NUNCA** ejecutes las pantallas (JFrames) individualmente.

4. **Iniciar sesión**

   Usá una de estas credenciales de prueba:

   | Usuario | Contraseña | Rol | Departamento |
   |---------|-----------|-----|--------------|
   | admin | admin123 | Admin | — (global) |
   | mgarcia | 123456 | RRHH | — (global) |
   | cmartinez | 123456 | Empleado | Ingeniería |
   | plopez | 123456 | Empleado | Administración |

---

## Roles y Permisos

El sistema tiene un control de acceso basado en **3 roles** con permisos que pueden limitarse a un departamento específico.

### Roles

| Rol | ¿Qué puede hacer? |
|-----|------------------|
| **Admin** | Todo. No tiene restricciones. Ve los 9 módulos y la gestión de permisos. |
| **RRHH** | Accede a los módulos que tenga habilitados en la tabla de permisos. Por defecto: Empleados, Contratos, Horarios y Marcación. |
| **Empleado** | Solo ve su propia marcación de entrada/salida y su boleta de pago. |

### ¿Cómo funcionan los permisos?

- Cada permiso se guarda en la tabla `Permiso` y define: **qué rol** puede hacer **qué acción** (ver/operar) sobre **qué módulo**.
- Los permisos pueden ser **globales** (aplica a todo el sistema) o **por departamento** (ej: un usuario de RRHH solo gestiona empleados de su departamento).
- Cuando iniciás sesión, el sistema carga todos tus permisos en memoria. Si un botón no te aparece en el menú, es porque no tenés permiso para ese módulo.

> 💡 **Admin tiene un bypass**: ni siquiera consulta la base de datos para verificar permisos. Todo le está permitido por hardcode.

### Gestión de Permisos

Solo **Admin** puede acceder a la pantalla de gestión de permisos (`FrmGestionPermisos`) para configurar qué ve cada rol.

---

## Módulos del Sistema

| # | Módulo | ¿Quién lo ve? |
|---|--------|---------------|
| 1 | 👥 Empleados | Admin, RRHH |
| 2 | ⏱ Marcación | Admin, RRHH, Empleado |
| 3 | 💰 Planillas | Admin, RRHH |
| 4 | 📄 Contratos | Admin, RRHH |
| 5 | 🕐 Horarios | Admin, RRHH |
| 6 | 📊 Reportes | Admin, RRHH |
| 7 | 🏢 Departamentos | Admin, RRHH |
| 8 | 🔄 Asignación Jornada | Admin, RRHH |
| 9 | 🔐 Gestión de Permisos | Admin |

> Los permisos de cada rol son configurables desde `FrmGestionPermisos`. La tabla de arriba muestra la configuración por defecto.

---

## Arquitectura

El proyecto sigue **MVC (Model-View-Controller)** con una capa de **Repositorios** y una capa de **Servicios**.

```
           ┌──────────┐
           │  JFrame  │  ← Captura clicks, muestra datos. NUNCA tiene lógica.
           └────┬─────┘
                │  delegá
           ┌────▼──────┐
           │ Controller │  ← Valida, orquesta, decide.
           └────┬──────┘
                │
        ┌───────┴───────┐
        │               │
   ┌────▼─────┐   ┌────▼────┐
   │ Service  │   │    DAO  │  ← SQL puro. Sin lógica de negocio.
   │ (lógica) │   └─────────┘
   └──────────┘
        │
   ┌────▼────┐
   │  Model  │  ← POJO. Solo datos.
   └─────────┘
```

### Capas

- **View** (JFrames): Capturá eventos → llamá al Controller. Nada más.
- **Controller**: Validá datos, armá objetos, llamá al Service/DAO, actualizá la View.
- **Service**: Lógica de negocio pura (cálculos, permisos). Sin JDBC.
- **DAO**: SQL directo. Sin mensajes al usuario.
- **Model**: Propiedades + getters/setters. Nada más.

---

## Base de Datos

**Servidor:** SQL Server con autenticación de Windows.
**Cadena de conexión:** configurada en `src/dao/Conexion.java`.

```
jdbc:sqlserver://localhost:1433;databaseName=AsistenciaPlanillasDB;
  integratedSecurity=true;encrypt=true;trustServerCertificate=true;
```

### Tablas principales

| Tabla | Descripción |
|-------|-------------|
| Empleado | Datos personales y laborales |
| Usuario | Credenciales de acceso, vinculado a un Empleado |
| Contrato | Tipos de contrato |
| Contratacion | Historial de contratos del empleado |
| Jornada | Horarios laborales |
| Departamento | Áreas de la empresa |
| AsignacionJornada | Relación empleado ↔ jornada |
| RegistroAsistencia | Marcaciones de entrada/salida |
| Planilla | Cabecera de liquidación |
| BoletaPago | Detalle de cada empleado en la planilla |
| BoletaDetalle | Conceptos individuales de pago/descuento |
| Permiso | Reglas de acceso por rol y módulo |

### Datos de prueba

El `scriptBD.sql` incluye usuarios, empleados, departamentos (Ingeniería, Administración), contratos y jornadas de ejemplo.

---

## Estructura del Proyecto

```
src/
├── main/           Punto de entrada
├── models/         POJOs (12 clases)
├── dao/            Acceso a datos (13 repositorios)
├── services/       Lógica de negocio (5 servicios)
├── controllers/    Orquestadores (12 controladores)
└── views/          Pantallas Swing (13 formularios)
```

Cada clase tiene **una responsabilidad**. Si estás agregando lógica de negocio, va en `services/`. Si es SQL, va en `dao/`. Si es UI, va en `views/`.

---

## Solución de Problemas

### Error de conexión TCP/IP

```
The TCP/IP connection to the host localhost, port 1433 has failed.
```

**Causa:** SQL Server tiene TCP/IP deshabilitado o puerto dinámico.

**Solución rápida:**
1. Abrí **SQL Server Configuration Manager**
2. **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Habilitá **TCP/IP**
4. En propiedades de TCP/IP → IP Addresses → IPAll: borrá **TCP Dynamic Ports**, poné **1433** en **TCP Port**
5. Reiniciá el servicio de SQL Server

### Warning de restricted method

```
WARNING: A restricted method in java.lang.System has been called
```

No te preocupes, es un warning inofensivo de Java 17+ con el driver JDBC. No afecta al funcionamiento.

---

## Créditos

Desarrollado por estudiantes de Programación III — UTEC.

**Stack:** Java + Swing, SQL Server, JDBC, NetBeans IDE, Ant.

¿Preguntas o sugerencias? Abrí un issue en el repositorio.
