/*WORKSTATIONS*/
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 1,'Estação de register request',1,NULL,NULL UNION ALL
select 2,'Estação de examine thoroughly',2,NULL,NULL UNION ALL
select 3,'Estação de check ticket',3,NULL,NULL UNION ALL
select 4,'Estação de decide',4,NULL,NULL UNION ALL
select 5,'Estação de reject request',5,NULL,NULL UNION ALL
select 6,'Estação de examine casually',6,NULL,NULL UNION ALL
select 7,'Estação de pay compensation',7,NULL,NULL UNION ALL
select 8,'Estação de reinitiate request',8,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;