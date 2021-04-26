create database ncfinderdb
go
USE [ncfinderdb]
GO

create table PROCESSES
(
    id                 bigint identity
        constraint PK_PROCESSES
        primary key,
    description        varchar(500),
    endDate            datetime2,
    startDate          datetime2,
    name               varchar(300) not null,
    numberOfActivities int          not null,
    numberOfCases      int          not null,
    parentProcess      bigint
        constraint FK_PROCESSES_PROCESSES
            references PROCESSES
)
    go

create table ACTIVITIES
(
    id           bigint identity
        constraint PK_ACTIVITIES
        primary key,
    name         varchar(300) not null,
    description  varchar(1000),
    subprocessId bigint
        constraint FK_ACTIVITIES_PROCESSES
            references PROCESSES
)
    go

create table MOULDS
(
    code        varchar(100) not null
        constraint PK_Moulds
            primary key,
    description varchar(300),
    processId   bigint
        constraint FK_MOULDS_PROCESSES
            references PROCESSES
)
    go

create table ROLES
(
    name        varchar(100) not null
        constraint PK_ROLES_1
            primary key,
    description varchar(300)
)
    go

create table TAGS
(
    rfid        varchar(100)                not null
        constraint PK_TAGS
            primary key,
    isAvailable bit                         not null,
    isUser      bit
        constraint DF_TAGS_isUser default 0 not null
)
    go

create table PARTS
(
    code        varchar(100) not null
        constraint PK_PARTS
            primary key,
    mouldCode   varchar(100)
        constraint FK_PARTS_MOULDS
            references MOULDS,
    description varchar(300),
    tagRfid     varchar(100)
        constraint FK_PARTS_TAGS
            references TAGS,
    processId   bigint
        constraint FK_PARTS_PROCESSES
            references PROCESSES
)
    go

create table USERS
(
    username varchar(200)  not null
        constraint PK_USERS
            primary key,
    password varchar(1000) not null,
    role     varchar(100)
        constraint FK_USERS_ROLES
            references ROLES,
    name     varchar(300),
    email    varchar(300),
    rfid     varchar(100)
        constraint FK_USERS_TAGS
            references TAGS
)
    go

create table WORKSTATIONS
(
    id               bigint identity
        constraint WORKSTATIONS_pk
        primary key nonclustered,
    name             varchar(200),
    activityId       bigint
        constraint WORKSTATIONS_ACTIVITIES_id_fk
            references ACTIVITIES,
    isTagging        bit,
    isEndWorkstation bit
)
    go

create table ACTIVITIES_USERS
(
    username      varchar(200)                                         not null
        constraint FK_ACTIVITIES_USERS_USERS
            references USERS,
    activityId    bigint                                               not null
        constraint FK_ACTIVITIES_USERS_ACTIVITIES
            references ACTIVITIES,
    startDate     datetime
        constraint DF_ACTIVITIES_USERS_startDate default sysdatetime() not null,
    endDate       datetime,
    workstationId bigint
        constraint ACTIVITIES_USERS_WORKSTATIONS_id_fk
            references WORKSTATIONS,
    constraint PK_ACTIVITIES_USERS
        primary key (username, activityId, startDate)
)
    go

create table ESPS
(
    id            bigint identity
        constraint PK_ESPS
        primary key,
    mac           nvarchar(100),
    workstationId bigint
        constraint ESPS_WORKSTATIONS_id_fk
            references WORKSTATIONS
)
    go

create table EVENTS
(
    id             bigint identity
        constraint PK_EVENTS
        primary key,
    activityId     bigint
        constraint FK_EVENTS_ACTIVITIES
            references ACTIVITIES,
    processId      bigint
        constraint FK_EVENTS_PROCESSES
            references PROCESSES,
    mouldCode      varchar(100)
        constraint FK_EVENTS_MOULDS
            references MOULDS,
    partCode       varchar(100)
        constraint FK_EVENTS_PARTS
            references PARTS,
    startDate      datetime2,
    endDate        datetime2,
    duration       bigint
        constraint DF_EVENTS_duration default 0 not null,
    isEstimatedEnd bit,
    workstationId  bigint
        constraint EVENTS_WORKSTATIONS_id_fk
            references WORKSTATIONS
)
    go

create unique index WORKSTATIONS_id_uindex
    on WORKSTATIONS (id)
go

/*Criacao de dados*/
/*
INSERT INTO ncfinderdb.dbo.ROLES (name, description) VALUES (N'Administrador', null)
INSERT INTO ncfinderdb.dbo.ROLES (name, description) VALUES (N'Gestor', null)
INSERT INTO ncfinderdb.dbo.ROLES (name, description) VALUES (N'Operador', null)
*/
/* ROLES */
insert INTO ncfinderdb.dbo.ROLES ([name],[description])
select 'Administrador',NULL UNION ALL
select 'Gestor',NULL UNION ALL
select 'Operador',NULL;

/*  TAGS */
insert ncfinderdb.dbo.TAGS ([rfid],[isAvailable],[isUser])
select ' 04 09 a9 92 86 52 81',1,0 UNION ALL
select ' 04 25 a9 92 86 52 80',1,0 UNION ALL
select ' 04 26 a9 92 86 52 81',0,0 UNION ALL
select ' 04 3b a8 92 86 52 81',1,0 UNION ALL
select ' 04 44 a9 92 86 52 80',1,0 UNION ALL
select ' 04 b4 aa 92 86 52 80',1,0 UNION ALL
select ' 59 55 ae 29',0,1 UNION ALL
select ' a0 43 1d a4',1,0 UNION ALL
select ' a6 aa a7 ac',1,0 UNION ALL
select ' b5 f5 77 1b',1,0 UNION ALL
select 'frontendTagTest1',0,0 UNION ALL
select 'frontendTagTest2',1,0 UNION ALL
select 'frontendTagTest3',0,0 UNION ALL
select 'frontendTagTest4',0,1 UNION ALL
select 'testRfidUser',1,0 UNION ALL
select 'userTag1',1,1 UNION ALL
select 'userTag2',0,1;

