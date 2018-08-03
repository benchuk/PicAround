

USE [Picup]
go
 CREATE TRIGGER [trg_pics_Insert]
 ON Pics
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