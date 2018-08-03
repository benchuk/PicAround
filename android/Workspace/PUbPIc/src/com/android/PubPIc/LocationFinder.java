package com.android.PubPIc;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import Interfaces.IAsyncLocationProviderOperation;
import Interfaces.ILocationResult;
import Services.ContainerService;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

import android.util.Log;
import android.widget.Toast;

public class LocationFinder
{
	private boolean gps_recorder_running;
	private Timer gpsTimer = new Timer();
	private String TAG = "Picup";
	private Location lastLocation;
	private long lastprovidertimestamp;
	Activity _activity;

	public LocationFinder(Activity activity) {
		_activity = activity;
	}

	public double getLatitude()
	{
		if (lastLocation != null)
		{
			return lastLocation.getLatitude();
		}
		return 0.404;
	}

	public double getLongitude()
	{
		if (lastLocation != null)
		{
			return lastLocation.getLongitude();
		}
		return 0.404;
	}

	public double getAltitude()
	{
		if (lastLocation != null)
		{
			return lastLocation.getAltitude();
		}
		return 0.404;
	}

	public Location getLocation()
	{
		return lastLocation;
	}

	private ContainerService containerService;

	private LinkedList<IAsyncLocationProviderOperation> _locationFoundDelegates = new LinkedList<IAsyncLocationProviderOperation>();

	public void RegisterForLocationChange(IAsyncLocationProviderOperation locationFoundDelegate)
	{
		_locationFoundDelegates.add(locationFoundDelegate);
	}

	public void UnRegisterForLocationChange(IAsyncLocationProviderOperation locationFoundDelegate)
	{
		_locationFoundDelegates.remove(locationFoundDelegate);
	}

	/**
	 * try to get the 'best' location selected from all providers
	 */
	private Location getBestLocation()
	{
		Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
		Location networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);

