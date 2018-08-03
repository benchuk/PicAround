
--USE Picup;
--GO
--IF OBJECT_ID ('Pics.trgAfterInsert', 'TR') IS NOT NULL
--   DROP TRIGGER Pics.trgAfterInsert;
--GO
 CREATE TRIGGER trgAfterInsert ON Pics
FOR INSERT  
AS
	 declare @gpsLocationg geography;
	 declare @GpsLocationb  varbinary(max);
	 select @GpsLocationb=i.GpsLocation_Bin from inserted i;	
	declare @Description nvarchar(max);
	declare @UserId int;
	declare @EventId int;
	declare @Link nvarchar(max);
	declare @ThumbLink  nvarchar(max);
	declare @DateTaken datetime;
	declare @GpsLocation_Bin varbinary(max);

	select @Description=i.[Description] from inserted i;	
	select @UserId=i.UserId from inserted i;	
	select @EventId=i.EventId from inserted i;	
	select @Link=i.Link from inserted i;	
	 @ThumbLink= (select Pics.t from inserted i);	
	select @DateTaken=i.DateTaken from inserted i;	
	select @GpsLocation_Bin=i.GpsLocation_Bin from inserted i;	


	SET @gpsLocationg = geography::STPointFromWKB(@GpsLocationb,4326)

	insert into [Pics]
        (DateTaken,[Description],EventId,GpsLocation_Bin,UserId,Link,[GpsLocation],[ThumbLink])
	values(@DateTaken,@Description, @EventId, @GpsLocation_Bin,@UserId,@Link,@gpsLocationg,@ThumbLink);

	PRINT 'AFTER INSERT trigger fired.'
GO