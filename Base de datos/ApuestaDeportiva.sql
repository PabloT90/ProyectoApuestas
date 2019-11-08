create 
database ApuestasDeportivas
go
use ApuestasDeportivas
go

CREATE TABLE Usuarios
(
	correo CHAR(30) not null,
	contraseña VARCHAR(10) NOT NULL,
	saldoActual INT NOT NULL,	

	----------------PK-------------------------
	constraint PK_Usuarios PRIMARY KEY (correo)
);
go

CREATE TABLE Competiciones
(
	id UNIQUEIDENTIFIER not null,
	nombre VARCHAR(20) NOT NULL,
	año INT,

	---------------pk------------------------------------
	constraint PK_Competiciones PRIMARY KEY (id),
);
go

CREATE TABLE Partidos
(
	id UNIQUEIDENTIFIER not null,
	resultadoLocal TINYINT null,
	resultadoVisitante TINYINT null,
	isAbierto BIT null,
	maxApuesta1 INT NOT NULL,
	maxApuesta2 INT NOT NULL,
	maxApuesta3 INT NOT NULL,
	fechaPartido SMALLDATETIME null,
	idCompeticion uniqueidentifier not null,

	---------------pk------------------------------------
	constraint PK_Partidos PRIMARY KEY (id),

	----------------------------fk-----------------------------------------------
	CONSTRAINT FK_Competiciones_Partidos FOREIGN KEY (idCompeticion) REFERENCES competiciones (id)
);
go


CREATE TABLE Apuestas (
	ID UNIQUEIDENTIFIER NOT NULL,
	Cuota TINYINT NOT NULL,
	FechaHoraApuesta SMALLDATETIME NOT NULL,
	DineroApostado SMALLMONEY NOT NULL,
	CorreoUsuario CHAR(30) NOT NULL,
	IDPartido UNIQUEIDENTIFIER NOT NULL,
	Tipo TINYINT NOT NULL,
	IsGanador bit NULL,--1 si es ganador y 0 si no lo es

	CONSTRAINT PK_APUESTA PRIMARY KEY (ID),
	CONSTRAINT FK_APUESTA_USUARIO FOREIGN KEY (CORREOUSUARIO) REFERENCES USUARIOS(CORREO) 
	ON DELETE NO ACTION ON UPDATE NO ACTION,	--NO SE PUEDEN NI ACTUALIZAR NI BORRAR

	CONSTRAINT FK_APUESTA_PARTIDO FOREIGN KEY(IDPARTIDO) REFERENCES PARTIDOS(ID)
	ON DELETE NO ACTION ON UPDATE NO ACTION
)




CREATE TABLE ApuestaTipo1
(
	id UNIQUEIDENTIFIER not null,
	NumGolesLocal TINYINT NOT NULL,
	numGolesVisitante TINYINT NOT NULL,

	---------------pk------------------------------------
	constraint PK_ApuestaTipo1 PRIMARY KEY (id),

	----------------------------fk-----------------------------------------------
	CONSTRAINT FK_APUESTA_APUESTATIPO1 FOREIGN KEY (ID) REFERENCES APUESTAS(ID)
	on update cascade on delete cascade
	
);
go

CREATE TABLE ApuestaTipo2
(	
	id UNIQUEIDENTIFIER not null,
	equipo VARCHAR(10) NOT NULL,
	goles TINYINT NOT NULL,

	---------------pk------------------------------------
	constraint PK_ApuestaTipo2 PRIMARY KEY (id),

	----------------------------fk-----------------------------------------------
	CONSTRAINT FK_APUESTA_APUESTATIPO2 FOREIGN KEY (ID) REFERENCES APUESTAS(ID)
	on update cascade on delete cascade
);
go

CREATE TABLE ApuestaTipo3
(
	id UNIQUEIDENTIFIER not null,
	ganador VARCHAR(15) NOT NULL,

	---------------pk------------------------------------
	constraint PK_ApuestaTipo3 PRIMARY KEY (id),
	----------------------------fk-----------------------------------------------
	CONSTRAINT FK_APUESTA_APUESTATIPO3 FOREIGN KEY (ID) REFERENCES APUESTAS(ID)
	on update cascade on delete cascade
);
go

CREATE TABLE Cuentas
(
	id INT IDENTITY(1,1) not null,
	saldo INT NOT NULL,
	fechaYHora SMALLDATETIME NOT NULL,
	correoUsuario CHAR(30),

	-----------------------PK-------------------------------
	constraint PK_Cuentas PRIMARY KEY (id),
	-----------------------FK--------------------------------------------
	CONSTRAINT FK_Usuarios_Cuentas FOREIGN KEY (correoUsuario) REFERENCES usuarios (correo) on update cascade on delete cascade
);

------------------chechs------------------------------------
alter table Usuarios add constraint CK_Email check (correo like '%@%.%')
go
alter table Usuarios add constraint CK_SaldoActual check (SaldoActual >=0)
go

--ganador debe ser empate, local o visitante
alter table ApuestaTipo3 add constraint CK_Ganador check (ganador in('empate','local', 'visitante'))
--begin transaction

--insert into Usuarios values 
--('nzhdeh@gmail','89999',55)
--go
--insert into Usuarios values 
--('nzhdehgmail.com','89999',55)
--go
--insert into Usuarios values 
--('nzhdeh@gmail.com         ','89999',55)
--go
--insert into Usuarios values 
--('nzhdeh@gmail.com         ','89999',-5)
--go
--insert into Usuarios values 
--('nzhdeh@gmail.com         ','89999',55)
--go
--rollback

--select * from Usuarios