		// if we have only one location available, the choice is easy
		if (gpslocation == null)
		{
			Log.i(TAG, "No GPS Location available.");

			return networkLocation;
		}
		else
		{
			return lastLocation = gpslocation;
			// ((PUbPIcActivity) acti).ShowDialog(networkLocation.toString());
		}
//		if (networkLocation == null)
//		{
//			Log.i(TAG, "No Network Location available");
//			return gpslocation;
//		}
//		else
//		{
//			// ((PUbPIcActivity) acti).ShowDialog(gpslocation.toString());
//		}
//
//		// a locationupdate is considered 'old' if its older than the configured
//		// update interval. this means, we didn't get a
//		// update from this provider since the last check
//		long old = System.currentTimeMillis() - getGPSCheckMilliSecsFromPrefs();
//		boolean gpsIsOld = (gpslocation.getTime() < old);
//		boolean networkIsOld = (networkLocation.getTime() < old);
//
//		// gps is current and available, gps is better than network
//		if (!gpsIsOld)
//		{
//			// Log.d(TAG, "Returning current GPS Location");
//			return lastLocation = gpslocation;
//		}
//
//		// gps is old, we can't trust it. use network location
//		if (!networkIsOld)
//		{
//			// Log.d(TAG, "GPS is old, Network is current, returning network");
//			return lastLocation = networkLocation;
//		}
//
//		// both are old return the newer of those two
//		if (gpslocation.getTime() > networkLocation.getTime())
//		{
//			Log.i(TAG, "Both are old, returning gps(newer)");
//			return lastLocation = gpslocation;
//		}
//		else
//		{
//			Log.i(TAG, "Both are old, returning network(newer)");
//			return lastLocation = networkLocation;
//		}
	}

	/**
	 * get the last known location from a specific provider (network/gps)
	 */
	private Location getLocationByProvider(String provider)
	{
		Location location = null;
		if (!isProviderSupported(provider))
		{
			return null;
		}

		LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

		try
		{
			if (locationManager.isProviderEnabled(provider))
			{

				location = locationManager.getLastKnownLocation(provider);

			}
		}
		catch (IllegalArgumentException e)
		{
			// Log.d(TAG, "Cannot acces Provider " + provider);
		}
		return location;
	}

	private boolean isProviderSupported(String provider)
	{
		return true;
	}

	public void startRecording()
	{
		gpsTimer.cancel();
		gpsTimer = new Timer();

		long checkInterval = getGPSCheckMilliSecsFromPrefs();
		long minDistance = getMinDistanceFromPrefs();

		// receive updates
		LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

		for (String s : locationManager.getAllProviders())
		{
			locationManager.requestLocationUpdates(s, checkInterval, minDistance, new LocationListener()

			{

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras)
				{

				}

				@Override
				public void onProviderEnabled(String provider)
				{

				}

				@Override
				public void onProviderDisabled(String provider)
				{

				}

				@Override
				public void onLocationChanged(Location location)
				{
					// if this is a gps location, we can use it
					// if
					// (location.getProvider().equals(LocationManager.GPS_PROVIDER))
					// {
					doLocationUpdate(location, true);
					// }
				}
			});

			// //Toast.makeText(this, "GPS Service STARTED",
			// Toast.LENGTH_LONG).show();
			gps_recorder_running = true;
		}

		// start the gps receiver thread
		gpsTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run()
			{
				Location location = getBestLocation();
				doLocationUpdate(location, false);

			}
		}, 0, checkInterval);

	}

	public Activity getApplicationContext()
	{
		return _activity;
	}

	private long getGPSCheckMilliSecsFromPrefs()
	{
		return 30000;
	}

	private long getMinDistanceFromPrefs()
	{
		return 1000;
	}

	public void doLocationUpdate(final Location l, boolean force)
	{

		if (l == null)
		{
			// ((PUbPIcActivity) acti).runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run()
			// {
			// //((PUbPIcActivity)
			// acti).locationTextView.setText("Location is null");
			// //String message = "Location is null";
			// //Toast.makeText(acti, message, Toast.LENGTH_LONG).show();
			// }
			// });
		}
		else
		{
			lastLocation = l;
			ContainerService.getInstance().Location = l;

			if (_locationFoundDelegates != null)
			{
				for (IAsyncLocationProviderOperation d : _locationFoundDelegates)
				{
					d.LocationFoundCompleted(new LocationFoundResult(l));
				}

			}
			// ((PUbPIcActivity) acti).runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run()
			// {
			//
			// Latitude = l.getLatitude();
			// Longitude = l.getLongitude();
			// //((PUbPIcActivity) acti).locationTextView.setText(l.toString());
			// //String message =
			// String.format(" Locationfinder -Current Location \n Longitude: %1$s \n Latitude: %2$s",
			// l.getLongitude(), l.getLatitude());
			// //Toast.makeText(acti, message, Toast.LENGTH_LONG).show();
			// }
			// });
		}
		long minDistance = getMinDistanceFromPrefs();

		Log.i(TAG, "update received:" + l);
		if (l == null)
		{
			Log.d(TAG, "Empty location");
			if (force)
			// Toast.makeText(this, "Current location not available",
			// Toast.LENGTH_SHORT).show();
			return;
		}
		if (lastLocation != null && l != null)
		{
			float distance = l.distanceTo(lastLocation);
			Log.i(TAG, "Distance to last: " + distance);

			if (l.distanceTo((Location) lastLocation) < minDistance && !force)
			{
				Log.i(TAG, "Position didn't change");
				return;
			}

			if (l.getAccuracy() >= lastLocation.getAccuracy() && l.distanceTo(lastLocation) < l.getAccuracy() && !force)
			{
				Log.i(TAG, "Accuracy got worse and we are still within the accuracy range.. Not updating");
				return;
			}

			if (l.getTime() <= lastprovidertimestamp && !force)
			{
				Log.i(TAG, "Timestamp not never than last");
				return;
			}

		}
		// upload/store your location here
	}
}