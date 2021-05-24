SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 5,'teleclaims',NULL,NULL,'Process5',11,3512,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;