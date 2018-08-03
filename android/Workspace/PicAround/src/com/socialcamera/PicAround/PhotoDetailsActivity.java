package com.socialcamera.PicAround;

import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.FeedEventParams;
import Services.HandleUserNameArrived;
import Services.ImageLoader;
import Services.LocationService;
import WebHelpers.DoneHandler;
import WebHelpers.DownloadImageTask;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.socialcamera.PicAround.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class PhotoDetailsActivity extends SherlockActivity {
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	ImageView imageDetails;
	String _fileName;
	RotateAnimation _anim;
	public ImageLoader imageLoader;
	private TextView _userName;
	private TextView _eventName;
	private ImageView _userImageOverlay;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ls.StartRecordingLocation();
		if (actionProvider!=null)
		{
			actionProvider.setShareIntent(createShareIntent());	
		}
	};
	
	@Override
	protected void onPause()
	{
		super.onPause();
		ls.StopRecordingLocation();
	};
	
	@Override
	public void onStart() {
		super.onStart();

		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/PhotoDetails");
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.getSherlock().setProgressBarIndeterminate(false);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		cs = ContainerService.getInstance();
		setTheme(R.style.Theme_Sherlock);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");
		
		// setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setContentView(R.layout.image_details);

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

		_anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		_anim.setInterpolator(new LinearInterpolator());
		_anim.setRepeatCount(Animation.INFINITE);
		_anim.setDuration(2000);

		imageLoader = new ImageLoader(getApplicationContext());
		if (cs.FacebookId == null) {
			fs.asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation() {
				@Override
				public void GetIdCompleted(String facebookID) {
					bar.setIcon(ContainerService.getInstance().FacebookUserImage);
					// isThereAreEventsNearToYou();
				}
			};
			fs.GetFacebookIdAsync();
		}
		else
		{
			bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		}

		bar.setTitle(cs.SelectedEventName);
		bar.setSubtitle(cs.SelectedPlace);
		imageDetails = (ImageView) findViewById(R.id.imagedetails);
		// final ImageView spinnerdetails = (ImageView)
		// findViewById(R.id.spinnerdetails);

		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);
		imageDetails.startAnimation(_anim);
		// spinnerdetails.startAnimation(_anim);

		if (!cs.UseImageView) {
			new DownloadImageTask(imageDetails, 1,new DoneHandler() {
				@Override
				public void Done() {
					// spinnerdetails.setVisibility(ImageView.INVISIBLE);
					// spinnerdetails.setAnimation(null);
					PhotoDetailsActivity.this.setSupportProgressBarIndeterminateVisibility(false);
					PhotoDetailsActivity.this.getSherlock().setProgressBarIndeterminate(false);
				};
			}).execute(cs.SelectedImageLink);
			// DownloadImageTask SAVE THE IMAGE AS PicAround.jpg file.
			_fileName = Environment.getExternalStorageDirectory() + "/PicAroundCache"
					+ File.separator + "PicAround1.jpg";
		} else {
			String imagePath = cs.SelectedImageSDPath;

			Bitmap bitmap = decodeSampledBitmapFromResource(imagePath, 400, 400);
			_fileName = imagePath;
			imageDetails.setImageBitmap(bitmap);
			imageDetails.setAnimation(null);
			// spinnerdetails.setVisibility(ImageView.INVISIBLE);
			// spinnerdetails.setAnimation(null);
			PhotoDetailsActivity.this.setSupportProgressBarIndeterminateVisibility(false);
			PhotoDetailsActivity.this.getSherlock().setProgressBarIndeterminate(false);
		}
		_userName = (TextView) findViewById(R.id.username);
		_eventName = (TextView) findViewById(R.id.eventname);

		_userImageOverlay = (ImageView) findViewById(R.id.overlay_image);

		FrameLayout layout = (FrameLayout) findViewById(R.id.overlay_image_background);
		layout.getBackground().setAlpha(100);

		FeedEventParams feedEventParams = new FeedEventParams(
				cs.SelectedImageUserId, "link", "placename",
				cs.SelectedImageEventName, "");
		_eventName.setText(feedEventParams.EventName);
		String userName = FacebookService.getInstance().GetUserNickName(
				feedEventParams.UserId, new HandleUserNameArrived() {

					@Override
					public void OnUserNameArrived(String userNameFacebook) {
						// TODO Auto-generated method stub
						super.OnUserNameArrived(userNameFacebook);
						_userName.setText(userNameFacebook);
					}

				});

		imageLoader
				.DisplayImage(feedEventParams.UserIdImage, _userImageOverlay);
	}

	public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// Calculate inSampleSize
		options.inSampleSize = 2; // calculateInSampleSize(options, reqWidth,
									// reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	@Override
	public void onBackPressed() {

		imageDetails.setImageResource(0);// Cleaer image to free memory
		super.onBackPressed();
	}
	ShareActionProvider actionProvider;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.layout.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

		// XXX: For now, ShareActionProviders must be displayed on the action
		// bar
		// Set file with share history to the provider and set the share intent.
		// MenuItem overflowItem =
		// menu.findItem(R.id.menu_item_share_action_provider_overflow);
		// ShareActionProvider overflowProvider =
		// (ShareActionProvider) overflowItem.getActionProvider();
		// overflowProvider.setShareHistoryFileName(
		// ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		// overflowProvider.setShareIntent(createShareIntent());

		return true;
	}

	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		
		mGaTracker.sendEvent("ui_action", "share", "UserIsSharingPicture", (long) 13);
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");

		Uri picUri = Uri.fromFile(new File(_fileName));
		shareIntent.putExtra(Intent.EXTRA_STREAM, picUri);
		shareIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						String.format(
								"Picaround has a picture to share with you, http://picaround.com/Gallery/GalleryByEventId?eventId=%s",
								cs.SelectedEventID));
		shareIntent.putExtra(Intent.EXTRA_SUBJECT,
				"PicAround has something to share with you.");

		return shareIntent;
	}

}
