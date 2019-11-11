CREATE FUNCTION anadirPartido(@id int,@abierto bit, @max1 int, @max2 int, @max3 int, @fecha smalldatetime, @idComp uniqueidentifier)
BEGIN
	insert into Partidos(id, isAbierto, maxApuesta1, maxApuesta2, maxApuesta3,fechaPartido,idCompeticion)
	VALUES(@id)
END
