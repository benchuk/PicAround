package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.android.PicAround.R;
//
//import Interfaces.ILocationResult;
//import Interfaces.IPlacesResult;
////import Services.AdapterView;
//import Services.ContainerService;
//import Services.FacebookService;
//import Services.LazyAdapter;
//import Services.ListViewHandler;
//import Services.LocationService;
////import Services.OnItemClickListener;
//import WebHelpers.EventParams;
//import WebHelpers.EventsResultHandler;
//import WebHelpers.EventsWebRequestHandlerParams;
//import WebHelpers.HttpRequestParameters;
//import WebHelpers.ProcessEventsWebResult;
//import WebHelpers.WebRequest;
//import WebHelpers.WebRequestResultHandler;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.HttpResponse;
//
//public class EventsFragment extends SherlockFragment
//{
//	private ListView m_listview;
//	private FragmentActivity context;
//	private LocationService ls;
//	private FacebookService fs;
//	private ContainerService cs;
//	String[] eventsResult;
//	ArrayList _eventsList;
//	ListViewHandler listViewHandler;
//	private HttpResponse response;
//	private ArrayList<EventParams> events;
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		ls = LocationService.getInstance();
//		cs = ContainerService.getInstance();
//		fs = FacebookService.getInstance();
//		
//
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState)
//	{
//		super.onActivityCreated(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		View view = inflater.inflate(R.layout.event_fregment, container, false);
//
//		
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
//		// StrictMode.ThreadPolicy policy = new
//		// StrictMode.ThreadPolicy.Builder().permitAll().build();
//		//
//		// StrictMode.setThreadPolicy(policy);
//		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
//		NameValuePairs.add(new BasicNameValuePair("userID", fs.facebookUserID));
//		if (cs.Location != null)
//		{
//			NameValuePairs.add(new BasicNameValuePair("lat", Double.toString(cs.Location.getLatitude())));
//			NameValuePairs.add(new BasicNameValuePair("lon", Double.toString(cs.Location.getLongitude())));
//		}
//		else
//		{
//			NameValuePairs.add(new BasicNameValuePair("lat", "404"));
//			NameValuePairs.add(new BasicNameValuePair("lon", "404"));
//		}
//		String onlyInLocation = new Boolean(!cs.AlowAllEvents).toString();
//		NameValuePairs.add(new BasicNameValuePair("onlyInLocation", onlyInLocation));
//		NameValuePairs.add(new BasicNameValuePair("radius", "1000"));
//
//		String baseUrl = "http://picaround.azurewebsites.net/Events/FindEventByLocation";
//		HttpRequestParameters parameters = new HttpRequestParameters();
//		parameters.url = baseUrl;
//		parameters.NameValuePairs = NameValuePairs;
//		parameters.webRequestHandler = new WebRequestResultHandler() {
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public void OnWebRequestCompleted(HttpResponse httpResponse)
//			{
//
//				// TODO Auto-generated method stub
//				super.OnWebRequestCompleted(httpResponse);
//				EventsWebRequestHandlerParams param = new EventsWebRequestHandlerParams();
//				param.httpResponse = httpResponse;
//				response = httpResponse;
//				// listViewHandler = new ListViewHandler(httpResponse);
//				// listViewHandler.getData();
//
//				param.eventsResultHandler = new EventsResultHandler() {
//					@Override
//					public void HandleEventResult(ArrayList eventsList)
//					{
//
//						_eventsList = eventsList;
//						SetEventsObjectToView(_eventsList);
//					};
//				};
//				getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
//				new ProcessEventsWebResult().execute(param);
//
//			}
//
//		};
//
//		new WebRequest().PostData(parameters);
//		// Click event for single list row
//
//		m_listview = (ListView) view.findViewById(R.id.listView2);
//		SetEventsToView(_eventsList);
//		m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//			{
//				cs.SelectedEventID =  events.get(position).EventId;
//				cs.SelectedEventName =  events.get(position).EventName;
//				cs.ActionBar.setTitle(cs.SelectedEventName);
//				cs.ActionBar.setSubtitle(cs.SelectedPlace);
//				//CharSequence place = ((TextView) view).getText();
//				//cs.SelectedEvent = place.toString();
//				cs.ActionBar.setSelectedNavigationItem(2);
//			}
//		});
//		return view;
//	}
//
//	public void SetEventsToView(ArrayList<EventParams> eventsResultLocal)
//	{
//		if (m_listview != null && eventsResultLocal != null)
//		{
//			// String[] stockArr = new String[eventsResultLocal.size()];
//			// stockArr = eventsResultLocal.toArray(stockArr);
//			context = getActivity();
//			LazyAdapter adapter = new LazyAdapter(getActivity(), eventsResultLocal);
//			m_listview.setAdapter(adapter);
//			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		}
//	}
//
//	public void SetEventsObjectToView(ArrayList<EventParams> eventsResultLocal)
//	{
//		if (m_listview != null && eventsResultLocal != null)
//		{
//			
//			events = eventsResultLocal;
//			// String[] stockArr = new String[eventsResultLocal.size()];
//			// stockArr = eventsResultLocal.toArray(stockArr);
//			context = getActivity();
//
//			LazyAdapter adapter = new LazyAdapter(getActivity(), eventsResultLocal);
//			m_listview.setAdapter(adapter);
//
//			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		}
//	}
//
//}
