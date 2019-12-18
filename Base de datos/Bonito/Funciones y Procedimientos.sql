CREATE 
FUNCTION 
[dbo].[cuantoDineroHayApostadoAUnPartido] (@IDPartido int, @tipoApuesta int)
	RETURNS INT AS
	BEGIN 

	DECLARE @RESULTADO INT
	SET @RESULTADO = 
		(SELECT SUM(A.CUOTA * A.DINEROAPOSTADO)
		FROM Partidos AS P
		INNER JOIN APUESTAS AS A
		ON P.id = A.IDPARTIDO
		WHERE @IDPARTIDO = P.ID
		AND @TIPOAPUESTA = A.TIPO)

	RETURN (@RESULTADO)
	END
GO

--

GO
create function [dbo].[FN_TipoApuesta](@IDApuesta int)
returns tinyint as
begin
	declare @Tipo as tinyint

	set @Tipo=(select Tipo from Apuestas
				where @IDApuesta=ID)

	return @Tipo
end
GO

--

GO
--ALTER
CREATE 
FUNCTION [dbo].[GananciaDeUnUsuarioConUnaApuesta] (@cuota int, @cantidadApostada int)
	RETURNS INT AS
	BEGIN 
	RETURN (@CUOTA * @CANTIDADAPOSTADA)
	END
	
GO

--

GO
CREATE FUNCTION [dbo].[obtenerCantidadApostada](@idPartido int, @idTipoApuesta tinyint)
RETURNS INT AS
BEGIN
	DECLARE @CapitalTotalApostado smallmoney

	SELECT @CapitalTotalApostado = SUM(DineroApostado) FROM Apuestas WHERE IDPartido = @idPartido AND Tipo = @idTipoApuesta

	RETURN @CapitalTotalApostado
END
GO

--
GO
/*
Interfaz
Nombre: obtenerCuotaTipo1
Comentario: Este m�todo nos permite obtener una cuota para una puesta del tipo 1 en un determinado partido.
Cabecera: function obtenerCuotaTipo1(@IdPartido int, @CantidadApostada smallmoney, @NumGolesLocal tinyint, @NumGolesVisitante tinyint)
Entrada:
	-@IdPartido int
	-@CantidadApostada smallmoney
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salida:
	-@Cuota decimal(6,2)
Postcondiciones: Si la cuota obtenida es menor que 1,5 la funci�n devuelve -1. 1 si F es 0.
*/
CREATE FUNCTION [dbo].[obtenerCuotaTipo1](@IdPartido int, @CantidadApostada smallmoney, @NumGolesLocal tinyint, @NumGolesVisitante tinyint) RETURNS decimal(6, 2)
BEGIN
	DECLARE @Cuota decimal(6,2)

	IF @CantidadApostada < 40
	BEGIN
		SET @Cuota = 4;
	END
	ELSE
	BEGIN
		DECLARE @T smallmoney = (dbo.obtenerParametroT(@IdPartido, 1))
		DECLARE @F smallmoney = (dbo.obtenerTipo1ParametroF(@IdPartido, @NumGolesLocal, @NumGolesVisitante))
		IF(@F = 0)
		BEGIN
			set @F = 1
		END
		
		SET @Cuota = ((@T/@F)-1)*0.8
			IF @Cuota < 1.5
			BEGIN
				SET @Cuota = -1
			END

	END
	RETURN @Cuota
END
go

--

/*
Interfaz
Nombre: obtenerCuotaTipo2
Comentario: Este m�todo nos permite obtener una cuota para una puesta del tipo 2 en un determinado partido.
Cabecera: function obtenerCuotaTipo2(@IdPartido int, @CantidadApostada smallmoney, @Equipo varchar(10), @Goles tinyint)
Entrada:
	-@IdPartido int
	-@CantidadApostada smallmoney
	-@Equipo varchar(10)
	-@Goles tinyint
Salida:
	-@Cuota decimal(6,2)
Postcondiciones: Si la cuota obtenida es menor que 1,5 la funci�n devuelve -1.
*/
CREATE FUNCTION [dbo].[obtenerCuotaTipo2](@IdPartido int, @CantidadApostada smallmoney, @Equipo varchar(10), @Goles tinyint) RETURNS decimal(6, 2)
BEGIN
	DECLARE @Cuota decimal(6,2)

	IF @CantidadApostada < 40
	BEGIN
		SET @Cuota = 4;
	END
	ELSE
	BEGIN
		DECLARE @T smallmoney = (dbo.obtenerParametroT(@IdPartido, 2))
		DECLARE @F smallmoney = (dbo.obtenerTipo2ParametroF(@IdPartido, @Equipo, @Goles))
		IF(@f= 0)
			SET @f= 1
		SET @Cuota = ((@T/@F)-1)*0.8
		IF @Cuota < 1.5
		BEGIN
			SET @Cuota = -1
		END
	END

	RETURN @Cuota
