
USE ApuestasDeportivas
GO
--drop database ApuestasDeportivas

--select * from Partidos

--select * from APUESTAS
--where IDPARTIDO = 'BA63955A-E6D6-440D-8994-2EC60A089BD9'

--select * from ApuestaTipo1

use ApuestasDeportivas
/*trigger para restar automáticamente el coste de la apuesta del saldo del usuario*/
GO
--ALTER
CREATE 
TRIGGER restarDineroApuestaInmediatamente
ON APUESTAS
AFTER INSERT AS 
BEGIN
	/*HAY QUE HACER DOS COSAS: MODIFICAR EL SALDO ACTUAL DEL USUARIO Y REGISTRAR EL MOVIMIENTO EN CUENTAS*/
	UPDATE Usuarios
	SET saldoActual = saldoActual - (SELECT DINEROAPOSTADO FROM inserted )
	WHERE correo = (SELECT CORREOUSUARIO FROM inserted)


	/*Nota: como voy a hacer un trigger que tras cada actualización de Usuarios, graba el movimiento del saldo en 
	Cuentas, esta parte aquí sobraría*/
	--INSERT INTO Cuentas ( saldo, fechaYHora, correoUsuario)
	--values( (SELECT saldoActual 
	--							FROM Usuarios as u
	--							inner join inserted as i
	--							on u.correo = i.CORREOUSUARIO  ), CURRENT_TIMESTAMP,(SELECT CORREOUSUARIO 
	
	--							FROM Usuarios as u
	--							inner join inserted as i
	--							on u.correo = i.CORREOUSUARIO  )  )
END
select * from APUESTAS

select * from ApuestaTipo1

select * from Usuarios
select * from Cuentas
where correoUsuario = 'lolo@gmail.com'


insert into Usuarios (correo, contraseña, saldoActual)
values('lolo@gmail.com', '123', 100)

insert into Usuarios (correo, contraseña, saldoActual)
values('angelavazquez@gmail.com', '123', 100)

insert into Usuarios (correo, contraseña, saldoActual)
values('pepe@gmail.com', '123', 100)

insert into Apuestas(ID, CUOTA, FECHAHORAAPUESTA, DINEROAPOSTADO, CORREOUSUARIO, IDPARTIDO, TIPO)
values(NEWID(), 1, CURRENT_TIMESTAMP, 15, 'lolo@gmail.com', '2B0981F4-EE1C-4EB0-861F-4EE5C2D06B6C', 1)

insert into Partidos(id, resultadoLocal, resultadoVisitante, isAbierto, maxApuesta1, maxApuesta2, maxApuesta3, idCompeticion)
values(NEWID(), 5,3,1,5000,2500,1600,'5FAAF209-1955-4620-A6CC-C2DA69052279')

insert into Partidos(id, resultadoLocal, resultadoVisitante, isAbierto, maxApuesta1, maxApuesta2, maxApuesta3, idCompeticion)
values(NEWID(), 5,3,1,5000,2500,1600,'5FAAF209-1955-4620-A6CC-C2DA69052279')

insert into Partidos(id, resultadoLocal, resultadoVisitante, isAbierto, maxApuesta1, maxApuesta2, maxApuesta3, idCompeticion)
values(NEWID(), 5,3,1,5000,2500,1600,'5FAAF209-1955-4620-A6CC-C2DA69052279')

insert into Competiciones(id, nombre, año)
values(NEWID(), 'Copa', 2010)

select * from Competiciones

select * from Partidos



/*PROCEDIMIENTOS */

--Funcion al que pasaremos el id de una apuesta y el correo de un usuario y nos devolvera el tipo de esa apuesta
GO
create function FN_TipoApuesta(@IDApuesta int)
returns tinyint as
begin
	declare @Tipo as tinyint

	set @Tipo=(select Tipo from Apuestas
				where @IDApuesta=ID)

	return @Tipo
end
go
declare @ret tinyint
set @ret = dbo.FN_TipoApuesta('71207D26-3402-4845-B69B-75560A8F8917')
print @ret

