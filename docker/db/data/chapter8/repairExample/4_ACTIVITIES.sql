SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 33,'Register',NULL,NULL UNION ALL
select 34,'Analyze Defect',NULL,NULL UNION ALL
select 35,'Repair (Complex)',NULL,NULL UNION ALL
select 36,'Test Repair',NULL,NULL UNION ALL
select 37,'Inform User',NULL,NULL UNION ALL
select 38,'Archive Repair',NULL,NULL UNION ALL
select 39,'Repair (Simple)',NULL,NULL UNION ALL
select 40,'Restart Repair',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;