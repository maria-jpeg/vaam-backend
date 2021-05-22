SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 4,'reviewing',NULL,NULL,'Process4',8,10,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;