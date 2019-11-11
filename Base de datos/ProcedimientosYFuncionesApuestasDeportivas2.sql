/*
Interfaz
Nombre: realizarApuesta
Comentario: Este procedimiento nos permite realizar una apuesta.
Dentro de este procedimiento se llama a la función obtenerCuota.
Cabecera: procedure realizarApuesta()
*/

--Primera actividad:
--T = total cantidad apostada de apuestas a un mismo partido y un mismo tipo de una apuesta determinada dada
--F = total cantidad apostada de apuestas de un mismo partido con un mismo tipo y misma especificaciones del tipo de una apuesta determinada dada

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
--GO
--CREATE FUNCTION obtenerCuota(@capitalApostado smallmoney, @tipo tinyint) RETURNS decimal(6, 2) 
--BEGIN
--	DECLARE @cuota decimal(6,2)

	

--	if @capitalApostado < 40	--Si el capital apostado es menos de 40
--	BEGIN
--		SELECT @cuota = 
--		CASE @tipo
--			WHEN 1 then 4
--			WHEN 2 then 3
--			WHEN 3 then 1.5
--		END
--	END
--	ELSE
--	BEGIN
--		SET @cuota = ((/)-1)*0.8
--	END

--END
--GO

/*
Interfaz
Nombre: cantidadTotalApostadaSegunTipoEnPartido
Comentario: Este método nos permite obtener el capital total apostado de un tipo
en un partido en concreto.
Cabecera:
*/

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
	SELECT * FROM Partidos WHERE isAbierto = 1--Supongo que 1 significa que sigue abierto
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
Postcondiciones: Si la apuesta es ganadora la función devuelve 0 y el capital ganado, si la apuesta es perdedora devuelve 1 y el capital perdido y si el partido aun no ha finalizado
se devuelve -1 y un capital de 0.
*/
CREATE PROCEDURE comprobarResultadoDeUnaApuesta(@idApuesta uniqueidentifier, @apuestaGanada bit OUTPUT, @capital smallmoney OUTPUT)
AS	--Creo un procedimiento porque tengo que devolver dos resultados, no creo una función de multiples instrucciones porque solo devolvería una fila.
BEGIN
	DECLARE @isGanador bit --= (SELECT IsGanador FROM Apuestas WHERE ID = @idApuesta)
	--Obtenemos como va el partido, si ha gnado, perdido o si aún no se ha asignado el resultado.
	SELECT @isGanador = ISNULL(IsGanador, -1) FROM Apuestas WHERE ID = @idApuesta
	
	SELECT @isGanador,
	CASE @capital
	  WHEN 1 THEN (SELECT DineroApostado * Cuota FROM Apuestas WHERE ID = @idApuesta)
	  WHEN 2 THEN (SELECT DineroApostado FROM Apuestas WHERE ID = @idApuesta)
	  ELSE 0
	END

	--if @isGanador = 1--Supongo que 1 significa que se ha ganado la apuesta
	--BEGIN
	--	SET @apuestaGanada = 1
	--	SET @capital = (SELECT DineroApostado * Cuota FROM Apuestas WHERE ID = @idApuesta) 
	--END
	--ELSE
	--BEGIN
	--	SET @apuestaGanada = 0
	--	SET @capital = (SELECT DineroApostado FROM Apuestas WHERE ID = @idApuesta)
	--END
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