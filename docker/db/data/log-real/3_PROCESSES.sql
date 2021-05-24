/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 10,'Processo Real',NULL,NULL,'Process10',16,114,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;