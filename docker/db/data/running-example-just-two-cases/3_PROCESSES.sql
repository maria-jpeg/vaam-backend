/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
/*running-example*/
select 2,'running-example-just-two-cases',NULL,NULL,'Process2',1,2,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;