/* USERS */
INSERT INTO ncfinderdb.dbo.USERS ([username],[password],[role],[name],[email],[rfid])
select 'admin','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','Teste','teste@gmail.com',NULL UNION ALL
select 'administrator','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Administrador','Admin','admin@admin.pt',NULL UNION ALL
select 'amelia','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Amelia','amelia@gmail.com',NULL UNION ALL
select 'bernardo','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','bernardo','bernardo@mail.pt',NULL UNION ALL
select 'denver','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Denver','denver@mail.pt',NULL UNION ALL
select 'GestorTeste1','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','Gestor Número Um','gestornum1@gmail.com',NULL UNION ALL
select 'katalon','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Katalon','katalon@mail.pt',NULL UNION ALL
select 'katalon2','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Katalon2','katalon2@mail.pt',NULL UNION ALL
select 'katalon4','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Katalon4','katalon4@mail.pt',NULL UNION ALL
select 'katalon5','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Katalon5','katalon5@mail.pt',NULL UNION ALL
select 'lisboa','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Lisboa','lisboa@mail.com',NULL UNION ALL
select 'nairobi','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Nairobi','nairobi@mail.pt',NULL UNION ALL
select 'patrock','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','patricia','patrock@mail.com',NULL UNION ALL
select 'randomtest','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','random','rafon@mfas.tt',NULL UNION ALL
select 'test','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Administrador','test','test@test.com',NULL UNION ALL
select 'tokyo','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Tokyo','tokyo@mail.com',NULL UNION ALL
select 'user1','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Teste2','teste2@gmail.com',NULL UNION ALL
select 'userAquis','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Joka','joka@mail.pt',NULL UNION ALL
select 'UserT','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Utilizador T','t@mail.pt',' 59 55 ae 29' UNION ALL
select 'UserTeste1','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','User Número Um','userteste1@gmail.com',NULL UNION ALL
select 'x','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Operador','Financial','financial@geral.pt',NULL UNION ALL
select 'Ze','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','Gestor','José Ribeiro','ze@mail.pt',NULL;

/* PROCESS */
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES ON;
insert INTO ncfinderdb.dbo.PROCESSES ([id], [description], [endDate], [startDate], [name], [numberOfActivities], [numberOfCases], [parentProcess])
select 25,'Processo Demo 1',NULL,'2020-04-27 21:59:00.0000000','ProcessoDemo1',7,5,NULL UNION ALL
select 28,'SubProcesso Demo 1',NULL,'2020-04-27 21:59:00.0000000','SubProcessoDemo1',6,4,25 UNION ALL
select 29,'Processo Demo 2',NULL,'2020-04-27 21:59:00.0000000','ProcessoDemo2',7,3,NULL UNION ALL
select 30,'SubProcesso Demo 2',NULL,'2020-04-27 21:59:00.0000000','SubProcessoDemo2',5,3,29 UNION ALL
select 58,'Katalon Test Process',NULL,NULL,'KatalonTest',2,1,NULL UNION ALL
select 60,'Katalon Test Process To Delete',NULL,NULL,'KatalonTestVolatil',0,0,NULL UNION ALL
select 61,'processo1',NULL,NULL,'processo1',0,0,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.PROCESSES OFF;

/* ACTIVITIES */
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES ON;
insert INTO ncfinderdb.dbo.ACTIVITIES ([id],[name],[description],[subprocessId])
select 1,'Desenho Molde',NULL,NULL UNION ALL
select 2,'Preparar Materiais',NULL,NULL UNION ALL
select 3,'Tagging',NULL,NULL UNION ALL
select 4,'Fresagem',NULL,NULL UNION ALL
select 5,'Maquinação Elétrodos',NULL,NULL UNION ALL
select 6,'Erosão',NULL,NULL UNION ALL
select 7,'Bancada',NULL,NULL UNION ALL
select 8,'Montagem Molde',NULL,NULL UNION ALL
select 9,'Testar Molde',NULL,NULL UNION ALL
select 10,'Entrega Molde',NULL,NULL UNION ALL
select 11,'Verificação',NULL,NULL UNION ALL
select 12,'Comprar Materiais',NULL,NULL UNION ALL
select 13,'CNC',NULL,NULL UNION ALL
select 14,'Projeto',NULL,NULL UNION ALL
select 15,'Aprovação Desenho',NULL,NULL UNION ALL
select 16,'Aprovação de Amostras',NULL,NULL UNION ALL
select 17,'Confirmação da Encomenda',NULL,NULL UNION ALL
select 18,'Reunir com o Cliente',NULL,NULL UNION ALL
select 19,'Aprovação dos Testes',NULL,NULL UNION ALL
select 20,'Preparação da Entrega',NULL,NULL UNION ALL
select 21,'Injeção',NULL,NULL UNION ALL
select 1181,'Assemblar Molde',NULL,NULL UNION ALL
select 1182,'Subprocess 28',NULL,28 UNION ALL
select 1183,'Subprocess 30',NULL,30 UNION ALL
select 1184,'KatalonTest','Katalon Test',NULL UNION ALL
select 1185,'Desvio',NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.ACTIVITIES OFF;