END
GO

--
GO
/*
Interfaz
Nombre: obtenerCuotaTipo3
Comentario: Este m�todo nos permite obtener una cuota para una puesta del tipo 3 en un determinado partido.
Cabecera: function obtenerCuotaTipo3(@IdPartido int, @CantidadApostada smallmoney, @Ganador varchar(15))
Entrada:
	-@IdPartido int
	-@CantidadApostada smallmoney
	-@Ganador varchar(15)
Salida:
	-@Cuota decimal(6,2)
Postcondiciones: Si la cuota obtenida es menor que 1,5 la funci�n devuelve -1.
*/
CREATE FUNCTION [dbo].[obtenerCuotaTipo3](@IdPartido int, @CantidadApostada smallmoney, @Ganador varchar(15)) RETURNS decimal(6, 2)
BEGIN
	DECLARE @Cuota decimal(6,2)

	IF @CantidadApostada < 40
	BEGIN
		SET @Cuota = 4;
	END
	ELSE
	BEGIN
		DECLARE @T smallmoney = (dbo.obtenerParametroT(@IdPartido, 3))
		DECLARE @F smallmoney = (dbo.obtenerTipo3ParametroF(@IdPartido, @Ganador))
		SET @Cuota = ((@T/@F)-1)*0.8
		IF @Cuota < 1.5
		BEGIN
			SET @Cuota = -1
		END
	END

	RETURN @Cuota
END

GO

--

GO
/*
Interfaz
Nombre: obtenerParametroT
Comentario: Este m�tdodo nos permite obtener la cantidad apostada para un partido con un determinado tipo de apuesta.
Cabecera: FUNCTION obtenerParametroT (@IdPartido int, @Tipo tinyint)
Entrada:
	-@IdPartido int
	-@Tipo tinyint
Salida:
	-@CantidadApostada smallmoney
Precondiciones:
	-@Tipo debe ser un n�mero entre 1 y 3.
Postcondiciones: Este m�todo nos devuelve la cantidad apostada para apuestas de un partido y un tipo determinado.
*/
CREATE FUNCTION [dbo].[obtenerParametroT] (@IdPartido int, @Tipo tinyint) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney

	SELECT @CantidadApostada =
	CASE @Tipo
		WHEN 1 THEN (SELECT SUM (DineroApostado) FROM Apuestas WHERE Tipo = 1 AND IDPartido = @IdPartido) 
		WHEN 2 THEN (SELECT SUM (DineroApostado) FROM Apuestas WHERE Tipo = 2 AND IDPartido = @IdPartido) 
		WHEN 3 THEN (SELECT SUM (DineroApostado) FROM Apuestas WHERE Tipo = 3 AND IDPartido = @IdPartido) 
	END

	RETURN @CantidadApostada
END
GO
--

