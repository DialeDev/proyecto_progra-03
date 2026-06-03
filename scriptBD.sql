-- ========================================================================
-- SISTEMA DE CONTROL DE ASISTENCIA Y PLANILLAS 
-- ========================================================================

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'AsistenciaPlanillasDB')
BEGIN
    CREATE DATABASE AsistenciaPlanillasDB;
END
GO

USE AsistenciaPlanillasDB;
GO

-- ========================================================================
-- 1. MÓDULO DE GESTIÓN DE PERSONAL 
-- ========================================================================

IF OBJECT_ID('dbo.Departamento', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Departamento (
        IdDepartamento INT IDENTITY(1,1) PRIMARY KEY,
        Nombre VARCHAR(100) NOT NULL,
        Activo BIT NOT NULL DEFAULT 1
    );
END

IF OBJECT_ID('dbo.Empleado', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Empleado (
        IdEmpleado INT IDENTITY(1,1) PRIMARY KEY,
        IdDepartamento INT NOT NULL,
        Nombres VARCHAR(100) NOT NULL,
        Apellidos VARCHAR(100) NOT NULL,
        DUI VARCHAR(10) NOT NULL UNIQUE,
        Activo BIT NOT NULL DEFAULT 1,
        CONSTRAINT FK_Empleado_Depto FOREIGN KEY (IdDepartamento) REFERENCES dbo.Departamento(IdDepartamento)
    );
END

-- ========================================================================
-- 2. MÓDULO DE SEGURIDAD 
-- ========================================================================

IF OBJECT_ID('dbo.Usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Usuario (
        IdUsuario INT IDENTITY(1,1) PRIMARY KEY,
        IdEmpleado INT NOT NULL,
        Username VARCHAR(50) NOT NULL UNIQUE,
        PasswordHash VARCHAR(255) NOT NULL, 
        Rol VARCHAR(50) NOT NULL DEFAULT 'RRHH',
        Activo BIT NOT NULL DEFAULT 1,
        CONSTRAINT FK_Usuario_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
    );
END

-- ========================================================================
-- 3. MÓDULO DE CONTRATACIÓN E HISTORIAL SALARIAL
-- ========================================================================

IF OBJECT_ID('dbo.Contrato', 'U') IS NULL
BEGIN
    -- Catálogo base de tipos de contrato (La Plantilla)
    CREATE TABLE dbo.Contrato (
        IdContrato INT IDENTITY(1,1) PRIMARY KEY,
        Nombre VARCHAR(100) NOT NULL,
        Descripcion VARCHAR(255) NULL,
        Activo BIT NOT NULL DEFAULT 1
    );
END

IF OBJECT_ID('dbo.Contratacion', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Contratacion (
        IdContratacion INT IDENTITY(1,1) PRIMARY KEY,
        IdEmpleado INT NOT NULL,
        IdContrato INT NOT NULL,
        FechaInicio DATE NOT NULL,
        FechaFin DATE NULL, -- Si es NULL, es el contrato actual vigente
        SalarioBaseAcordado DECIMAL(10,2) NULL CHECK (SalarioBaseAcordado >= 0),
        ValorHoraAcordado DECIMAL(10,2) NULL CHECK (ValorHoraAcordado >= 0),
        Observaciones VARCHAR(255) NULL,
        Vigente BIT NOT NULL DEFAULT 1,
        CONSTRAINT FK_Contratacion_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado),
        CONSTRAINT FK_Contratacion_Contrato FOREIGN KEY (IdContrato) REFERENCES dbo.Contrato(IdContrato)
    );
END

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='Contratacion' AND COLUMN_NAME='Observaciones')
    ALTER TABLE dbo.Contratacion ADD Observaciones VARCHAR(255) NULL;

-- ========================================================================
-- 4. MÓDULO DE HORARIOS Y CONTROL DE ASISTENCIA
-- ========================================================================

IF OBJECT_ID('dbo.Jornada', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Jornada (
        IdJornada INT IDENTITY(1,1) PRIMARY KEY,
        Nombre VARCHAR(50) NOT NULL,
        HoraEntradaEsperada TIME NOT NULL,
        HoraSalidaEsperada TIME NOT NULL,
        MargenToleranciaMinutos INT NOT NULL DEFAULT 0, 
        Activa BIT NOT NULL DEFAULT 1
    );
END

IF OBJECT_ID('dbo.AsignacionJornada', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.AsignacionJornada (
        IdAsignacion INT IDENTITY(1,1) PRIMARY KEY,
        IdEmpleado INT NOT NULL,
        IdJornada INT NOT NULL,
        FechaAsignacion DATE NOT NULL DEFAULT GETDATE(),
        FechaFin DATE NULL,
        Activa BIT NOT NULL DEFAULT 1,
        CONSTRAINT FK_Asig_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado),
        CONSTRAINT FK_Asig_Jornada FOREIGN KEY (IdJornada) REFERENCES dbo.Jornada(IdJornada)
    );
END
-- Agregar columna FechaFin si la tabla ya existe sin ella
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='AsignacionJornada' AND COLUMN_NAME='FechaFin')
    ALTER TABLE dbo.AsignacionJornada ADD FechaFin DATE NULL;

IF OBJECT_ID('dbo.RegistroAsistencia', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.RegistroAsistencia (
        IdRegistro BIGINT IDENTITY(1,1) PRIMARY KEY,
        IdEmpleado INT NOT NULL,
        MarcaEntrada DATETIME NOT NULL,
        MarcaSalida DATETIME NULL, 
        FaltaJustificada BIT NOT NULL DEFAULT 0,
        CONSTRAINT FK_Registro_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
    );
END

-- ========================================================================
-- 5. MÓDULO DE NÓMINA 
-- ========================================================================

IF OBJECT_ID('dbo.Planilla', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Planilla (
        IdPlanilla INT IDENTITY(1,1) PRIMARY KEY,
        Mes INT NOT NULL CHECK (Mes BETWEEN 1 AND 12),
        Anio INT NOT NULL,
        FechaEmision DATE NOT NULL DEFAULT GETDATE(),
        Estado VARCHAR(20) NOT NULL DEFAULT 'Borrador' CHECK (Estado IN ('Borrador', 'Emitida', 'Anulada'))
    );
END

IF OBJECT_ID('dbo.BoletaPago', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.BoletaPago (
        IdBoleta BIGINT IDENTITY(1,1) PRIMARY KEY,
        IdPlanilla INT NOT NULL,
        IdEmpleado INT NOT NULL,
        SueldoBaseAplicado DECIMAL(10,2) NOT NULL,
        HorasOrdinariasCalculadas DECIMAL(5,2) NOT NULL DEFAULT 0, -- Espacio corregido
        HorasExtraDiurnas DECIMAL(5,2) NOT NULL DEFAULT 0,
        HorasExtraNocturnas DECIMAL(5,2) NOT NULL DEFAULT 0,
        TotalIngresos DECIMAL(10,2) NOT NULL DEFAULT 0,
        TotalDeducciones DECIMAL(10,2) NOT NULL DEFAULT 0,
        LiquidoAPagar DECIMAL(10,2) NOT NULL DEFAULT 0,
        CONSTRAINT FK_Boleta_Planilla FOREIGN KEY (IdPlanilla) REFERENCES dbo.Planilla(IdPlanilla),
        CONSTRAINT FK_Boleta_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
    );
END

IF OBJECT_ID('dbo.BoletaDetalle', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.BoletaDetalle (
        IdDetalle BIGINT IDENTITY(1,1) PRIMARY KEY,
        IdBoleta BIGINT NOT NULL,
        Concepto VARCHAR(100) NOT NULL, 
        Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('Ingreso', 'Deduccion')),
        Monto DECIMAL(10,2) NOT NULL,
        CONSTRAINT FK_Detalle_Boleta FOREIGN KEY (IdBoleta) REFERENCES dbo.BoletaPago(IdBoleta)
    );
END
GO

-- ========================================================================
-- 6. SEED DATA - DATOS INICIALES (IDEMPOTENTE)
-- ========================================================================

-- 6.1 Departments
IF NOT EXISTS (SELECT 1 FROM dbo.Departamento WHERE Nombre = 'RRHH')
BEGIN
    INSERT INTO dbo.Departamento (Nombre, Activo) VALUES ('RRHH', 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Departamento WHERE Nombre = 'Ingeniería')
BEGIN
    INSERT INTO dbo.Departamento (Nombre, Activo) VALUES ('Ingeniería', 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Departamento WHERE Nombre = 'Administración')
BEGIN
    INSERT INTO dbo.Departamento (Nombre, Activo) VALUES ('Administración', 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Departamento WHERE Nombre = 'Operaciones')
BEGIN
    INSERT INTO dbo.Departamento (Nombre, Activo) VALUES ('Operaciones', 1);
END
GO

-- 6.2 Tipos de Contrato
IF NOT EXISTS (SELECT 1 FROM dbo.Contrato WHERE Nombre = 'Indefinido')
BEGIN
    INSERT INTO dbo.Contrato (Nombre, Descripcion, Activo) 
    VALUES ('Indefinido', 'Contrato laboral sin fecha de vencimiento', 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Contrato WHERE Nombre = 'Fijo')
BEGIN
    INSERT INTO dbo.Contrato (Nombre, Descripcion, Activo) 
    VALUES ('Fijo', 'Contrato laboral por período determinado', 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Contrato WHERE Nombre = 'Por Hora')
BEGIN
    INSERT INTO dbo.Contrato (Nombre, Descripcion, Activo) 
    VALUES ('Por Hora', 'Contrato por horas trabajadas', 1);
END
GO

-- 6.3 Jornadas por defecto
IF NOT EXISTS (SELECT 1 FROM dbo.Jornada WHERE Nombre = 'Matutina')
BEGIN
    INSERT INTO dbo.Jornada (Nombre, HoraEntradaEsperada, HoraSalidaEsperada, MargenToleranciaMinutos, Activa) 
    VALUES ('Matutina', '07:00:00', '16:00:00', 15, 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Jornada WHERE Nombre = 'Vespertina')
BEGIN
    INSERT INTO dbo.Jornada (Nombre, HoraEntradaEsperada, HoraSalidaEsperada, MargenToleranciaMinutos, Activa) 
    VALUES ('Vespertina', '13:00:00', '22:00:00', 15, 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Jornada WHERE Nombre = 'Completa')
BEGIN
    INSERT INTO dbo.Jornada (Nombre, HoraEntradaEsperada, HoraSalidaEsperada, MargenToleranciaMinutos, Activa) 
    VALUES ('Completa', '08:00:00', '17:00:00', 10, 1);
END
GO

-- 6.4 Permisos por Rol
IF OBJECT_ID('dbo.Permiso', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Permiso (
        IdPermiso INT IDENTITY(1,1) PRIMARY KEY,
        Rol VARCHAR(50) NOT NULL,
        IdDepartamento INT NULL,
        Modulo VARCHAR(50) NOT NULL,
        PuedeVer BIT NOT NULL DEFAULT 0,
        PuedeOperar BIT NOT NULL DEFAULT 0
    );
END
GO

-- Seed data: Admin — all 8 modules, all departments (IdDepartamento = NULL = global)
IF NOT EXISTS (SELECT 1 FROM dbo.Permiso WHERE Rol = 'Admin' AND Modulo = 'Empleados')
BEGIN
    INSERT INTO dbo.Permiso (Rol, IdDepartamento, Modulo, PuedeVer, PuedeOperar) VALUES
        ('Admin', NULL, 'Empleados', 1, 1),
        ('Admin', NULL, 'Marcacion', 1, 1),
        ('Admin', NULL, 'Planillas', 1, 1),
        ('Admin', NULL, 'Contratos', 1, 1),
        ('Admin', NULL, 'Horarios', 1, 1),
        ('Admin', NULL, 'Reportes', 1, 1),
        ('Admin', NULL, 'Departamentos', 1, 1),
        ('Admin', NULL, 'AsignacionJornada', 1, 1),
        ('Admin', NULL, 'Permisos', 1, 1),
        ('Admin', NULL, 'Usuarios', 1, 1);
END
GO

-- Seed data: RRHH — employee management modules
IF NOT EXISTS (SELECT 1 FROM dbo.Permiso WHERE Rol = 'RRHH' AND Modulo = 'Empleados')
BEGIN
    INSERT INTO dbo.Permiso (Rol, IdDepartamento, Modulo, PuedeVer, PuedeOperar) VALUES
        ('RRHH', NULL, 'Empleados', 1, 1),
        ('RRHH', NULL, 'Contratos', 1, 1),
        ('RRHH', NULL, 'Planillas', 1, 1),
        ('RRHH', NULL, 'Reportes', 1, 1);
END
GO

-- Seed data: Empleado — only marcacion (time tracking), view-only
IF NOT EXISTS (SELECT 1 FROM dbo.Permiso WHERE Rol = 'Empleado' AND Modulo = 'Marcacion')
BEGIN
    INSERT INTO dbo.Permiso (Rol, IdDepartamento, Modulo, PuedeVer, PuedeOperar) VALUES
        ('Empleado', NULL, 'Marcacion', 1, 1);
END
GO

-- ========================================================================
-- 7. USUARIO DE PRUEBA / ADMIN
-- ========================================================================

-- 7.1 Verificar si ya existe el empleado de prueba
DECLARE @IdDeptoRRHH INT;
SELECT @IdDeptoRRHH = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'RRHH';

DECLARE @IdEmpleadoAdmin INT;

IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '00000000-0')
BEGIN
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo)
    VALUES (@IdDeptoRRHH, 'Admin', 'Sistema', '00000000-0', 1);
    
    SET @IdEmpleadoAdmin = SCOPE_IDENTITY();
END
ELSE
BEGIN
    SELECT @IdEmpleadoAdmin = IdEmpleado FROM dbo.Empleado WHERE DUI = '00000000-0';
END
GO

-- 7.2 Crear usuario admin (Password: admin123 - hash simple para pruebas)
DECLARE @IdEmpleadoAdmin INT;
SELECT @IdEmpleadoAdmin = IdEmpleado FROM dbo.Empleado WHERE DUI = '00000000-0';

IF @IdEmpleadoAdmin IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE Username = 'admin')
BEGIN
    INSERT INTO dbo.Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo)
    VALUES (@IdEmpleadoAdmin, 'admin', 'admin123', 'Admin', 1);
END
GO

-- ========================================================================
-- MENSAJE DE CONFIRMACIÓN
-- ========================================================================
GO

-- ========================================================================
-- 8. TEST DATA
-- ========================================================================

-- Empleados de prueba
IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '12345678-9')
BEGIN
    DECLARE @IdDeptoRRHH INT, @IdDeptoIng INT;
    SELECT @IdDeptoRRHH = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'RRHH';
    SELECT @IdDeptoIng = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'Ingeniería';

    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoRRHH, 'María', 'García', '12345678-9', 1);
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoIng, 'Carlos', 'Martínez', '87654321-0', 1);
END
GO

