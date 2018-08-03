package com.android.PubPIc;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.LazyAdapter;
import Services.ListViewHandler;
import Services.LocationService;
import Services.SensorService;
import WebHelpers.EventParams;
import WebHelpers.EventsResultHandler;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.FindIfEventsExistsResultHandler;
import WebHelpers.FindIfEventsExsitsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ProcessEventsWebResult;
import WebHelpers.ProcessFindIfEventsExistsWebResult;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;

public class TabNavigation extends SherlockActivity // SherlockFragmentActivity
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	private ListView m_listview;
	private Activity context;
	String[] eventsResult;
	ArrayList _eventsList;
	ListViewHandler listViewHandler;
	private HttpResponse response;
	private ArrayList<EventParams> events;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);

		super.onCreate(savedInstanceState);
		// setTheme(R.style.TextAppearance_Sherlock_Widget_ActionBar_Subtitle_Inverse);

		bar = getSupportActionBar();

		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);
		// bar.setTitle("Activity Title");

		bar.setIcon(R.drawable.abs__spinner_48_inner_holo);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		// bar.addTab(bar.newTab().setIcon(R.drawable.placewhite).setTabListener(new
		// TabListener<PlacesFragment>(this, new String[]
		// {
		// "PlacesFragment"
		// }, PlacesFragment.class, null)));
		//
		// bar.addTab(bar.newTab().setIcon(R.drawable.eventwhite).setTabListener(new
		// TabListener<EventsFragment>(this, new String[]
		// {
		// "EventsFragment", "NewEventFregment"
		// }, EventsFragment.class, null)));
		// bar.addTab(bar.newTab().setIcon(R.drawable.camerawhite).setTabListener(new
		// TabListener<CameraFragment>(this, new String[]
		// {
		// "CameraFragment"
		// }, CameraFragment.class, null)));
		// bar.addTab(bar.newTab().setIcon(R.drawable.gallerywhite).setTabListener(new
		// TabListener<GalleryFragment>(this, new String[]
		// {
		// "GalleryFragment"
		// }, GalleryFragment.class, null)));
		// bar.addTab(bar.newTab().setIcon(R.drawable.feed).setTabListener(new
		// TabListener<FeedEventsFragment>(this, new String[]
		// {
		// "FeedEventsFragment"
		// }, FeedEventsFragment.class, null)));
		bar.setDisplayUseLogoEnabled(false);
		setSupportProgressBarIndeterminateVisibility(false);

		// if (savedInstanceState != null)
		// {
		//
		// bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		// }

		ls = LocationService.getInstance();
		ls.LoadService(this);

		SensorService sensorService = SensorService.getInstance();
		sensorService.LoadService(this);

		cs = ContainerService.getInstance();
		cs.TabNavigation = this;
		cs.ActionBar = bar;

		fs.getInstance().asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation()
		{
			@Override
			public void GetIdCompleted(String facebookID)
			{
				bar.setIcon(ContainerService.getInstance().FacebookUserImage);
				// isThereAreEventsNearToYou();
			}
		};
		fs.GetFacebookIdAsync();

		setContentView(R.layout.event_fregment);
		// View view = inflater.inflate(, container, false);

		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		//
		// StrictMode.setThreadPolicy(policy);
		LoadEvents();
		// Click event for single list row

		m_listview = (ListView) findViewById(R.id.listView2);
		SetEventsToView(_eventsList);

		m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				cs.SelectedEventID = events.get(position).EventId;
				cs.SelectedEventName = events.get(position).EventName;
				cs.SelectedPlace = events.get(position).LocationName;
				cs.ActionBar.setTitle(cs.SelectedEventName);
				cs.ActionBar.setSubtitle(cs.SelectedPlace);
				// CharSequence place = ((TextView) view).getText();
				// cs.SelectedEvent = place.toString();
				// cs.ActionBar.setSelectedNavigationItem(2);
				Intent gallery = new Intent(TabNavigation.this, GalleryActivity.class);
				TabNavigation.this.startActivity(gallery);
			}
		});

		// Intent result = new Intent(this,DialogEventsNearToYou.class);
		// result.setClassName("com.android.PubPIc","DialogEventsNearToYou");
		// / startActivity(result);
		// Dialog a = new Dialog(DialogEventsNearToYou.this,
		// R.style.DialogWindowTitle_Sherlock);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCodeForRefresh == requestCode)
		{
			LoadEvents();
		}
	};

	public void LoadEvents()
	{
		SetEventsObjectToView(new ArrayList<EventParams>());
		setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("userID", fs.facebookUserID));
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
		String onlyInLocation = new Boolean(!cs.AlowAllEvents).toString();
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

				// TODO Auto-generated method stub
				super.OnWebRequestCompleted(httpResponse);
				EventsWebRequestHandlerParams param = new EventsWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				response = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.eventsResultHandler = new EventsResultHandler() {
					@Override
					public void HandleEventResult(ArrayList eventsList)
					{

						_eventsList = eventsList;
						SetEventsObjectToView(_eventsList);
						setSupportProgressBarIndeterminateVisibility(false);
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
			context = this;
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
			context = this;

			LazyAdapter adapter = new LazyAdapter(this, eventsResultLocal);
			m_listview.setAdapter(adapter);
		}
	}

	int resultCodeForRefresh = 123;

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0)
		{
			return false;
		}
		int THEME = item.getItemId();
		// Toast.makeText(this, "\"" + item.getTitle() + "\"",
		// Toast.LENGTH_SHORT).show();
		// Intent result = new Intent(this,AddNewEventFragment.class);
		// // result.setClassName("com.android.PubPIc","DialogEventsNearToYou");
		// startActivity(result);
		// return true;
		// ((Menu) item).findItem(R.drawable.icon).setVisible(false);
		// MenuItem switchButton = ((Menu) item).findItem(R.drawable.icon);

		switch (item.getItemId())
		{

			case android.R.id.home:
				// tabSwap( "EventsFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
				// app icon in action bar clicked; go home
				// Intent intent = new Intent(this, CameraIntent.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				return true;
			case 1:
				Intent result = new Intent(this, AddNewEventFragment.class);
				// result.setClassName("com.android.PubPIc","DialogEventsNearToYou");
				LoadEvents();
				startActivityForResult(result, resultCodeForRefresh);
				return true;
			case 2:
				cs.AlowAllEvents = !cs.AlowAllEvents;
				if (cs.AlowAllEvents)
				{
					sub.getItem(1).setIcon(R.drawable.rating_good);
				}
				else
				{
					sub.getItem(1).setIcon(R.drawable.rating_bad);
				}
				LoadEvents();
				return true;
				// tabSwap( "PlacesFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
			default:
				return super.onOptionsItemSelected(item);
				// tabSwap( "EventsFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
				// FragmentTransaction ft =
				// this.getSupportFragmentManager().beginTransaction();
				// Fragment mFragment =
				// this.getSupportFragmentManager().findFragmentByTag("tab2");
				// Fragment mFragment1 =
				// this.getSupportFragmentManager().findFragmentByTag("tab5");
				// ft.hide(mFragment);
				// if (mFragment1 == null)
				// {
				// mFragment = Fragment.instantiate(this,
				// GalleryFragment.class.getName(), null);
				// ft.add(android.R.id.content, mFragment, "tab5");
				// ft.commit();
				// }
				// ft.add(android.R.id.content, mFragment1, "tab5");
				// ft.commit();
				// bar.removeTab(bar.getSelectedTab());
				// bar.addTab(bar.newTab().setIcon(R.drawable.placewhite).setTabListener(new
				// TabListener<PlacesFragment>(this, "tab2",
				// PlacesFragment.class, null)),1,true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// super.onSaveInstanceState(outState);
		// outState.putInt("tab",
		// getSupportActionBar().getSelectedNavigationIndex());
	}

	SubMenu sub;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		sub = menu.addSubMenu("Options");
		cs.subMenu = sub;

		// Used to put dark icons on light action bar
		// boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

		sub.add(0, 1, 0, "New event").setIcon(R.drawable.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		sub.add(0, 2, 1, "See all events").setIcon(R.drawable.rating_bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// sub.add("Search").setIcon(true ? R.drawable.ic_search_inverse :
		// R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// sub.add("Refresh").setIcon(true ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Save").setIcon(true ? R.drawable.ic_compose_inverse :
		// R.drawable.ic_compose).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Search").setIcon(true ? R.drawable.ic_search_inverse :
		// R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Refresh").setIcon(true ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		FacebookService.getInstance().ExtendAccessTokenIfNeeded(this);
		this.setProgressBarIndeterminateVisibility(false);
	}
	
	public void tabSwap(String currentfregmentName, String newFregmentName, String newClassName)
	{
		// FragmentTransaction ft =
		// this.getSupportFragmentManager().beginTransaction();
		// Fragment mFragment =
		// this.getSupportFragmentManager().findFragmentByTag(currentfregmentName);
		// Fragment mFragment1 =
		// this.getSupportFragmentManager().findFragmentByTag(newFregmentName);
		// // ft.hide(mFragment);
		//
		// if (mFragment1 == null)
		// {
		//
		// mFragment1 = Fragment.instantiate(this, newClassName, null);
		// ft.add(android.R.id.content, mFragment1, newFregmentName);
		//
		// ft.detach(mFragment);
		// ft.commit();
		//
		// }
		// else
		// {
		// ft.detach(mFragment);
		// ft.attach(mFragment1);
		// ft.commit();
		// }
		// // ft.commit();
	}

	private void isThereAreEventsNearToYou()
	{
		LocationService ls = LocationService.getInstance();
		ContainerService cs = ContainerService.getInstance();
		FacebookService fs = FacebookService.getInstance();
		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("userID", fs.facebookUserID));
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
		String onlyInLocation = new Boolean(!cs.AlowAllEvents).toString();
		NameValuePairs.add(new BasicNameValuePair("onlyInLocation", onlyInLocation));
		NameValuePairs.add(new BasicNameValuePair("radius", "1000"));

		String baseUrl = "http://picaround.azurewebsites.net/Events/IsEventNearYou";
		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		parameters.NameValuePairs = NameValuePairs;
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{

				// TODO Auto-generated method stub
				super.OnWebRequestCompleted(httpResponse);
				FindIfEventsExsitsWebRequestHandlerParams param = new FindIfEventsExsitsWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				HttpResponse response = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.eventsResultHandler = new FindIfEventsExistsResultHandler()
				{
					@Override
					public void HandleEventResult(Boolean eventsList)
					{
						if (eventsList == true)
						{
							isEventAvailable();
						}

					};
				};

				new ProcessFindIfEventsExistsWebResult().execute(param);

			}

		};

		new WebRequest().PostData(parameters);

	}

	private void isEventAvailable()
	{
		Intent result = new Intent(this, DialogEventsNearToYou.class);
		// result.setClassName("com.android.PubPIc","DialogEventsNearToYou");
		startActivity(result);
	}

	public class TabListener<T extends Fragment> implements ActionBar.TabListener
	{
		private final FragmentActivity mActivity;
		private final String[] mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment[] mFragment;

		public TabListener(FragmentActivity activity, String[] tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;
			FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			int i = 0;
			mFragment = new Fragment[tag.length];
			for (String s : tag)
			{
				mFragment[i] = mActivity.getSupportFragmentManager().findFragmentByTag(s);

				if (mFragment[i] != null && !mFragment[i].isDetached())
				{
					ft.detach(mFragment[i]);
				}
				i++;
			}

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			ft = mActivity.getSupportFragmentManager().beginTransaction();

			if (mFragment[0] == null)
			{
				mFragment[0] = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment[0], mTag[0]);
				ft.commit();
			}
			else
			{
				ft.attach(mFragment[0]);
				ft.commit();
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			ft = mActivity.getSupportFragmentManager().beginTransaction();
			for (String s : mTag)
			{
				Fragment f = mActivity.getSupportFragmentManager().findFragmentByTag(s);
				if (f != null)
				{
					ft.detach(f);

				}
			}
			ft.commitAllowingStateLoss();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}

	}

	// @Override
		// public void onBackPressed() {
		// this.setResult(RESULT_OK);
		// this.finish();
		// }
}