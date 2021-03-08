USE [dbdcdeadcd7ddb447e9f4bab1000bd996d]
GO

/****** Object:  Table [dbo].[PROCESSES]    Script Date: 05/04/2020 01:55:15 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PROCESSES](
	[id] [bigint] NOT NULL,
	[description] [varchar](500) NULL,
	[end_date] [datetime2](7) NULL,
	[start_date] [datetime2](7) NULL,
	[name] [varchar](300) NULL,
	[numberOfActivities] [int] NOT NULL,
	[numberOfCases] [int] NOT NULL,
	[parent_process] [bigint] NULL,
 CONSTRAINT [PK_PROCESSES] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PROCESSES]  WITH CHECK ADD  CONSTRAINT [FK_PROCESSES_PROCESSES] FOREIGN KEY([parent_process])
REFERENCES [dbo].[PROCESSES] ([id])
GO

ALTER TABLE [dbo].[PROCESSES] CHECK CONSTRAINT [FK_PROCESSES_PROCESSES]
GO