-- Usuarios de prueba
DECLARE @IdEmpleadoMaria INT, @IdEmpleadoCarlos INT;
SELECT @IdEmpleadoMaria = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
SELECT @IdEmpleadoCarlos = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';

IF @IdEmpleadoMaria IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE Username = 'mgarcia')
    INSERT INTO dbo.Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo) VALUES (@IdEmpleadoMaria, 'mgarcia', '123456', 'RRHH', 1);
IF @IdEmpleadoCarlos IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE Username = 'cmartinez')
    INSERT INTO dbo.Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo) VALUES (@IdEmpleadoCarlos, 'cmartinez', '123456', 'Empleado', 1);
GO

-- Empleado de prueba en Administración (rol Empleado, solo marcación)
IF NOT EXISTS (SELECT 1 FROM dbo.Empleado WHERE DUI = '55555555-5')
BEGIN
    DECLARE @IdDeptoAdmin INT;
    SELECT @IdDeptoAdmin = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'Administración';
    INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo) VALUES (@IdDeptoAdmin, 'Pedro', 'López', '55555555-5', 1);
END
GO

DECLARE @IdEmpleadoPedro INT;
SELECT @IdEmpleadoPedro = IdEmpleado FROM dbo.Empleado WHERE DUI = '55555555-5';

IF @IdEmpleadoPedro IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE Username = 'plopez')
    INSERT INTO dbo.Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo) VALUES (@IdEmpleadoPedro, 'plopez', '123456', 'Empleado', 1);
