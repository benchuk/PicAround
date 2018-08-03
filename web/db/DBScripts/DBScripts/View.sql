use picup
go
CREATE VIEW [vw_picsAsBinary]
 AS
 SELECT Id, [Description],CONVERT(VARBINARY(MAX), [GpsLocation], 0) AS [GpsLocation],UserId,EventId,Link,DateTaken
 FROM Pics


