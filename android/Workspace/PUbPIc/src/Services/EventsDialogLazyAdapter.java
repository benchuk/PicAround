package Services;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.actionbarsherlock.app.SherlockActivity;
import com.android.PubPIc.R;



import WebHelpers.EventParams;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsDialogLazyAdapter extends BaseAdapter {
    
    private SherlockActivity activity;
    private ArrayList<EventParams> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public EventsDialogLazyAdapter(SherlockActivity a, ArrayList<EventParams> d)
    {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
               
             vi = inflater.inflate(R.layout.list_row_dialog,  null);

             TextView  title = (TextView)vi.findViewById(R.id.title); // title
          TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
         TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
          ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image	
        
        
      
        EventParams eventParams = data.get(position);
        
        // Setting all values in listview
        title.setText(eventParams.EventName);
        artist.setText(eventParams.LocationName);
        duration.setText(getTime(eventParams.EndDate));
        imageLoader.DisplayImage(eventParams.ThumbLink,thumb_image);
        return vi;
        
		
    }
    private String getTime(String date)
    {
    	// Remove prefix and suffix extra string information
    	String dateString = date.replace("/Date(", "").replace(")/", ""); 

    	// Split date and timezone parts
    	String[] dateParts = dateString.split("[+-]");

    	// The date must be in milliseconds since January 1, 1970 00:00:00 UTC
    	// We want to be sure that it is a valid date and time, aka the use of Calendar
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(Long.parseLong(dateParts[0]));

    	// If you want to play with time zone:
    	//calendar.setTimeZone(TimeZone.getTimeZone(dateParts[1]));

    	// Read back and look at it, it must be the same
    	long timeinmilliseconds = calendar.getTimeInMillis();

    	// Convert it to a Date() object now:
    	Date date1 = calendar.getTime();
    	
    	return date1.toGMTString();
	 
	}
}