GO

-- Contrataciones de prueba
DECLARE @IdContratoIndef INT, @IdContratoFijo INT;
SELECT @IdContratoIndef = IdContrato FROM dbo.Contrato WHERE Nombre = 'Indefinido';
SELECT @IdContratoFijo = IdContrato FROM dbo.Contrato WHERE Nombre = 'Fijo';

DECLARE @IdEmpMaria INT, @IdEmpCarlos INT, @IdEmpPedro INT;
SELECT @IdEmpMaria = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
SELECT @IdEmpCarlos = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';
SELECT @IdEmpPedro = IdEmpleado FROM dbo.Empleado WHERE DUI = '55555555-5';

IF @IdEmpMaria IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdEmpMaria)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Observaciones, Vigente)
    VALUES (@IdEmpMaria, @IdContratoIndef, '2024-01-15', 1200.00, 7.50, 'Contratación inicial', 1);
IF @IdEmpCarlos IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdEmpCarlos)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Observaciones, Vigente)
    VALUES (@IdEmpCarlos, @IdContratoFijo, '2024-03-01', 1500.00, 9.38, 'Sin observaciones', 1);
IF @IdEmpPedro IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.Contratacion WHERE IdEmpleado = @IdEmpPedro)
    INSERT INTO dbo.Contratacion (IdEmpleado, IdContrato, FechaInicio, SalarioBaseAcordado, ValorHoraAcordado, Observaciones, Vigente)
    VALUES (@IdEmpPedro, @IdContratoFijo, '2025-01-01', 800.00, 5.00, 'Ingreso reciente', 1);
