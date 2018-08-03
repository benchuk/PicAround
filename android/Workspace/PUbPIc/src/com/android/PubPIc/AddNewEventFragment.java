package com.android.PubPIc;

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

import com.actionbarsherlock.app.SherlockActivity;

import Interfaces.ILocationResult;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewEventFragment extends SherlockActivity
{
	private String TAG = "Picup";
	EditText eventName;
	com.android.PubPIc.NumberPicker eventDuration;
	// DatePicker newEventDate;
	// TimePicker eventStartTime;
	// TimePicker eventEndTime;
	Button ok;
	Button cancel;
	private LocationService ls;
	private FacebookService fs;
	private ContainerService cs;
	private Spinner m_spinner;
	ArrayList<PlacesParams> placesResult;
	ProgressDialog progress;
	ProgressDialog progressEvent;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		setContentView(R.layout.new_event_layout);
		ok = (Button) findViewById(R.id.ok__createnewevent);
		cancel = (Button) findViewById(R.id.cancel__createnewevent);
		m_spinner = (Spinner) findViewById(R.id.places_spinner);
		progress = ProgressDialog.show(this, "Create New Event","Loading facebook places, please wait...", true);
		
		fs.GetPlacesAsync(AddNewEventFragment.this, cs.Location, new FacebookAsyncPlacesSearch()
		{
			private ImageLoader imageLoader;

			@Override
			public void searchComleted(IPlacesResult places)
			{
				placesResult = places.GetPlaces();

				if (m_spinner != null)
				{
					final List<String> list = new ArrayList<String>();
					for (PlacesParams nameValuePair : placesResult)
					{
						list.add(nameValuePair.Name);
					}

					ArrayAdapter<String> adp1 = new ArrayAdapter<String>(AddNewEventFragment.this, android.R.layout.simple_list_item_1, list);
					adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					m_spinner.setAdapter(adp1);
					progress.dismiss();
				}

			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				progressEvent = ProgressDialog.show(AddNewEventFragment.this, "Create New Event","Creating Event, please wait...", true);
				eventName = (EditText) findViewById(R.id.editText_new_event_name);
				//eventDuration = (com.android.PubPIc.NumberPicker) findViewById(R.id.editText_event_duration);
				//int eventDurationInt = eventDuration.getValue();
				int eventDurationInt = 24;
				
				DateFormat.getDateTimeInstance();

				Date now = new Date();

				Calendar cal = Calendar.getInstance(); // creates calendar
				cal.setTime(new Date()); // sets calendar time/date
				cal.add(Calendar.HOUR_OF_DAY, eventDurationInt); // adds
																	// eventDurationInt
																	// hours
				Date endTime = cal.getTime(); // returns new date object,
												// eventDurationInt hours in the
												// future

				ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
				NameValuePairs.add(new BasicNameValuePair("userID", fs.facebookUserID));
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

				String locationName = m_spinner.getSelectedItem().toString();
				NameValuePairs.add(new BasicNameValuePair("locationID", locationName));// should
																					// be
																					// facebook
																					// location
																					// id.
				String baseUrl = "http://picaround.azurewebsites.net/Events/CreateNewEvent";
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
								cs.getInstance().SelectedEventID = response.replace("\n", "");
								;
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
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i(TAG, "Picup - failed to create new event: Reason" + e.toString());
						}

					}

				};

				// Create new event on server
				new WebRequest().PostData(parameters);
				//ContainerService.getInstance().ActionBar.setSelectedNavigationItem(2);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// ContainerService.getInstance().TabNavigation.tabSwap("NewEventFregment","EventsFragment",
				// EventsFragment.class.getName());
				finish();
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
