SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 9,'Estação de invite reviewers',1,NULL,NULL UNION ALL
select 10,'Estação de get review 2',1,NULL,NULL UNION ALL
select 11,'Estação de get review 3',1,NULL,NULL UNION ALL
select 12,'Estação de get review 1',1,NULL,NULL UNION ALL
select 13,'Estação de collect reviews',1,NULL,NULL UNION ALL
select 14,'Estação de invite additional reviewer',1,NULL,NULL UNION ALL
select 15,'Estação de get review X',1,NULL,NULL UNION ALL
select 16,'Estação de reject',1,NULL,NULL UNION ALL
select 17,'Estação de time-out 1',1,NULL,NULL UNION ALL
select 18,'Estação de time-out X',1,NULL,NULL UNION ALL
select 19,'Estação de accept',1,NULL,NULL UNION ALL
select 20,'Estação de time-out 2',1,NULL,NULL UNION ALL
select 21,'Estação de time-out 3',1,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;