GO

-- Asignaciones de jornada de prueba
DECLARE @IdJornMat INT, @IdJornVesp INT;
SELECT @IdJornMat = IdJornada FROM dbo.Jornada WHERE Nombre = 'Matutina';
SELECT @IdJornVesp = IdJornada FROM dbo.Jornada WHERE Nombre = 'Vespertina';

DECLARE @IdEmpM INT, @IdEmpC INT, @IdEmpP INT;
SELECT @IdEmpM = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
SELECT @IdEmpC = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';
SELECT @IdEmpP = IdEmpleado FROM dbo.Empleado WHERE DUI = '55555555-5';

IF @IdEmpM IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdEmpM)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdEmpM, @IdJornMat, GETDATE(), 1);
IF @IdEmpC IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdEmpC)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdEmpC, @IdJornVesp, GETDATE(), 1);
IF @IdEmpP IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.AsignacionJornada WHERE IdEmpleado = @IdEmpP)
    INSERT INTO dbo.AsignacionJornada (IdEmpleado, IdJornada, FechaAsignacion, Activa) VALUES (@IdEmpP, @IdJornMat, GETDATE(), 1);
GO

-- Registros de asistencia de prueba
DECLARE @IdEmpA1 INT, @IdEmpA2 INT;
SELECT @IdEmpA1 = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
SELECT @IdEmpA2 = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';

