SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 22,'Estação de incoming claim',1,NULL,NULL UNION ALL
select 23,'Estação de B check if sufficient information is available',1,NULL,NULL UNION ALL
select 24,'Estação de B register claim',1,NULL,NULL UNION ALL
select 25,'Estação de determine likelihood of claim',1,NULL,NULL UNION ALL
select 26,'Estação de end',1,NULL,NULL UNION ALL
select 27,'Estação de S check if sufficient information is available',1,NULL,NULL UNION ALL
select 28,'Estação de S register claim',1,NULL,NULL UNION ALL
select 29,'Estação de assess claim',1,NULL,NULL UNION ALL
select 30,'Estação de advise claimant on reimbursement',1,NULL,NULL UNION ALL
select 31,'Estação de initiate payment',1,NULL,NULL UNION ALL
select 32,'Estação de close claim',1,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;