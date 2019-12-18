use ApuestasDeportivas

/*Funcion escalar para consultar lo que gana un usuario con una apuesta en caso de que acierte
Se calcula tal que así:
	ganancia = cantidadApostada x cuota
*/

/*go
--ALTER
CREATE 
FUNCTION 
GananciaDeUnUsuarioConUnaApuesta (@cuota int, @cantidadApostada int)
	RETURNS INT AS
	BEGIN 
	RETURN (@CUOTA * @CANTIDADAPOSTADA)
	END
	
go

SELECT dbo.GananciaDeUnUsuarioConUnaApuesta(4,3) as prueba
*/
-----------------

/*Trigger  que comprueba si el usuario es ganador o perdedor y actualiza su saldo*/
go
--ALTER
CREATE 
TRIGGER TR_esGanador 
	ON Partidos 
	after update
	/*Hay que hacerlo sobre Partidos, porque Apuestas no se puede actualizar y al crearla no se sabe si ha ganado o no*/
	AS 
	begin
		if update (resultadoLocal) and update (resultadoVisitante) 
				begin		
					UPDATE usuarios 
						set saldoActual += (A.cuota * A.dineroApostado)
						from Apuestas as A						
						inner join inserted as I
						on I.id = A.IDPartido
						where A.IsGanador = 1
						and correo = A.CorreoUsuario
				end
	end
GO
--poner que este trigger se ejecute el último
sp_settriggerorder @triggername='TR_esGanador' , @order='Last', @stmttype = 'UPDATE'
go
	---------------

	/*Procedimiento que recibe correo del usuario y una nueva cantidad para que el usuario pueda añadir más dinero*/
	--ALTER
	CREATE 
	PROCEDURE anadirMasDinero (@correoUsuario char(30), @cantidadNueva int)
	AS
	BEGIN
		UPDATE usuarios 
		SET saldoActual = saldoActual + @cantidadNueva 
		WHERE correo = @correoUsuario
	END

	exec dbo.anadirMasDinero @correoUsuario = 'angelavazquez@gmail.com', @cantidadNueva = 50

	select * from Usuarios
	select * from Cuentas


	go

	---------------------
	/*Procedimiento para sacar dinero*/

	--	ALTER
	CREATE 
	PROCEDURE sacarDinero (@correoUsuario char(30), @cantidadASacar int)
	AS
	BEGIN
		UPDATE usuarios 
		SET saldoActual = saldoActual - @cantidadASacar
		WHERE correo = @correoUsuario
	END