IF @IdEmpA1 IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.RegistroAsistencia WHERE IdEmpleado = @IdEmpA1)
BEGIN
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdEmpA1, '2025-05-01 07:00:00', '2025-05-01 16:00:00', 0);
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdEmpA1, '2025-05-02 07:15:00', '2025-05-02 16:30:00', 0);
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdEmpA1, '2025-05-03 07:00:00', '2025-05-03 17:00:00', 0);
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdEmpA2, '2025-05-01 13:00:00', '2025-05-01 22:00:00', 0);
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada) VALUES (@IdEmpA2, '2025-05-02 13:30:00', '2025-05-02 22:15:00', 0);
END
GO

-- Planilla de prueba
DECLARE @IdPlanillaTest INT;
IF NOT EXISTS (SELECT 1 FROM dbo.Planilla WHERE Mes = 5 AND Anio = 2025)
BEGIN
    INSERT INTO dbo.Planilla (Mes, Anio, FechaEmision, Estado) VALUES (5, 2025, GETDATE(), 'Borrador');
    SET @IdPlanillaTest = SCOPE_IDENTITY();
END
ELSE
    SELECT @IdPlanillaTest = IdPlanilla FROM dbo.Planilla WHERE Mes = 5 AND Anio = 2025;
GO

-- Boleta de pago de prueba
IF NOT EXISTS (SELECT 1 FROM dbo.BoletaPago)
BEGIN
    DECLARE @IdPlan INT;
    SELECT @IdPlan = IdPlanilla FROM dbo.Planilla WHERE Mes = 5 AND Anio = 2025;

    DECLARE @IdEmpB1 INT, @IdEmpB2 INT;
    SELECT @IdEmpB1 = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
    SELECT @IdEmpB2 = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';

    INSERT INTO dbo.BoletaPago (IdPlanilla, IdEmpleado, SueldoBaseAplicado, HorasOrdinariasCalculadas, HorasExtraDiurnas, HorasExtraNocturnas, TotalIngresos, TotalDeducciones, LiquidoAPagar)
    VALUES (@IdPlan, @IdEmpB1, 1200.00, 160.00, 0, 0, 1200.00, 160.50, 1039.50);

    DECLARE @IdBoleta BIGINT = SCOPE_IDENTITY();

    INSERT INTO dbo.BoletaDetalle (IdBoleta, Concepto, Tipo, Monto) VALUES (@IdBoleta, 'Sueldo Base', 'Ingreso', 1200.00);
    INSERT INTO dbo.BoletaDetalle (IdBoleta, Concepto, Tipo, Monto) VALUES (@IdBoleta, 'AFP (7.25%)', 'Deduccion', 87.00);
    INSERT INTO dbo.BoletaDetalle (IdBoleta, Concepto, Tipo, Monto) VALUES (@IdBoleta, 'ISSS (3%)', 'Deduccion', 30.00);
