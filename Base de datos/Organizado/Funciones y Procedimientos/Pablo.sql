/*
Procedimiento almacenado que permite añadir un nuevo partido a la base de datos.
*/
GO
CREATE PROCEDURE anadirPartido(@id int,@abierto bit, @max1 int, @max2 int, @max3 int, @fecha smalldatetime, @idComp uniqueidentifier) AS
BEGIN
	insert into Partidos(id, isAbierto, maxApuesta1, maxApuesta2, maxApuesta3,fechaPartido,idCompeticion)
	VALUES(@id,@abierto, @max1, @max2, @max3, @fecha, @idComp)
END
GO

/*
Abre un partido para que se pueda apostar en el.
*/
GO
CREATE PROCEDURE abrirPartido(@idPartido INT) AS
BEGIN
	UPDATE Partidos
	SET isAbierto = 1 --Creo que asi es abierto.
	WHERE id = @idpartido
END
GO

/*
Cierra un partido para que no se pueda apostar en el.
*/
GO
CREATE PROCEDURE cerrarPartido(@idPartido INT) AS
BEGIN
	UPDATE Partidos
	SET isAbierto = 0 --Creo que asi es abierto.
	WHERE id = @idpartido
END
GO

SELECT * FROM Partidos