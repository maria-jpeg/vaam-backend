/*ACTIVITIES_USERS*/
insert INTO ncfinderdb.dbo.ACTIVITIES_USERS ([username],[activityId],[startDate],[endDate],[workstationId])
/*running-example*/
select 'pete',1,'2011-03-30 11:02:00.000000', '2011-03-30 11:02:00.000000',1 UNION ALL
select 'sue',2,'2011-03-31 10:06:00.000000', '2011-03-31 10:06:00.000000',2 UNION ALL
select 'mike',3,'2011-04-05 15:12:00.000000', '2011-04-05 15:12:00.000000',3 UNION ALL
select 'sara',4,'2011-04-06 11:18:00.000000', '2011-04-06 11:18:00.000000',4 UNION ALL
select 'pete',5,'2011-04-07 14:24:00.000000', '2011-04-07 14:24:00.000000',5 UNION ALL

select 'mike',1,'2011-05-30 11:32:00.000000', '2011-05-30 11:32:00.000000',1 UNION ALL
select 'mike',3,'2011-05-30 12:12:00.000000', '2011-05-30 12:12:00.000000',3 UNION ALL
select 'sean',6,'2011-05-30 14:16:00.000000', '2011-05-30 14:16:00.000000',6 UNION ALL
select 'sara',4,'2011-06-05 11:22:00.000000', '2011-06-05 11:22:00.000000',4 UNION ALL
select 'ellen',7,'2011-06-08 12:05:00.000000', '2011-06-08 12:05:00.000000',7;