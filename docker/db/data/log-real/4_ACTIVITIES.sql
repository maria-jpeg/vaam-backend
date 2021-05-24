/*ACTIVITIES*/
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 1001,'trabalho interno',NULL,NULL UNION ALL
select 1002,'bancada',NULL,NULL UNION ALL
select 1003,'retificação',NULL,NULL UNION ALL
select 1004,'fresagem',NULL,NULL UNION ALL
select 1005,'torno',NULL,NULL UNION ALL
select 1006,'preliminares',NULL,NULL UNION ALL
select 1007,'estudos reológicos',NULL,NULL UNION ALL
select 1008,'maquinação de eléctrodos',NULL,NULL UNION ALL
select 1009,'erosão',NULL,NULL UNION ALL
select 1010,'polimento',NULL,NULL UNION ALL
select 1011,'furação',NULL,NULL UNION ALL
select 1012,'prensa',NULL,NULL UNION ALL
select 1013,'estrutura',NULL,NULL UNION ALL
select 1014,'não conformidade',NULL,NULL UNION ALL
select 1015,'manutenção',NULL,NULL UNION ALL
select 1016,'peças',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;