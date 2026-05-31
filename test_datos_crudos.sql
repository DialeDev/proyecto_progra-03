-- ========================================================================
-- SCRIPT DE INYECCIÓN DE DATOS CRUDOS PARA PRUEBAS DE ESTRÉS Y CÁLCULOS
-- ========================================================================
USE AsistenciaPlanillasDB;
GO

PRINT '--- 1. LIMPIANDO DATOS DE PRUEBA ANTERIORES DEL MES DE JUNIO (SI EXISTEN) ---'
-- Esto previene errores si corrés el script varias veces.
DELETE FROM dbo.RegistroAsistencia WHERE MarcaEntrada >= '2026-06-01' AND MarcaEntrada < '2026-07-01';
GO

PRINT '--- 2. CREANDO NUEVOS EMPLEADOS DE PRUEBA ---'
DECLARE @IdDeptoRRHH INT, @IdDeptoIng INT, @IdDeptoOp INT;
SELECT @IdDeptoRRHH = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'RRHH';
SELECT @IdDeptoIng = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'Ingeniería';
SELECT @IdDeptoOp = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'Operaciones';

-- Insertamos 3 empleados nuevos
IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '11111111-1')
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoRRHH, 'Ana', 'Pérez', '11111111-1', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '22222222-2')
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoIng, 'Luis', 'Gómez', '22222222-2', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '33333333-3')
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoOp, 'Carmen', 'Díaz', '33333333-3', 1);
GO

PRINT '--- 3. CREANDO CONTRATACIONES VIGENTES PARA LOS EMPLEADOS ---'
DECLARE @IdContratoFijo INT, @IdContratoHora INT;
SELECT @IdContratoFijo = IdContrato FROM dbo.Contrato WHERE Nombre = 'Fijo';
SELECT @IdContratoHora = IdContrato FROM dbo.Contrato WHERE Nombre = 'Por Hora';

DECLARE @IdAna INT, @IdLuis INT, @IdCarmen INT;
SELECT @IdAna = IdEmpleado FROM dbo.Empleado WHERE DUI = '11111111-1';
SELECT @IdLuis = IdEmpleado FROM dbo.Empleado WHERE DUI = '22222222-2';
SELECT @IdCarmen = IdEmpleado FROM dbo.Empleado WHERE DUI = '33333333-3';

-- Ana: Salario alto, Contrato Fijo
IF NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdAna)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Vigente)
    VALUES (@IdAna, @IdContratoFijo, '2026-01-01', 2500.00, NULL, 1);

-- Luis: Salario medio, Contrato Fijo
IF NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdLuis)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Vigente)
    VALUES (@IdLuis, @IdContratoFijo, '2026-02-01', 1200.00, NULL, 1);

-- Carmen: Pago por hora
IF NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdCarmen)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Vigente)
    VALUES (@IdCarmen, @IdContratoHora, '2026-03-01', NULL, 15.00, 1);
GO

PRINT '--- 4. ASIGNANDO JORNADAS DE TRABAJO ---'
DECLARE @IdJornadaCompleta INT, @IdJornadaMatutina INT;
SELECT @IdJornadaCompleta = IdJornada FROM dbo.Jornada WHERE Nombre = 'Completa';
SELECT @IdJornadaMatutina = IdJornada FROM dbo.Jornada WHERE Nombre = 'Matutina';

DECLARE @IdAna INT, @IdLuis INT, @IdCarmen INT;
SELECT @IdAna = IdEmpleado FROM dbo.Empleado WHERE DUI = '11111111-1';
SELECT @IdLuis = IdEmpleado FROM dbo.Empleado WHERE DUI = '22222222-2';
SELECT @IdCarmen = IdEmpleado FROM dbo.Empleado WHERE DUI = '33333333-3';

IF NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdAna)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdAna, @IdJornadaCompleta, '2026-01-01', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdLuis)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdLuis, @IdJornadaCompleta, '2026-02-01', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdCarmen)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdCarmen, @IdJornadaMatutina, '2026-03-01', 1);
GO

PRINT '--- 5. INYECTANDO MARCACIONES DE ASISTENCIA (MES DE JUNIO 2026) ---'
DECLARE @IdAna INT, @IdLuis INT, @IdCarmen INT;
SELECT @IdAna = IdEmpleado FROM dbo.Empleado WHERE DUI = '11111111-1';
SELECT @IdLuis = IdEmpleado FROM dbo.Empleado WHERE DUI = '22222222-2';
SELECT @IdCarmen = IdEmpleado FROM dbo.Empleado WHERE DUI = '33333333-3';

-- Inyectamos datos para 5 días laborales para probar cálculos
-- DÍA 1 (Normal)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdAna, '2026-06-01 08:00:00', '2026-06-01 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdLuis, '2026-06-01 08:00:00', '2026-06-01 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdCarmen, '2026-06-01 07:00:00', '2026-06-01 16:00:00', 0);

-- DÍA 2 (Horas extra diurnas para Luis, Ana normal, Carmen falta)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdAna, '2026-06-02 08:00:00', '2026-06-02 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdLuis, '2026-06-02 08:00:00', '2026-06-02 19:30:00', 0); -- 2.5 hrs extra
-- Carmen no vino

-- DÍA 3 (Ana llega tarde, Luis normal, Carmen normal)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdAna, '2026-06-03 09:30:00', '2026-06-03 17:00:00', 0); -- Tardanza
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdLuis, '2026-06-03 08:00:00', '2026-06-03 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdCarmen, '2026-06-03 07:00:00', '2026-06-03 16:00:00', 0);

-- DÍA 4 (Ana normal, Luis hace horas nocturnas, Carmen normal)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdAna, '2026-06-04 08:00:00', '2026-06-04 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdLuis, '2026-06-04 08:00:00', '2026-06-04 22:00:00', 0); -- 5 hrs extra (algunas nocturnas)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdCarmen, '2026-06-04 07:00:00', '2026-06-04 16:00:00', 0);

-- DÍA 5 (Ana falta justificada, Luis normal, Carmen hace horas extra)
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdAna, '2026-06-05 00:00:00', '2026-06-05 00:00:00', 1); -- Falta justificada
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdLuis, '2026-06-05 08:00:00', '2026-06-05 17:00:00', 0);
INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdCarmen, '2026-06-05 07:00:00', '2026-06-05 18:00:00', 0); -- 2 hrs extra
GO

PRINT '========================================================================'
PRINT '  DATOS DE PRUEBA INYECTADOS CON ÉXITO'
PRINT '========================================================================'
PRINT '  AHORA ABRÍ EL PROGRAMA JAVA Y HACÉ ESTO:'
PRINT '  1. Logueate como admin'
PRINT '  2. Andá a Planillas'
PRINT '  3. Generá una planilla nueva para: MES 6 (Junio) y AÑO 2026'
PRINT '  4. Revisá si el cálculo de horas extra, llegadas tardías y descuentos'
PRINT '     de ley (ISSS/AFP) funcionan correctamente para Ana, Luis y Carmen.'
PRINT '========================================================================'
GO
