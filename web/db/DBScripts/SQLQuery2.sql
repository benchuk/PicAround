USE [Picup]
GO
/****** Object:  Trigger [dbo].[trg_pics_Insert]    Script Date: 6/26/2012 2:04:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 ALTER TRIGGER [dbo].[trg_pics_Insert]
 ON [dbo].[Pics]
AFTER INSERT 
as
DECLARE @geo geography;
DECLARE @id int;
SET @geo = geography::STPointFromWKB((SELECT [GpsLocation_Bin] FROM inserted), 4326);
SET @id = (SELECT PicId FROM inserted);
 BEGIN
 --INSERT INTO [dbo].[Pics]([Description],[GpsLocation],UserId,EventId,Link,DateTaken,GpsLocation_Bin,ThumbLink)
 --SELECT [Description],,UserId,EventId,Link,DateTaken,GpsLocation_Bin,ThumbLink
 --FROM inserted
  UPDATE Pics set GpsLocation = @geo WHERE PicId = @id
 END


