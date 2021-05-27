/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 2,'running-example-just-two-cases','2011-06-08 12:06:00.000000','2011-03-30 11:02:00.000000','Process2',8,2,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;