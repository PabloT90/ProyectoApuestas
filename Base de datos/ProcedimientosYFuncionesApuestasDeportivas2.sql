/*
Interfaz
Nombre: realizarApuesta
Comentario: Este procedimiento nos permite realizar una apuesta.
Dentro de este procedimiento se llama a la función obtenerCuota.
Cabecera: procedure realizarApuesta()
*/



/*
Interfaz
Nombre: obtenerCuota
Comentario: Esta función nos permite obtener la cuota de una apuesta.
Cabecera: function obtenerCuota(capitalApostado smallmoney, @tipo tinyint)
Entrada:
	-capitalApostado smallmoney
	-tipo tinyint
Salida:
	-cuota decimal(6,2)
Postcondiciones: El método devuelve un número decimal asociado al nombre,
que es la nueva cuota.
*/
GO
CREATE FUNCTION obtenerCuota(@capitalApostado smallmoney, @tipo tinyint) RETURNS decimal(6, 2) 
BEGIN
	DECLARE @cuota decimal(6,2)

	

	if @capitalApostado < 40	--Si el capital apostado es menos de 40
	BEGIN
		SELECT @cuota = 
		CASE @tipo
			WHEN 1 then 4
			WHEN 2 then 3
			WHEN 3 then 1.5
		END
	END
	ELSE
	BEGIN
		SET @cuota = ((/)-1)*0.8
	END

END
GO

/*
Interfaz
Nombre: cantidadTotalApostadaSegunTipoEnPartido
Comentario: Este método nos permite obtener el capital total apostado de un tipo
en un partido en concreto.
Cabecera:
*/
--Dani sacame las comprobaciones de aqui!!!!!!
Begin transaction
insert into Competiciones(id,nombre,año) values(NEWID(), 'Copa del Rey', 2019)
go
Select * from Competiciones
insert into Partidos(id,resultadoLocal,resultadoVisitante,isAbierto,maxApuesta1,maxApuesta2,maxApuesta3,fechaPartido,idCompeticion) 
values(NEWID(), 3, 4, 0, 60, 80, 90, '2019-14-07 11:00:00', '6027536A-F049-474D-90C8-744315FB4B6C')
go
insert into Usuarios(correo,contraseña,saldoActual) 
values ('lauraortiz@gmail.com', 245, 300 )
go
SELECT * FROM Usuarios
SELECT * FROM Partidos
insert into Apuestas(ID, cuota, FechaHoraApuesta, DineroApostado, CorreoUsuario, IDPartido, Tipo, IsGanador) 
values(NEWID(), 3.8, '2019-05-06 10:45:00', 10, 'lauraortiz@gmail.com', '68143F26-0C1B-4288-9749-25A542DCE9DA', 2, 0)
go
SELECT * FROM Apuestas
--insert into Apuestas (ID,FechaHoraApuesta,DineroApostado,CorreoUsuario,Cuota) values (NEWID(),CURRENT_TIMESTAMP,20,'danielgordillo9@gmail.com',4.5)
rollback

GO
/*
Interfaz
Nombre: obtenerPartidosDisponiblesParaApostar
Comentario: Este método nos permite obtener todos los partidos en 
los que se puede realizar una apuesta.
Cabecera: function obtenerPartidosDisponiblesParaApostar()
Salida:
	-Una Tabla de partidos disponibles
Postcondiciones: El método devuelve un tabla con todos los partidos
en los que se puede apostar.
*/
CREATE FUNCTION obtenerPartidosDisponiblesParaApostar ()
RETURNS TABLE
AS
RETURN
	SELECT * FROM Partidos WHERE isAbierto = 0--Supongo que 0 significa que sigue abierto
GO