/*HAY QUE ACTUALIZAR EL SALDO DE LAS CUENTAS DE LOS USUARIOS Y LOS MOVIMIENTOS DE SUS CUENTAS en caso
de que ganen una apuesta*/


--go
--declare @VarTipoTabla table(
--    IDApuesta uniqueidentifier,
--	TipoApuesta tinyint
--)

--insert into @VarTipoTabla (IDApuesta,TipoApuesta)
--select ID,Tipo from Apuestas
--where IDPartido='47B61C3C-AC7B-48AA-AF01-4FD05FD43506'  and IsGanador!=1

--select * from @VarTipoTabla

go
create 
or alter 
trigger T_ActualizarGanador on Partidos
after update as
begin
	declare @IDPartido int,
			@ResLocal tinyint,
			@ResVisitante tinyint,
			--@isAbierto bit,
			--@ApuestaMaxima1 int,
			--@ApuestaMaxima2 int,
			--@ApuestaMaxima3 int,
			@FechaPartido smalldatetime,
			@IdCompeticion int

	declare @Tipo tinyint,
			@ApostadoResLocal tinyint,
			@ApostadoResVisitante tinyint,
			@NombreEquipo varchar(10),
			@NumGolesEquipo tinyint,
			@ApostadoResLocalTipo2 tinyint,
			@ApostadoResVisitanteTipo2 tinyint,
			@EquipoGanador varchar(10)


	declare miCursor cursor for select ID,resultadoLocal,resultadoVisitante,fechaPartido,idCompeticion from inserted

	open miCursor

	fetch next from miCursor into @IDPartido,@ResLocal,@ResVisitante,@FechaPartido,@IdCompeticion

	while(@@FETCH_STATUS=0)
	begin
		if update(resultadoLocal) or update(resultadoVisitante)
		begin
			if exists (select Tipo from Apuestas
					where @IDPartido=IDPartido and Tipo=1)
			begin

					update Apuestas
					set IsGanador=1
					from
					Apuestas as A
					inner join
					ApuestaTipo1 as AT1
					on A.ID = AT1.id
					where IDPartido=@IDPartido and Tipo=1
					and
					AT1.NumGolesLocal = @ResLocal
					and
					AT1.numGolesVisitante = @ResVisitante
					

				
			end--if tipo 1
			

			------------------------------------



			if exists (select Tipo from Apuestas
					where @IDPartido=IDPartido and Tipo=2)
			begin
				--select @NombreEquipo=AT2.equipo ,@NumGolesEquipo=AT2.goles from ApuestaTipo2 as AT2
				--inner join Apuestas as A on AT2.id=A.ID
				--where @IDPartido=A.IDPartido

				--if @NombreEquipo='visitante' and @NumGolesEquipo=@ResVisitante
				--begin
					update Apuestas
					set IsGanador=1
					from
					Apuestas as A
					inner join
					ApuestaTipo2 as AT2
					on A.ID = AT2.id
					where IDPartido=@IDPartido and Tipo=2
					and 
					(
					(AT2.equipo = 'visitante' AND AT2.goles = @ResVisitante)
					or
					(AT2.equipo = 'local' AND AT2.goles = @ResLocal)
					)
				--end--if
				--else if @NombreEquipo='local' and @NumGolesEquipo=@ResLocal
				--begin
			--		update Apuestas
			--		set IsGanador=1
			--		where IDPartido=@IDPartido and Tipo=2
			--	end--if
			end--if tipo 2

			-------------------------------------------------------

			/*antiguo*/
			--	if @NombreEquipo='visitante' and @ResVisitante>@ResLocal
			--	begin
			--		update Apuestas
			--		set IsGanador=1
			--		where IDPartido=@IDPartido and Tipo=3
			--	end--if
			--	else if @NombreEquipo='local' and @ResVisitante<@ResLocal
			--	begin
			--		update Apuestas
			--		set IsGanador=1
			--		where IDPartido=@IDPartido and Tipo=3
			--	end--if
			--	else if @NombreEquipo='empate' and @ResVisitante=@ResLocal
			--	begin
			--		update Apuestas
			--		set IsGanador=1
			--		where IDPartido=@IDPartido and Tipo=3
			--	end--if

			/*fin antiguo*/

			if exists (select Tipo from Apuestas
					where @IDPartido=IDPartido and Tipo=3)
			begin
			
			update
			Apuestas
			set IsGanador = 1
			from Apuestas as A
			inner join ApuestaTipo3 as AT3
			on A.ID = AT3.id
			where IDPartido=@IDPartido and Tipo=3
			AND
			(
			(AT3.ganador = 'local' AND @ResLocal > @ResVisitante)
			or
			(AT3.ganador = 'visitante' AND @ResLocal < @ResVisitante)
			or
			(AT3.ganador = 'empate' AND @ResLocal = @ResVisitante)
			)
			end--if tipo 3


		end--fin if update

	fetch next from miCursor into @IDPartido,@ResLocal,@ResVisitante,@FechaPartido,@IdCompeticion

	end--fin de while
	close miCursor--cerramos
	deallocate miCursor--liberamos la memoria