/* MOULDS */
insert INTO ncfinderdb.dbo.MOULDS ([code],[description],[processId])
select 'mouldA','Description mould A',25 UNION ALL
select 'mouldB','Description mould B',25 UNION ALL
select 'mouldC','Description mould C',25 UNION ALL
select 'mouldD','Description mould D',25 UNION ALL
select 'mouldKatalon','Katalo Test',25 UNION ALL
select 'mouldT','Description mould T',25 UNION ALL
select 'mouldX','Description mould X',29 UNION ALL
select 'mouldY','Description mould Y',29 UNION ALL
select 'mouldZ','Description mould Z',29;

/* PARTS */
insert INTO ncfinderdb.dbo.PARTS ([code],[mouldCode],[description],[tagRfid],[processId])
select 'partA1','mouldA','hEY oH Lets Go!','frontendTagTest3',28 UNION ALL
select 'partA2','mouldA','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partA3','mouldA','hEY oH Lets Go!',' 04 26 a9 92 86 52 81',28 UNION ALL
select 'partA4','mouldA','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partB1','mouldB','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partB2','mouldB','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partB3','mouldB','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partC1','mouldC','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partC2','mouldC','hEY oH Lets Go!',NULL,28 UNION ALL
select 'partT1','mouldT','Description partT1',' a6 aa a7 ac',28 UNION ALL
select 'partT2','mouldT','Description partT2',' a0 43 1d a4',28 UNION ALL
select 'partX1','mouldX','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partX2','mouldX','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partX3','mouldX','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partX4','mouldX','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partY1','mouldY','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partY2','mouldY','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partY3','mouldY','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partZ1','mouldZ','hEY oH Lets Go!',NULL,30 UNION ALL
select 'partZ2','mouldZ','hEY oH Lets Go!',NULL,30;

/* WORKSTATIONS */
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS ON;
insert INTO ncfinderdb.dbo.WORKSTATIONS ([id],[name],[activityId],[isTagging],[isEndWorkstation])
select 1,'F1',4,NULL,NULL UNION ALL
select 2,'F2',4,NULL,NULL UNION ALL
select 3,'F3',4,NULL,NULL UNION ALL
select 4,'F4',4,NULL,NULL UNION ALL
select 5,'E1',6,NULL,NULL UNION ALL
select 6,'E2',6,0,NULL UNION ALL
select 7,'E3',6,NULL,NULL UNION ALL
select 9,'D1',1,NULL,NULL UNION ALL
select 10,'D2',1,NULL,NULL UNION ALL
select 14,'B1',7,0,0 UNION ALL
select 15,'B2',7,0,0 UNION ALL
select 16,'B3',7,0,1 UNION ALL
select 17,'B4',7,0,1 UNION ALL
select 18,'ME1',5,0,0 UNION ALL
select 20,'ME2',5,0,0 UNION ALL
select 22,'TAG1',3,1,0 UNION ALL
select 23,'TAG2',3,1,0;
SET IDENTITY_INSERT ncfinderdb.dbo.WORKSTATIONS OFF;

