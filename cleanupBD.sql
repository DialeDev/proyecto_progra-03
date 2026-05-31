-- ========================================================================
-- SISTEMA DE CONTROL DE ASISTENCIA Y PLANILLAS — CLEANUP
-- Elimina la base de datos, la recrea y seedea únicamente el usuario admin.
-- ========================================================================

-- ========================================================================
-- 0. DROP & CREATE (fuerza cierre de conexiones activas)
-- ========================================================================

USE master;
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'AsistenciaPlanillasDB')
BEGIN
    ALTER DATABASE AsistenciaPlanillasDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE AsistenciaPlanillasDB;
END
GO

CREATE DATABASE AsistenciaPlanillasDB;
GO

USE AsistenciaPlanillasDB;
GO

-- ========================================================================
-- 1. MÓDULO DE GESTIÓN DE PERSONAL
-- ========================================================================

CREATE TABLE dbo.Departamento (
    IdDepartamento INT IDENTITY(1,1) PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Activo BIT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.Empleado (
    IdEmpleado INT IDENTITY(1,1) PRIMARY KEY,
    IdDepartamento INT NOT NULL,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    DUI VARCHAR(10) NOT NULL UNIQUE,
    Activo BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Empleado_Depto FOREIGN KEY (IdDepartamento) REFERENCES dbo.Departamento(IdDepartamento)
);

-- ========================================================================
-- 2. MÓDULO DE SEGURIDAD
-- ========================================================================

CREATE TABLE dbo.Usuario (
    IdUsuario INT IDENTITY(1,1) PRIMARY KEY,
    IdEmpleado INT NOT NULL,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    Rol VARCHAR(50) NOT NULL DEFAULT 'RRHH',
    Activo BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Usuario_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
);

-- ========================================================================
-- 3. MÓDULO DE CONTRATACIÓN E HISTORIAL SALARIAL
-- ========================================================================

CREATE TABLE dbo.Contrato (
    IdContrato INT IDENTITY(1,1) PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('Fijo', 'Por Hora')),
    Descripcion VARCHAR(255) NULL,
    Activo BIT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.Contratacion (
    IdContratacion INT IDENTITY(1,1) PRIMARY KEY,
    IdEmpleado INT NOT NULL,
    IdContrato INT NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE NULL,
    SalarioBaseAcordado DECIMAL(10,2) NULL CHECK (SalarioBaseAcordado >= 0),
    ValorHoraAcordado DECIMAL(10,2) NULL CHECK (ValorHoraAcordado >= 0),
    Vigente BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Contratacion_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado),
    CONSTRAINT FK_Contratacion_Contrato FOREIGN KEY (IdContrato) REFERENCES dbo.Contrato(IdContrato)
);

-- ========================================================================
-- 4. MÓDULO DE HORARIOS Y CONTROL DE ASISTENCIA
-- ========================================================================

CREATE TABLE dbo.Jornada (
    IdJornada INT IDENTITY(1,1) PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    HoraEntradaEsperada TIME NOT NULL,
    HoraSalidaEsperada TIME NOT NULL,
    MargenToleranciaMinutos INT NOT NULL DEFAULT 0,
    Activa BIT NOT NULL DEFAULT 1
);

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

CREATE TABLE dbo.RegistroAsistencia (
    IdRegistro BIGINT IDENTITY(1,1) PRIMARY KEY,
    IdEmpleado INT NOT NULL,
    MarcaEntrada DATETIME NOT NULL,
    MarcaSalida DATETIME NULL,
    FaltaJustificada BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Registro_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
);

-- ========================================================================
-- 5. MÓDULO DE NÓMINA
-- ========================================================================

CREATE TABLE dbo.Planilla (
    IdPlanilla INT IDENTITY(1,1) PRIMARY KEY,
    Mes INT NOT NULL CHECK (Mes BETWEEN 1 AND 12),
    Anio INT NOT NULL,
    FechaEmision DATE NOT NULL DEFAULT GETDATE(),
    Estado VARCHAR(20) NOT NULL DEFAULT 'Borrador' CHECK (Estado IN ('Borrador', 'Emitida', 'Anulada'))
);

