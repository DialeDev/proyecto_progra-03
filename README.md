# Sistema de Control de Asistencia y Generación de Planillas de Pago

Este repositorio contiene el código fuente del proyecto de Programación III. Actualmente estamos construyendo un prototipo funcional (V1) que guarda los datos temporalmente en la memoria RAM para validar la lógica del negocio antes de conectarnos a la base de datos SQL Server.

Este documento sirve como manual estricto para los miembros del equipo y como contexto arquitectónico para cualquier asistente de Inteligencia Artificial que nos ayude a generar código.

---

## 1. Instrucciones de Ejecución

El proyecto tiene un único punto de entrada seguro. 
**NUNCA** ejecutes las pantallas (JFrames) de forma individual dando clic derecho y "Run File". 

Para iniciar la aplicación:
1. Abre NetBeans.
2. Ve al archivo `src/main/App.java`.
3. Ejecuta ese archivo. Este se encargará de cargar la configuración visual y abrir la pantalla de Login correctamente.

---

## 2. La Plantilla Maestra (Golden Template)

Si necesitas programar una nueva pantalla (por ejemplo, la Gestión de Contratos o Horarios) **no inventes una nueva forma de hacerlo**. Debes copiar exactamente la estructura de cómo construimos el módulo de **Gestión de Empleados**.

Ese módulo es nuestra plantilla perfecta y consta de 4 partes que debes estudiar:

1. **El Modelo (`models/Empleado.java`):** Solo tiene los datos (ID, nombres, apellidos) y sus Getters/Setters.
2. **El Guardado (`dao/memory/EmpleadoDAOMemory.java`):** Es el único archivo que mete o saca datos de nuestra base de datos falsa (`DatabaseMock`).
3. **El Cerebro (`controllers/EmpleadoController.java`):** Es el archivo más importante. Lee lo que el usuario escribió en la pantalla, verifica que no esté vacío y le ordena al DAO que lo guarde. También se encarga de actualizar la tabla visual.
4. **La Pantalla (`views/FrmGestionEmpleados.java`):** Es el diseño visual. Sus botones no calculan nada, solo avisan al Controlador que alguien hizo clic.

Si tu nuevo módulo se parece a esta estructura, tu código será aprobado.

---

## 3. Estructura de Carpetas

Todo el código debe ir estrictamente dentro de estos paquetes en la carpeta `src`. No dejes archivos sueltos.

```text
src/
├── main/
│   └── App.java                      # Único archivo que inicia el programa.
│
├── models/                           # Clases con datos puros.
│   ├── Empleado.java                 
│   ├── Contrato.java                 
│   ├── Contratacion.java             # Historial de salarios del empleado.
│   ├── Jornada.java                  
│   ├── RegistroAsistencia.java       
│   ├── Usuario.java                  # Datos para el inicio de sesión.
│   └── Planilla.java                 
│
├── utils/                            # Herramientas globales.
│   └── SesionGlobal.java             # Guarda quién inició sesión en el sistema.
│
├── dao/                              # Capa que guarda y busca información.
│   ├── interfaces/                   # Archivos que dicen QUÉ se puede hacer.
│   │   ├── IEmpleadoDAO.java
│   │   ├── IAsistenciaDAO.java
│   │   └── IPlanillaDAO.java
│   └── memory/                       # Archivos que dicen CÓMO se hace en la RAM.
│       ├── DatabaseMock.java         # Nuestra base de datos temporal (Listas estáticas).
│       ├── EmpleadoDAOMemory.java
│       ├── AsistenciaDAOMemory.java
│       └── PlanillaDAOMemory.java
│
├── controllers/                      # Lógica principal. Conectan la Pantalla con el DAO.
│   ├── AuthController.java           
│   ├── DashboardController.java      
│   ├── AsistenciaController.java     
│   ├── PlanillaController.java       
│   ├── HorarioController.java        
│   └── EmpleadoController.java       # <-- Úsalo como ejemplo para guiarte.
│
└── views/                            # Pantallas visuales (JFrames).
    ├── FrmLogin.java                 
    ├── FrmDashboard.java             
    ├── FrmMarcacion.java             
    ├── FrmPlanillas.java             
    ├── FrmGestionHorarios.java       
    ├── FrmGestionEmpleados.java      
    └── FrmGestionContratos.java      
```
---

## 4. Reglas de Oro (No Negociables)

Cualquier código que rompa estas reglas será rechazado durante la revisión:

1. **Las Pantallas son "tontas":** Un JFrame solo sirve para que el usuario escriba texto y haga clic. **PROHIBIDO** hacer validaciones (como revisar si un texto está vacío), cálculos matemáticos o guardar datos directamente en el código de los botones.
2. **Los Controladores son los jefes:** El Controlador recibe el texto de la pantalla, aplica las reglas (ej. calcular horas extra) y luego se lo envía al DAO. Todo botón de la pantalla debe llamar a un método del Controlador.
3. **El DAO es el archivero:** Los archivos dentro de `dao/memory` solo meten o sacan datos de las listas. No deben mostrar mensajes de error en pantalla ni validar reglas de negocio. Solo guardan y leen.
4. **Programa usando las Interfaces:** Cuando crees un Controlador, debes llamar al DAO usando su interfaz, nunca la clase de memoria directa.
   - *Forma Correcta:* `IEmpleadoDAO dao = new EmpleadoDAOMemory();`
   - *Forma Incorrecta:* `EmpleadoDAOMemory dao = new EmpleadoDAOMemory();`