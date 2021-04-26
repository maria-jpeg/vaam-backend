/*ACTIVITIES*/
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 1,'maquinação de eléctrodos',NULL,NULL UNION ALL
select 2,'maquinação',NULL,NULL UNION ALL
select 3,'fresagem',NULL,NULL UNION ALL
select 4,'retificação',NULL,NULL UNION ALL
select 5,'torno',NULL,NULL UNION ALL
select 6,'erosão',NULL,NULL UNION ALL
select 7,'bancada',NULL,NULL UNION ALL
select 8,'polimento',NULL,NULL UNION ALL
select 9,'furação',NULL,NULL UNION ALL
select 10,'fio',NULL,NULL UNION ALL
select 11,'preliminares',NULL,NULL UNION ALL
select 12,'trabalho interno',NULL,NULL UNION ALL
select 13,'estrutura',NULL,NULL UNION ALL
select 14,'não conformidade',NULL,NULL UNION ALL
select 15,'desenho elétrodos',NULL,NULL UNION ALL
select 16,'cam',NULL,NULL UNION ALL
select 17,'prensa',NULL,NULL UNION ALL
select 18,'peças',NULL,NULL UNION ALL
select 19,'estudos reológicos',NULL,NULL
    SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;