/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 3,'running-example-non-conforming','2012-01-24 14:57:00.0000000','2011-12-30 11:02:00.000000','Process3',8,10,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;