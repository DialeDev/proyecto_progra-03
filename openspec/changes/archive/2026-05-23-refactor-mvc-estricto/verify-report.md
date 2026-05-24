## Verify Report: refactor-mvc-estricto

### Summary
- **CRITICAL**: 1 issues que rompen funcionalidad
- **WARNING**: 0 issues que deberían arreglarse
- **SUGGESTION**: 1 mejoras opcionales

### By Spec

#### controller-view-messaging
- ✅ R-VC2: Controllers do not import javax.swing.JOptionPane — evidence: AuthController imports list: dao.RepositorioUsuario, models.Usuario, utils.SesionGlobal, views.FrmLogin (src/controllers/AuthController.java:3-8)
- ✅ R-VC2: Controllers do not import javax.swing.* — evidence: EmpleadoController imports javax.swing.table.DefaultTableModel (specific class) not a wildcard (src/controllers/EmpleadoController.java:8)
- ✅ R-VC1: Each view exposes mostrarMensaje(String tipo, String mensaje) — evidence: FrmLogin.mostrarMensaje(...) present (src/views/FrmLogin.java:42-49); FrmGestionEmpleados.mostrarMensaje(...) present (src/views/FrmGestionEmpleados.java:25-32); FrmGestionContratos.mostrarMensaje(...) present (src/views/FrmGestionContratos.java:32-39); FrmRegistroContratos.mostrarMensaje(...) present (src/views/FrmRegistroContratos.java:79-86)
- ✅ Messages preserved and used by controllers (examples): AuthController uses "Credenciales incorrectas o usuario inactivo." (src/controllers/AuthController.java:37); EmpleadoController uses "Empleado registrado." and "Todos los campos son obligatorios." (src/controllers/EmpleadoController.java:51-56,82-83)
- ✅ AuthController delegates to vista.mostrarMensaje() instead of JOptionPane — evidence: AuthController calls vista.mostrarMensaje("error", "Credenciales incorrectas o usuario inactivo.") (src/controllers/AuthController.java:37)
- ✅ EmpleadoController uses vista.mostrarMensaje()/vista.mostrarConfirmacion() — evidence: calls mostrarConfirmacion in desactivarEmpleado() and mostrarMensaje in guardarEmpleado() (src/controllers/EmpleadoController.java:121,51-56)
- ✅ ContratoController uses vista.mostrarMensaje()/mostrarConfirmacion() — evidence: guardarTipoContrato uses vista.mostrarMensaje("exito", "Guardado exitosamente.") and mostrarConfirmacion used in actualizarTipoContrato (src/controllers/ContratoController.java:82-83,100-104)
- ✅ RegistroContratoController calls vistaRegistro.mostrarMensaje() for outcomes — evidence: guardarContratacion() calls vistaRegistro.mostrarMensaje("exito", "Contratación registrada exitosamente.") and error path (src/controllers/RegistroContratoController.java:82-89)

#### repository-planilla
- ✅ R-RP2: PlanillaController has no java.sql imports nor dao.Conexion usage — evidence: PlanillaController imports only RepositorioPlanilla, RepositorioEmpleado, RepositorioContratacion and delegates to planillaDAO.crearPlanillaConBoletas(...) (src/controllers/PlanillaController.java:3-6,48-51)
- ✅ R-RP1: RepositorioPlanilla.crearPlanillaConBoletas(...) exists and manages transaction lifecycle — evidence: method conectar, setAutoCommit(false), crear(conn, planilla), generador.generarYGuardarBoleta(...), commit/rollback/close implemented (src/dao/RepositorioPlanilla.java:99-124; specifically conn = Conexion.getConnection() at line 106, conn.setAutoCommit(false) at 107, conn.commit() at 112, rollback in catch at 115-117, close in finally at 120-122)
- ✅ Transaction covers insert planilla, obtain generated ID and insert boletas — evidence: crear(conn, planilla) returns idPlanilla then loop generador.generarYGuardarBoleta(conn, idPlanilla, ...) invoked for each Empleado (src/dao/RepositorioPlanilla.java:108-111)
- ✅ commit/rollback/close handled — evidence: try/catch/finally with rollback on Exception and close in finally (src/dao/RepositorioPlanilla.java:105-123)

#### controller-empleado
- ✅ R-CE2: FrmGestionEmpleados does NOT import or instantiate controllers.DepartamentoController — evidence: imports at top include controllers.DashboardController and controllers.EmpleadoController but no DepartamentoController (src/views/FrmGestionEmpleados.java:3-6)
- ✅ R-CE1: EmpleadoController.exponer obtenerDepartamentos() returning List<Departamento> — evidence: public List<Departamento> obtenerDepartamentos() { return departamentoDAO.findAll(); } (src/controllers/EmpleadoController.java:28-30)
- ⚠️ R-CE2 runtime ordering bug (CRITICAL): FrmGestionEmpleados.cargarDepartamentos() calls controller.obtenerDepartamentos() but controller is initialized after configurarComponentes() in the constructor, causing a NullPointerException at runtime when the view loads — evidence: constructor calls configurarComponentes() before assigning this.controller = new EmpleadoController(this) (src/views/FrmGestionEmpleados.java:17-22 and cargarDepartamentos uses controller.obtenerDepartamentos() at src/views/FrmGestionEmpleados.java:56-59). This breaks functionality at view initialization.

### Overall Verdict
FAIL

---

Notes:
- The functional show-stopper is the view/controller initialization order in FrmGestionEmpleados leading to a NullPointerException when the form is constructed. The change that removed direct DepartamentoController instantiation in the view is correct by design, but the controller must be available before views call controller methods (move controller assignment earlier or delay cargarDepartamentos until after controller creation).
- Suggestion (non-blocking): Consider reviewing other view constructors for similar ordering issues when moving logic from views to controllers.
