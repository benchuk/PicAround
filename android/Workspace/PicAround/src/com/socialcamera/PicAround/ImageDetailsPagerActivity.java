package com.socialcamera.PicAround;

import java.io.File;
import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.FeedEventParams;
import Services.HandleUserNameArrived;
import Services.ImageLoader;
import Services.LocationService;
import WebHelpers.DoneHandler;
import WebHelpers.DownloadImageTask;
import WebHelpers.ProfileImageLoader;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.socialcamera.PicAround.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ImageDetailsPagerActivity extends SherlockActivity implements OnPageChangeListener
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	boolean initReady = false;
	RotateAnimation _anim;

	// private TextView _userName;
	// private TextView _eventName;
	// private ImageView _userImageOverlay;
	ShareActionProvider actionProvider;
	private Context context;
	private ImageDetailsPagerAdapter adapter;
	LayoutInflater inflater;
	private ViewPager pager;
	// TitlePageIndicator titlePageIndicator;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			mGaTracker.sendEvent("ui_action_android", "button_press", "User closed photo details pager activity", (long) 1);
			// cs.FetchedAlbums = null;
			// cs.AddedCells = null;
			cs = null;
			fs = null;
			bar = null;
			_anim = null;
			actionProvider = null;
			context = null;
			adapter = null;
			inflater = null;
			pager = null;
			mGaInstance = null;
			mGaTracker = null;
			
			finish();
			System.gc();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN && _menu != null)
			{
				mGaTracker.sendEvent("ui_action_android", "button_press", "User is showing menu in photo details view (there is no menu only share)", (long) 1);
				_menu.performIdentifierAction(0, 0);
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private Intent createShareIntent()
	{
		//mGaTracker.sendEvent("ui_action", "share", "UserIsSharingPicture", (long) 13);
		String fileName;
		int selectedIndex = pager.getCurrentItem();
		if (selectedIndex >= cs.FetchedAlbums.length)
		{
			fileName = cs.AddedCells.get(selectedIndex - cs.FetchedAlbums.length).GetImageSdPath();
		}
		else
		{
			fileName = Environment.getExternalStorageDirectory() + "/PicAroundCache" + File.separator + "PicAround" + selectedIndex + ".jpg";
		}

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri picUri = Uri.fromFile(new File(fileName));
		shareIntent.putExtra(Intent.EXTRA_STREAM, picUri);
		shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("Picaround has a picture to share with you, http://picaround.com/Gallery/GalleryByEventId?eventId=%s", cs.SelectedEventID));
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PicAround has something to share with you.");

		return shareIntent;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		ls = LocationService.getInstance();
		ls.StartRecordingLocation();
		if (actionProvider != null)
		{
			actionProvider.setShareIntent(createShareIntent());
		}
	};

	@Override
	protected void onPause()
	{
		super.onPause();
		ls.StopRecordingLocation();
		ls = null;
	};

	@Override
	public void onStart()
	{
		super.onStart();

		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/PhotoDetailsPager");
	}

	@Override
	public void onStop()
	{
		super.onStop();

		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getSherlock().setProgressBarIndeterminate(false);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		cs = ContainerService.getInstance();
		setTheme(R.style.Theme_Sherlock);
		// setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setContentView(R.layout.image_details);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");
		bar = getSupportActionBar();

		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);
		// bar.setTitle("Activity Title");

		bar.setIcon(R.drawable.picaroundicon);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		context = this;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		adapter = new ImageDetailsPagerAdapter();
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(3);
		pager.setOnPageChangeListener(this);
		// pager.setAdapter(adapter);

		// Get a reference to the TitlePageIndicator
		// titlePageIndicator = (TitlePageIndicator)
		// findViewById(R.id.indicator);

		// Attach adapter to ViewPager
		pager.setAdapter(adapter);
		pager.setCurrentItem(cs.Position);
		// Attach ViewPager to the indicator
		// titlePageIndicator.setViewPager(pager);

		_anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		_anim.setInterpolator(new LinearInterpolator());
		_anim.setRepeatCount(Animation.INFINITE);
		_anim.setDuration(2000);

		bar.setIcon(cs.FacebookUserImage);

		// if (cs.FacebookId == null)
		// {
		// fs.asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation() {
		// @Override
		// public void GetIdCompleted(String facebookID)
		// {
		// if (bar!=null)
		// {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }
		// // isThereAreEventsNearToYou();
		// }
		// };
		// fs.GetFacebookIdAsync();
		// }
		// else
		// {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }

		bar.setTitle(cs.SelectedEventName);
		bar.setSubtitle(cs.SelectedPlace);

		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);

	}

	public Bitmap decodeSampledBitmapFromResource(String path)
	{
		Bitmap res = null;
		try
		{
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			// Calculate inSampleSize
			options.inSampleSize = 2; // calculateInSampleSize(options,
										// reqWidth,
										// reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			res = BitmapFactory.decodeFile(path, options);
		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			mGaTracker.sendEvent("auto_action_android", "Error", e.toString(), (long) 1);
			myTracker.sendException(e.getMessage(), false);
		}
		return res;
	}

	Menu _menu;

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0)
		{
			return false;
		}
		
		switch (item.getItemId())
		{
			default:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "User is sharing a photo", (long) 1);
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		_menu = menu;
		menu.add(0, 1, 0, "").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.layout.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		if (actionProvider != null)
		{
			actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
			actionProvider.setShareIntent(createShareIntent());
		}
		initReady = true;

		return true;
	}

	private class ImageDetailsPagerAdapter extends PagerAdapter
	{
		private View[] _layouts = new View[cs.FetchedAlbums.length + cs.AddedCells.size()];

		// @Override
		// public CharSequence getPageTitle(int position)
		// {
		// return Integer.toString(position);
		// }

		public int getCount()
		{
			if (cs != null && cs.FetchedAlbums != null && cs.AddedCells != null)
			{
				return cs.FetchedAlbums.length + cs.AddedCells.size();
			}
			return 0;
		}

		public Object instantiateItem(View collection, int position)
		{

			View layout = null;
			if (_layouts[position] == null)
			{
				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = mInflater.inflate(R.layout.signle_image, null);
				ImageView imageDetails;
				imageDetails = (ImageView) layout.findViewById(R.id.imagedetails);

				FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				imageDetails.setLayoutParams(imageViewParams);

				imageDetails.startAnimation(_anim);
				// final ImageView spinnerdetails = (ImageView)
				// findViewById(R.id.imagedetails);
				// spinnerdetails.startAnimation(_anim);

				final TextView userName = (TextView) layout.findViewById(R.id.username);
				TextView eventName = (TextView) layout.findViewById(R.id.eventname);

				ImageView userImageOverlay = (ImageView) layout.findViewById(R.id.overlay_image);

				FrameLayout slayout = (FrameLayout) layout.findViewById(R.id.overlay_image_background);
				slayout.getBackground().setAlpha(100);
				ImageLoader imageLoader = new ImageLoader(getApplicationContext());

				if (position < cs.FetchedAlbums.length)
				{
					FeedEventParams feedEventParams = new FeedEventParams(cs.FetchedAlbums[position].UserId, "link", "placename", cs.FetchedAlbums[position].EventName, "");
					eventName.setText(feedEventParams.EventName);
					if (feedEventParams.UserId.length() < 16)
					{
						FacebookService.getInstance().GetUserNickName(feedEventParams.UserId, new HandleUserNameArrived()
						{
							@Override
							public void OnUserNameArrived(String userNameFacebook)
							{
								// TODO Auto-generated method stub
								super.OnUserNameArrived(userNameFacebook);
								userName.setText(userNameFacebook);
							}

						});
					}
					else
					{
						FacebookService.getInstance().GetUserNickNameGoogle(feedEventParams.UserId, new HandleUserNameArrived()
						{
							@Override
							public void OnUserNameArrived(String userNameFacebook)
							{
								// TODO Auto-generated method stub
								super.OnUserNameArrived(userNameFacebook);
								userName.setText(userNameFacebook);
							}

						});
					}

					cs.Resources = getResources();
					imageLoader.DisplayImage(feedEventParams.UserIdImage, userImageOverlay);
					// DownloadImageTask SAVE THE IMAGE AS PicAround.jpg file.
					new DownloadImageTask(imageDetails, position, new DoneHandler()
					{
						@Override
						public void Done()
						{
							// spinnerdetails.setVisibility(ImageView.INVISIBLE);
							// spinnerdetails.setAnimation(null);
							ImageDetailsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
							ImageDetailsPagerActivity.this.getSherlock().setProgressBarIndeterminate(false);
						};
					}).execute(cs.FetchedAlbums[position].MidQLink);

				}
				else
				{
					String imagePath = cs.AddedCells.get(position - cs.FetchedAlbums.length).GetImageSdPath();

					FeedEventParams feedEventParams = new FeedEventParams(cs.FacebookId, "link", "placename", cs.SelectedEventName, "");
					eventName.setText(feedEventParams.EventName);
					if (feedEventParams.UserId.length() < 16)
					{
						FacebookService.getInstance().GetUserNickName(feedEventParams.UserId, new HandleUserNameArrived()
						{

							@Override
							public void OnUserNameArrived(String userNameFacebook)
							{
								// TODO Auto-generated method stub
								super.OnUserNameArrived(userNameFacebook);
								userName.setText(userNameFacebook);
							}

						});
					}
					else
					{
						FacebookService.getInstance().GetUserNickNameGoogle(feedEventParams.UserId, new HandleUserNameArrived()
						{

							@Override
							public void OnUserNameArrived(String userNameFacebook)
							{
								// TODO Auto-generated method stub
								super.OnUserNameArrived(userNameFacebook);
								userName.setText(userNameFacebook);
							}

						});
					}
					imageLoader.DisplayImage(feedEventParams.UserIdImage, userImageOverlay);
					Bitmap bitmap = decodeSampledBitmapFromResource(imagePath);
					if (bitmap != null)
					{
						imageDetails.setImageBitmap(bitmap);
						imageDetails.setAnimation(null);
						FrameLayout.LayoutParams imageViewParams2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
						imageDetails.setLayoutParams(imageViewParams2);
					}
					// spinnerdetails.setVisibility(ImageView.INVISIBLE);
					// spinnerdetails.setAnimation(null);
					ImageDetailsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
					ImageDetailsPagerActivity.this.getSherlock().setProgressBarIndeterminate(false);
				}

				((ViewPager) collection).addView((View) layout, 0);
				_layouts[position] = layout;
			}
			else
			{
				layout = _layouts[position];
			}

			return layout;

		}

		public void destroyItem(View collection, int position, Object view)
		{
			_layouts[position] = null;
			((ViewPager) collection).removeView((View) view);
		}

		public boolean isViewFromObject(View view, Object object)
		{
			return view == ((View) object);

		}

		public void finishUpdate(View arg0)
		{
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1)
		{
		}

		public Parcelable saveState()
		{
			return null;

		}

		public void startUpdate(View arg0)
		{
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0)
	{
		if (initReady)
		{
			actionProvider.setShareIntent(createShareIntent());
		}
	}
}
