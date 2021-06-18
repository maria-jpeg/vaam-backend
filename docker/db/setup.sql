-- noinspection SqlDialectInspectionForFile

create
database ncfinderdb
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
    rfid        varchar(100) not null
        constraint PK_TAGS
            primary key,
    isAvailable bit          not null,
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
    username      varchar(200) not null
        constraint FK_ACTIVITIES_USERS_USERS
            references USERS,
    activityId    bigint       not null
        constraint FK_ACTIVITIES_USERS_ACTIVITIES
            references ACTIVITIES,
    startDate     datetime2                                    --vaam -> datetime para datetime2
        constraint DF_ACTIVITIES_USERS_startDate default sysdatetime() not null,
    endDate       datetime2,
    workstationId bigint
        constraint ACTIVITIES_USERS_WORKSTATIONS_id_fk
            references WORKSTATIONS,
    constraint PK_ACTIVITIES_USERS
        primary key (username, activityId, startDate, endDate) --vaam -> adicionei endDate aqui
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

create
unique index WORKSTATIONS_id_uindex
    on WORKSTATIONS (id)
go

create table DASHBOARD
(
    id            bigint identity
        constraint PK_DASHBOARD
            primary key,
    date datetime2 default sysdatetime() not null,
    value int,
    unit varchar(50),
    description varchar(250),
    processId bigint
        constraint DASHBOARD_PROCESSES_id_fk
            references PROCESSES
)
go