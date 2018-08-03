package com.socialcamera.PicAround;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.socialcamera.PicAround.R;
import com.facebook.FacebookException;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Interfaces.IPlacesResult;
import Services.ContainerService;
import Services.FacebookService;
import Services.ImageLoader;
import Services.LocationService;
import Services.PlacesParams;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewEventFragment extends SherlockFragmentActivity
{
	private String TAG = "Picup";
	EditText eventName;
	com.socialcamera.PicAround.NumberPicker eventDuration;
	// DatePicker newEventDate;
	// TimePicker eventStartTime;
	// TimePicker eventEndTime;
	Button ok;
	Button cancel;
	Button placesButton;
	private FacebookService fs;
	private ContainerService cs;
	ArrayList<PlacesParams> placesResult;
	ProgressDialog progress;
	ProgressDialog progressEvent;
	boolean userAskedForlocationServicesIntentREQUEST = false;
	AlertDialog alert;
	boolean _userDisabledLocationSERVICES = false;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
	ActionBar bar;

	@Override
	public void onStart()
	{
		super.onStart();

		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/AddNewEvent");
	}

	@Override
	public void onStop()
	{
		super.onStop();

		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	public void showErrorDialog()
	{
		mGaTracker.sendEvent("auto_action", "locationProble,", "AddNewEventLocationServicesAreOff", (long) 16);

		String alert1 = "Your location services are turned off.";
		String alert2 = "You can activate network based location or GPS location:";
		String alert3 = "I Recommend truning on both GPS location and network location, for better performance";
		String alert4 = "Please note that network based location are less accurate and GPS will work only under clear skies";

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Where are you?");
		builder.setIcon(R.drawable.ic_launcher_settings);
		builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4).setCancelable(false).setPositiveButton("Open preferences?", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				mGaTracker.sendEvent("auto_action", "locationProble,", "AddNewEventUserOpendLocationSettings", (long) 17);
				userAskedForlocationServicesIntentREQUEST = true;
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
			}
		}).setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id)
			{
				mGaTracker.sendEvent("auto_action", "locationProble,", "AddNewEventUserDISABLEDLocationSettings", (long) 18);
				cs.UserDisabledLocation = true;
				dialog.cancel();
				finish();

			}
		});
		alert = builder.create();
		alert.show();
	}

	public void timerDelayRemoveDialogLocation(long time, final Dialog d)
	{
		new Handler().postDelayed(new Runnable() {
			public void run()
			{
				d.dismiss();
				if (cs.Location == null)
				{
					mGaTracker.sendEvent("auto_action", "locationProble_timerDelayRemoveDialogLocation,", "AddNewEventUserDisabledLocationServices", (long) 15);
					_userDisabledLocationSERVICES = true;
					String alert1 = "You have selected not to activate any of you location services or you have no GPS reception";
					String alert2 = "Note that the program might not work as expected...";
					String alert3 = "I will do my best :)";

					AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEventFragment.this);
					builder.setTitle("Where are you?");
					builder.setIcon(R.drawable.ic_action_locate);
					builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							cs.UserDisabledLocation = true;
							finish();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
				else
				{
					mGaTracker.sendEvent("auto_action", "locationProble_timerDelayRemoveDialogLocation,", "AddNewEventUserACTIVATEDLocationServicesOK", (long) 15);
					Init();
				}
			}
		}, time);
	}

	public void timerDelayRemoveDialog(long time, final Dialog d)
	{
		new Handler().postDelayed(new Runnable() {
			public void run()
			{
				if (!_progressIsClosed)
				{
					if (d != null)
					{
						d.dismiss();
					}
				}
			}
		}, time);
	}

	private GoogleMap mMap;

	private void setUpMap()
	{

		mMap.addMarker(new MarkerOptions().position(new LatLng(cs.Location.getLatitude(), cs.Location.getLongitude())).title("You are here"));
		// Move the camera instantly to hamburg with a zoom of 15.
		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(, 15));

		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(cs.Location.getLatitude(), cs.Location.getLongitude())) // Sets
																																				// the
																																				// center
																																				// of
																																				// the
																																				// map
																																				// to
																																				// Mountain
																																				// View
		.zoom(16) // Sets the zoom
		.tilt(40) // Sets the tilt of the camera to 30 degrees
		.build(); // Creates a CameraPosition from the builder
		// Zoom in, animating the camera.
		// mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
	}

	private void setUpMapIfNeeded()
	{
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null)
		{

			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// findViewById(R.id.map)
			// Check if we were successful in obtaining the map.

		}
		if (mMap != null)
		{
			setUpMap();
		}
	}

	boolean _progressIsClosed = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (cs.SelectedFBPlace != null)
		{
			placesButton.setText(cs.SelectedFBPlace);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");

		setContentView(R.layout.new_event_layout);
		setUpMapIfNeeded();
		ok = (Button) findViewById(R.id.ok__createnewevent);
		cancel = (Button) findViewById(R.id.cancel__createnewevent);
		// m_spinner = (Spinner) findViewById(R.id.places_spinner);
		placesButton = (Button) findViewById(R.id.open_places_picker_button);
		final Intent intent = new Intent(this, PlacePickerFragmentActivity.class);
		placesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{

				
				if (cs.GooglepluseLogin)
				{
					final ProgressDialog p = ProgressDialog.show(AddNewEventFragment.this, "Loading places around you", "Please wait...");
//					AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEventFragment.this);
//					builder.setTitle("Loading places around you");
//					builder.setIcon(R.drawable.ic_action_locate);
//					builder.setMessage("Please Wait...");
//					final AlertDialog alert = builder.create();
//					alert.show();
					fs.GetPlacesAsync(cs.Location, new FacebookAsyncPlacesSearch()
					{

						@Override
						public void searchComleted(IPlacesResult places)
						{
							cs.GooglePlaces = places.GetPlaces();
							Intent intent = new Intent(getBaseContext(), GooglePlusPlacesActivity.class);
							startActivityForResult(intent, 987);
							p.dismiss();
						}
					});
				}
				else
				{
					// startActivityForResult(result, resultCodeForRefresh);
					startActivityForResult(intent, 456);
				}
			}
		});

		bar = getSupportActionBar();
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("Create New Event");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);

		bar.setIcon(cs.FacebookUserImage);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setUpMapIfNeeded();
		if (userAskedForlocationServicesIntentREQUEST)
		{
			alert.dismiss();
			ProgressDialog progress = ProgressDialog.show(this, "processing your actions...", "", true);
			timerDelayRemoveDialogLocation(5000, progress);
		}
		else
		{
			if (cs.UserDisabledLocation)
			{
				AlertCannotAddEventWithoutLocationServices();
			}
			if (!_userDisabledLocationSERVICES && !cs.UserDisabledLocation)
			{
				Init();
			}
		}
	};

	private void AlertCannotAddEventWithoutLocationServices()
	{
		mGaTracker.sendEvent("ui_action", "AddNewEvent", "UserIsTryingToAddEventWithoutLocationOrEventName", (long) 14);
		_userDisabledLocationSERVICES = true;
		String alert1 = "Sorry cannot add new events without a location service.";

		AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEventFragment.this);
		builder.setTitle("Cannot create a new event.");
		builder.setIcon(R.drawable.ic_action_locate);
		builder.setMessage(alert1).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	boolean _suggestedGPSsettingsToUser = false;

	public void Init()
	{
		if (cs.Location != null)
		{
			// progress = ProgressDialog.show(this, "Create New Event",
			// "Loading facebook places, please wait...", true);
			// timerDelayRemoveDialog(5000, progress);

			// if (m_spinner != null)
			// {
			// final List<String> list = new ArrayList<String>();
			// for (PlacesParams nameValuePair : placesResult)
			// {
			// list.add(nameValuePair.Name);
			// }

			// ArrayAdapter<String> adp1 = new
			// ArrayAdapter<String>(AddNewEventFragment.this,
			// android.R.layout.simple_list_item_1, list);
			// adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// m_spinner.setAdapter(adp1);
			// progress.dismiss();
			// _progressIsClosed = true;
			// }
			//
			// }
			// });
		}
		else
		{
			if (!_suggestedGPSsettingsToUser)
			{
				showErrorDialog();
			}
		}

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				eventName = (EditText) findViewById(R.id.editText_new_event_name);
				String locationName = null;
				String placeName = (String) placesButton.getText();
				if (placeName.equalsIgnoreCase("Click to Select..."))
				{
					placeName = null;
				}
				locationName = placeName;
				if (eventName != null && eventName.getText().toString().trim().length() != 0 && locationName != null && locationName.trim().length() != 0)
				{

					progressEvent = ProgressDialog.show(AddNewEventFragment.this, "Create New Event", "Creating Event, please wait...", true);
					timerDelayRemoveDialog(5000, progress);

					// eventDuration = (com.android.PubPIc.NumberPicker)
					// findViewById(R.id.editText_event_duration);
					// int eventDurationInt = eventDuration.getValue();
					int eventDurationInt = 24;

					DateFormat.getDateTimeInstance();

					Date now = new Date();

					Calendar cal = Calendar.getInstance(); // creates calendar
					cal.setTime(new Date()); // sets calendar time/date
					cal.add(Calendar.HOUR_OF_DAY, eventDurationInt); // adds
																		// eventDurationInt
																		// hours
					Date endTime = cal.getTime(); // returns new date object,
													// eventDurationInt hours in
													// the
													// future

					ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
					NameValuePairs.add(new BasicNameValuePair("userID", cs.FacebookId));
					NameValuePairs.add(new BasicNameValuePair("eventName", eventName.getText().toString()));
					NameValuePairs.add(new BasicNameValuePair("eventStartDateTime", Long.toString(now.getTime())));
					NameValuePairs.add(new BasicNameValuePair("eventEndtDateTime", Long.toString(endTime.getTime())));
					if (cs.Location != null)
					{
						NameValuePairs.add(new BasicNameValuePair("placeLat", Double.toString(cs.Location.getLatitude())));
						NameValuePairs.add(new BasicNameValuePair("placeLong", Double.toString(cs.Location.getLongitude())));
					}
					else
					{
						NameValuePairs.add(new BasicNameValuePair("placeLat", "404"));
						NameValuePairs.add(new BasicNameValuePair("placeLong", "404"));
					}
					locationName = "NOT_SET";

					locationName = (String) placesButton.getText();

					NameValuePairs.add(new BasicNameValuePair("locationID", locationName));// should
																							// be
																							// facebook
																							// location
																							// id.
					String baseUrl = "http://picaround.azurewebsites.net/Events/CreateNewEvent";
					HttpRequestParameters parameters = new HttpRequestParameters();
					parameters.url = baseUrl;
					parameters.NameValuePairs = NameValuePairs;

					parameters.webRequestHandler = new WebRequestResultHandler()
					{

						@SuppressWarnings("unchecked")
						@Override
						public void OnWebRequestCompleted(HttpResponse httpResponse)
						{

							// TODO Auto-generated method stub
							super.OnWebRequestCompleted(httpResponse);
							EventsWebRequestHandlerParams param = new EventsWebRequestHandlerParams();
							param.httpResponse = httpResponse;

							try
							{

								if (httpResponse.getStatusLine().getStatusCode() == 200)
								{
									HttpEntity entity = httpResponse.getEntity();
									InputStream is = null;
									try
									{
										is = entity.getContent();
									}
									catch (IllegalStateException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									catch (IOException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();

									}

									String response = convertStreamToString(is);
									cs.SelectedEventID = response.replace("\n", "");
									cs.UserAddedNewEvent = true;
								}
								else
								{
									Toast.makeText(AddNewEventFragment.this, "Failed to create new event", Toast.LENGTH_SHORT);
								}
								progressEvent.dismiss();
								finish();
							}
							catch (IllegalStateException e)
							{
								Tracker myTracker = EasyTracker.getTracker();
								myTracker.sendException(e.getMessage(), false);
								e.printStackTrace();
								Log.i(TAG, "Picup - failed to create new event: Reason" + e.toString());
							}

						}

					};

					// Create new event on server
					new WebRequest().PostData(parameters);
					// ContainerService.getInstance().ActionBar.setSelectedNavigationItem(2);
				}
				else
				{
					String alert1 = "Please fill the event name:";
					String alert2 = "For Example if you are at Jenna's wedding your best friend, just type";
					String alert3 = " -- Jenna's wedding -- ";
					String alert4 = "And select where the wedding is taking place - Enjoy!";

					AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEventFragment.this);
					builder.setTitle("Event Name or place, cannot be empty");
					builder.setIcon(R.drawable.ic_action_edit);
					builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4).setCancelable(false).setPositiveButton("Got it", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.dismiss();
						}
					});
					AlertDialog missingEventNameDialog = builder.create();
					missingEventNameDialog.show();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// ContainerService.getInstance().TabNavigation.tabSwap("NewEventFregment","EventsFragment",
				// EventsFragment.class.getName());
				AddNewEventFragment.this.finish();
			}
		});
	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState)
	// {
	// super.onActivityCreated(savedInstanceState);
	// }

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState)
	// {
	// //
	// getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
	//
	//
	//
	// return view;
	// }

	private String convertStreamToString(InputStream is)
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append((line + "\n"));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
