package com.android.PubPIc;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.actionbarsherlock.app.SherlockFragment;

import Interfaces.ILocationResult;
import Interfaces.IPlacesResult;
//import Services.AdapterView;
import Services.ContainerService;
import Services.FacebookService;
import Services.FeedEventLazyAdapter;
import Services.FeedEventParams;
import Services.LazyAdapter;
import Services.ListViewHandler;
import Services.LocationService;
//import Services.OnItemClickListener;
import WebHelpers.EventParams;
import WebHelpers.EventsResultHandler;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.FeedEventsResultHandler;
import WebHelpers.FeedEventsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ProcessEventsWebResult;
import WebHelpers.ProcessFeedEventsWebResult;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;

public class FeedEventsFragment extends SherlockFragment
{
	private ListView m_listview;
	private FragmentActivity context;
	private LocationService ls;
	private FacebookService fs;
	private ContainerService cs;
	String[] eventsResult;
	ArrayList _feedEventsList;
	ListViewHandler listViewHandler;
	private HttpResponse response;
	private ArrayList<FeedEventParams> feedEvents;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();
		
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		//
		// StrictMode.setThreadPolicy(policy);
	



	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("userID", fs.facebookUserID));
	
		String baseUrl = "http://picaround.azurewebsites.net/Feed/GetFeedByEventID?eventID="+ ContainerService.getInstance().SelectedEventID ;
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
				FeedEventsWebRequestHandlerParams param = new FeedEventsWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				response = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.feedEventsResultHandler = new FeedEventsResultHandler() {
					@Override
					public void HandleEventResult(ArrayList feedEventsList)
					{

						_feedEventsList =feedEventsList;
						SetEventsObjectToView(_feedEventsList);
					};
				};
				getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
				new ProcessFeedEventsWebResult().execute(param);

			}

		};

		new WebRequest().PostData(parameters);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		View view = inflater.inflate(R.layout.feed_event_fregment, container, false);

		// Click event for single list row

		m_listview = (ListView) view.findViewById(R.id.listView3);
		SetEventsToView(_feedEventsList);
		m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				//cs.SelectedEventID =  feedEvents.get(position).EventId;
				//cs.SelectedEventName =  feedEvents.get(position).EventName;
			//	cs.ActionBar.setTitle(cs.SelectedEventName);
			//	cs.ActionBar.setSubtitle(cs.SelectedPlace);
				//CharSequence place = ((TextView) view).getText();
				//cs.SelectedEvent = place.toString();
			//	cs.ActionBar.setSelectedNavigationItem(2);
			}
		});
		return view;
	}

	public void SetEventsToView(ArrayList<FeedEventParams> feedEventsResultLocal)
	{
		if (m_listview != null && feedEventsResultLocal != null)
		{
			// String[] stockArr = new String[eventsResultLocal.size()];
			// stockArr = eventsResultLocal.toArray(stockArr);
			context = getActivity();
			FeedEventLazyAdapter adapter = new FeedEventLazyAdapter(getActivity(), feedEventsResultLocal);
			m_listview.setAdapter(adapter);
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	public void SetEventsObjectToView(ArrayList<FeedEventParams> feedEventsResultLocal)
	{
		if (m_listview != null && feedEventsResultLocal != null)
		{
			
			feedEvents = feedEventsResultLocal;
			// String[] stockArr = new String[eventsResultLocal.size()];
			// stockArr = eventsResultLocal.toArray(stockArr);
			context = getActivity();

			FeedEventLazyAdapter adapter = new FeedEventLazyAdapter(getActivity(), feedEventsResultLocal);
			m_listview.setAdapter(adapter);

			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		}
	}

}
