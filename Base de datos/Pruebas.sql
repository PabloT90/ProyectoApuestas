	Use ApuestasDeportivas 
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

insert into Competiciones(id,nombre,año) values(NEWID(), 'Copa del Rey', 2019)

insert into Competiciones(id,nombre,año) values(NEWID(), 'Copa autoestima', 2019)

SELECT * FROM Competiciones

insert into Partidos values(NEWID(), 4,5,1,50,30,35,'2019-3-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(NEWID(), 3,1,0,40,15,40,'2019-9-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))
insert into Partidos values(NEWID(), 4,5,1,20,30,60,'2019-25-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(NEWID(), 1,6,0,35,20,50,'2019-10-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values(NEWID(), 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '67D99F79-5FFF-487A-B5F1-E27F2E2EE426')
DELETE  FROM Partidos WHERE id = '069A05DA-1DDC-44AC-B345-0956DB138F88'

SELECT * FROM Partidos

SELECT * FROM Usuarios
insert into Partidos values(NEWID(), 1,2,0,35,20,50,'2019-10-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))

INSERT INTO Usuarios VALUES('sulviagurdillo@gmail.com','sul07',500)

INSERT INTO Usuarios VALUES('decisiones@gmail.com','dc19',1000)

Insert into Apuestas values(NEWID(),5.2,'2019-9-11 10:01:00',5.5,'sulviagurdillo@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 1),3,1)

INSERT INTO Apuestas VALUES(NEWID(),2.8,'2019-10-11 10:00:00', 2.9,'decisiones@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 2),2,1)

Rollback 
Commit

--Funcion obtenerPartidosDisponiblesParaApostar

SELECT * FROM dbo.obtenerPartidosDisponiblesParaApostar()

--Procediemiento comprobarResultadoDeUnaApuesta
--DISABLE TRIGGER dbo.noSeAceptanModificaciones On dbo.Apuestas
DELETE FROM Usuarios 
SELECT * FROM Usuarios
--TIPOS Y NO SALE DINERO
declare @ganada bit
declare @dineros smallmoney
declare @id uniqueidentifier
--ALTER Table Usuarios
--Alter COLUMN saldoActual smallmoney

--Alter table Usuarios
--DROP CONSTRAINT CK_SaldoActual
--El correo electronico tiene un char(30) y no un varchar
Set @id =(SELECT ID FROM Apuestas Where CorreoUsuario = 'decisiones@gmail.com')
EXECUTE dbo.comprobarResultadoDeUnaApuesta @id,@ganada,@dineros
SELECT @ganada, @dineros

--Procedure ingresoACuenta
Declare @resultado as tinyint
EXECUTE dbo.ingresoACuenta 'sulviagurdillo@gmail.com',500,@resultado
Select @resultado
GO
Declare @resultado as tinyint
EXECUTE dbo.ingresoACuenta 'silviagurdillo@gmail.com',500,@resultado
Select @resultado
GO
Declare @resultado as tinyint
EXECUTE dbo.ingresoACuenta 'sulviagurdillo@gmail.com',0,@resultado
Select @resultado
Select * From Cuentas
--El saldoActual del usuario es int