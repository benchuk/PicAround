package Services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.socialcamera.PicAround.R;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedEventLazyAdapter extends BaseAdapter {
    
    private FragmentActivity activity;
    private ArrayList<FeedEventParams> data;
    private LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public FeedEventLazyAdapter(FragmentActivity a, ArrayList<FeedEventParams> d)
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
               
             vi = inflater.inflate(R.layout.list_feed_row,  null);

             TextView  description = (TextView)vi.findViewById(R.id.feed_description); // title
          final TextView userName = (TextView)vi.findViewById(R.id.feed_userId); // artist name
         TextView place = (TextView)vi.findViewById(R.id.feed_place); // duration
          ImageView thumb_image=(ImageView)vi.findViewById(R.id.feed_list_imagethumb); // thumb image	
          ImageView image=(ImageView)vi.findViewById(R.id.feed_list_image); // thumb image	
        
      
          FeedEventParams feedEventParams = data.get(position);
        
        // Setting all values in listview
          description.setText(feedEventParams.Description);
          place.setText(feedEventParams.PlaceName +","+ feedEventParams.EventName);
          userName.setText(feedEventParams.UserId);
          String userrName = FacebookService.getInstance().GetUserNickName(feedEventParams.UserId,new HandleUserNameArrived(){
        	 
        	@Override
        	public void OnUserNameArrived(String userNameFacebook) {
        		// TODO Auto-generated method stub
        		super.OnUserNameArrived(userNameFacebook);
        	//	userName.setText(userNameFacebook);
        	}
        	  
          });
         
          imageLoader.DisplayImage(feedEventParams.Link,image);
          imageLoader.DisplayImage(feedEventParams.UserIdImage,thumb_image);
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