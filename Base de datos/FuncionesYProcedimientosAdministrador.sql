--Tareas de administrador

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
CREATE PROCEDURE addPartido(@resultadoLocal tinyint, @resultadoVisitante tinyint, @isAbierto bit, @MaxApuesta1 int, @MaxApuesta2 int, @MaxApuesta3 int, @FechaPartido smalldatetime, @IdCompeticion int, @Error smallint OUTPUT) AS
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
CREATE PROCEDURE abrirPartido(@IdPartido int, @FilasModificadas tinyint OUTPUT) AS
BEGIN
	UPDATE Partidos 
		SET isAbierto = 0
	WHERE id = @IdPartido
	SET @FilasModificadas = @@ROWCOUNT	--Nos devuelve el n�mero de filas afectadas en la transacci�n anterior
END
GO

GO
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
CREATE FUNCTION obtenerApuestasNoContabilizadas(@idPartido int)
RETURNS TABLE
AS
RETURN
    SELECT * FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0 --0 significa que no ha sido contabilizada
GO

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
CREATE PROCEDURE contabilizarApuestasNoContabilizadas(@idPartido int)
AS
BEGIN
	DECLARE @IdApuesta int
	DECLARE puntero CURSOR FOR SELECT ID, Contabilizada FROM Apuestas WHERE IDPartido = @idPartido AND Contabilizada = 0
	OPEN puntero
	FETCH NEXT FROM puntero INTO @IdApuesta --Nos permite apuntar al primer dato del cursor

	WHILE (@@FETCH_STATUS = 0) --Mientras a�n haya filas
	BEGIN
		UPDATE Apuestas SET Contabilizada = 1 WHERE ID = @IdApuesta--Ya existe un trigger que ingresa el beneficio de la apuesta que hayan sido victoriosas
		FETCH NEXT FROM puntero INTO @IdApuesta
	END

	--DEALLOCATE puntero --Liberamos los recursos del puntero
	CLOSE puntero
END

GO
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
CREATE PROCEDURE insertarPartido(@maximoTipo1 smallmoney, @maximoTipo2 smallmoney, @maximoTipo3 smallmoney, @fechaPartido smalldatetime)
AS
BEGIN
	INSERT INTO Partidos (maxApuesta1, maxApuesta2, maxApuesta3, fechaPartido) VALUES (@maximoTipo1, @maximoTipo2, @maximoTipo3, @fechaPartido)
END
GO