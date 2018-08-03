package com.android.PubPIc;

import android.location.Location;
import Interfaces.ILocationResult;

public class LocationFoundResult implements ILocationResult
{

	public LocationFoundResult(Location location)
	{
		_location = location;
	}
	Location _location;
	@Override
	public Location GetLocation()
	{
		// TODO Auto-generated method stub
		return _location;
	}

}
