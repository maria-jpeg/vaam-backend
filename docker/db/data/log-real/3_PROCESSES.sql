/*PROCESSES*/
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 10,'Processo Real','2021-02-11 15:11:58.5800000','2020-07-03 08:05:10.1000000','Process10',16,114,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;