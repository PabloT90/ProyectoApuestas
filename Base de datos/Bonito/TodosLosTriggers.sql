go
CREATE
TRIGGER [dbo].[noApuestesLoQueNoTienes]
ON [dbo].[Apuestas]
AFTER INSERT, UPDATE AS
BEGIN
	IF EXISTS
		(SELECT id
		FROM inserted as I
		inner join Usuarios as U
		on I.correoUsuario = U.correo
		WHERE u.saldoActual < I.DINEROAPOSTADO
		)
	BEGIN
		RAISERROR('No puedes apostar más de lo que tienes',16,1)
		ROLLBACK
	END
END
go
-----------
GO
CREATE TRIGGER [dbo].[SumarDinero] ON [dbo].[Apuestas]
AFTER UPDATE AS
BEGIN
	DECLARE @Correo varchar(30)
	DECLARE @cuota int
	DECLARE @DineroApostado int
	DECLARE @ganador int 
	SELECT @ganador = isGanador from inserted

	IF(@GANADOR = 1)
	begin
		SELECT @Correo = CorreoUsuario, @cuota = cuota, @DineroApostado = DineroApostado FROM inserted
		--Actualizar el saldo del usuario
		UPDATE Usuarios
		SET saldoActual += @DineroApostado*@cuota
		WHERE correo = @Correo
	end
END
GO
----
GO
CREATE 
TRIGGER [dbo].[restarDineroApuestaInmediatamente]
ON [dbo].[Apuestas]
AFTER INSERT AS 
BEGIN
	
	UPDATE Usuarios
	SET saldoActual = saldoActual - (SELECT DINEROAPOSTADO FROM inserted )
	WHERE correo = (SELECT CORREOUSUARIO FROM inserted)
END
go
-----
go
CREATE trigger [dbo].[T_ActualizarGanador] on [dbo].[Partidos]
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
	UPDATE Apuestas
	SET FechaHoraApuesta = FechaHoraApuesta
	FROM Apuestas
	where IDPartido = @IDPartido
	close miCursor--cerramos
	deallocate miCursor--liberamos la memoria
end --cierra el trigger
go

-----
go
CREATE 
--drop
TRIGGER [dbo].[grabarMovimientoEnCuenta]
ON [dbo].[Usuarios]
AFTER UPDATE AS 
BEGIN

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
go
-----
go
CREATE
--drop
TRIGGER [dbo].[grabarMovimientoEnCuentaAfterInsert]
ON [dbo].[Usuarios]
AFTER insert AS 
BEGIN
	

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
go