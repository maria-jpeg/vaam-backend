SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 6,'repairExample','1970-01-24 06:25:00.0000000','1970-01-01 10:09:00.0000000','Process6',8,1104,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;