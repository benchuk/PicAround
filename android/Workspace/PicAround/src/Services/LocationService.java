package Services;

import com.socialcamera.PicAround.LocationFinder;

import Interfaces.IAsyncLocationProviderOperation;
import android.app.Activity;
import android.location.Location;

public class LocationService
{
	private static LocationService instance;
	private LocationFinder _locationFinder;

	// private LocationManager locationManager;

	public static LocationService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new LocationService();
		}
		return instance;
	}

	private LocationService() {
		// Constructor hidden because this is a singleton
	}

	public void StartRecordingLocation()
	{
		if (_locationFinder != null)
		{
			_locationFinder.startRecording();
		}
		else
		{
			_locationFinder = new LocationFinder(_activity);
			_locationFinder.startRecording();
		}
	}

	public void StopRecordingLocation()
	{
		if (_locationFinder != null)
		{
			_locationFinder.stopRecording();
		}
		else
		{
			_locationFinder = new LocationFinder(_activity);
			_locationFinder.stopRecording();
		}
	}
	Activity _activity;
	public void LoadService(Activity activity)
	{
		_activity = activity;
		_locationFinder = new LocationFinder(activity);
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
