/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 10000 [Id]
      ,[Description]
      ,[GpsLocation]
      ,[UserId]
      ,[EventId]
      ,[Link]
      ,[DateTaken]
      ,[GpsLocation_Bin]
      ,[ThumbLink]
  FROM [Picup].[dbo].[Pics] order by id desc