--Primera actividad:
--T = total cantidad apostada de apuestas a un mismo partido y un mismo tipo de una apuesta determinada dada
--F = total cantidad apostada de apuestas de un mismo partido con un mismo tipo y misma especificaciones del tipo de una apuesta determinada dada


/*
Nombre: realizarApuestaTipo1
Comentario: Este procedimiento nos permite realizar una apuesta del tipo 1.
Dentro de este procedimiento se llama a la función obtenerCuota.
Cabecera: procedure realizarApuesta(@FechaHora smalldatetime, @CapitalAApostar smallmoney, @Correo char(30), @IdPartido uniqueidentifier, @NumGolesLocal tinyint, @NumGolesVisitante tinyint, @Error smallint)
Entradas:
	-@FechaHora smalldatetime
	-@CapitalAApostar smallmoney
	-@Correo char(30)
	-@IdPartido uniqueidentifier
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salidas:
	-@Error smallint
Postcondiciones: El procedimiento nos permite realizar una apuesta del tipo 1. El procedimiento
devuelve 0 si se ha realizado correctamente la apuesta, -1 si el correo es incorrecto, -2
si el partido no existe, -3 si el capital es negativo o igual a 0, -4 si el
número de goles locales en menor que 0 o -5 si el número de goles del visitante es menor que 0.
*/
CREATE PROCEDURE realizarApuestaTipo1(@FechaHora smalldatetime, @CapitalAApostar smallmoney, @Correo char(30), @IdPartido uniqueidentifier, @NumGolesLocal tinyint, @NumGolesVisitante tinyint, @Error smallint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @Correo)
	BEGIN
		IF EXISTS(SELECT * FROM Partidos WHERE id = @IdPartido)
		BEGIN
			IF @CapitalAApostar > 0
			BEGIN
				IF @NumGolesLocal >= 0
				BEGIN
					IF @NumGolesVisitante >= 0
					BEGIN
						
						SET @Error = -0
					END
					ELSE
					BEGIN
						SET @Error = -5
					END
				END
				ELSE
				BEGIN
					SET @Error = -4
				END
			END
			ELSE
			BEGIN
				SET @Error = -3
			END
		END
		ELSE
		BEGIN
			SET @Error = -2
		END
	END
	ELSE
	BEGIN
		SET @Error = -1
	END
END

GO
/*
Interfaz
Nombre: obtenerCuotaTipo1
Comentario: Este método nos permite obtener una cuota para una puesta del tipo 1 en un determinado partido.
Cabecera: function obtenerCuotaTipo1(@IdPartido uniqueidentifier, @CantidadApostada smallmoney, @NumGolesLocal tinyint, @NumGolesVisitante tinyint)
Entrada:
	-@IdPartido uniqueidentifier
	-@CantidadApostada smallmoney
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salida:
	-@Cuota decimal(6,2)
Postcondiciones: Si la cuota obtenida es menor que 1,5 la función devuelve -1.
*/
CREATE FUNCTION obtenerCuotaTipo1(@IdPartido uniqueidentifier, @CantidadApostada smallmoney, @NumGolesLocal tinyint, @NumGolesVisitante tinyint) RETURNS decimal(6, 2)
BEGIN
	DECLARE @Cuota decimal(6,2)

	IF @CantidadApostada < 40
	BEGIN
		SET @Cuota = 4;
	END
	ELSE
	BEGIN
		DECLARE @T smallmoney = (SELECT * FROM obtenerParametroT(@IdPartido, 1))
		DECLARE @F smallmoney = (SELECT * FROM obtenerTipo1ParametroF(@IdPartido, @NumGolesLocal, @NumGolesVisitante))
		SET @Cuota = ((@T/@F)-1)*8.0
		IF @Cuota < 1.5
		BEGIN
			SET @Cuota = -1
		END
	END

	RETURN @Cuota
END

GO

