/*WORKSTATIONS*/
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 1001,'Estação de trabalho interno',1001,NULL,NULL UNION ALL
select 1002,'Estação de bancada',1002,NULL,NULL UNION ALL
select 1003,'Estação de retificação',1003,NULL,NULL UNION ALL
select 1004,'Estação de fresagem',1004,NULL,NULL UNION ALL
select 1005,'Estação de torno',1005,NULL,NULL UNION ALL
select 1006,'Estação de preliminares',1006,NULL,NULL UNION ALL
select 1007,'Estação de estudos reológicos',1007,NULL,NULL UNION ALL
select 1008,'Estação de maquinação de eléctrodos',1008,NULL,NULL UNION ALL
select 1009,'Estação de erosão',1009,NULL,NULL UNION ALL
select 1010,'Estação de polimento',1010,NULL,NULL UNION ALL
select 1011,'Estação de furação',1011,NULL,NULL UNION ALL
select 1012,'Estação de prensa',1012,NULL,NULL UNION ALL
select 1013,'Estação de estrutura',1013,NULL,NULL UNION ALL
select 1014,'Estação de não conformidade',1014,NULL,NULL UNION ALL
select 1015,'Estação de manutenção',1015,NULL,NULL UNION ALL
select 1016,'Estação de peças',1016,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;