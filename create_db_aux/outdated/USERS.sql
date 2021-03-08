USE dbdcdeadcd7ddb447e9f4bab1000bd996d
GO

/****** Object:  Table [dbo].[USERS]    Script Date: 25/03/2020 01:19:32 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[USERS](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[password] [varchar](255) NULL,
	[username] [varchar](255) NULL,
	[role] [varchar](300) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

