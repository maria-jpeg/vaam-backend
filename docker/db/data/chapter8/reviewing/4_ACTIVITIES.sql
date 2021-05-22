SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 9,'invite reviewers',NULL,NULL UNION ALL
select 10,'get review 2',NULL,NULL UNION ALL
select 11,'get review 3',NULL,NULL UNION ALL
select 12,'get review 1',NULL,NULL UNION ALL
select 13,'collect reviews',NULL,NULL UNION ALL
select 14,'invite additional reviewer',NULL,NULL UNION ALL
select 15,'get review X',NULL,NULL UNION ALL
select 16,'reject',NULL,NULL UNION ALL
select 17,'time-out 1',NULL,NULL UNION ALL
select 18,'time-out X',NULL,NULL UNION ALL
select 19,'accept',NULL,NULL UNION ALL
select 20,'time-out 2',NULL,NULL UNION ALL
select 21,'time-out 3',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;