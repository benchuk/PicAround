package com.socialcamera.PicAround;

import Services.ContainerService;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;

public class PlacePickerFragmentActivity extends FragmentActivity
{
	private PlacePickerFragment placePickerFragment;
	private static final int SEARCH_RADIUS_METERS = 1000;
	private static final int SEARCH_RESULT_LIMIT = 50;
	private static final String SEARCH_TEXT = "restaurant";
	private static final int LOCATION_CHANGE_THRESHOLD = 50; // meters
	private ContainerService cs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_fregment);
		cs = ContainerService.getInstance();
		// Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragmentToShow = null;
		Uri intentUri = getIntent().getData();

		if (savedInstanceState == null)
		{
			placePickerFragment = new PlacePickerFragment();
		}
		else
		{
			placePickerFragment = (PlacePickerFragment) manager.findFragmentById(R.id.picker_fragment);
		}
		placePickerFragment.setOnSelectionChangedListener(new PickerFragment.OnSelectionChangedListener()
		{
			@Override
			public void onSelectionChanged(PickerFragment<?> fragment)
			{
				finishActivity(); // call finish since you can only pick one
									// place
			}
		});
		placePickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener()
		{

			@Override
			public void onError(PickerFragment<?> fragment, FacebookException error)
			{
				// PlacePickerFragmentActivity.this.onError(error);

			}
		});
		placePickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener()
		{
			@Override
			public void onDoneButtonClicked(PickerFragment<?> fragment)
			{
				finishActivity();
			}
		});
		fragmentToShow = placePickerFragment;

		manager.beginTransaction().replace(R.id.picker_fragment, fragmentToShow).commit();
	}

	private void finishActivity()
	{
		if (placePickerFragment.getSelection() != null)
		{
			cs.SelectedFBPlace = placePickerFragment.getSelection().getName();
		}
		setResult(RESULT_OK, null);
		finish();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// Configure the place picker: search center, radius,
		// query, and maximum results.
		if (placePickerFragment != null)
		{
			placePickerFragment.setLocation(cs.Location);
			placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
			// placePickerFragment.setSearchText(SEARCH_TEXT);
			placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
			// Start the API call
			placePickerFragment.loadData(true);
		}
	}

}
