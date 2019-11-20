USE PruebasPablo
GO

Begin tran

insert into Competiciones values('Copa del Rey', 2019)

insert into Competiciones values('Copa autoestima', 2019)

SELECT * FROM Competiciones

insert into Partidos values(4,5,1,50,30,35,'2019-3-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(3,1,0,40,15,40,'2019-9-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))
insert into Partidos values(4,5,1,20,30,60,'2019-25-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))
insert into Partidos values(1,6,0,35,20,50,'2019-10-12 11:00:00',(Select id From Competiciones Where nombre = 'Copa autoestima'))

SELECT * FROM Partidos
insert into Partidos values(1,2,0,35,20,50,'2019-10-11 11:00:00',(Select id From Competiciones Where nombre = 'Copa del Rey'))

INSERT INTO Usuarios VALUES('sulviagurdilloabcdef@gmail.com','sul07',500)

INSERT INTO Usuarios VALUES('decisionesdificile@gmail.com','dc19',1000)
SELECT * FROM Usuarios

Insert into Apuestas values(5.2,'2019-9-11 10:01:00',5.5,'sulviagurdilloabcdef@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 1),3,1)

INSERT INTO Apuestas VALUES(2.8,'2019-10-11 10:00:00', 2.9,'decisionesdificile@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 2),2,1)

INSERT INTO Apuestas VALUES(4.2,'2019-9-11 10:01:00',3.5,'sulviagurdilloabcdef@gmail.com',(Select id From Partidos WHERE resultadoVisitante = 2),1,1)

SELECT * FROM Apuestas

INSERT INTO ApuestaTipo1 VALUES((SELECT id FROM Apuestas Where Tipo = 1),4,5)

INSERT INTO ApuestaTipo2 VALUES((SELECT id FROM Apuestas Where Tipo = 2),'Visitante',2)

INSERT INTO ApuestaTipo3 VALUES((SELECT id FROM Apuestas Where Tipo = 3),'visitante')

SELECT * FROM ApuestaTipo1
SELECT * FROM ApuestaTipo2
SELECT * FROM ApuestaTipo3

COMMIT
ROLLBACK

