SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 6,'repairExample',NULL,NULL,'Process6',8,1104,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;