GO
/*
Interfaz
Nombre: obtenerTipo1ParametroF
Comentario: Este m�todo nos permite obtener el par�metro F para el m�todo obtenerCuotaTipo1.
Cabecera: function obtenerTipo1ParametroF (@IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint)
Entrada:
	-@IdPartido int
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salida:
	-@CantidadApostada smallmoney
Postcondiciones: El m�todo devuelve la cantidad apostada para apuestas iguales a las que estamos grabando.
*/
CREATE FUNCTION [dbo].[obtenerTipo1ParametroF] (@IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney
	=
	(SELECT SUM (A.DineroApostado) FROM Apuestas AS [A]
		INNER JOIN ApuestaTipo1 AS [T1] ON A.ID = T1.id
		WHERE T1.NumGolesLocal = @NumGolesLocal AND T1.numGolesVisitante = @NumGolesVisitante AND IDPartido = @IdPartido)
	SET @CantidadApostada = ISNULL(@CantidadApostada,0)
	RETURN @CantidadApostada
END
GO

--
GO
/*
Interfaz
Nombre: obtenerTipo2ParametroF
Comentario: Este m�todo nos permite obtener el par�metro F para el m�todo obtenerCuotaTipo2.
Cabecera: function obtenerTipo2ParametroF (@IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint)
Entrada:
	-@IdPartido int
	-@Equipo varchar(10)
	-@Goles tinyint
Salida:
	-@CantidadApostada smallmoney
Postcondiciones: El m�todo devuelve la cantidad apostada para apuestas iguales a las que estamos grabando.
*/
CREATE FUNCTION [dbo].[obtenerTipo2ParametroF] (@IdPartido int, @Equipo varchar(10), @Goles tinyint) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney
	=
	(SELECT SUM (A.DineroApostado) FROM Apuestas AS [A]
		INNER JOIN ApuestaTipo2 AS [T2] ON A.ID = T2.id
		WHERE T2.equipo = @Equipo AND T2.goles = @Goles AND IDPartido = @IdPartido)

		SET @CantidadApostada = ISNULL(@CantidadApostada,0)
	RETURN @CantidadApostada
END
GO
--

/*
Interfaz
Nombre: obtenerTipo3ParametroF
Comentario: Este m�todo nos permite obtener el par�metro F para el m�todo obtenerCuotaTipo3.
Cabecera: function obtenerTipo3ParametroF (@IdPartido int, @Ganador varchar(15))
Entrada:
	-@IdPartido int
	-@Ganador varchar(15)
Salida:
	-@CantidadApostada smallmoney
Postcondiciones: El m�todo devuelve la cantidad apostada para apuestas iguales a las que estamos grabando.
*/
CREATE FUNCTION [dbo].[obtenerTipo3ParametroF] (@IdPartido int, @Ganador varchar(15)) RETURNS smallmoney
BEGIN
	DECLARE @CantidadApostada smallmoney
	=
	(SELECT SUM (A.DineroApostado) FROM Apuestas AS [A]
		INNER JOIN ApuestaTipo3 AS [T3] ON A.ID = T3.id
		WHERE T3.ganador = @Ganador AND IDPartido = @IdPartido)

	RETURN @CantidadApostada
END
GO
--

/*
Interfaz
Nombre: apuestasSinContabilizarDeUnPartido
Comentario: Esta funci�n nos devuelve las apuestas sin contabilizar de un partido.
Cabecera: function apuestasSinContabilizarDeUnPartido(@idPartido int)
Entrada:
	-@idPartido int
Salida:
	-Tabla con las apuestas sin contabilizar del partido
Postcondiciones: La funci�n devuelve una tabla con las apuestas sin contabilizar del partido, 
si el partido no existe o si no tiene apuestas sin contabilizar la funci�n devuelve una tabla vac�a.
*/
CREATE FUNCTION [dbo].[apuestasSinContabilizarDeUnPartido](@idPartido int)
RETURNS TABLE
AS
RETURN
	SELECT * FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0
GO
--

/*
Interfaz
Nombre: partidosConApuestasSinContabilizar
Comentario: Esta funci�n nos devuelve todos los partidos que tienen apuestas sin
contabilizar.
Cabecera: function partidosConApuestasSinContabilizar()
Salida:
	-Tabla de id's de los partidos con apuestas no contabilizadas
*/
CREATE FUNCTION [dbo].[partidosConApuestasSinContabilizar]()
RETURNS TABLE
AS
RETURN
	SELECT IDPartido FROM (
	SELECT IDPartido, COUNT(*) AS [Apuestas Sin Contabilizar] FROM Apuestas WHERE Contabilizada = 0
		GROUP BY IDPartido) AS [C1] WHERE [Apuestas Sin Contabilizar] > 0
GO
--

/*
Interfaz
Nombre: obtenerApuestasNoContabilizadas
Comentario: Este m�todo nos permite obtener todas las apuestas no contabilizadas de un partido.
Cabecera: function obtenerApuestasNoContabilizadas(@idPartido int)
Entrada:
	-@idPartido int
Salida:
	-Tabla de apuestas no contabilizadas
Postcondiciones: El m�todo devuelve una tabla con las apuestas no contabilizadas de un partido.
*/
CREATE FUNCTION [dbo].[obtenerApuestasNoContabilizadas](@idPartido int)
RETURNS TABLE
AS
RETURN
    SELECT * FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0 --0 significa que no ha sido contabilizada
GO

--

/*
Interfaz
Nombre: obtenerPartidosDisponiblesParaApostar
Comentario: Este m�todo nos permite obtener todos los partidos en 
los que se puede realizar una apuesta.
Cabecera: function obtenerPartidosDisponiblesParaApostar()
Salida:
	-Una Tabla de partidos disponibles
Postcondiciones: El m�todo devuelve un tabla con todos los partidos
en los que se puede apostar.
*/
CREATE FUNCTION [dbo].[obtenerPartidosDisponiblesParaApostar] ()
RETURNS TABLE
AS
RETURN
	SELECT * FROM Partidos WHERE isAbierto = 1--Supongo que 1 significa que sigue abierto
GO
--

/*
Interfaz
Nombre: abrirPartido
Comentario: Este m�todo nos permite abrir un patido de la base de datos.
Cabecera: PROCEDURE abrirPartido(@IdPartido int, @FilasModificadas tinyint OUTPUT)
Entrada:
	-@IdPartido int
Salida:
	-@FilasModificadas
Postcondiciones: Este m�todo devuelve un n�mero asociado al nombre, que son el n�mero de filas modificadas.
0 significa que no se ha encontrado el partido y 1 que se ha copnseguido modificar su estado.
*/
CREATE PROCEDURE [dbo].[abrirPartido](@IdPartido int, @FilasModificadas tinyint OUTPUT) AS
BEGIN
	UPDATE Partidos 
		SET isAbierto = 0
	WHERE id = @IdPartido
	SET @FilasModificadas = @@ROWCOUNT	--Nos devuelve el n�mero de filas afectadas en la transacci�n anterior
END
GO
--

GO
/*
Interfaz
Nombre: addPartido
Comentario: Este m�todo nos permite insertar un nuevo partido en la base de datos.
Cabecera: PROCEDURE addPartido(@resultadoLocal tinyint, @resultadoVisitante tinyint, @isAbierto bit, @MaxApuesta1 int, @MaxApuesta2 int, @MaxApuesta3 int, @FechaPartido smalldatetime, @IdCompeticion int, @Error smallint OUTPUT)
Entrada:
	-@resultadoLocal tinyint
	-@resultadoVisitante tinyint
	-@isAbierto bit
	-@MaxApuesta1 int
	-@MaxApuesta2 int
	-@MaxApuesta3 int
	-@FechaPartido smalldatetime
	-@IdCompeticion int
Salida:
	-@Error smallint
Postcondiciones: El m�todo devuelve un n�mero entero asociado al nombre, -1 si maxApuesta1 es igual o menor que cero o si es nulo, 
-2 si maxApuesta2 es igual o menor que cero o si es nulo, -3 si maxApuesta3 es igual o menor que cero o si es nulo, -4 si idCompeticion 
es igual o menor que cero o si es nulo o 0 si se ha conseguido insertar el nuevo partido en la base de datos.
*/
CREATE PROCEDURE [dbo].[addPartido](@resultadoLocal tinyint, @resultadoVisitante tinyint, @isAbierto bit, @MaxApuesta1 int, @MaxApuesta2 int, @MaxApuesta3 int, @FechaPartido smalldatetime, @IdCompeticion int, @Error smallint OUTPUT) AS
BEGIN
	IF @MaxApuesta1 != null AND @MaxApuesta1 > 0
	BEGIN
		IF @MaxApuesta2 != null AND @MaxApuesta2 > 0
		BEGIN
			IF @MaxApuesta3 != null AND @MaxApuesta3 > 0
			BEGIN
				IF @IdCompeticion != null AND @IdCompeticion > 0
				BEGIN
					INSERT INTO Partidos VALUES(@resultadoLocal, @resultadoVisitante, @isAbierto, @MaxApuesta1, @MaxApuesta2, @MaxApuesta3, @FechaPartido, @IdCompeticion)
					SET @Error = 0
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

--
/*
Interfaz
Nombre: apuestasSinContabilizarDeUnPartido
Comentario: Esta funci�n nos devuelve las apuestas sin contabilizar de un partido.
Cabecera: function apuestasSinContabilizarDeUnPartido(@idPartido int)
Entrada:
	-@idPartido int
Salida:
	-Tabla con las apuestas sin contabilizar del partido
Postcondiciones: La funci�n devuelve una tabla con las apuestas sin contabilizar del partido, 
si el partido no existe o si no tiene apuestas sin contabilizar la funci�n devuelve una tabla vac�a.
*/
CREATE procedure [dbo].[apuestasSinContabilizarDeUnPartido2](@idPartido int)
AS
	SELECT * FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0
GO
------
go
CREATE PROCEDURE [dbo].[cerrarPartido](@idPartido INT) AS
BEGIN
	UPDATE Partidos
	SET isAbierto = 0 --Creo que asi es abierto.
	WHERE id = @idpartido
END
GO
----
/*
Interfaz
Nombre: comprobarResultadoDeUnaApuesta
Comentario: Este m�todo nos permite comprobar el resultado de una apuesta.
El m�todo nos devuelve si la apuesta ha sigo ganadora y el capital ganado o perdido
dependiendo de si se ha ganado la apuesta.
Cabecera: function comprobarResultadoDeUnaApuesta(@idApuesta int, @apuestaGanada smallint OUTPUT, @capital smallmoney OUTPUT)
Entrada:
	-@idApuesta int
Salida:
	-@apuestaGanada smallint OUTPUT
	-@capital smallmoney OUTPUT
Postcondiciones: Si la apuesta es ganadora la funci�n devuelve 0 y el capital ganado, si la apuesta es perdedora devuelve 1 y el capital perdido y si el partido aun no ha finalizado
se devuelve -1 y un capital de 0.
*/
CREATE PROCEDURE [dbo].[comprobarResultadoDeUnaApuesta](@idApuesta int, @apuestaGanada smallint OUTPUT, @capital smallmoney OUTPUT)
AS	--Creo un procedimiento porque tengo que devolver dos resultados, no creo una funci�n de multiples instrucciones porque solo devolver�a una fila.
BEGIN
	--DECLARE @isGanador smallint --= (SELECT IsGanador FROM Apuestas WHERE ID = @idApuesta)
	--Obtenemos como va el partido, si ha gnado, perdido o si a�n no se ha asignado el resultado.
	SELECT @apuestaGanada = ISNULL(IsGanador, -1) FROM Apuestas WHERE ID = @idApuesta
	
	SELECT @capital =
	CASE @apuestaGanada
	  WHEN 1 THEN (SELECT DineroApostado * Cuota FROM Apuestas WHERE ID = @idApuesta)
	  WHEN 2 THEN (SELECT DineroApostado FROM Apuestas WHERE ID = @idApuesta)
	  ELSE 0
	END
END
GO
----
GO
CREATE PROCEDURE [dbo].[consultarApuestaTipo1](@IdPartido int)
AS
BEGIN
	SELECT A.DineroApostado, A1.NumGolesLocal, A1.numGolesVisitante FROM Apuestas AS[A]
		INNER JOIN ApuestaTipo1 AS[A1] ON A.ID = A1.id
		WHERE IDPartido = @IdPartido
	ORDER BY NumGolesLocal, Tipo
END
GO
------
/*
Interfaz
Nombre: consultarApuestaTipo2
Comentario: Este m�todo nos devuelve la apuesta y el dinero del tipo 2
Cabecera: PROCEDURE consultarApuestaTipo2(@IdPartido int)
Salida:
	-Cursor listadoCompeticiones
Postcondiciones: La funci�n devuelve devuelve la apuesta y el dinero del tipo 2
*/
CREATE PROCEDURE [dbo].[consultarApuestaTipo2](@IdPartido int)
AS
BEGIN
	SELECT A.DineroApostado, A1.equipo, A1.goles FROM Apuestas AS[A]
		INNER JOIN ApuestaTipo2 AS[A1] ON A.ID = A1.id
		WHERE IDPartido = @IdPartido
	ORDER BY goles, Tipo
END
GO
-----
/*
Interfaz
Nombre: consultarApuestaTipo3
Comentario: Este m�todo nos devuelve la apuesta y el dinero del tipo 3
Cabecera:  PROCEDURE consultarApuestaTipo3(@IdPartido int)
Salida:
	-Cursor listadoCompeticiones
Postcondiciones: La funci�n devuelve la apuesta y el dinero del tipo 3
*/
CREATE PROCEDURE [dbo].[consultarApuestaTipo3](@IdPartido int)
AS
BEGIN
	SELECT A.DineroApostado, A1.Ganador FROM Apuestas AS[A]
		INNER JOIN ApuestaTipo3 AS[A1] ON A.ID = A1.id
		WHERE IDPartido = @IdPartido
	ORDER BY ganador, Tipo
END
GO
-----
/*
Interfaz
Nombre: contabilizarApuestasNoContabilizadas
Comentario: Este m�todo nos permite contabilizar todas las apuestas que se hayan realizado a un partido.
Solo se contabilizar� las apuestas que no hayan sido marcadas como contabilizada.
Cabecera: procedure contabilizarApuestasNoContabilizadas(@idPartido int)
Entrada:
	-@idPartido int
Postcondiciones: El m�todo contabiliza todas las apuestas no marcadas de un partido espec�fico.
*/
CREATE PROCEDURE [dbo].[contabilizarApuestasNoContabilizadas](@idPartido int)
AS
BEGIN
	DECLARE @IdApuesta int
	DECLARE puntero CURSOR FOR SELECT ID FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0
	OPEN puntero
	FETCH NEXT FROM puntero INTO @IdApuesta --Nos permite apuntar al primer dato del cursor

	WHILE (@@FETCH_STATUS = 0) --Mientras a�n haya filas
	BEGIN
		UPDATE Apuestas SET Contabilizada = 1 WHERE ID = @IdApuesta AND IsGanador = 1--Ya existe un trigger que ingresa el beneficio de la apuesta que hayan sido victoriosas
		FETCH NEXT FROM puntero INTO @IdApuesta
	END

	CLOSE puntero
	DEALLOCATE puntero --Liberamos los recursos del puntero
END
GO
-------
GO
/*
Interfaz
Nombre: ingresoACuenta
Comentario: Este m�todo nos permite ingresar un capital a la cuenta de un usuario.
Cabecera: procedure ingresoACuenta (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso smallint OUTPUT)
Entrada:
	-@CorreoUsuario varchar(30)
	-@ingreso smallmoney
Salida:
	-@resultadoIngreso smallint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar el ingreso, -1
si el correo es incorrecto o -2 si el ingreso es negativo o igual a 0.
*/
CREATE PROCEDURE [dbo].[ingresoACuenta] (@CorreoUsuario varchar(30), @ingreso smallmoney, @resultadoIngreso smallint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @CorreoUsuario)
	BEGIN
		IF @ingreso > 0
		BEGIN
			--Un usuario puede tener m�s de un id de cuenta??????? Se podr�a haber creado otra tabla RegistrosCuentas
			--DECLARE @IDCuenta int = (SELECT MAX(id) FROM Cuentas WHERE correoUsuario = @CorreoUsuario)--Obtenemos el id de la cuenta del usuario
			--DECLARE @SaldoActual smallmoney = (SELECT saldoActual FROM Usuarios WHERE correo = @CorreoUsuario)
			--INSERT INTO Cuentas (saldo, fechaYHora, correoUsuario) VALUES ((@SaldoActual + @ingreso), CURRENT_TIMESTAMP, @CorreoUsuario)
			UPDATE Usuarios
				SET saldoActual += @ingreso
			WHERE correo = @CorreoUsuario
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
------
/*
Interfaz
Nombre: insertarPartido
Comentario: Este m�todo nos permite insertar un partido en la base de datos.
Cabecera: procedure insertarPartido(@maximoTipo1 smallmoney, @maximoTipo2 smallmoney, @maximoTipo3 smallmoney, @fechaPartido smalldatetime)
Entrada:
	-@maximoTipo1 smallmoney
	-@maximoTipo2 smallmoney
	-@maximoTipo3 smallmoney
	-@fechaPartido smalldatetime
Postcondiciones: El m�todo inserta un partido en la base de datos.
*/
CREATE PROCEDURE [dbo].[insertarPartido](@maximoTipo1 smallmoney, @maximoTipo2 smallmoney, @maximoTipo3 smallmoney, @fechaPartido smalldatetime)
AS
BEGIN
	INSERT INTO Partidos (isAbierto,maxApuesta1, maxApuesta2, maxApuesta3, fechaPartido, idCompeticion) VALUES (0,@maximoTipo1, @maximoTipo2, @maximoTipo3, @fechaPartido, 1)
END
GO
-------
GO
/*
Interfaz
Nombre: partidosConApuestasSinContabilizar
Comentario: Esta funci�n nos devuelve todos los partidos que tienen apuestas sin
contabilizar.
Cabecera: function partidosConApuestasSinContabilizar()
Salida:
	-Tabla de id's de los partidos con apuestas no contabilizadas
*/
create procedure [dbo].[partidosConApuestasSinContabilizar2]
AS
	SELECT IDPartido FROM (
	SELECT IDPartido, COUNT(*) AS [Apuestas Sin Contabilizar] FROM Apuestas WHERE Contabilizada = 0
		GROUP BY IDPartido) AS [C1] WHERE [Apuestas Sin Contabilizar] > 0
GO
-------
GO
/*
Nombre: realizarApuestaTipo1
Comentario: Este procedimiento nos permite realizar una apuesta del tipo 1.
Dentro de este procedimiento se llama a la funci�n obtenerCuota.
Cabecera: procedure realizarApuesta(@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint, @Error smallint)
Entradas:
	-@CapitalAApostar smallmoney
	-@Correo char(30)
	-@IdPartido int
	-@NumGolesLocal tinyint
	-@NumGolesVisitante tinyint
Salidas:
	-@Error smallint
Postcondiciones: El procedimiento nos permite realizar una apuesta del tipo 1. El procedimiento
devuelve 0 si se ha realizado correctamente la apuesta, -1 si el correo es incorrecto, -2
si el partido no existe, -3 si el capital es negativo o igual a 0, -4 si el
n�mero de goles locales en menor que 0 , -5 si el n�mero de goles del visitante es menor que 0 o -6 si la cuota es menor que 1,5.
*/
CREATE PROCEDURE [dbo].[realizarApuestaTipo1](@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @NumGolesLocal smallint, @NumGolesVisitante smallint, @Error smallint OUTPUT)
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
						DECLARE @Cuota decimal(5,2) = dbo.obtenerCuotaTipo1(@IdPartido, @CapitalAApostar, @NumGolesLocal, @NumGolesVisitante)
							IF @Cuota >= 1.5
							BEGIN
								DECLARE @FechaActual smalldatetime = CURRENT_TIMESTAMP
								INSERT INTO Apuestas VALUES(@Cuota, @FechaActual, @CapitalAApostar, @Correo, @IdPartido, 1, 0,0)  
								INSERT INTO ApuestaTipo1 VALUES((SELECT ID FROM Apuestas WHERE IDPartido = @IdPartido AND CorreoUsuario = @Correo AND FechaHoraApuesta = @FechaActual), @NumGolesLocal,@NumGolesVisitante)
								SET @Error = 0
							END
							ELSE
							BEGIN
								SET @Error = -6
							END
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
------
GO
/*
Nombre: realizarApuestaTipo2
Comentario: Este procedimiento nos permite realizar una apuesta del tipo 2.
Dentro de este procedimiento se llama a la funci�n obtenerCuota.
Cabecera: procedure realizarApuesta(@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint, @Error smallint)
Entradas:
	-@CapitalAApostar smallmoney
	-@Correo char(30)
	-@Equipo varchar(10) 
	-@Goles tinyint
	-@IdPartido int
Salidas:
	-@Error smallint
Postcondiciones: El procedimiento nos permite realizar una apuesta del tipo 2. El procedimiento
devuelve 0 si se ha realizado correctamente la apuesta, -1 si el correo es incorrecto, -2
si el partido no existe, -3 si el capital es negativo o igual a 0, -4 si el
equipo no es igual a 'local' o 'visitante' , -5 si el n�mero de goles es menor que 0 o -6 si la cuota es menor que 1,5.
*/
CREATE PROCEDURE [dbo].[realizarApuestaTipo2](@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @Equipo varchar(10), @Goles smallint, @Error smallint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @Correo)
	BEGIN
		IF EXISTS(SELECT * FROM Partidos WHERE id = @IdPartido)
		BEGIN
			IF @CapitalAApostar > 0
			BEGIN
				IF @Equipo = 'local' OR @Equipo = 'visitante'
				BEGIN
					IF @Goles >= 0
					BEGIN
						DECLARE @Cuota decimal(5,2) = dbo.obtenerCuotaTipo2(@IdPartido, @CapitalAApostar, @Equipo, @Goles)
						IF @Cuota > 1.5
						BEGIN
							DECLARE @FechaActual smalldatetime = CURRENT_TIMESTAMP
							INSERT INTO Apuestas VALUES(@Cuota, @FechaActual, @CapitalAApostar, @Correo, @IdPartido, 2, 0,0)  
							INSERT INTO ApuestaTipo2 VALUES((SELECT ID FROM Apuestas WHERE IDPartido = @IdPartido AND CorreoUsuario = @Correo AND FechaHoraApuesta = @FechaActual), @Equipo,@Goles)
							SET @Error = -0
						END
						ELSE
						BEGIN
						 SET @Error = -6
						END
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
--------
GO
/*
Nombre: realizarApuestaTipo3
Comentario: Este procedimiento nos permite realizar una apuesta del tipo 3.
Dentro de este procedimiento se llama a la funci�n obtenerCuota.
Cabecera: procedure realizarApuesta(@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @NumGolesLocal tinyint, @NumGolesVisitante tinyint, @Error smallint)
Entradas:
	-@CapitalAApostar smallmoney
	-@Correo char(30)
	-@IdPartido int
	-@Equipo varchar(10) 
	-@Ganador varchar(15)
Salidas:
	-@Error smallint
Postcondiciones: El procedimiento nos permite realizar una apuesta del tipo 2. El procedimiento
devuelve 0 si se ha realizado correctamente la apuesta, -1 si el correo es incorrecto, -2
si el partido no existe, -3 si el capital es negativo o igual a 0, -4 si el
el ganador es diferente de 'local' o 'visitante', -5 si la cuota es menor que 1,5.
*/
CREATE PROCEDURE [dbo].[realizarApuestaTipo3](@CapitalAApostar smallmoney, @Correo char(30), @IdPartido int, @Ganador varchar(15), @Error smallint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @Correo)
	BEGIN
		IF EXISTS(SELECT * FROM Partidos WHERE id = @IdPartido)
		BEGIN
			IF @CapitalAApostar > 0
			BEGIN
				IF @Ganador = 'local' OR @Ganador = 'visitante'
				BEGIN
					DECLARE @Cuota decimal = dbo.obtenerCuotaTipo3(@IdPartido, @CapitalAApostar, @Ganador)
					IF @Cuota > 1.5
					BEGIN
						DECLARE @FechaActual smalldatetime = CURRENT_TIMESTAMP
						INSERT INTO Apuestas VALUES(@Cuota, @FechaActual, @CapitalAApostar, @Correo, @IdPartido, 3, 0, 0)  
						INSERT INTO ApuestaTipo3 VALUES((SELECT ID FROM Apuestas WHERE IDPartido = @IdPartido AND CorreoUsuario = @Correo AND FechaHoraApuesta = @FechaActual), @Ganador)
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
-----------
GO
/*
Interfaz
Nombre: retirarCapitalCuenta
Comentario: Este m�todo nos permite retirar capital de una cuenta de usuario.
Cabecera: procedure retirarCapitalCuenta (@CorreoUsuario varchar(30), @capitalARetirar smallmoney, @resultadoTransaccion tinyint OUTPUT)
Entrada:
	-@CorreoUsuario varchar(30)
	-@capitalARetirar smallmoney
Salida:
	-@resultadoTransaccion smallint
Postcondiciones: El procedimiento devuelve 0 si se ha conseguido realizar la transacci�n, -1
si el correo es incorrecto, -2 si el capitalARetirar es negativo o igual a 0 o -3 si capitalARetirar es superior al saldo del usuario.
*/
CREATE PROCEDURE [dbo].[retirarCapitalCuenta] (@CorreoUsuario varchar(30), @capitalARetirar smallmoney, @resultadoIngreso smallint OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM Usuarios WHERE correo = @CorreoUsuario)
	BEGIN
		IF @capitalARetirar > 0
		BEGIN
			IF @capitalARetirar <= (SELECT saldoActual FROM Usuarios WHERE correo = @CorreoUsuario)
			BEGIN	
				UPDATE Usuarios
					SET saldoActual -= @capitalARetirar
				WHERE correo = @CorreoUsuario
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
--------
GO
	CREATE 
	PROCEDURE [dbo].[sacarDinero] (@correoUsuario char(30), @cantidadASacar int)
	AS
	BEGIN
		UPDATE usuarios 
		SET saldoActual = saldoActual - @cantidadASacar
		WHERE correo = @correoUsuario
	END
GO