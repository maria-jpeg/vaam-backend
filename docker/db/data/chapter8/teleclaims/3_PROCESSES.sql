SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 5,'teleclaims','1970-01-01 12:10:40.0000000','1970-01-01 00:00:00','Process5',11,3512,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;