END
GO

-- ========================================================================
-- 9. TEST DATA — MAYO 2026 (flujo completo con horas extra)
-- ========================================================================

-- Asistencia Mayo 2026
-- María (RRHH, Matutina 07-16): día normal + día con extra diurna
-- Carlos (Ingeniería, Vespertina 13-22): día con extra diurna + nocturna
-- Pedro (Admin, Matutina 07-16): días normales
IF NOT EXISTS (SELECT 1 FROM dbo.RegistroAsistencia WHERE YEAR(MarcaEntrada) = 2026 AND MONTH(MarcaEntrada) = 5)
BEGIN
    DECLARE @IdM INT, @IdC INT, @IdP INT;
    SELECT @IdM = IdEmpleado FROM dbo.Empleado WHERE DUI = '12345678-9';
    SELECT @IdC = IdEmpleado FROM dbo.Empleado WHERE DUI = '87654321-0';
    SELECT @IdP = IdEmpleado FROM dbo.Empleado WHERE DUI = '55555555-5';

    -- María: día normal
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdM, '2026-05-04 07:00:00', '2026-05-04 16:00:00', 0);
    -- María: entra temprano + sale tarde → 1h extra diurna
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdM, '2026-05-05 06:30:00', '2026-05-05 16:30:00', 0);
    -- María: día normal
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdM, '2026-05-06 07:00:00', '2026-05-06 16:00:00', 0);

    -- Carlos: entra antes (12:00 = extra diurna) + sale después (23:00 = extra nocturna)
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdC, '2026-05-04 12:00:00', '2026-05-04 23:00:00', 0);
    -- Carlos: día normal
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdC, '2026-05-05 13:00:00', '2026-05-05 22:00:00', 0);
    -- Carlos: sale 30min después → extra nocturna
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdC, '2026-05-06 13:00:00', '2026-05-06 22:30:00', 0);

    -- Pedro: días normales
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdP, '2026-05-04 07:00:00', '2026-05-04 16:00:00', 0);
    INSERT INTO dbo.RegistroAsistencia (IdEmpleado, MarcaEntrada, MarcaSalida, FaltaJustificada)
        VALUES (@IdP, '2026-05-05 08:00:00', '2026-05-05 16:00:00', 0);
END
GO

PRINT '==============================================';
PRINT '  BASE DE DATOS CONFIGURADA CORRECTAMENTE   ';
PRINT '==============================================';
PRINT '  Usuario: admin                              ';
PRINT '  Password: admin123                          ';
PRINT '==============================================';

PRINT '--- Datos de prueba Mayo 2026 ---';
PRINT '  María García (RRHH, Matutina): 3 días, 1h extra diurna';
PRINT '  Carlos Martínez (Ingeniería, Vespertina): 3 días, 1h extra diurna + 1.5h extra nocturna';
PRINT '  Pedro López (Admin, Matutina): 2 días normales';
PRINT '==============================================';
GO