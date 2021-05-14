/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 1,'running-example',NULL,NULL,'Process1',8,6,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;