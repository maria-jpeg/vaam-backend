SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 22,'incoming claim',NULL,NULL UNION ALL
select 23,'B check if sufficient information is available',NULL,NULL UNION ALL
select 24,'B register claim',NULL,NULL UNION ALL
select 25,'determine likelihood of claim',NULL,NULL UNION ALL
select 26,'end',NULL,NULL UNION ALL
select 27,'S check if sufficient information is available',NULL,NULL UNION ALL
select 28,'S register claim',NULL,NULL UNION ALL
select 29,'assess claim',NULL,NULL UNION ALL
select 30,'advise claimant on reimbursement',NULL,NULL UNION ALL
select 31,'initiate payment',NULL,NULL UNION ALL
select 32,'close claim',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;