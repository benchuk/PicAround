USE Picup;
GO
CREATE PROCEDURE IsLocationExistsInRadius
    @Lat float, 
    @Long float,
	@Radius int
AS 
DECLARE @UserLocation geography
SET @UserLocation = geography::Point(@Lat, @Long, 4326)
SELECT * FROM Locations WHERE GpsLocation.STDistance(@UserLocation) <= @Radius
GO


USE Picup;
GO
CREATE PROCEDURE GetEventsInRadius
    @Lat float, 
    @Long float,
	@Radius int
AS 
DECLARE @UserLocation geography
SET @UserLocation = geography::Point(@Lat, @Long, 4326)

SELECT EndDate,EventId,EventName,LocationId,LocationName,StartDate,ThumbLink From
(SELECT e.EndDate as EndDate, e.EventId as EventId , e.EventName as EventName, l.LocationId as LocationId, l.LocationName as LocationName, e.StartDate as StartDate  FROM Locations l INNER JOIN Events e ON l.LocationId = e.LocationId_FK WHERE l.GpsLocation.STDistance(@UserLocation) <= @Radius) as res
LEFT JOIN  (Select  top 1 ThumbLink, EventId_FK  From Pics ) as ppp ON res.EventId = ppp.EventId_FK
GO
