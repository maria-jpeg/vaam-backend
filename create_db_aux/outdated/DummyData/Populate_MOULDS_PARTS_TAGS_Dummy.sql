GO  
INSERT INTO dbo.MOULDS (code,description)   
    VALUES ('m1','Molde dummy 1');  
GO
GO  
INSERT INTO dbo.MOULDS (code,description)   
    VALUES ('m2','Molde dummy 2');  
GO 


GO  
INSERT INTO dbo.PARTS(code,code_mould,description,rfid_tag)   
    VALUES ('p1','m1','Peça dummy 1',NULL);  
GO
GO  
INSERT INTO dbo.PARTS (code,code_mould,description,rfid_tag)   
    VALUES ('p2','m1','Peça dummy 2',NULL);  
GO

GO  
INSERT INTO dbo.TAGS (rfid,is_available)   
    VALUES (' 59 55 ae 29',1);  
GO
GO  
INSERT INTO dbo.TAGS (rfid,is_available)   
    VALUES (' a6 aa a7 ac',1);
GO 
GO  
INSERT INTO dbo.TAGS (rfid,is_available)   
    VALUES (' a0 43 1d a4',1);
GO 


GO  
INSERT INTO dbo.USERS (password,username)   
    VALUES ('12345','admin');  
GO

GO  
INSERT INTO dbo.USERS (password,username)   
    VALUES ('12345','user1');  
GO

GO  
INSERT INTO dbo.USERS (password,username)   
    VALUES ('12345','user2');  
GO