GO
/*
Interfaz
Nombre: comprobarResultadoDeUnaApuesta
Comentario: Este método nos permite comprobar el resultado de una apuesta.
El método nos devuelve si la apuesta ha sigo ganadora y el capital ganado o perdido
dependiendo de si se ha ganado la apuesta.
Cabecera: function comprobarResultadoDeUnaApuesta(@idApuesta uniqueidentifier, @apuestaGanada bit OUTPUT, @capital smallmoney OUTPUT)
Entrada:
	-@idApuesta uniqueidentifier
Salida:
	-@apuestaGanada bit OUTPUT
	-@capital smallmoney OUTPUT
Postcondiciones: Si la apuesta es ganadora la función devuelve 0 y en caso contrarion devuelve 1.
*/
CREATE PROCEDURE comprobarResultadoDeUnaApuesta(@idApuesta uniqueidentifier, @apuestaGanada bit OUTPUT, @capital smallmoney OUTPUT)
AS	--Creo un procedimiento porque tengo que devolver dos resultados, no creo una función de multiples instrucciones porque solo devolvería una fila.
BEGIN
	DECLARE @isGanador bit = (SELECT IsGanador FROM Apuestas WHERE ID = @idApuesta)

	--TODO	--> Tengo que preguntar que ocurre si aún no se ha registrado isGanador
	--Posiblemente tendre que hacer una comprobación y devolver -1 si aun no se sabe el resultado de la apuesta
	if @isGanador = 0--Supongo que 0 significa que se ha ganado la apuesta
	BEGIN
		SET @apuestaGanada = 0
		SET @capital = (SELECT DineroApostado * Cuota FROM Apuestas WHERE ID = @idApuesta) 
	END
	ELSE
	BEGIN
		SET @apuestaGanada = 1
		SET @capital = (SELECT DineroApostado FROM Apuestas WHERE ID = @idApuesta)
	END
END
GO

GO
/*
Interfaz
Nombre: ingresoACuenta
Comentario: Este método nos permite ingresar un capital a la cuenta de un usuario.
Cabecera: procedure ingresoACuenta (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso tinyint OUTPUT)
Entrada:
	-@CorreoUsuario varchar(30)
	-@ingreso smallmoney
Salida:
	-@resultadoIngreso tinyint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar el ingreso, -1
si el correo es incorrecto o -2 si el ingreso es negativo o igual a 0.
*/
CREATE PROCEDURE ingresoACuenta (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso tinyint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @CorreoUsuario)
	BEGIN
		IF @ingreso > 0
		BEGIN
			--Un usuario puede tener más de un id de cuenta??????? Se podría haber creado otra tabla RegistrosCuentas
			DECLARE @IDCuenta int = (SELECT MAX(id) FROM Cuentas WHERE correoUsuario = @CorreoUsuario)--Obtenemos el id de la cuenta del usuario
			DECLARE @SaldoActual smallmoney = (SELECT saldo FROM Cuentas WHERE id = @IDCuenta)
			INSERT INTO Cuentas (saldo, fechaYHora, correoUsuario) VALUES ((@SaldoActual + @ingreso), CURRENT_TIMESTAMP, @CorreoUsuario)
			SET @resultadoIngreso = 0
		END
		ELSE
		BEGIN
			SET @resultadoIngreso = -2
		END
	END
	ELSE
	BEGIN
		SET @resultadoIngreso = -1
	END
END
GO

GO
/*
Interfaz
Nombre: retirarCapitalCuenta
Comentario: Este método nos permite retirar capital de una cuenta de usuario.
Cabecera: procedure retirarCapitalCuenta (@CorreoUsuario varchar(30), @capitalARetirar smallmoney, @resultadoTransaccion tinyint OUTPUT)
Entrada:
	-@CorreoUsuario varchar(30)
	-@capitalARetirar smallmoney
Salida:
	-@resultadoTransaccion tinyint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar la transacción, -1
si el correo es incorrecto, -2 si el capitalARetirar es negativo o igual a 0 o -3 si capitalARetirar es superior al saldo del usuario.
*/
CREATE PROCEDURE retirarCapitalCuenta (@CorreoUsuario varchar(30), @capitalARetirar smallmoney, @resultadoIngreso tinyint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @CorreoUsuario)
	BEGIN
		IF @capitalARetirar > 0
		BEGIN
		--Un usuario puede tener más de un id de cuenta???????
			DECLARE @IDCuenta int = (SELECT MAX(id) FROM Cuentas WHERE correoUsuario = @CorreoUsuario)--Obtenemos el id de la cuenta del usuario
			DECLARE @SaldoActual smallmoney = (SELECT saldo FROM Cuentas WHERE id = @IDCuenta)--Obtenemos el saldo actual del usuario
			IF @capitalARetirar <= @SaldoActual
			BEGIN	
				INSERT INTO Cuentas (saldo, fechaYHora, correoUsuario) VALUES ((@SaldoActual - @capitalARetirar), CURRENT_TIMESTAMP, @CorreoUsuario)
				SET @resultadoIngreso = 0
			END
			ELSE
			BEGIN
				SET @resultadoIngreso = -3
			END
		END
		ELSE
		BEGIN
			SET @resultadoIngreso = -2
		END
	END
	ELSE
	BEGIN
		SET @resultadoIngreso = -1
	END
END
GO