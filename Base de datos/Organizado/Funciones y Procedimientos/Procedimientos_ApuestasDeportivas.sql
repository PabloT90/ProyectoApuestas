
USE PruebasPablo
GO

use PruebasPablo
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

/*HAY QUE ACTUALIZAR EL SALDO DE LAS CUENTAS DE LOS USUARIOS Y LOS MOVIMIENTOS DE SUS CUENTAS en caso
de que ganen una apuesta*/

go
create 
or alter 
trigger T_ActualizarGanador on Partidos
after update as
begin
	declare @IDPartido int,
			@ResLocal tinyint,
			@ResVisitante tinyint,
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
		--if update(resultadoLocal) or update(resultadoVisitante)
		--begin
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
			end--if tipo 2

			-------------------------------------------------------

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


		--end--fin if update

	fetch next from miCursor into @IDPartido,@ResLocal,@ResVisitante,@FechaPartido,@IdCompeticion

	end--fin de while
	close miCursor--cerramos
	deallocate miCursor--liberamos la memoria
end --cierra el trigger

go

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
