package com.android.PubPIc;

import android.location.Location;
import com.facebook.android.Facebook;
import android.content.Context;

public class FacebookPlaceParams
{
	public Location Location;
	public Facebook Facebook;
	public String FacebookUserID;
	public String AccessToken;
	public FacebookPlaceParams(Location location, Facebook facebook, String accessToken)
	{
		Location=location;
		Facebook = facebook;
		AccessToken = accessToken;
	}
	
}
