/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 100,'Processo 100',NULL,NULL,'Process100',1,2,NULL UNION ALL
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;