USE [ncfinderdb]
GO

/****** Object:  Table [dbo].[PROCESSES]    Script Date: 03/05/2020 13:26:06 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
/* ----------------------------------------------------------PROCESSES---------------------------------------------------------- */
CREATE TABLE [dbo].[PROCESSES](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[description] [varchar](500) NULL,
	[endDate] [datetime2](7) NULL,
	[startDate] [datetime2](7) NULL,
	[name] [varchar](300) NULL,
	[numberOfActivities] [int] NOT NULL,
	[numberOfCases] [int] NOT NULL,
	[parentProcess] [bigint] NULL,
 CONSTRAINT [PK_PROCESSES] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PROCESSES]  WITH CHECK ADD  CONSTRAINT [FK_PROCESSES_PROCESSES] FOREIGN KEY([parentProcess])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[PROCESSES] CHECK CONSTRAINT [FK_PROCESSES_PROCESSES]
GO

/* ----------------------------------------------------------ACTIVITIES---------------------------------------------------------- */

CREATE TABLE [dbo].[ACTIVITIES](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[name] [varchar](300) NOT NULL,
	[description] [varchar](1000) NULL,
	[isTagging] [bit] NULL,
	[isEndActivity] [bit] NULL,
	[subprocessId] [bigint] NULL,
 CONSTRAINT [PK_ACTIVITIES] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ACTIVITIES]  WITH CHECK ADD  CONSTRAINT [FK_ACTIVITIES_PROCESSES] FOREIGN KEY([subprocessId])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[ACTIVITIES] CHECK CONSTRAINT [FK_ACTIVITIES_PROCESSES]
GO

