/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 100000 [PicId]
      ,[Description]
      ,[GpsLocation]
      ,[UserId_FK]
      ,[EventId_FK]
      ,[Link]
      ,[DateTaken]
      ,[GpsLocation_Bin]
      ,[ThumbLink]
      ,[MidQLink]
  FROM [Picup].[dbo].[Pics] order by PicId desc