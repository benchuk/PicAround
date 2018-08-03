package com.socialcamera.PicAround;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.ImageLoader;
import Services.LazyAdapter;
import Services.ListViewHandler;
import Services.LocationService;
import Services.SensorService;
import Services.Utils;
import WebHelpers.EventParams;
import WebHelpers.EventsResultHandler;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ImageArrivedHandler;
import WebHelpers.ProcessEventsWebResult;
import WebHelpers.ProfileImageLoader;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.socialcamera.PicAround.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class TabNavigation extends SherlockActivity // SherlockFragmentActivity
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	private ListView m_listview;
	String[] eventsResult;
	ArrayList _eventsList;
	ListViewHandler listViewHandler;
	private ArrayList<EventParams> events;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
	SubMenu sub;
	ShareActionProvider actionProvider;
	int resultCodeForRefresh = 123;
	int locationServicesIntentREQUEST = 4456;
	boolean _userDisabledLocationSERVICES = false;
	boolean _waitingForLocation = false;

	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_MENU){
	// if (event.getAction() == KeyEvent.ACTION_DOWN && _menu != null)
	// {
	// _menu.performIdentifierAction(0, 0);
	// return true;
	// }
	// }
	// };
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			// bar = null;
			// m_listview = null;
			// eventsResult = null;
			// _eventsList = null;
			// listViewHandler = null;
			// events = null;
			// mGaInstance = null;
			// mGaTracker = null;
			// ls = null;
			// cs.SelectedEventID = null;
			// cs.SelectedEventName = null;
			// cs = null;
			// fs = null;
			mGaTracker.sendEvent("ui_action_android", "button_press", "Exit app using back button", (long) 1);
			finish();
			System.gc();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			mGaTracker.sendEvent("ui_action_android", "button_press", "Show menu on events view", (long) 1);
			if (event.getAction() == KeyEvent.ACTION_DOWN && _menu != null)
			{
				_menu.performIdentifierAction(0, 0);
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		UploadedEventsHandler.id = 0;
		ls.StartRecordingLocation();
		FacebookService.getInstance().ExtendAccessTokenIfNeeded();
		this.setProgressBarIndeterminateVisibility(false);

		if (cs.Location == null && !_userDisabledLocationSERVICES && !_waitingForLocation && !cs.UserDisabledLocation)
		{
			showErrorDialog();
		}
		if (actionProvider != null)
		{
			actionProvider.setShareIntent(createShareIntent());
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		LocationService.getInstance().StopRecordingLocation();
	};

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/Events");
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		this.getSherlock().setProgressBarIndeterminate(false);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setContentView(R.layout.event_fregment);
		// setTheme(R.style.TextAppearance_Sherlock_Widget_ActionBar_Subtitle_Inverse);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent)
			{
				mGaTracker.sendEvent("ui_action_android", "button_press", "Logout - close events activity", (long) 1);
				finish();
			}
		}, intentFilter);
		
		bar = getSupportActionBar();
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("Searching...");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);
		// bar.setTitle("Activity Title");

		bar.setIcon(R.drawable.picaroundicon);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		if (cs.FacebookUserImage == null)
		{
			mGaTracker.sendEvent("auto_action_android", "load_profile_image", "Try to load profile image", (long) 1);
			// String uid = "http://graph.facebook.com/" + cs.FacebookId +
			// "/picture?type=large";
			ProfileImageLoader getimage = new ProfileImageLoader();
			try
			{
				if (!cs.GooglepluseLogin)
				{
					getimage.GetImageProfile(cs.FacebookId, getApplicationContext(), getResources(), new ImageArrivedHandler()
					{
						@Override
						public void OnImageArrive(final Bitmap bitmap)
						{
							new Handler().post(new Runnable() {
								@Override
								public void run()
								{
									Drawable drawable = new BitmapDrawable(getResources(), bitmap);
									cs.FacebookUserImage = drawable;
									bar.setIcon(cs.FacebookUserImage);
									mGaTracker.sendEvent("auto_action_android", "load_profile_image", "Facebook profile ok", (long) 1);
								}
							});

						};

					});
				}
				else
				{
					getimage.GetGoogleImageProfile(cs.FacebookId, getApplicationContext(), getResources(), new ImageArrivedHandler()
					{
						@Override
						public void OnImageArrive(final Bitmap bitmap)
						{
							new Handler().post(new Runnable() {
								@Override
								public void run()
								{
									Drawable drawable = new BitmapDrawable(getResources(), bitmap);
									cs.FacebookUserImage = drawable;
									bar.setIcon(cs.FacebookUserImage);
									mGaTracker.sendEvent("auto_action_android", "load_profile_image", "G plus profile ok", (long) 1);
								}
							});

						};

					});
				}
			}
			catch (IOException e)
			{
				mGaTracker.sendEvent("auto_action_android", "load_profile_image", e.toString(), (long) 1);
				e.printStackTrace();
			}
		}
		else
		{
			bar.setIcon(cs.FacebookUserImage);
		}

		ls = LocationService.getInstance();
		ls.LoadService(this);
		ls.StartRecordingLocation();

		SensorService sensorService = SensorService.getInstance();
		sensorService.LoadService(this);

		// fs.asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation() {
		// @Override
		// public void GetIdCompleted(String facebookID)
		// {
		// //cs.FacebookId = facebookID;
		// new Handler().post(new Runnable() {
		// @Override
		// public void run() {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }
		// });
		// // new Runnable() {
		// // public void run() {
		// // runOnUiThread(new Runnable() {
		// // public void run()
		// // {
		// //
		// // }
		// // });
		// // }
		// // };
		// }
		// };
		// fs.GetFacebookIdAsync();
		// if (fs.facebookUserID == "0")
		// {
		// fs.GetFacebookIdAsync();
		// }
		// else
		// {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }

		// View view = inflater.inflate(, container, false);
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		//
		// StrictMode.setThreadPolicy(policy);

		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);

		LoadEvents();

		m_listview = (ListView) findViewById(R.id.listView2);
		m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{	
				cs.SelectedEventID = events.get(position).EventId;
				cs.SelectedEventName = events.get(position).EventName;
				cs.SelectedPlace = events.get(position).LocationName;
				
				mGaTracker.sendEvent("ui_action_android", "user_selected_event", cs.SelectedEventName, (long) 1);
				
				Intent gallery = new Intent(TabNavigation.this, GalleryActivity.class);
				TabNavigation.this.startActivity(gallery);
			}
		});
	}

	public void timerDelayRemoveDialog(long time, final Dialog d)
	{
		new Handler().postDelayed(new Runnable() {
			public void run()
			{
				d.dismiss();
				if (cs.Location == null)
				{
					mGaTracker.sendEvent("auto_action_android", "locationProble_timerDelayRemoveDialog,", "EventsViewUserDISABLEDLocationServices", (long) 1);
					_userDisabledLocationSERVICES = true;
					String alert1 = "You have selected not to activate any of you location services or you have no GPS reception";
					String alert2 = "Note that the program might not work as expected...";
					String alert3 = "I will do my best :)";

					AlertDialog.Builder builder = new AlertDialog.Builder(TabNavigation.this);
					builder.setTitle("Where are you?");
					builder.setIcon(R.drawable.ic_action_locate);
					builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{

						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
				else
				{
					mGaTracker.sendEvent("auto_action_android", "locationProble_timerDelayRemoveDialog,", "EventsViewUserACTIVATEDLocationServicesOK", (long) 20);
					LoadEvents();
				}
			}
		}, time);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCodeForRefresh == requestCode && cs.UserAddedNewEvent)
		{
			LoadEvents();
		}
		if (requestCode == locationServicesIntentREQUEST)
		{
			ProgressDialog progress = ProgressDialog.show(this, "processing your actions...", "", true);
			timerDelayRemoveDialog(5000, progress);
			_waitingForLocation = true;

		}
	};

	public void LoadEvents()
	{
		mGaTracker.sendEvent("auto_action_android", "load_events", "Events View - Loading Events", (long) 1);
		
		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);
		SetEventsObjectToView(new ArrayList<EventParams>());

		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("userID", cs.FacebookId));
		if (cs.Location != null)
		{
			NameValuePairs.add(new BasicNameValuePair("lat", Double.toString(cs.Location.getLatitude())));
			NameValuePairs.add(new BasicNameValuePair("lon", Double.toString(cs.Location.getLongitude())));
		}
		else
		{
			NameValuePairs.add(new BasicNameValuePair("lat", "404"));
			NameValuePairs.add(new BasicNameValuePair("lon", "404"));
		}
		String onlyInLocation = new Boolean(true).toString();// new
																// Boolean(!cs.AlowAllEvents).toString();
		NameValuePairs.add(new BasicNameValuePair("onlyInLocation", onlyInLocation));
		NameValuePairs.add(new BasicNameValuePair("radius", "1000"));

		String baseUrl = "http://picaround.azurewebsites.net/Events/FindEventByLocation";
		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		parameters.NameValuePairs = NameValuePairs;
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{
				super.OnWebRequestCompleted(httpResponse);
				EventsWebRequestHandlerParams param = new EventsWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.eventsResultHandler = new EventsResultHandler() {
					@Override
					public void HandleEventResult(ArrayList eventsList)
					{
						if (eventsList == null || eventsList.size() == 0)
						{
							bar.setTitle("No Events Around You");
							bar.setSubtitle("Please Create A New Event.");
							_menu.performIdentifierAction(0, 0);
						}
						else
						{
							bar.setTitle("Events Around You");
							bar.setSubtitle("Select one to join.");
						}
						_eventsList = eventsList;
						mGaTracker.sendEvent("ui_action_android", "load_events", "events loaded ok", (long) 1);
						SetEventsObjectToView(_eventsList);
						TabNavigation.this.setSupportProgressBarIndeterminateVisibility(false);
						TabNavigation.this.getSherlock().setProgressBarIndeterminate(false);
					};
				};
				new ProcessEventsWebResult().execute(param);
			}
		};

		new WebRequest().PostData(parameters);
	}

	public void LoadMyEvents()
	{
		mGaTracker.sendEvent("auto_action_android", "load_my_events,", "EventsView -  loading user events", (long) 1);
		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);
		SetEventsObjectToView(new ArrayList<EventParams>());

		bar.setTitle("Searching...");
		bar.setSubtitle("");

		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("userID", cs.FacebookId));

		String baseUrl = "http://picaround.azurewebsites.net/User/UserEvents";

		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		parameters.NameValuePairs = NameValuePairs;
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{
				super.OnWebRequestCompleted(httpResponse);
				EventsWebRequestHandlerParams param = new EventsWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.eventsResultHandler = new EventsResultHandler() {
					@Override
					public void HandleEventResult(ArrayList eventsList)
					{
						if (eventsList == null || eventsList.size() == 0)
						{
							mGaTracker.sendEvent("auto_action_android", "load_my_events,", "EventsView -  User has no events", (long) 1);
							bar.setTitle("You Have No Events Yet");
							bar.setSubtitle("Join one or create your own");
						}
						else
						{
							mGaTracker.sendEvent("auto_action_android", "load_my_events,", "EventsView -  User events loaded ok", (long) 1);
							bar.setTitle("Your Events");
							bar.setSubtitle("Select one to view.");
						}
						_eventsList = eventsList;
						SetEventsObjectToView(_eventsList);
						TabNavigation.this.setSupportProgressBarIndeterminateVisibility(false);
						TabNavigation.this.getSherlock().setProgressBarIndeterminate(false);
					};
				};
				new ProcessEventsWebResult().execute(param);
			}
		};

		new WebRequest().PostData(parameters);
	}

	public void SetEventsToView(ArrayList<EventParams> eventsResultLocal)
	{
		if (m_listview != null && eventsResultLocal != null)
		{
			// String[] stockArr = new String[eventsResultLocal.size()];
			// stockArr = eventsResultLocal.toArray(stockArr);
			LazyAdapter adapter = new LazyAdapter(this, eventsResultLocal);
			m_listview.setAdapter(adapter);
			this.getSherlock().setProgressBarIndeterminate(false);
		}
	}

	public void SetEventsObjectToView(ArrayList<EventParams> eventsResultLocal)
	{
		if (m_listview != null && eventsResultLocal != null)
		{
			events = eventsResultLocal;
			// String[] stockArr = new String[eventsResultLocal.size()];
			// stockArr = eventsResultLocal.toArray(stockArr);
			LazyAdapter adapter = new LazyAdapter(this, eventsResultLocal);
			m_listview.setAdapter(adapter);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getItemId() == android.R.id.home || item.getItemId() == 0)
		{
			return false;
		}

		switch (item.getItemId())
		{

			case 0:
				return true;
			case 1:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "Create New Event", (long) 1);
				Intent result = new Intent(this, AddNewEventFragment.class);
				startActivityForResult(result, resultCodeForRefresh);
				return true;
			case 2:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "Refresh Events List (Menu)", (long) 1);
				bar.setTitle("Searching...");
				bar.setSubtitle("");
				LoadEvents();
				return true;
			case 3:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "Load User Events", (long) 1);
				LoadMyEvents();
				return true;
			case 4:
				if (item.getTitle().equals("Profile Settings"))
				{
					mGaTracker.sendEvent("ui_action_android", "menu_press", "Load User Profile Setting activity", (long) 1);
					Intent result2 = new Intent(this, ProfileActivity.class);
					startActivity(result2);
				}
				return true;
			default:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "User is sharing the app", (long) 1);
				return super.onOptionsItemSelected(item);
		}
	}

	Menu _menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		_menu = menu;
		sub = menu.addSubMenu("Options");
		sub.add(0, 1, 0, "New event").setIcon(R.drawable.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.add(0, 2, 1, "Check for new events").setIcon(R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.add(0, 3, 2, "Open My events").setIcon(R.drawable.ic_action_user).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.add(0, 4, 3, "Profile Settings").setIcon(R.drawable.settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		getSupportMenuInflater().inflate(R.layout.share_action_provider, sub);
		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		sub.getItem(1).setIcon(R.drawable.ic_action_share).setTitle("Share this app");
		actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		if (actionProvider != null)
		{
			actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
			actionProvider.setShareIntent(createShareIntent());
		}
		return true;
	}

	public void showErrorDialog()
	{
		mGaTracker.sendEvent("auto_action", "locationProblem", "noLocationData", (long) 8);
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
				mGaTracker.sendEvent("auto_action_android", "locationProblem", "user Opened Location Preferences", (long) 9);
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), locationServicesIntentREQUEST);
			}
		}).setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id)
			{
				mGaTracker.sendEvent("auto_action_android", "locationProblem", "user DISABLED Location in Preferences Or no loaction", (long) 10);
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private Intent createShareIntent()
	{
		//mGaTracker.sendEvent("ui_action", "share", "UserIsSharingTheAppLink", (long) 11);
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "Try PicAround for android, you can download it here: http://picaround.com/Mobile/share/download");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Try PicAround, download it for your android...");
		return shareIntent;
	}
}