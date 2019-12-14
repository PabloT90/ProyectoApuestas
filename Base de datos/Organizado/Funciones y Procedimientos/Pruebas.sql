	Use PruebasPablo 
	GO
--	--Dani sacame las comprobaciones de aqui!!!!!!
--Begin transaction
--insert into Competiciones(id,nombre,año) values(NEWID(), 'Copa del Rey', 2019)
--go
--Select * from Competiciones
--insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
--values(NEWID(), 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '6027536A-F049-474D-90C8-744315FB4B6C')
--go
--insert into Usuarios(correo,contraseña,saldoActual) 
--values ('lauraortiz@gmail.com', 245, 300 )
--go
--SELECT * FROM Usuarios
--SELECT * FROM Partidos
--insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
--values(NEWID(), 3.8, '2019-05-06 10:45:00', 10, 'lauraortiz@gmail.com', '68143F26-0C1B-4288-9749-25A542DCE9DA', 2, 0)
--go
--SELECT * FROM Apuestas
----insert into Apuestas (ID,FechaHoraApuesta,DineroApostado,CorreoUsuario,Cuota) values (NEWID(),CURRENT_TIMESTAMP,20,'danielgordillo9@gmail.com',4.5)
rollback


--Prueba de las Funciones

--obtenerPartidosDisponiblesParaApostar no tiene nada asi que es mas que nada para meter datos

Begin tran

insert into Competiciones(id,nombre,año) values( 'Copa del Rey', 2019)

insert into Competiciones(id,nombre,año) values( 'Copa autoestima', 2019)

SELECT * FROM Competiciones

insert into Partidos values(NEWID(), 4,5,1,50,30,35,'2019-3-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(NEWID(), 3,1,0,40,15,40,'2019-9-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))
insert into Partidos values(NEWID(), 4,5,1,20,30,60,'2019-25-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(NEWID(), 1,6,0,35,20,50,'2019-10-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))

SELECT * FROM Partidos
--insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
--values(NEWID(), 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '67D99F79-5FFF-487A-B5F1-E27F2E2EE426')
--DELETE  FROM Partidos WHERE id = '069A05DA-1DDC-44AC-B345-0956DB138F88'

SELECT * FROM Partidos

