go 
use ApuestasDeportivas
go

--USUARIOS
insert into Usuarios(correo,contraseña,saldoActual) 
values ('juanperez@gmail.com', 230, 200 )
go
insert into Usuarios(correo,contraseña,saldoActual) 
values ('lauraortiz@gmail.com', 245, 300 )
go
insert into Usuarios(correo,contraseña,saldoActual) 
values ('pedrohernandez@gmail.com', 562, 50 )
go

insert into Usuarios(correo,contraseña,saldoActual) 
values ('saragutierrez@gmail.com', 222, 300 )
go
insert into Usuarios(correo,contraseña,saldoActual) 
values ('mariaalonso@gmail.com', 632, 160 )
go
insert into Usuarios(correo,contraseña,saldoActual) 
values ('danielruiz@gmail.com', 351, 100 )
go

--COMPETICIONES
insert into Competiciones(id,nombre,año) values('7FB066EF-9286-4CCB-8A2E-66B3D6157EA7', 'Copa Mundial', 2018)
go
insert into Competiciones(id,nombre,año) values('E332EFCB-DFAB-4BA7-B4F1-93647A33B765', 'Copa del Rey', 2019)
go
insert into Competiciones(id,nombre,año) values('5AF772A0-FA43-4F5A-A2DC-FC554D7B0C3E', 'Champions League', 2019)
go

--select * from Competiciones
--select * from Partidos
--select * from Usuarios
--select * from Partidos
--select * from Apuestas

--PARTIDOS
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('639621A1-8C51-4E3D-AC1B-D41F9A4624A2', 2, 1, 0, 30, 60, 100,'2018-20-06 12:30:00', '7FB066EF-9286-4CCB-8A2E-66B3D6157EA7')
go
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('636ADF46-23A0-46D3-AD6A-7399943FAB7A', 3, 4, 0, 60, 80, 90, '2018-14-07 11:00:00', '7FB066EF-9286-4CCB-8A2E-66B3D6157EA7')
go
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('9611C93E-45EC-40E3-BFB1-21E635C00DE9', 4, 5, 0, 70, 90, 100, '2019-14-07 11:00:00','E332EFCB-DFAB-4BA7-B4F1-93647A33B765')
go
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('CE3D5688-8213-48F3-A0E2-86213C7D90A5', 2, 1, 0, 30, 60, 100,'2019-20-06 12:30:00', 'E332EFCB-DFAB-4BA7-B4F1-93647A33B765')
go
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('6955145D-7827-4667-B208-1556D06578D2', 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '5AF772A0-FA43-4F5A-A2DC-FC554D7B0C3E')
go
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values('B3D3BA77-7DEA-4805-9D65-95BC5F23E955', 4, 5, 0, 70, 90, 100, '2019-14-07 11:00:00','5AF772A0-FA43-4F5A-A2DC-FC554D7B0C3E')
go


--APUESTAS
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('6AC32299-B5D7-4641-8B9F-892AFDA535E7', 3.5, '2019-08-10 12:00:00', 20, 'juanperez@gmail.com', '639621A1-8C51-4E3D-AC1B-D41F9A4624A2',1, 0)
go

insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('E36F9E95-C99B-400E-B105-EECB88AB01FE', 3, '2019-05-06 10:45:00', 10, 'lauraortiz@gmail.com', '9611C93E-45EC-40E3-BFB1-21E635C00DE9', 1, 0)
go
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('16C330C6-0A0D-46D8-987B-A2D9F46E4894', 4, '2018-10-05 14:25:23', 5,'danielruiz@gmail.com', '639621A1-8C51-4E3D-AC1B-D41F9A4624A2', 1, 0)
go

insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('FBB41423-E02B-446B-B61B-EF585C1DD8ED', 3.5, '2019-08-10 12:00:00', 20, 'juanperez@gmail.com', '6955145D-7827-4667-B208-1556D06578D2',2, 0)
go

insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('25A7FA03-261A-4088-9681-F070027E1CB3', 3, '2019-04-06 10:45:00', 8, 'mariaalonso@gmail.com', '6955145D-7827-4667-B208-1556D06578D2', 2, 0)
go
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('CAC1F683-F628-44E8-91B6-34E758585F82', 3.5, '2018-11-05 14:20:23', 10,'saragutierrez@gmail.com', '6955145D-7827-4667-B208-1556D06578D2', 2, 0)
go

insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('DB5B6D0E-55C9-4AA6-B469-874A26AA767C', 3, '2018-9-05 14:25:23', 15,'pedrohernandez@gmail.com', '636ADF46-23A0-46D3-AD6A-7399943FAB7A', 3, 0)
go
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('99B8FDDE-637B-4CA7-A2F4-DA83A36CAD90', 4, '2018-8-05 14:25:23', 10,'danielruiz@gmail.com', 'B3D3BA77-7DEA-4805-9D65-95BC5F23E955', 3, 0)
go
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values('F14D0B3A-CC77-4377-B945-AA6B1DBACA0C', 3, '2018-10-05 13:25:23', 25,'mariaalonso@gmail.com', '6955145D-7827-4667-B208-1556D06578D2', 3, 0)
go



--select * from Apuestas
--select * from ApuestaTipo1
--select * from Competiciones

--APUESTA TIPO 1
insert into ApuestaTipo1(id, NumGolesLocal, numGolesVisitante) values('6AC32299-B5D7-4641-8B9F-892AFDA535E7', 1, 5)
go
insert into ApuestaTipo1(id, NumGolesLocal, numGolesVisitante) values('E36F9E95-C99B-400E-B105-EECB88AB01FE', 0, 2)
go
insert into ApuestaTipo1(id, NumGolesLocal, numGolesVisitante) values('16C330C6-0A0D-46D8-987B-A2D9F46E4894', 3, 2)
go

--select * from Partidos
--select * from Apuestas
--select * from ApuestaTipo3
--select * from competiciones

--APUESTA TIPO 2
insert into ApuestaTipo2(id, equipo, goles) values('FBB41423-E02B-446B-B61B-EF585C1DD8ED', 'visitante', 5)
go
insert into ApuestaTipo2(id, equipo, goles) values('25A7FA03-261A-4088-9681-F070027E1CB3', 'local', 3)
go
insert into ApuestaTipo2(id, equipo, goles) values('CAC1F683-F628-44E8-91B6-34E758585F82', 'local', 1)
go

--APUESTA TIPO 3
insert into ApuestaTipo3(id, ganador) values('DB5B6D0E-55C9-4AA6-B469-874A26AA767C', 0)
go
insert into ApuestaTipo3(id, ganador) values('99B8FDDE-637B-4CA7-A2F4-DA83A36CAD90',1)
go
insert into ApuestaTipo3(id, ganador) values('F14D0B3A-CC77-4377-B945-AA6B1DBACA0C',1)
go

--CUENTAS
set dateformat 'ymd' --Para establecer formato correcto de fecha
/*insert into Cuentas(saldo, fechaYHora, correoUsuario) values(200, GETDATE(), 'juanperez@gmail.com')
go*/
insert into Cuentas/*(saldo, fechaYHora, correoUsuario) */values(200, '2018-08-07 11:00:00', 'juanperez@gmail.com')
go
insert into Cuentas/*(saldo, fechaYHora, correoUsuario)*/ values(300,'2018-08-07 11:00:00', 'lauraortiz@gmail.com')
go
insert into Cuentas/*(saldo, fechaYHora, correoUsuario)*/ values(50,'2018-08-20 11:30:00', 'pedrohernandez@gmail.com')
go
insert into Cuentas/*(saldo, fechaYHora, correoUsuario)*/ values(300, '2018-08-07 11:00:00', 'saragutierrez@gmail.com')
go
insert into Cuentas/*(saldo, fechaYHora, correoUsuario)*/ values(160,'2018-08-07 11:00:00', 'mariaalonso@gmail.com')
go
insert into Cuentas/*(saldo, fechaYHora, correoUsuario)*/ values(100,'2018-08-20 11:30:00', 'danielruiz@gmail.com')
go