GO
/*
Interfaz
Nombre: obtenerTipo1ParametroF
Comentario: Este método nos permite obtener el parámetro F para el método obtenerCuotaTipo1.
Cabecera: function obtenerTipo1ParametroT (@IdPartido uniqueidentifier, @NumGolesLocal tinyint, @NumGolesVisitante tinyint)
Entrada:
	-@IdPartido uniqueidentifier
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salida:
	-@CantidadApostada smallmoney
Postcondiciones: El método devuelve la cantidad apostada para apuestas iguales a las que estamos grabando.
*/
CREATE FUNCTION obtenerTipo1ParametroF (@IdPartido uniqueidentifier, @NumGolesLocal tinyint, @NumGolesVisitante tinyint) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney
	=
	(SELECT SUM (A.DineroApostado) FROM Apuestas AS [A]
		INNER JOIN ApuestaTipo1 AS [T1] ON A.ID = T1.id
		WHERE T1.NumGolesLocal = @NumGolesLocal AND T1.numGolesVisitante = @NumGolesVisitante)

	RETURN @CantidadApostada
END
GO

GO
/*
Interfaz
Nombre: obtenerParametroT
Comentario: Este métdodo nos permite obtener la cantidad apostada para un partido con un determinado tipo de apuesta.
Cabecera: FUNCTION obtenerParametroT (@IdPartido uniqueidentifier, @Tipo tinyint)
Entrada:
	-@IdPartido uniqueidentifier
	-@Tipo tinyint
Salida:
	-@CantidadApostada smallmoney
Precondiciones:
	-@Tipo debe ser un número entre 1 y 3.
Postcondiciones: Este método nos devuelve la cantidad apostada para apuestas de un partido y un tipo determinado.
*/
CREATE FUNCTION obtenerParametroT (@IdPartido uniqueidentifier, @Tipo tinyint) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney

	SELECT @CantidadApostada =
	CASE @Tipo
		WHEN 1 THEN (SELECT SUM (@CantidadApostada) FROM Apuestas WHERE Tipo = 1) 
		WHEN 2 THEN (SELECT SUM (@CantidadApostada) FROM Apuestas WHERE Tipo = 2) 
		WHEN 3 THEN (SELECT SUM (@CantidadApostada) FROM Apuestas WHERE Tipo = 3) 
	END

	RETURN @CantidadApostada
END
GO

/*
Interfaz
Nombre: obtenerCantidadApostada
Comentario: Este método nos permite obtener el capital total apostado en
un determinado partido con un tipo de apuesta específico.
Cabecera: function obtenerCantidadApostada(@idPartido uniqueidentifier, @idTipoApuesta tinyint)
Entrada:
	-@idPartido uniqueidentifier
	-@idTipoApuesta tinyint
Salida:
	-@CapitalTotalApostado smallmoney
Postcondiciones: La función devuelve el capital total apostado para ese partido y ese tipo en concreto.
*/
GO
CREATE FUNCTION obtenerCantidadApostada(@idPartido uniqueidentifier, @idTipoApuesta tinyint)
RETURNS INT AS
BEGIN
	DECLARE @CapitalTotalApostado smallmoney

	SELECT @CapitalTotalApostado = SUM(DineroApostado) FROM Apuestas WHERE IDPartido = @idPartido AND Tipo = @idTipoApuesta

	RETURN @CapitalTotalApostado
END
GO



/*
Interfaz
Nombre: obtenerCantidadApostada	//No valida hacer una por cada tipo de apuesta
Comentario: Este método nos permite obtener el capital total apostado en
un determinado partido con un tipo de apuesta específico y con las mismas especificaciones
en el tipo de apuesta, es decir, para apuestas que tengan el mismo valor en el tipo. Todos
estos datos son suministrados desde una apuesta concreta.
Este método es utilizado en el método realizar apuesta.
Cabecera: function obtenerCantidadApostada(@IdApuesta)
Entrada:
	-@idPartido uniqueidentifier
	-@idTipoApuesta tinyint
Salida:
	-@CapitalTotalApostado smallmoney
Postcondiciones: La función devuelve el capital total apostado para ese partido y ese tipo en concreto.
*/
--GO
--CREATE FUNCTION obtenerCantidadApostadaTipoEspecifico(@idApuesta uniqueidentifier)
--RETURNS INT AS
--BEGIN
--	DECLARE @CapitalTotalApostado smallmoney
--	DECLARE @Tipo tinyint = (SELECT Tipo FROM Apuestas WHERE ID = @idApuesta) --Obtenemos el tipo de la apuesta
--	DECLARE @IdPartido uniqueidentifier = (SELECT IDPartido FROM Apuestas WHERE ID = @idApuesta)--Obtenemos el id del partido
	
