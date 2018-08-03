USE Picup
GO

  --UPDATE Pics set GpsLocation = GPSdata
  

  UPDATE
    Pics
SET
    GpsLocation = res.GPSdatag, GpsLocation_Bin = res.gpsdatab
 
FROM
	 (select GpsLocation as GPSdatag , GpsLocation_Bin AS gpsdatab, EventId FROM Locations l INNER JOIN  Events e ON l.LocationId = e.LocationId_FK) as res

	 where EventId_FK = res.EventId


--INNER JOIN
  
--ON
--    Pics.EventId_FK = other_table.id

	
--SELECT EndDate,EventId,EventName,LocationId,LocationName,StartDate,ThumbLink From
--(SELECT e.EndDate as EndDate, e.EventId as EventId , e.EventName as EventName, l.LocationId as LocationId, l.LocationName as LocationName, e.StartDate as StartDate  FROM Locations l INNER JOIN Events e ON l.LocationId = e.LocationId_FK WHERE l.GpsLocation.STDistance(@UserLocation) <= @Radius) as res
--LEFT JOIN  (Select TOP 1 ThumbLink, EventId_FK  From Pics ) as ppp ON res.EventId = ppp.EventId_FK