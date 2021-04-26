/* ROLES */
insert INTO ncfinderdb.dbo.ROLES ([name],[description])
select 'Administrador',NULL UNION ALL
select 'Gestor',NULL UNION ALL
select 'Operador',NULL;