/* EVENTS */
SET IDENTITY_INSERT ncfinderdb.dbo.EVENTS ON;
insert INTO ncfinderdb.dbo.EVENTS ([id], [activityId], [processId], [mouldCode], [partCode], [startDate], [endDate], [duration], [isEstimatedEnd], [workstationId])
select 1,1,25,'mouldA',NULL,'2020-05-01 08:45:00.2030000','2020-05-01 11:45:00.2030000',10800000,NULL,NULL UNION ALL
select 4,1,25,'mouldB',NULL,'2020-05-01 09:45:00.2030000','2020-05-01 10:45:00.2030000',3600000,NULL,NULL UNION ALL
select 6,1,25,'mouldC',NULL,'2020-05-01 14:20:00.2030000','2020-05-01 16:20:00.2030000',7200000,NULL,NULL UNION ALL
select 7,1,25,'mouldD',NULL,'2020-05-01 15:10:00.2030000','2020-05-01 18:10:00.2030000',10800000,NULL,NULL UNION ALL
select 8,1,29,'mouldX',NULL,'2020-05-02 10:48:00.2030000','2020-05-02 12:48:00.2030000',7200000,NULL,NULL UNION ALL
select 9,1,29,'mouldY',NULL,'2020-05-02 14:45:00.2030000','2020-05-02 16:45:00.2030000',7200000,NULL,NULL UNION ALL
select 10,1,29,'mouldZ',NULL,'2020-05-02 15:50:00.2030000','2020-05-02 16:50:00.2030000',3600000,NULL,NULL UNION ALL
select 12,2,25,'mouldA',NULL,'2020-05-01 11:50:00.2030000','2020-05-01 12:35:00.2030000',2700000,NULL,NULL UNION ALL
select 14,2,25,'mouldB',NULL,'2020-05-01 10:45:00.2030000','2020-05-01 11:15:00.2030000',1800000,NULL,NULL UNION ALL
select 15,2,25,'mouldC',NULL,'2020-05-01 16:20:00.2030000','2020-05-01 16:50:00.2030000',1800000,NULL,NULL UNION ALL
select 16,2,25,'mouldD',NULL,'2020-05-01 18:10:00.2030000','2020-05-01 19:10:00.2030000',3600000,NULL,NULL UNION ALL
select 17,2,29,'mouldX',NULL,'2020-05-02 12:50:00.2030000','2020-05-02 14:20:00.2030000',5400000,NULL,NULL UNION ALL
select 19,2,29,'mouldY',NULL,'2020-05-02 16:45:00.2030000','2020-05-02 17:45:00.2030000',3600000,NULL,NULL UNION ALL
select 20,2,29,'mouldZ',NULL,'2020-05-02 17:00:00.2030000','2020-05-02 17:45:00.2030000',2700000,NULL,NULL UNION ALL
select 21,3,25,'mouldA',NULL,'2020-05-01 12:45:00.2030000','2020-05-01 13:00:00.2030000',900000,NULL,NULL UNION ALL
select 22,3,25,'mouldB',NULL,'2020-05-01 11:20:00.2030000','2020-05-01 11:25:00.2030000',300000,NULL,NULL UNION ALL
select 23,3,25,'mouldC',NULL,'2020-05-01 16:50:00.2030000','2020-05-01 17:00:00.2030000',600000,NULL,NULL UNION ALL
select 24,3,25,'mouldD',NULL,'2020-05-01 19:13:00.2030000','2020-05-01 19:23:00.2030000',600000,NULL,NULL UNION ALL
select 25,3,29,'mouldX',NULL,'2020-05-02 14:24:00.2030000','2020-05-02 14:34:00.2030000',600000,NULL,NULL UNION ALL
select 26,3,29,'mouldY',NULL,'2020-05-02 17:52:00.2030000','2020-05-02 17:57:00.2030000',300000,NULL,NULL UNION ALL
select 28,3,29,'mouldZ',NULL,'2020-05-02 17:51:00.2030000','2020-05-02 17:56:00.2030000',300000,NULL,NULL UNION ALL
select 38,1182,25,'mouldA',NULL,'2020-05-04 12:45:39.2228902','2020-05-04 14:00:11.3581910',4472136,0,NULL UNION ALL
select 39,4,28,'mouldA','partA1','2020-05-04 12:45:39.2228902','2020-05-04 13:37:52.9797354',3133757,1,2 UNION ALL
select 40,5,28,'mouldA','partA1','2020-05-04 13:38:59.3229266','2020-05-04 13:39:13.2240778',79902,0,18 UNION ALL
select 41,4,28,'mouldA','partA2','2020-05-04 13:38:24.3505310','2020-05-04 13:38:55.0661434',30716,1,2 UNION ALL
select 42,5,28,'mouldA','partA2','2020-05-04 13:38:55.3781354','2020-05-04 13:39:40.6637742',45285,0,18 UNION ALL
select 43,4,28,'mouldA','partA3','2020-05-04 13:39:30.7892274','2020-05-04 13:40:01.1772482',30388,1,3 UNION ALL
select 44,5,28,'mouldA','partA3','2020-05-04 13:40:01.5204394','2020-05-04 13:41:46.1781558',104658,0,18 UNION ALL
select 45,1182,25,'mouldB',NULL,'2020-05-04 13:41:28.4258110','2020-05-04 13:59:43.7626755',1095337,0,NULL UNION ALL
select 46,4,28,'mouldB','partB1','2020-05-04 13:41:28.4258110','2020-05-04 13:41:41.4046782',12979,0,3 UNION ALL
select 47,4,28,'mouldA','partA4','2020-05-04 13:41:54.3835454','2020-05-04 13:42:17.4397542',23056,0,4 UNION ALL
select 48,5,28,'mouldB','partB1','2020-05-04 13:42:11.7926990','2020-05-04 13:42:23.2428054',11450,0,20 UNION ALL
select 49,5,28,'mouldA','partA4','2020-05-04 13:43:22.1468950','2020-05-04 13:44:55.7132958',93567,0,18 UNION ALL
select 50,4,28,'mouldB','partB2','2020-05-04 13:43:49.2901990','2020-05-04 13:45:17.6151342',88325,1,2 UNION ALL
select 51,4,28,'mouldB','partB3','2020-05-04 13:44:25.1224802','2020-05-04 13:45:53.8530050',88731,0,4 UNION ALL
select 52,5,28,'mouldB','partB2','2020-05-04 13:45:18.0519230','2020-05-04 13:46:14.7408694',56689,0,20 UNION ALL
select 53,5,28,'mouldB','partB3','2020-05-04 13:46:03.3063626','2020-05-04 13:46:20.2319286',16925,0,20 UNION ALL
select 54,1182,25,'mouldC',NULL,'2020-05-04 13:46:28.5465154','2020-05-04 14:00:27.0200890',838474,0,NULL UNION ALL
select 55,4,28,'mouldC','partC1','2020-05-04 13:46:28.5465154','2020-05-04 13:46:54.5510486',26005,0,2 UNION ALL
select 56,5,28,'mouldC','partC1','2020-05-04 13:47:28.6361746','2020-05-04 13:48:52.9671350',84331,0,18 UNION ALL
select 57,4,28,'mouldC','partC2','2020-05-04 13:47:30.5237262','2020-05-04 13:47:45.4992975',14976,0,2 UNION ALL
select 58,5,28,'mouldC','partC2','2020-05-04 13:48:34.0917400','2020-05-04 13:48:34.4037300',312,0,18 UNION ALL
select 59,6,28,'mouldA','partA4','2020-05-04 13:50:37.4993845','2020-05-04 13:50:51.6637305',14164,0,6 UNION ALL
select 60,7,28,'mouldA','partA4','2020-05-04 13:50:56.2967820','2020-05-04 13:51:24.2822850',27986,0,16 UNION ALL
select 61,6,28,'mouldA','partA1','2020-05-04 13:51:05.3444920','2020-05-04 13:55:20.3200120',14976,0,5 UNION ALL
select 62,7,28,'mouldA','partA1','2020-05-04 13:56:17.3500915','2020-05-04 13:57:18.4221340',61072,0,17 UNION ALL
select 63,6,28,'mouldB','partB1','2020-05-04 13:56:23.7302870','2020-05-04 13:57:12.3851275',48655,0,7 UNION ALL
select 64,6,28,'mouldB','partB3','2020-05-04 13:56:35.1179220','2020-05-04 13:56:35.6327055',515,0,7 UNION ALL
select 65,7,28,'mouldB','partB3','2020-05-04 13:57:00.1863185','2020-05-04 13:57:45.7368585',45550,0,16 UNION ALL
select 66,7,28,'mouldB','partB1','2020-05-04 13:57:42.7885530','2020-05-04 13:58:23.8932355',41105,0,17 UNION ALL
select 67,6,28,'mouldA','partA2','2020-05-04 13:58:05.5638230','2020-05-04 13:58:36.7316240',31168,1,6 UNION ALL
select 68,7,28,'mouldA','partA2','2020-05-04 13:58:37.0748130','2020-05-04 13:59:00.2088715',23134,0,16 UNION ALL
select 69,6,28,'mouldA','partA3','2020-05-04 13:58:45.5609410','2020-05-04 13:59:06.2146790',20654,0,5 UNION ALL
select 70,6,28,'mouldB','partB2','2020-05-04 13:58:50.1315945','2020-05-04 13:59:30.6278965',40496,1,7 UNION ALL
select 71,7,28,'mouldA','partA3','2020-05-04 13:59:09.0069895','2020-05-04 14:00:11.0929995',62086,0,17 UNION ALL
select 72,6,28,'mouldC','partC1','2020-05-04 13:59:14.6072100','2020-05-04 13:59:52.0148110',37407,0,5 UNION ALL
select 73,6,28,'mouldC','partC2','2020-05-04 13:59:21.2837960','2020-05-04 13:59:57.8646235',36581,0,6 UNION ALL
select 74,7,28,'mouldB','partB2','2020-05-04 13:59:30.8930880','2020-05-04 13:59:43.1230960',12230,0,16 UNION ALL
select 75,7,28,'mouldC','partC1','2020-05-04 13:59:54.3391365','2020-05-04 14:00:23.0734155',28734,0,17 UNION ALL
select 76,7,28,'mouldC','partC2','2020-05-04 13:59:59.6741655','2020-05-04 14:00:26.7704970',27096,0,17 UNION ALL
select 77,1183,29,'mouldX',NULL,'2020-05-04 14:09:31.5388830','2020-05-04 14:18:33.4842348',541946,0,NULL UNION ALL
select 78,4,30,'mouldX','partX4','2020-05-04 14:09:31.5388830','2020-05-04 14:09:59.5869638',28048,0,NULL UNION ALL
select 79,4,30,'mouldX','partX1','2020-05-04 14:09:53.1755282','2020-05-04 14:10:08.2915406',15116,0,NULL UNION ALL
select 80,5,30,'mouldX','partX4','2020-05-04 14:10:03.9392522','2020-05-04 14:11:47.8169886',103877,0,NULL UNION ALL
select 81,5,30,'mouldX','partX1','2020-05-04 14:10:10.4442854','2020-05-04 14:11:03.2333318',52789,0,NULL UNION ALL
select 82,4,30,'mouldX','partX2','2020-05-04 14:10:18.5872766','2020-05-04 14:11:07.8664130',49279,0,NULL UNION ALL
select 83,4,30,'mouldX','partX3','2020-05-04 14:10:26.9486622','2020-05-04 14:10:42.0178758',15069,0,NULL UNION ALL
select 84,1183,29,'mouldY',NULL,'2020-05-04 14:10:31.3945482','2020-05-04 14:17:36.7488414',425354,0,NULL UNION ALL
select 85,4,30,'mouldY','partY2','2020-05-04 14:10:31.3945482','2020-05-04 14:11:24.2771922',52883,0,NULL UNION ALL
select 86,4,30,'mouldY','partY1','2020-05-04 14:10:35.9496314','2020-05-04 14:12:40.7464314',124797,0,NULL UNION ALL
select 87,5,30,'mouldX','partX3','2020-05-04 14:10:45.5901842','2020-05-04 14:11:35.4153066',49825,0,NULL UNION ALL
select 88,1183,29,'mouldZ',NULL,'2020-05-04 14:10:53.8579722','2020-05-04 14:17:08.4667666',374609,0,NULL UNION ALL
select 89,4,30,'mouldZ','partZ2','2020-05-04 14:10:53.8579722','2020-05-04 14:12:01.9346266',68077,1,NULL UNION ALL
select 90,5,30,'mouldX','partX2','2020-05-04 14:11:10.9863330','2020-05-04 14:12:12.4799562',61493,0,NULL UNION ALL
select 91,4,30,'mouldZ','partZ1','2020-05-04 14:11:30.9538210','2020-05-04 14:12:36.7997326',65846,0,NULL UNION ALL
select 92,5,30,'mouldZ','partZ2','2020-05-04 14:12:02.2310190','2020-05-04 14:12:02.9797998',748,0,NULL UNION ALL
select 93,4,30,'mouldY','partY3','2020-05-04 14:12:21.9957122','2020-05-04 14:12:51.1201654',29125,1,NULL UNION ALL
select 94,5,30,'mouldY','partY2','2020-05-04 14:12:28.8127374','2020-05-04 14:13:00.5423238',31730,0,NULL UNION ALL
select 95,5,30,'mouldY','partY1','2020-05-04 14:12:44.0847458','2020-05-04 14:13:30.2907610',46206,0,NULL UNION ALL
select 96,5,30,'mouldY','partY3','2020-05-04 14:12:51.4009582','2020-05-04 14:13:15.2683462',23868,0,NULL UNION ALL
select 97,5,30,'mouldZ','partZ1','2020-05-04 14:13:11.6024402','2020-05-04 14:14:03.0499210',51447,0,NULL UNION ALL
select 98,6,30,'mouldZ','partZ1','2020-05-04 14:14:41.4873354','2020-05-04 14:15:24.2302394',42743,0,NULL UNION ALL
select 99,6,30,'mouldX','partX3','2020-05-04 14:14:47.3995838','2020-05-04 14:15:00.7538106',13354,0,NULL UNION ALL
select 100,6,30,'mouldX','partX2','2020-05-04 14:15:01.7356162','2020-05-04 14:15:10.7356162',9000,0,NULL UNION ALL
select 101,7,30,'mouldX','partX2','2020-05-04 14:15:19.0823714','2020-05-04 14:16:01.7356162',42653,0,NULL UNION ALL
select 102,7,30,'mouldZ','partZ1','2020-05-04 14:15:27.1473646','2020-05-04 14:16:32.0884994',64941,0,NULL UNION ALL
select 103,7,30,'mouldX','partX3','2020-05-04 14:15:35.3995530','2020-05-04 14:16:27.5646154',52165,0,NULL UNION ALL
select 104,6,30,'mouldY','partY1','2020-05-04 14:15:42.5753690','2020-05-04 14:16:01.4040862',18829,0,NULL UNION ALL
select 105,6,30,'mouldY','partY3','2020-05-04 14:15:53.9942762','2020-05-04 14:16:14.2425570',20248,0,NULL UNION ALL
select 106,7,30,'mouldY','partY1','2020-05-04 14:16:39.9350982','2020-05-04 14:17:27.6386750',47703,0,NULL UNION ALL
select 107,7,30,'mouldY','partY3','2020-05-04 14:16:46.5649282','2020-05-04 14:17:36.6084450',50044,0,NULL UNION ALL
select 108,6,30,'mouldY','partY2','2020-05-04 14:16:50.9016170','2020-05-04 14:16:57.5782458',6677,0,NULL UNION ALL
select 109,6,30,'mouldZ','partZ2','2020-05-04 14:16:59.7465902','2020-05-04 14:17:02.6325162',2886,0,NULL UNION ALL
select 110,7,30,'mouldY','partY2','2020-05-04 14:17:00.8697614','2020-05-04 14:17:06.0020298',5133,0,NULL UNION ALL
select 111,7,30,'mouldZ','partZ2','2020-05-04 14:17:07.4527926','2020-05-04 14:17:08.3263702',874,0,NULL UNION ALL
select 112,6,30,'mouldX','partX1','2020-05-04 14:17:45.8277623','2020-05-04 14:18:07.9010548',22074,0,NULL UNION ALL
select 113,6,30,'mouldX','partX4','2020-05-04 14:17:59.9141108','2020-05-04 14:18:13.7196683',13805,0,NULL UNION ALL
select 114,7,30,'mouldX','partX1','2020-05-04 14:18:10.6621663','2020-05-04 14:26:05.2301553',474568,0,NULL UNION ALL
select 115,7,30,'mouldX','partX4','2020-05-04 14:18:25.6844848','2020-05-04 16:31:45.0000000',7999316,0,NULL UNION ALL
select 116,8,29,'mouldZ',NULL,'2020-05-04 14:17:08.4667666','2020-05-04 16:17:08.4667666',7200000,NULL,NULL UNION ALL
select 117,8,29,'mouldY',NULL,'2020-05-04 14:17:36.7488414','2020-05-04 15:17:36.7488414',3600000,NULL,NULL UNION ALL
select 118,8,29,'mouldX',NULL,'2020-05-04 14:28:05.4329488','2020-05-04 15:28:05.4329488',3600000,NULL,NULL UNION ALL
select 119,9,29,'mouldZ',NULL,'2020-05-04 16:49:08.4667666','2020-05-04 20:49:08.4667666',14400000,NULL,NULL UNION ALL
select 120,9,29,'mouldY',NULL,'2020-05-04 15:53:36.7488414','2020-05-04 19:53:36.7488414',14400000,NULL,NULL UNION ALL
select 121,9,29,'mouldX',NULL,'2020-05-04 15:38:05.4329488','2020-05-04 18:38:05.4329488',10800000,NULL,NULL UNION ALL
select 122,10,29,'mouldZ',NULL,'2020-05-05 10:30:08.4667666','2020-05-05 12:30:08.4667666',7200000,NULL,NULL UNION ALL
select 123,10,29,'mouldY',NULL,'2020-05-05 09:50:36.7488414','2020-05-05 10:50:36.7488414',3600000,NULL,NULL UNION ALL
select 124,10,29,'mouldX',NULL,'2020-05-05 08:20:05.4329488','2020-05-05 12:20:05.4329488',14400000,NULL,NULL UNION ALL
select 126,8,25,'mouldA',NULL,'2020-05-06 08:20:05.4329488','2020-05-06 12:20:05.4329488',14400000,NULL,NULL UNION ALL
select 127,8,25,'mouldB',NULL,'2020-05-07 08:20:05.4329488','2020-05-07 12:20:05.4329488',14400000,NULL,NULL UNION ALL
select 128,8,25,'mouldC',NULL,'2020-05-08 08:20:05.4329488','2020-05-08 12:20:05.4329488',14400000,NULL,NULL UNION ALL
select 129,9,25,'mouldA',NULL,'2020-05-06 12:25:05.4329488','2020-05-06 13:25:05.4329488',3600000,NULL,NULL UNION ALL
select 130,9,25,'mouldB',NULL,'2020-05-07 12:25:05.4329488','2020-05-07 13:25:05.4329488',3600000,NULL,NULL UNION ALL
select 131,9,25,'mouldC',NULL,'2020-05-08 12:25:05.4329488','2020-05-08 13:25:05.4329488',3600000,NULL,NULL UNION ALL
select 132,10,25,'mouldA',NULL,'2020-05-06 14:25:05.4329488','2020-05-06 15:25:05.4329488',3600000,NULL,NULL UNION ALL
select 134,10,25,'mouldB',NULL,'2020-05-07 14:25:05.4329488','2020-05-07 15:25:05.4329488',3600000,NULL,NULL UNION ALL
select 135,10,25,'mouldC',NULL,'2020-05-08 14:25:05.4329488','2020-05-08 15:25:05.4329488',3600000,NULL,NULL UNION ALL
select 137,8,25,'mouldC',NULL,'2020-05-08 15:26:05.4329488','2020-05-08 16:26:05.4329488',3600000,NULL,NULL UNION ALL
select 140,9,25,'mouldC',NULL,'2020-05-10 16:21:14.7740000','2020-05-10 17:59:07.0430000',5872269,0,NULL UNION ALL
select 145,10,25,'mouldC',NULL,'2020-05-10 18:59:07.0430000','2020-05-10 19:59:07.0430000',3600000,0,NULL UNION ALL
select 200,20,58,'mouldKatalon',NULL,'2020-06-10 14:25:05.4320000','2020-06-10 16:25:05.4320000',7200000,0,NULL UNION ALL
select 201,1184,58,'mouldKatalon',NULL,'2021-05-01 08:45:00.2030000','2021-05-01 08:45:00.2030000',0,0,NULL UNION ALL
select 202,1184,58,'mouldKatalon',NULL,'2021-05-01 08:45:00.2030000','2021-05-01 08:45:00.2030000',0,0,NULL UNION ALL
select 203,1184,58,'mouldKatalon',NULL,'2021-05-01 08:45:00.2030000','2021-05-01 08:45:00.2030000',0,0,NULL UNION ALL
select 243,1182,25,'mouldT',NULL,'2020-07-11 21:16:31.9003640','2020-07-11 21:17:21.6939680',49793,0,NULL UNION ALL
select 244,4,28,'mouldT','partT1','2020-07-11 21:16:31.9783615','2020-07-11 22:40:38.9357385',6957,0,4 UNION ALL
select 245,4,28,'mouldT','partT2','2020-07-11 21:50:48.2330405','2020-07-11 23:20:55.1904175',6957000,1,3 UNION ALL
select 246,5,28,'mouldT','partT2','2020-07-11 23:35:55.1904175','2020-07-11 23:59:55.1904175',17768,0,17 UNION ALL
select 247,5,28,'mouldT','partT1','2020-07-11 22:41:39.9357385','2020-07-11 22:42:40.9357385',16129,0,17 UNION ALL
select 249,6,28,'mouldT','partT1','2020-07-12 09:16:31.9800000','2020-07-12 16:27:31.9800000',7023000,1,6 UNION ALL
select 250,6,28,'mouldT','partT2','2020-07-12 16:30:31.9800000','2020-07-12 17:30:31.9800000',3600000,0,5 UNION ALL
select 256,1185,28,'mouldT','partT1','2020-07-12 17:30:31.9800000','2020-07-12 18:30:31.9800000',3600000,0,NULL UNION ALL
select 257,1185,30,'mouldX','partX1','2020-07-12 18:30:31.9800000','2020-07-12 18:30:31.9800000',3600000,0,NULL UNION ALL
select 259,7,28,'mouldT','partT1','2020-07-12 18:50:31.9800000','2020-07-12 19:30:31.9800000',2500000,1,NULL UNION ALL
select 260,21,28,'mouldT','partT2','2020-07-12 17:30:31.9800000','2020-07-12 19:26:55.9800000',3600000,NULL,NULL;
SET IDENTITY_INSERT ncfinderdb.dbo.EVENTS OFF;

