/*WORKSTATIONS*/
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 1,'Estação de maquinação de eléctrodos',1,NULL,NULL UNION ALL
select 2,'Estação de maquinação',2,NULL,NULL UNION ALL
select 3,'Estação de fresagem',3,NULL,NULL UNION ALL
select 4,'Estação de retificação',4,NULL,NULL UNION ALL
select 5,'Estação de torno',5,NULL,NULL UNION ALL
select 6,'Estação de erosão',6,NULL,NULL UNION ALL
select 7,'Estação de bancada',7,NULL,NULL UNION ALL
select 8,'Estação de polimento',8,NULL,NULL UNION ALL
select 9,'Estação de furação',9,NULL,NULL UNION ALL
select 10,'Estação de fio',10,NULL,NULL UNION ALL
select 11,'Estação de preliminares',11,NULL,NULL UNION ALL
select 12,'Estação de trabalho interno',12,NULL,NULL UNION ALL
select 13,'Estação de estrutura',13,NULL,NULL UNION ALL
select 14,'Estação de não conformidade',14,NULL,NULL UNION ALL
select 15,'Estação de desenho elétrodos',15,NULL,NULL UNION ALL
select 16,'Estação de cam',16,NULL,NULL UNION ALL
select 17,'Estação de prensa',17,NULL,NULL UNION ALL
select 18,'Estação de peças',18,NULL,NULL UNION ALL
select 19,'Estação de estudos reológicos',19,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;