/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 1,'running-example','2011-01-24 14:57:00.0000000','2010-12-30 11:02:00.000000','Process1',8,6,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;