/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 3,'running-example-non-conforming',NULL,NULL,'Process3',8,10,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;