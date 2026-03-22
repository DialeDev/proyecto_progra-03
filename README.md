# Sistema de Control de Asistencia y Generación de Planillas de Pago

Bienvenido al repositorio del proyecto de programación Ⅲ

## Estructura de Carpetas (Arquitectura)

```text
src/
├── main/
│   └── App.java                      # Punto de entrada (main). Llama al LoginController.
│
├── models/                           # Entidades puras. Solo atributos, getters y setters.
│   ├── Empleado.java                 # Datos del empleado.
│   ├── Contrato.java                 # Validaciones de tipo de contrato (Fijo/Hora).
│   ├── Jornada.java                  # Definición de horarios y tolerancias.
│   ├── RegistroAsistencia.java       # Timestamps de entrada/salida.
│   └── Planilla.java                 # Datos de consolidación de pagos.
│
├── dao/                              # Capa de Acceso a Datos (Mock en Memoria por ahora).
│   ├── interfaces/                   # Contratos de los DAO.
│   │   ├── IEmpleadoDAO.java
│   │   ├── IAsistenciaDAO.java
│   │   └── IPlanillaDAO.java
│   └── memory/                       # Implementación temporal (Simulación de BD).
│       ├── DatabaseMock.java         # ÚNICA FUENTE DE VERDAD. Contiene las List<> estáticas.
│       ├── EmpleadoDAOMemory.java
│       ├── AsistenciaDAOMemory.java
│       └── PlanillaDAOMemory.java
│
├── controllers/                      # Lógica de negocio. Conectan la Vista con el DAO.
│   ├── AuthController.java           # Gestiona la lógica del Login.
│   ├── DashboardController.java      # Carga métricas y navega a otras ventanas.
│   ├── AsistenciaController.java     # Lógica de marcación y cálculo automático de horas.
│   ├── PlanillaController.java       # Motor de cálculo de deducciones y leyes sociales.
│   ├── HorarioController.java        # Gestiona la asignación de jornadas.
│   ├── EmpleadoController.java       # Gestiona CRUD de empleados.
│	└── ContratoController.java		  # Gestiona CRUD de contratos.
│
├── views/                            # JFrames (Interfaces Gráficas). CERO lógica de negocio aquí.
    ├── FrmLogin.java                 # Pantalla de acceso.
    ├── FrmDashboard.java             # Menú principal tras el login.
    ├── FrmMarcacion.java             # Interfaz para que el empleado marque Entrada/Salida.
    ├── FrmPlanillas.java             # Interfaz para generar y ver planilas.
    ├── FrmGestionHorarios.java       # Interfaz de jornadas.
    ├── FrmGestionEmpleados.java      # Interfaz de registro de personal.
    └── FrmGestionContratos.java      # Interfaz de contratos.
```

## Reglas de Oro para el Equipo (No Negociables)

1.  **Las Vistas (JFrames) son "tontas":** Un JFrame solo recoge datos de los TextFields y se los envía al Controlador. **PROHIBIDO** hacer cálculos matemáticos o guardar datos directamente en los eventos de los botones.
    
2.  **Los Controladores son los jefes:** El Controlador recibe los datos del JFrame, valida que sean correctos, aplica la lógica de negocio (ej. calcular horas) y se los envía al DAO para guardarlos.
    
3.  **El DAO es el archivero:** Los archivos dentro de `dao/memory` solo guardan o buscan datos en la memoria. No imprimen mensajes (`System.out.print`), no validan si el DUI es correcto. Solo guardan y leen.
    
4.  **Uso de Interfaces:** Los Controladores SIEMPRE deben declarar los DAOs usando la interfaz.
    
    -   _Correcto:_ `IEmpleadoDAO dao = new EmpleadoDAOMemory();`
        
    -   _Incorrecto:_ `EmpleadoDAOMemory dao = new EmpleadoDAOMemory();`
        

