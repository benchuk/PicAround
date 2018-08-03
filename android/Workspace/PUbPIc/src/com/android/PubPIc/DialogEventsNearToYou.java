package com.android.PubPIc;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Services.ContainerService;
import Services.EventsDialogLazyAdapter;
import Services.FacebookService;
import Services.LazyAdapter;
import Services.LocationService;
import WebHelpers.EventParams;
import WebHelpers.EventsResultHandler;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ProcessEventsWebResult;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import android.view.View;


public class DialogEventsNearToYou extends SherlockActivity {
	
	private ListView m_listview;
	private LocationService ls;
	private FacebookService fs;
	private  ContainerService cs;
	private HttpResponse response;
	private ArrayList<EventParams> events;
	private SherlockActivity context;
	ArrayList _eventsList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        ls = LocationService.getInstance();
    	cs = ContainerService.getInstance();
    	fs = FacebookService.getInstance();
	
		
		
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		//
		// StrictMode.setThreadPolicy(policy);
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
						 setContentView(R.layout.text);
						m_listview = (ListView)findViewById(R.id.listView3);
						SetEventsObjectToView(_eventsList);
						m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
						{
							public void onItemClick(AdapterView<?> parent, View view, int position, long id)
							{
								
								cs.SelectedEventID =  events.get(position).EventId;
								cs.SelectedEventName =  events.get(position).EventName;
								cs.ActionBar.setTitle(cs.SelectedEventName);
								cs.ActionBar.setSubtitle(cs.SelectedPlace);
								//CharSequence place = ((TextView) view).getText();
								//cs.SelectedEvent = place.toString();
								
								cs.ActionBar.setSelectedNavigationItem(2);
								finish();
							//	dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)); 
							}
							
						});
						
					
					      //  ((TextView)findViewById(R.id.text)).setText(R.string.dialog_content);
					
					};
				};
			
				new ProcessEventsWebResult().execute(param);

			}

		};
        
		new WebRequest().PostData(parameters); 
		
	//	SetEventsToView(_eventsList);
    }

	public void SetEventsToView(ArrayList<EventParams> eventsResultLocal)
	{
		if (m_listview != null && eventsResultLocal != null)
		{
			// String[] stockArr = new String[eventsResultLocal.size()];
			// stockArr = eventsResultLocal.toArray(stockArr);
			context = this;
			EventsDialogLazyAdapter adapter = new EventsDialogLazyAdapter(this, eventsResultLocal);
			m_listview.setAdapter(adapter);
			
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

			EventsDialogLazyAdapter adapter = new EventsDialogLazyAdapter(this, eventsResultLocal);
			m_listview.setAdapter(adapter);

			
		}
	}

	

// @Override
//public boolean onKeyDown(int keyCode, KeyEvent event) {
//	dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,  KeyEvent.KEYCODE_BACK)); 
//	return super.onKeyDown(keyCode, event);
//}
@Override
public void finish() {
	// TODO Auto-generated method stub
	super.finish();
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    super.onSaveInstanceState(outState);
}
		
}
