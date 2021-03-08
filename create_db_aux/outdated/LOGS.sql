USE dbdcdeadcd7ddb447e9f4bab1000bd996d
GO

/****** Object:  Table [dbo].[LOGS]    Script Date: 24/03/2020 23:31:54 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

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

