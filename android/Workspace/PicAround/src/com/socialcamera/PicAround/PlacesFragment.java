package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.android.PicAround.R;
//
//import Interfaces.ILocationResult;
//import Interfaces.IPlacesResult;
//import Services.ContainerService;
//import Services.FacebookService;
//import Services.ImageLoader;
//import Services.LazyAdapter;
//import Services.LocationService;
//import Services.PlacesLazyAdapter;
//import Services.PlacesParams;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.app.ActionBar;
//import android.app.LauncherActivity.ListItem;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.location.Location;
//
//public class PlacesFragment extends SherlockFragment
//{
//
//	private ListView m_listview;
//	private FragmentActivity context;
//	private LocationService ls;
//	private FacebookService fs;
//	private ContainerService cs;
//	ArrayList<PlacesParams> placesResult;
//	private PlacesLazyAdapter adapter;
//	private ArrayList<PlacesParams> places;
//	private ImageView user_picture;
//	private View view;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		ls = LocationService.getInstance();
//		fs = FacebookService.getInstance();
//		cs = ContainerService.getInstance();
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
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
//
//		view = inflater.inflate(R.layout.details, container, false);
//		m_listview = (ListView) view.findViewById(R.id.listView1);
//
//		AsyncLocationProviderOperation lop = new AsyncLocationProviderOperation()
//		{
//
//			@Override
//			public void LocationFoundCompleted(ILocationResult locationResult)
//			{
//				ls.UnRegisterForLocationsChange(this);
//
//				// TODO Auto-generated method stub
//				super.LocationFoundCompleted(locationResult);
//				if (locationResult != null && locationResult.GetLocation() != null)
//				{
//
//					cs.Location = locationResult.GetLocation();
//					fs.GetPlacesAsync(locationResult.GetLocation(), new FacebookAsyncPlacesSearch()
//					{
//						private ImageLoader imageLoader;
//
//						@Override
//						public void searchComleted(IPlacesResult places)
//						{
//
//							super.searchComleted(places);
//							// android.app.ActionBar.setIcon();
//							// StrictMode.ThreadPolicy policy = new
//							// StrictMode.ThreadPolicy.Builder().permitAll().build();
//							// StrictMode.setThreadPolicy(policy);
//							// user_picture=(ImageView)view.findViewById(R.drawable.icon);
//							// String img_value = null;
//							// imageLoader=new
//							// ImageLoader(getActivity().getApplicationContext());
//							//
//							//
//							// img_value ="http://graph.facebook.com/"+
//							// cs.FacebookId+"/picture?type=large";
//							// //Bitmap mIcon1 =
//							// BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
//							// // user_picture.setImageBitmap(mIcon1);
//							// imageLoader.DisplayImage(img_value,user_picture);
//							// android.app.ActionBar.setIcon(R.drawable.menu_precedent);
//
//							placesResult = places.GetPlaces();
//
//							if (m_listview != null)
//							{
//								SetPlacesToView(placesResult);
//							}
//						}
//					});
//				}
//			}
//		};
//		ls.RegisterForLocationsChange(lop);
//
//		if (placesResult != null)
//		{
//			SetPlacesToView(placesResult);
//		}
//		m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//			{
//				// PlacesParams selectedFromList =
//				// (PlacesParams)(m_listview.(position));
//
//				// CharSequence place = ((TextView) view).getText().toString();
//				cs.SelectedPlace = places.get(position).Name;
//				cs.ActionBar.setSubtitle(cs.SelectedPlace);
//				cs.ActionBar.setSelectedNavigationItem(1);
//			}
//		});
//		return view;
//	}
//
//	public void SetPlacesToView(ArrayList<PlacesParams> placesResult)
//	{
//		places = placesResult;
//		if (m_listview != null && placesResult != null)
//		{
//			// String[] stockArr = new String[eventsResultLocal.size()];
//			// stockArr = eventsResultLocal.toArray(stockArr);
//			context = getActivity();
//
//			adapter = new PlacesLazyAdapter(getActivity(), placesResult);
//			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//			// android.R.layout.simple_list_item_1, placesResult);
//			m_listview.setAdapter(adapter);
//			// ContainerService.getInstance().places = placesResult;
//			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		}
//	}
//
//}