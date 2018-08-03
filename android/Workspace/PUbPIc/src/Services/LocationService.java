package Services;

import com.android.PubPIc.LocationFinder;

import Interfaces.IAsyncLocationProviderOperation;
import Interfaces.ILocationResult;
import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;

public class LocationService
{
	private static LocationService instance;
	private LocationFinder _locationFinder;
	//private LocationManager locationManager;

	public static LocationService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new LocationService();
		}
		return instance;
	}

	

	private LocationService()
	{
		// Constructor hidden because this is a singleton
	}

	public void LoadService(Activity activity)
	{
		_locationFinder = new LocationFinder(activity);
		_locationFinder.startRecording();
	}
	public void RegisterForLocationsChange(IAsyncLocationProviderOperation locationFoundDelegate)
	{
		_locationFinder.RegisterForLocationChange(locationFoundDelegate);
	}
	public void UnRegisterForLocationsChange(IAsyncLocationProviderOperation locationFoundDelegate)
	{
		_locationFinder.UnRegisterForLocationChange(locationFoundDelegate);
	}
	public Location GetLocation()
	{
		return _locationFinder.getLocation();
	}
}