/* ----------------------------------------------------------ROLES---------------------------------------------------------- */
CREATE TABLE [dbo].[ROLES](
	[name] [varchar](100) NOT NULL,
	[description] [varchar](300) NULL,
 CONSTRAINT [PK_ROLES_1] PRIMARY KEY CLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

/* ----------------------------------------------------------LOGS---------------------------------------------------------- */
CREATE TABLE [dbo].[LOGS](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[description] [varchar](255) NULL,
	[endDate] [datetime2](7) NULL,
	[fileName] [varchar](255) NULL,
	[numberOfActivities] [int] NOT NULL,
	[numberOfCases] [int] NOT NULL,
	[startDate] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

/* ----------------------------------------------------------TAGS---------------------------------------------------------- */
CREATE TABLE [dbo].[TAGS](
	[rfid] [varchar](100) NOT NULL,
	[isAvailable] [bit] NOT NULL,
	[isUser] [bit] NOT NULL,
 CONSTRAINT [PK_TAGS] PRIMARY KEY CLUSTERED 
(
	[rfid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TAGS] ADD  CONSTRAINT [DF_TAGS_isUser]  DEFAULT ((0)) FOR [isUser]
GO

/* ----------------------------------------------------------USERS---------------------------------------------------------- */
CREATE TABLE [dbo].[USERS](
	[username] [varchar](200) NOT NULL,
	[password] [varchar](1000) NOT NULL,
	[role] [varchar](100) NULL,
	[name] [varchar](300) NULL,
	[email] [varchar](300) NULL,
	[rfid] [varchar](100) NULL,
 CONSTRAINT [PK_USERS] PRIMARY KEY CLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[USERS]  WITH CHECK ADD  CONSTRAINT [FK_USERS_ROLES] FOREIGN KEY([role])
REFERENCES [dbo].[ROLES] ([name])
GO

ALTER TABLE [dbo].[USERS] CHECK CONSTRAINT [FK_USERS_ROLES]
GO

ALTER TABLE [dbo].[USERS]  WITH CHECK ADD  CONSTRAINT [FK_USERS_TAGS] FOREIGN KEY([rfid])
REFERENCES [dbo].[TAGS] ([rfid])
GO

ALTER TABLE [dbo].[USERS] CHECK CONSTRAINT [FK_USERS_TAGS]
GO

/* ----------------------------------------------------------ACTIVITIES_USERS---------------------------------------------------------- */
CREATE TABLE [dbo].[ACTIVITIES_USERS](
	[username] [varchar](200) NOT NULL,
	[activityId] [bigint] NOT NULL,
	[startDate] [datetime] NOT NULL,
	[endDate] [datetime] NULL,
 CONSTRAINT [PK_ACTIVITIES_USERS] PRIMARY KEY CLUSTERED 
(
	[username] ASC,
	[activityId] ASC,
	[startDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ACTIVITIES_USERS] ADD  CONSTRAINT [DF_ACTIVITIES_USERS_startDate]  DEFAULT (sysdatetime()) FOR [startDate]
GO

ALTER TABLE [dbo].[ACTIVITIES_USERS]  WITH CHECK ADD  CONSTRAINT [FK_ACTIVITIES_USERS_ACTIVITIES] FOREIGN KEY([activityId])
REFERENCES [dbo].[ACTIVITIES] ([id])
GO

ALTER TABLE [dbo].[ACTIVITIES_USERS] CHECK CONSTRAINT [FK_ACTIVITIES_USERS_ACTIVITIES]
GO

ALTER TABLE [dbo].[ACTIVITIES_USERS]  WITH CHECK ADD  CONSTRAINT [FK_ACTIVITIES_USERS_USERS] FOREIGN KEY([username])
REFERENCES [dbo].[USERS] ([username])
GO

ALTER TABLE [dbo].[ACTIVITIES_USERS] CHECK CONSTRAINT [FK_ACTIVITIES_USERS_USERS]
GO

/* ----------------------------------------------------------ESPS---------------------------------------------------------- */
CREATE TABLE [dbo].[ESPS](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[mac] [nvarchar](100) NULL,
	[activityId] [bigint] NULL,
 CONSTRAINT [PK_ESPS] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ESPS]  WITH CHECK ADD  CONSTRAINT [FK_ESPS_ACTIVITIES] FOREIGN KEY([id])
REFERENCES [dbo].[ACTIVITIES] ([id])
GO

ALTER TABLE [dbo].[ESPS] CHECK CONSTRAINT [FK_ESPS_ACTIVITIES]
GO

/* ----------------------------------------------------------MOULDS---------------------------------------------------------- */
CREATE TABLE [dbo].[MOULDS](
	[code] [varchar](100) NOT NULL,
	[description] [varchar](300) NULL,
	[processId] [bigint] NULL,
 CONSTRAINT [PK_Moulds] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[MOULDS]  WITH CHECK ADD  CONSTRAINT [FK_MOULDS_PROCESSES] FOREIGN KEY([processId])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[MOULDS] CHECK CONSTRAINT [FK_MOULDS_PROCESSES]
GO

/* ----------------------------------------------------------PARTS---------------------------------------------------------- */
CREATE TABLE [dbo].[PARTS](
	[code] [varchar](100) NOT NULL,
	[mouldCode] [varchar](100) NULL,
	[description] [varchar](300) NULL,
	[tagRfid] [varchar](100) NULL,
	[processId] [bigint] NULL,
 CONSTRAINT [PK_PARTS] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PARTS]  WITH CHECK ADD  CONSTRAINT [FK_PARTS_MOULDS] FOREIGN KEY([mouldCode])
REFERENCES [dbo].[MOULDS] ([code])
GO

ALTER TABLE [dbo].[PARTS] CHECK CONSTRAINT [FK_PARTS_MOULDS]
GO

ALTER TABLE [dbo].[PARTS]  WITH CHECK ADD  CONSTRAINT [FK_PARTS_PROCESSES] FOREIGN KEY([processId])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[PARTS] CHECK CONSTRAINT [FK_PARTS_PROCESSES]
GO

ALTER TABLE [dbo].[PARTS]  WITH CHECK ADD  CONSTRAINT [FK_PARTS_TAGS] FOREIGN KEY([tagRfid])
REFERENCES [dbo].[TAGS] ([rfid])
GO

ALTER TABLE [dbo].[PARTS] CHECK CONSTRAINT [FK_PARTS_TAGS]
GO

/* ----------------------------------------------------------EVENTS---------------------------------------------------------- */
CREATE TABLE [dbo].[EVENTS](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[caseId] [bigint] NULL,
	[duration] [bigint] NOT NULL,
	[endDate] [datetime2](7) NULL,
	[name] [varchar](300) NULL,
	[resource] [varchar](200) NULL,
	[role] [varchar](100) NULL,
	[startDate] [datetime2](7) NULL,
	[LOG_ID] [bigint] NULL,
	[partCode] [varchar](100) NULL,
	[processId] [bigint] NULL,
	[isEstimatedEnd] [bit] NULL,
	[mouldCode] [varchar](100) NULL,
	[username] [varchar](200) NULL,
	[activityId] [bigint] NULL,
 CONSTRAINT [PK_EVENTS] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[EVENTS] ADD  CONSTRAINT [DF_EVENTS_duration]  DEFAULT ((0)) FOR [duration]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FK_EVENTS_ACTIVITIES] FOREIGN KEY([activityId])
REFERENCES [dbo].[ACTIVITIES] ([id])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FK_EVENTS_ACTIVITIES]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FK_EVENTS_MOULDS] FOREIGN KEY([mouldCode])
REFERENCES [dbo].[MOULDS] ([code])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FK_EVENTS_MOULDS]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FK_EVENTS_PARTS] FOREIGN KEY([partCode])
REFERENCES [dbo].[PARTS] ([code])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FK_EVENTS_PARTS]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FK_EVENTS_PROCESSES] FOREIGN KEY([processId])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FK_EVENTS_PROCESSES]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FK_EVENTS_USERS] FOREIGN KEY([username])
REFERENCES [dbo].[USERS] ([username])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FK_EVENTS_USERS]
GO

ALTER TABLE [dbo].[EVENTS]  WITH CHECK ADD  CONSTRAINT [FKro808iycxm1vfukekdwla0o1b] FOREIGN KEY([LOG_ID])
REFERENCES [dbo].[LOGS] ([id])
GO

ALTER TABLE [dbo].[EVENTS] CHECK CONSTRAINT [FKro808iycxm1vfukekdwla0o1b]
GO