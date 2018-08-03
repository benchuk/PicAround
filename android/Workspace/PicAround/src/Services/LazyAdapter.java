package Services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.socialcamera.PicAround.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import WebHelpers.EventParams;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter
{

	private Activity activity;
	private ArrayList<EventParams> data;
	private LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	Map<String, View> _views = new HashMap<String, View>();
	private int _size;

	public LazyAdapter(Activity a, ArrayList<EventParams> d) {
		activity = a;
		data = d;
		_size = data.size();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount()
	{
		return _size;
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		try
		{
			String strPosition = Integer.toString(position);
			if (_views.containsKey(strPosition))
			{
				return _views.get(strPosition);
			}
			View vi;

			vi = inflater.inflate(R.layout.list_row, null);

			TextView title = (TextView) vi.findViewById(R.id.title); // title
			TextView artist = (TextView) vi.findViewById(R.id.artist); // artist
																		// name
			TextView duration = (TextView) vi.findViewById(R.id.duration); // duration
			ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																					// image

			EventParams eventParams = data.get(position);
			// SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			// Setting all values in listview
			title.setText(eventParams.EventName);
			artist.setText(eventParams.LocationName);
			duration.setText(sdf.format(eventParams.DateTimeStart));
			if (eventParams.ThumbLink != null && !eventParams.ThumbLink.equalsIgnoreCase("http://picaround.blob.core.windows.net/"))
			{
				imageLoader.DisplayImage(eventParams.ThumbLink, thumb_image);
			}
			else
			{
				thumb_image.setImageResource(R.drawable.blue);
			}
			_views.put(strPosition, vi);
			return vi;
		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false); 
		}
		return null;
	}

	private String getTime(String date)
	{
		// Remove prefix and suffix extra string information
		String dateString = date.replace("/Date(", "").replace(")/", "");

		// Split date and timezone parts
		String[] dateParts = dateString.split("[+-]");

		// The date must be in milliseconds since January 1, 1970 00:00:00 UTC
		// We want to be sure that it is a valid date and time, aka the use of
		// Calendar
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(dateParts[0]));

		// If you want to play with time zone:
		// calendar.setTimeZone(TimeZone.getTimeZone(dateParts[1]));

		// Read back and look at it, it must be the same
		long timeinmilliseconds = calendar.getTimeInMillis();

		// Convert it to a Date() object now:
		Date date1 = calendar.getTime();

		return date1.toGMTString();

	}
}