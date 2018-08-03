package Services;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;

import com.socialcamera.PicAround.EventsWebRequestHandler;

import WebHelpers.EventParams;
import android.widget.ListView;

public class ListViewHandler {
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_NAME = "name"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_STARTDATE = "startdate";
	static final String KEY_ENDTDATE = "enddate";
	static final String KEY_URL = "url";
	static final String KEY_LOCATIONID = "locationid";
	
	ListView list;
    LazyAdapter adapter;
	private HttpResponse httpResponse;
	

	public ListViewHandler(HttpResponse response)
	{
		httpResponse = response;
	
	}
	
	
	public ArrayList<HashMap<String, String>> getData()
	{
	EventsWebRequestHandler eh = new EventsWebRequestHandler();
		
		ArrayList<EventParams> eventParams =  eh.GetObjectEventsforplace(httpResponse); 
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	
		// looping through all song nodes <song>
		for (int i = 0; i < eventParams.size(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			EventParams e = (EventParams)eventParams.get(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, e.EventId);
			map.put(KEY_NAME, e.EventName);
			map.put(KEY_LOCATIONID, e.LocationId);
			map.put(KEY_STARTDATE, e.StartDate);
			map.put(KEY_ENDTDATE, e.EndDate);
			map.put(KEY_URL, e.ThumbLink);
			// adding HashList to ArrayList
			list.add(map);
	}
		
return list;
			
	}	
}
