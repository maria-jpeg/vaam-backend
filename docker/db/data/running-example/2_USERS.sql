/*USERS*/
INSERT INTO ncfinderdb.dbo.USERS ([username],[password],[role],[name],[email],[rfid])
select 'admin','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Administrador','Admin','admin@admin.pt',NULL UNION ALL
select 'gestor','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','Gestor','gestor@gmail.pt',NULL UNION ALL
select 'operador','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Operador','operador@gmail.pt',NULL UNION ALL
/*running-example*/
select 'pete','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Pete','pete@gmail.pt',NULL UNION ALL
select 'sue','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Sue','sue@gmail.pt',NULL UNION ALL
select 'mike','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Mike','mike@gmail.pt',NULL UNION ALL
select 'sara','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Sara','sara@gmail.pt',NULL UNION ALL
select 'sean','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Sean','sean@gmail.pt',NULL UNION ALL
select 'ellen','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Ellen','ellen@gmail.pt',NULL;