end --cierra el trigger





go
---------pruebas tipo 1
begin tran
update Partidos
set resultadoLocal=1,
	resultadoVisitante=5
where id='84B28789-7A36-417E-8B56-5AC4CC582F1B'

select * from Apuestas
select * from Partidos
select * from ApuestaTipo1


update ApuestaTipo1
set NumGolesLocal = 1,
	numGolesVisitante = 5

rollback

------pruebas tipo 2
begin tran
update Partidos
set resultadoLocal=1,
	resultadoVisitante=5

where id='A21EA695-68C5-4AF8-870C-77B3C3D50ECB'

begin tran
update Partidos
set resultadoLocal=1,
	resultadoVisitante=7
where id='A21EA695-68C5-4AF8-870C-77B3C3D50ECB'


------pruebas tipo 3
begin tran
update Partidos

set resultadoLocal=3,
	resultadoVisitante=1
where id='A21EA695-68C5-4AF8-870C-77B3C3D50ECB'

update Partidos

set resultadoLocal=2,
	resultadoVisitante=1
where id='C11875D3-CB0F-49AA-AF9C-27895AAE5455'


rollback
select * from Partidos
select * from Apuestas
select * from ApuestaTipo1


select * from ApuestaTipo2
select * from ApuestaTipo3




/*Despues se hace un trigger para que cada vez que se actualice la tabla usuario con el saldo, ese movimiento quede grabado en la entidad
cuenta*/
GO
--ALTER
CREATE 
--drop
TRIGGER grabarMovimientoEnCuenta
ON USUARIOS
AFTER UPDATE AS 
BEGIN
	
	/*comprobamos si la columna del saldo ha sido modificada*/

	if exists (select saldoActual from inserted)
	begin
	INSERT INTO Cuentas ( saldo, fechaYHora, correoUsuario)
	values( (SELECT u.saldoActual 
								FROM Usuarios as u
								inner join inserted as i
								on u.correo = i.correo  ), CURRENT_TIMESTAMP,
								(SELECT i.correo
								FROM Usuarios as c
								inner join inserted as i
								on c.correo = i.correo  )  )
	end

	
END

/*crear un trigger para que en cuanto insertes un usuario
se registre en la cuenta su saldo de partida*/
GO
--ALTER
CREATE 
--drop
TRIGGER grabarMovimientoEnCuentaAfterInsert
ON USUARIOS
AFTER insert AS 
BEGIN
	
	/*comprobamos si la columna del saldo ha sido modificada*/

	if exists (select saldoActual from inserted)
	begin
	INSERT INTO Cuentas ( saldo, fechaYHora, correoUsuario)
	values( (SELECT u.saldoActual 
								FROM Usuarios as u
								inner join inserted as i
								on u.correo = i.correo  ), CURRENT_TIMESTAMP,
								(SELECT i.correo
								FROM Usuarios as c
								inner join inserted as i
								on c.correo = i.correo  )  )
	end

	
END
