/*ACTIVITIES*/
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
/*running example*/
select 1,'register request',NULL,NULL UNION ALL
select 2,'examine thoroughly',NULL,NULL UNION ALL
select 3,'check ticket',NULL,NULL UNION ALL
select 4,'decide',NULL,NULL UNION ALL
select 5,'reject request',NULL,NULL UNION ALL
select 6,'examine casually',NULL,NULL UNION ALL
select 7,'pay compensation',NULL,NULL UNION ALL
select 8,'reinitiate request',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;