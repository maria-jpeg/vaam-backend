SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 4,'reviewing','2010-07-18 00:00:00.0000000','2006-01-03 23:00:00.0000000','Process4',13,100,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;