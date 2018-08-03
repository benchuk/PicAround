Insert into Locations (Name,GpsLocation,GpsLocation_Bin) values
 ('????? ?????? ?????',(SELECT GpsLocation from Pics where id=413),(SELECT GpsLocation_Bin from Pics where id=413))
