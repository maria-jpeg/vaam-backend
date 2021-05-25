SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 33,'Estação de Register',1,NULL,NULL UNION ALL
select 34,'Estação de Analyze Defect',1,NULL,NULL UNION ALL
select 35,'Estação de Repair (Complex)',1,NULL,NULL UNION ALL
select 36,'Estação de Test Repair',1,NULL,NULL UNION ALL
select 37,'Estação de Inform User',1,NULL,NULL UNION ALL
select 38,'Estação de Archive Repair',1,NULL,NULL UNION ALL
select 39,'Estação de Repair (Simple)',1,NULL,NULL UNION ALL
select 40,'Estação de Restart Repair',1,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;