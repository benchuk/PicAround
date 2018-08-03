package com.android.PubPIc;

public class UploadFileParams
{
	 String UserID;
	 String Latitude;
	 String Longtitude;
	 String Altitude;
	 String Heading;
	 String Place;
	 String EventLabel;
	 String EventID;
	
	 public UploadFileParams(String userID, String latitude, String longtitude, String altitude, String heading, String place, String eventLabel, String eventID)
	 {
		 UserID = userID;
		 Latitude = latitude;
		 Longtitude = longtitude;
		 Altitude = altitude;
		 Heading = heading;
		 Place = place;
		 EventLabel = eventLabel;
		 EventID = eventID;
	 }

}