/* ESPS */
SET IDENTITY_INSERT ncfinderdb.dbo.ESPS ON;
insert INTO ncfinderdb.dbo.ESPS ([id],[mac],[workstationId])
select 1,'30:AE:A4:17:9F:F8',17 UNION ALL
select 4,'30:AE:A4:14:D2:04',1;
SET IDENTITY_INSERT ncfinderdb.dbo.ESPS OFF;

/* ACTIVITIES_USERS */
insert INTO ncfinderdb.dbo.ACTIVITIES_USERS ([username],[activityId],[startDate],[endDate],[workstationId])
select 'denver',1,'2020-05-01 10:30:30.257','2020-05-01 17:30:30.257',NULL UNION ALL
select 'denver',2,'2020-05-01 11:26:45.093','2020-05-01 17:26:45.093',NULL UNION ALL
select 'denver',4,'2020-05-04 13:40:54.383','2020-05-04 13:50:54.383',1 UNION ALL
select 'denver',4,'2020-05-04 16:10:07.423','2020-05-04 17:10:07.423',4 UNION ALL
select 'denver',4,'2020-07-11 20:16:31.980','2020-07-11 23:50:31.980',3 UNION ALL
select 'denver',4,'2020-07-12 09:16:31.980','2020-07-12 19:16:31.980',2 UNION ALL
select 'denver',5,'2020-05-04 13:31:53.323','2020-05-04 14:37:53.323',20 UNION ALL
select 'denver',5,'2020-05-04 13:40:53.323','2020-05-04 14:00:53.323',18 UNION ALL
select 'denver',5,'2020-05-04 16:17:09.263','2020-05-04 18:17:09.263',18 UNION ALL
select 'denver',6,'2020-05-04 13:50:37.500','2020-05-04 13:52:37.500',5 UNION ALL
select 'denver',6,'2020-05-04 13:55:37.500','2020-05-04 14:30:37.500',6 UNION ALL
select 'denver',6,'2020-05-04 16:27:46.147','2020-05-04 16:29:46.147',7 UNION ALL
select 'denver',7,'2020-05-04 13:51:37.500','2020-05-04 14:00:27.500',17 UNION ALL
select 'lisboa',1,'2020-05-01 09:26:45.093','2020-05-01 16:26:45.093',NULL UNION ALL
select 'lisboa',2,'2020-05-01 16:30:30.257','2020-05-01 21:30:30.257',NULL UNION ALL
select 'lisboa',4,'2020-05-04 13:39:30.790','2020-05-04 13:49:32.000',3 UNION ALL
select 'lisboa',4,'2020-05-04 13:45:30.790','2020-05-04 13:50:30.790',2 UNION ALL
select 'lisboa',5,'2020-05-04 13:37:53.323','2020-05-04 14:10:53.323',18 UNION ALL
select 'lisboa',6,'2020-05-04 13:55:37.500','2020-05-04 13:59:37.500',7 UNION ALL
select 'lisboa',7,'2020-05-04 13:40:37.500','2020-05-04 19:55:37.500',16 UNION ALL
select 'lisboa',7,'2020-05-04 16:31:06.323','2020-05-04 19:55:37.500',16 UNION ALL
select 'lisboa',8,'2020-05-08 08:20:05.433','2020-05-08 17:20:05.433',NULL UNION ALL
select 'nairobi',1,'2020-05-01 09:00:00.203','2020-05-01 09:45:00.203',NULL UNION ALL
select 'nairobi',4,'2020-05-04 10:39:30.790','2020-05-04 12:39:30.790',4 UNION ALL
select 'nairobi',4,'2020-05-04 13:00:30.790','2020-05-04 20:00:30.790',2 UNION ALL
select 'nairobi',4,'2020-05-04 13:38:24.350','2020-05-04 13:59:24.350',2 UNION ALL
select 'nairobi',4,'2020-05-04 13:39:30.790','2020-05-04 14:50:35.000',3 UNION ALL
select 'nairobi',4,'2020-07-11 21:16:31.980','2020-07-11 22:37:31.980',4 UNION ALL
select 'nairobi',4,'2020-07-12 09:16:31.980','2020-07-12 12:16:31.980',2 UNION ALL
select 'nairobi',6,'2020-05-04 13:50:37.500','2020-05-04 13:50:38.500',6 UNION ALL
select 'nairobi',6,'2020-05-04 13:51:37.500','2020-05-04 13:52:37.500',5 UNION ALL
select 'tokyo',1,'2020-05-01 08:45:00.203','2020-05-02 12:48:00.203',NULL UNION ALL
select 'tokyo',2,'2020-05-01 11:26:45.093','2020-05-02 18:26:45.093',NULL UNION ALL
select 'tokyo',4,'2020-05-04 08:00:00.203','2020-05-04 19:45:00.203',2 UNION ALL
select 'tokyo',4,'2020-05-04 12:45:39.223','2020-05-04 14:45:39.223',2 UNION ALL
select 'tokyo',4,'2020-05-04 13:00:00.203','2020-05-04 14:00:00.203',2 UNION ALL
select 'tokyo',4,'2020-05-04 16:03:23.473','2020-05-04 16:04:25.473',4 UNION ALL
select 'tokyo',4,'2020-07-12 12:30:31.980','2020-07-12 17:30:31.980',2 UNION ALL
select 'tokyo',5,'2020-05-04 13:46:00.323','2020-05-04 13:47:53.323',20 UNION ALL
select 'tokyo',7,'2020-07-12 17:30:31.980','2020-07-12 19:30:31.980',NULL UNION ALL
select 'tokyo',8,'2020-05-04 14:17:08.467','2020-05-04 15:17:08.467',NULL UNION ALL
select 'tokyo',8,'2020-05-04 16:00:08.467','2020-05-04 18:00:08.467',NULL UNION ALL
select 'tokyo',8,'2020-05-06 08:20:05.433','2020-05-07 20:20:05.433',NULL UNION ALL
select 'tokyo',21,'2020-07-12 17:30:31.980','2020-07-12 19:30:31.980',NULL UNION ALL
select 'user1',6,'2020-05-04 13:55:37.500','2020-05-04 14:56:37.500',5 UNION ALL
select 'UserT',4,'2020-07-12 10:23:52.307','2020-07-12 10:23:57.157',2 UNION ALL
select 'UserT',7,'2020-07-12 10:24:04.067','2020-07-12 10:24:09.340',17;