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


--Prueba de las Funcines

--obtenerPartidosDisponiblesParaApostar no tiene nada asi que es mas que nada para meter datos

Begin tran

insert into Competiciones(id,nombre,año) values(NEWID(), 'Copa del Rey', 2019)

insert into Competiciones values(NEWID(), 'Copa de la autoestima perdida', 2019)

insert into Partidos values(NEWID(), 4,5,0,50,'2019-3-12 11:00:00','')

insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values(NEWID(), 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '6027536A-F049-474D-90C8-744315FB4B6C')

Rollback 
Commit