package com.socialcamera.PicAround;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ContainerService;
import Services.PlacesParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class GooglePlusPlacesActivity extends SherlockActivity
{
	private ContainerService cs;
	private ActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_plus_places);
		cs = ContainerService.getInstance();
		bar = getSupportActionBar();
		bar.setTitle("Pick a place");
		final ListView listview = (ListView) findViewById(R.id.listview);
		Bundle b = getIntent().getExtras();
		final ArrayList<PlacesParams> places = cs.GooglePlaces;

		final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, places);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				final PlacesParams item = places.get(position);
				cs.SelectedFBPlace = item.Name;
				finish();
			}

		});
	}

	private class StableArrayAdapter extends ArrayAdapter<PlacesParams>
	{

		HashMap<Integer, PlacesParams> map = new HashMap<Integer, PlacesParams>();
		private Context context;
		Map<Integer, View> _views = new HashMap<Integer, View>();
		List<PlacesParams> _places;

		public StableArrayAdapter(Context context, int textViewResourceId, List<PlacesParams> objects) {
			super(context, textViewResourceId, objects);
			this.context = context;
			_places = objects;
		}
		public int getCount()
		{
			return _places.size();
		}
		
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			if (_views.containsKey(position))
			{
				return _views.get(position);
			}
			View vi;
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			vi = inflater.inflate(R.layout.list_row, null);

			TextView title = (TextView) vi.findViewById(R.id.title); // title
			TextView artist = (TextView) vi.findViewById(R.id.artist); // artist
			artist.setText("");														// name
			TextView duration = (TextView) vi.findViewById(R.id.duration); // duration
			duration.setText("");	
			ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																					// image
			title.setText(_places.get(position).Name);
			_views.put(position, vi);

			return vi;
		}

		@Override
		public PlacesParams getItem(int position)
		{
			return map.get(position);
		};

		@Override
		public boolean hasStableIds()
		{
			return true;
		}

	}

}