--	SELECT @CapitalTotalApostado =
--		CASE @Tipo
--			WHEN 1 THEN (SELECT SUM(DineroApostado) FROM Apuestas AS [A] 
--							INNER JOIN ApuestaTipo1 AS [T1] ON A.ID = T1.id
--								WHERE A.IDPartido = @IdPartido AND T1.NumGolesLocal = (SELECT NumGolesLocal FROM ApuestaTipo1 WHERE ID = @idApuesta)
--								AND T1.numGolesVisitante = (SELECT numGolesVisitante FROM ApuestaTipo1 WHERE ID = @idApuesta))
--			WHEN 2 THEN (SELECT SUM(DineroApostado) FROM Apuestas AS [A] 
--							INNER JOIN ApuestaTipo2 AS [T2] ON A.ID = T2.id
--								WHERE A.IDPartido = @IdPartido AND T2.equipo = (SELECT equipo FROM ApuestaTipo2 WHERE ID = @idApuesta)
--								AND T2.goles = (SELECT goles FROM ApuestaTipo2 WHERE ID = @idApuesta))
--			WHEN 3 THEN (SELECT SUM(DineroApostado) FROM Apuestas AS [A] 
--							INNER JOIN ApuestaTipo3 AS [T3] ON A.ID = T3.id
--								WHERE A.IDPartido = @IdPartido AND T3.ganador = (SELECT ganador FROM ApuestaTipo3 WHERE ID = @idApuesta))
--		END

--	RETURN @CapitalTotalApostado
--END
--GO

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
Cabecera: function comprobarResultadoDeUnaApuesta(@idApuesta uniqueidentifier, @apuestaGanada smallint OUTPUT, @capital smallmoney OUTPUT)
Entrada:
	-@idApuesta uniqueidentifier
Salida:
	-@apuestaGanada smallint OUTPUT
	-@capital smallmoney OUTPUT
Postcondiciones: Si la apuesta es ganadora la función devuelve 0 y el capital ganado, si la apuesta es perdedora devuelve 1 y el capital perdido y si el partido aun no ha finalizado
se devuelve -1 y un capital de 0.
*/
CREATE PROCEDURE comprobarResultadoDeUnaApuesta(@idApuesta uniqueidentifier, @apuestaGanada smallint OUTPUT, @capital smallmoney OUTPUT)
AS	--Creo un procedimiento porque tengo que devolver dos resultados, no creo una función de multiples instrucciones porque solo devolvería una fila.
BEGIN
	--DECLARE @isGanador smallint --= (SELECT IsGanador FROM Apuestas WHERE ID = @idApuesta)
	--Obtenemos como va el partido, si ha gnado, perdido o si aún no se ha asignado el resultado.
	SELECT @apuestaGanada = ISNULL(IsGanador, -1) FROM Apuestas WHERE ID = @idApuesta
	
	SELECT @capital =
	CASE @apuestaGanada
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
Cabecera: procedure ingresoACuenta (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso smallint OUTPUT)
Entrada:
	-@CorreoUsuario varchar(30)
	-@ingreso smallmoney
Salida:
	-@resultadoIngreso smallint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar el ingreso, -1
si el correo es incorrecto o -2 si el ingreso es negativo o igual a 0.
*/
CREATE PROCEDURE ingresoACuenta (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso smallint OUTPUT)
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
	-@resultadoTransaccion smallint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar la transacción, -1
si el correo es incorrecto, -2 si el capitalARetirar es negativo o igual a 0 o -3 si capitalARetirar es superior al saldo del usuario.
*/
CREATE PROCEDURE retirarCapitalCuenta (@CorreoUsuario varchar(30), @capitalARetirar smallmoney, @resultadoIngreso smallint OUTPUT)
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