SELECT * FROM Usuarios
insert into Partidos values(NEWID(), 1,2,0,35,20,50,'2019-10-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))

INSERT INTO Usuarios VALUES('sulviagurdilloabcdef@gmail.com','sul07',500)

INSERT INTO Usuarios VALUES('decisionesdificile@gmail.com','dc19',1000)

Insert into Apuestas values(NEWID(),5.2,'2019-9-11 10:01:00',5.5,'sulviagurdilloabcdef@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 1),3,1)

INSERT INTO Apuestas VALUES(NEWID(),2.8,'2019-10-11 10:00:00', 2.9,'decisionesdificile@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 2),2,1)

Rollback 
Commit

--Funcion obtenerPartidosDisponiblesParaApostar

SELECT * FROM dbo.obtenerPartidosDisponiblesParaApostar()

--Procediemiento comprobarResultadoDeUnaApuesta
--ENABLE TRIGGER dbo.noSeAceptanModificaciones On dbo.Apuestas
DELETE FROM Usuarios 

SELECT * FROM Usuarios


--ALTER Table Usuarios
--Alter COLUMN saldoActual smallmoney

Alter table Usuarios
DROP CONSTRAINT CK_SaldoActual
--El correo electronico tiene un char(30) y no un varchar
SELECT * FROM Apuestas


declare @ganada smallint
declare @dineros smallmoney
declare @id int
Set @id =(SELECT ID FROM Apuestas Where CorreoUsuario = 'decisionesdificile@gmail.com')
EXECUTE dbo.comprobarResultadoDeUnaApuesta @id,@ganada OUTPUT,@dineros OUTPUT
SELECT @ganada AS ganado, @dineros as dinero

--Hemos cambiado el saldo de la tabla cuentas de int a smallmoney

--Procedure ingresoACuenta

-- 0
Declare @resultado as smallint
EXECUTE dbo.ingresoACuenta 'sulviagurdilloabcdef@gmail.com',200.0,@resultado OUTPUT
Select @resultado
GO
ALTER TABLE CUENTAS
ALTER COLUMN saldo smallmoney

ALTER TABLE Usuarios
ALTER COLUMN saldoActual smallmoney
-- -1
Declare @resultado as smallint
EXECUTE dbo.ingresoACuenta 'silviagurdillo@gmail.com',500,@resultado OUTPUT
Select @resultado
GO
SELECT * FROM Cuentas
-- -2
Declare @resultado as smallint
EXECUTE dbo.ingresoACuenta 'sulviagurdilloabcdef@gmail.com',0,@resultado OUTPUT
SELECT  @resultado
Select * From Cuentas
--El saldoActual del usuario es int

--Procedure retirarCapitalCuenta
--Resultado me devuelve null pero si que se retira el dinero
-- 0
GO
DECLARE @resultado smallint
DECLARE @retirado Smallmoney
SET @retirado = 200.5
EXECUTE dbo.retirarCapitalCuenta 'sulviagurdilloabcdef@gmail.com',@retirado,@resultado OUTPUT
SELECT @resultado as resultado
Select * From Cuentas

-- -1
GO
DECLARE @resultado smallint
DECLARE @retirado Smallmoney
SET @retirado = 5.5 
EXECUTE dbo.retirarCapitalCuenta 'decioionesdificil@gmail.com',@retirado,@resultado OUTPUT
SELECT @resultado as resultado

-- -2
GO
DECLARE @resultado smallint
DECLARE @retirado Smallmoney
SET @retirado = 0
EXECUTE dbo.retirarCapitalCuenta 'decisionesdificile@gmail.com',@retirado,@resultado OUTPUT
SELECT @resultado as resultado

SELECT * FROM Usuarios

-- -3
GO
DECLARE @resultado smallint
DECLARE @retirado Smallmoney
SET @retirado = 10000
EXECUTE dbo.retirarCapitalCuenta 'decisionesdificile@gmail.com',@retirado,@resultado OUTPUT
SELECT @resultado as resultado

--Funciones del ejercicio 10 
SELECT * FROM Apuestas
SELECT * FROM ApuestaTipo1
SELECT * FROM ApuestaTipo2
SELECT * FROM ApuestaTipo3

SELECT * FROM dbo.consultarApuestasTipo1 (7,4,5)
SELECT * FROM dbo.consultarApuestasTipo2(6,'Visitante',2)
SELECT * FROM dbo.consultarApuestasTipo3(5,'visitante')

GO
--Procedure realizarApuestaTipo1
--0
--DECLARE @Correo char = 'sulviagurdilloabcdef@gmail.com'
--SELECT * FROM Usuarios WHERE correo = 'sulviagurdilloabcdef@gmail.com'
SELECT * FROM Apuestas
SELECT * FROM ApuestaTipo1
SELECT * FROM Partidos
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 5
DECLARE @IDPartido int = 17
DECLARE @GolesL int = 3
DECLARE @GolesV int = 2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'sulviagurdilloabcdef@gmail.com',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado

-- -1
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 20
DECLARE @IDPartido int = 17
DECLARE @GolesL int = 3
DECLARE @GolesV int = 2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'correoerroeno',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado
GO

-- -2

GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 20
DECLARE @IDPartido int = 1
DECLARE @GolesL int = 3
DECLARE @GolesV int = 2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'sulviagurdilloabcdef@gmail.com',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado
GO

-- -3

GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 0
DECLARE @IDPartido int = 17
DECLARE @GolesL int = 3
DECLARE @GolesV int = 2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'sulviagurdilloabcdef@gmail.com',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado
GO

-- -4
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 20
DECLARE @IDPartido int = 17
DECLARE @GolesL int = -1
DECLARE @GolesV int = 2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'sulviagurdilloabcdef@gmail.com',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado
GO

-- -5
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 20
DECLARE @IDPartido int = 17
DECLARE @GolesL int = 1
DECLARE @GolesV int = -2
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo1 @fechaHora, @capital,'sulviagurdilloabcdef@gmail.com',@IDPartido,@GolesL,@GolesV,@resultado OUTPUT
SELECT @resultado
GO

GO

--Procedure realizarApuestaTipo2
SELECT * FROM Apuestas
SELECT * FROM ApuestaTipo2
SELECT * FROM Partidos
SELECT * FROM Usuarios
-- 0
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 1
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Visitante'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'decisionesdificile@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado

GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 25
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO

-- -1
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 25
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Visitante'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'incorrecto', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO
-- -2
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 25
DECLARE @IDPartido int = 25
DECLARE @Equipo varchar(10) = 'Visitante'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO
-- -3
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 0
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO
-- -4
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 25
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Algo'
DECLARE @Goles int = 3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO
-- -5
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 25
DECLARE @IDPartido int = 19
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @Goles int = -3
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo2 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @Goles, @resultado OUTPUT
SELECT @resultado
GO

--Procedure realizarApuestaTipo3
SELECT * FROM Apuestas
SELECT * FROM ApuestaTipo3
SELECT * FROM Partidos
SELECT * FROM Usuarios
--0
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 10
DECLARE @IDPartido int = 20
DECLARE @Equipo varchar(10) = 'Visitante'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 1
DECLARE @IDPartido int = 20
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'decisionesdificile@gmail.com', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado

-- -1
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 10
DECLARE @IDPartido int = 2
DECLARE @Equipo varchar(10) = 'Visitante'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'error', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado
GO
-- -2
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 10
DECLARE @IDPartido int = 2
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado
GO
-- -3
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 0
DECLARE @IDPartido int = 20
DECLARE @Equipo varchar(10) = 'Local'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado
GO
-- -4
GO
DECLARE @fechaHora smalldatetime = '2019-3-12 10:01:00'
DECLARE @capital smallmoney = 10
DECLARE @IDPartido int = 20
DECLARE @Equipo varchar(10) = 'Leo'
DECLARE @resultado smallint
EXECUTE dbo.realizarApuestaTipo3 @fechaHora, @capital, 'sulviagurdilloabcdef@gmail.com', @IDPartido, @Equipo, @resultado OUTPUT
SELECT @resultado
GO

--Funcion obtenerCuotaTipo1
SELECT * FROM Apuestas
SELECT * FROM Partidos
SELECT * FROM ApuestaTipo1
SELECT dbo.obtenerCuotaTipo1 (1,25,6,8)
SELECT dbo.obtenerCuotaTipo1 (2,25,6,8)
SELECT dbo.obtenerCuotaTipo1 (1,30,9,7)
SELECT dbo.obtenerCuotaTipo1 (1,30,0,0)
SELECT dbo.obtenerCuotaTipo1 (15,50,3,2)


--ESTO NUNCA VA A PASAR LO MISMO PASA CON EL RESTO
--Cuota menor que 1,5
DELETE FROM Apuestas WHERE ID = 28
DELETE FROM Apuestas WHERE ID = 25
DELETE FROM ApuestaTipo3 WHERE id = 25
DELETE FROM ApuestaTipo3 WHERE id = 28

SELECT * FROM ApuestaTipo3
SELECT * FROM Partidos
SELECT * FROM Apuestas
SELECT dbo.cuantoDineroHayApostadoAUnPartido (19,1)
SELECT dbo.obtenerTipo1ParametroF(15,3,2)
SELECT dbo.obtenerCuotaTipo1 (15,50,3,2)

--Funcion obtenerCuotaTipo2
SELECT dbo.obtenerCuotaTipo2 (1,25,'Local',8)
SELECT dbo.obtenerCuotaTipo2 (2,25,'Visitante',8)
SELECT dbo.obtenerCuotaTipo2 (1,30,'Local',7)
SELECT dbo.obtenerCuotaTipo2 (1,30,'Visitante',0)
--Me da Null
SELECT dbo.obtenerCuotaTipo2 (7,50,'Visitante',0)

--Funcion obtenerCuotaTipo3
SELECT dbo.obtenerCuotaTipo3 (1,25,'Local')
SELECT dbo.obtenerCuotaTipo3 (2,25,'Visitante')
SELECT dbo.obtenerCuotaTipo3 (1,30,'Local')
SELECT dbo.obtenerCuotaTipo3 (1,30,'Visitante')
--Me da Null
SELECT dbo.obtenerCuotaTipo3 (4,50,'Local')

--TERMINAR DE REVISAR EN CLASE QUE SE ME VA
--Funcion obtenerTipo1ParametroF
SELECT * FROM Apuestas
SELECT dbo.obtenerTipo1ParametroF(1,6,5)
--Funcion obtenerTipo3ParametroF
SELECT * FROM ApuestaTipo3
SELECT dbo.obtenerTipo3ParametroF(16,'visitante')

--Funcion obtenerParametroT
SELECT * FROM Apuestas
SELECT * FROM Partidos
--DECLARE @Cantidad smallmoney = ([dbo].[obtenerParametroT](16,2))
SELECT dbo.obtenerParametroT(16,2)
SELECT dbo.obtenerParametroT(19,3)
SELECT dbo.obtenerParametroT(18,1)