CREATE TABLE dbo.BoletaPago (
    IdBoleta BIGINT IDENTITY(1,1) PRIMARY KEY,
    IdPlanilla INT NOT NULL,
    IdEmpleado INT NOT NULL,
    SueldoBaseAplicado DECIMAL(10,2) NOT NULL,
    HorasOrdinariasCalculadas DECIMAL(5,2) NOT NULL DEFAULT 0,
    HorasExtraDiurnas DECIMAL(5,2) NOT NULL DEFAULT 0,
    HorasExtraNocturnas DECIMAL(5,2) NOT NULL DEFAULT 0,
    TotalIngresos DECIMAL(10,2) NOT NULL DEFAULT 0,
    TotalDeducciones DECIMAL(10,2) NOT NULL DEFAULT 0,
    LiquidoAPagar DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT FK_Boleta_Planilla FOREIGN KEY (IdPlanilla) REFERENCES dbo.Planilla(IdPlanilla),
    CONSTRAINT FK_Boleta_Empleado FOREIGN KEY (IdEmpleado) REFERENCES dbo.Empleado(IdEmpleado)
);

CREATE TABLE dbo.BoletaDetalle (
    IdDetalle BIGINT IDENTITY(1,1) PRIMARY KEY,
    IdBoleta BIGINT NOT NULL,
    Concepto VARCHAR(100) NOT NULL,
    Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('Ingreso', 'Deduccion')),
    Monto DECIMAL(10,2) NOT NULL,
    CONSTRAINT FK_Detalle_Boleta FOREIGN KEY (IdBoleta) REFERENCES dbo.BoletaPago(IdBoleta)
);

-- ========================================================================
-- 5B. MÓDULO DE PERMISOS (RBAC)
-- ========================================================================

CREATE TABLE dbo.Permiso (
    IdPermiso INT IDENTITY(1,1) PRIMARY KEY,
    Rol VARCHAR(50) NOT NULL,
    IdDepartamento INT NULL,
    Modulo VARCHAR(50) NOT NULL,
    PuedeVer BIT NOT NULL DEFAULT 0,
    PuedeOperar BIT NOT NULL DEFAULT 0
);

-- ========================================================================
-- 6. SEED DATA — SOLO ADMIN
-- ========================================================================

-- Único departamento bootstrap necesario por la FK de Empleado.
-- El admin es del sistema, no de RRHH.
INSERT INTO dbo.Departamento (Nombre, Activo) VALUES ('Sistema', 1);

DECLARE @IdDeptoSistema INT;
SELECT @IdDeptoSistema = IdDepartamento FROM dbo.Departamento WHERE Nombre = 'Sistema';

INSERT INTO dbo.Empleado (IdDepartamento, Nombres, Apellidos, DUI, Activo)
VALUES (@IdDeptoSistema, 'Admin', 'Sistema', '00000000-0', 1);

DECLARE @IdEmpleadoAdmin INT;
SELECT @IdEmpleadoAdmin = IdEmpleado FROM dbo.Empleado WHERE DUI = '00000000-0';

-- Password: admin123 (hash simple para pruebas)
INSERT INTO dbo.Usuario (IdEmpleado, Username, PasswordHash, Rol, Activo)
VALUES (@IdEmpleadoAdmin, 'admin', 'admin123', 'Admin', 1);

-- ========================================================================
-- CONFIRMACIÓN
-- ========================================================================

PRINT '==============================================';
PRINT '  CLEANUP COMPLETADO                          ';
PRINT '==============================================';
PRINT '  Usuario: admin                              ';
PRINT '  Password: admin123                          ';
PRINT '  Base de datos completamente limpia.         ';
PRINT '==============================================';
GO
