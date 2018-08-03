package com.android.PubPIc;

import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.ImageLoader;
import Services.LocationService;
import WebHelpers.DownloadImageTask;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

public class PhotoDetailsActivity extends SherlockActivity
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	ImageView imageDetails;
	String _fileName;
	RotateAnimation _anim;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		this.getSherlock().setProgressBarIndeterminate(false);

		super.onCreate(savedInstanceState);
		cs = ContainerService.getInstance();
		setTheme(R.style.Theme_Sherlock);
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

		bar.setIcon(R.drawable.abs__spinner_48_inner_holo);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		_anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		_anim.setInterpolator(new LinearInterpolator());
		_anim.setRepeatCount(Animation.INFINITE);
		_anim.setDuration(2000);
		
		fs.getInstance().asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation()
		{
			@Override
			public void GetIdCompleted(String facebookID)
			{
				bar.setIcon(ContainerService.getInstance().FacebookUserImage);
				// isThereAreEventsNearToYou();
			}
		};
		fs.GetFacebookIdAsync();

		bar.setDisplayUseLogoEnabled(false);
		setSupportProgressBarIndeterminateVisibility(false);
		cs.ActionBar = bar;
		cs.ActionBar.setTitle(cs.SelectedEventName);
		cs.ActionBar.setSubtitle(cs.SelectedPlace);
		imageDetails = (ImageView) findViewById(R.id.imagedetails);
		
		imageDetails.startAnimation(_anim);
		
		if (!cs.UseImageView){
			new DownloadImageTask(imageDetails).execute(cs.SelectedImageLink);
			//DownloadImageTask SAVE THE IMAGE AS PicAround.jpg file.
			_fileName = Environment.getExternalStorageDirectory()+ File.separator + "PicAround.jpg";
		}
		else
		{
			String imagePath = cs.SelectedImageSDPath;
			
			Bitmap bitmap = decodeSampledBitmapFromResource(imagePath, 400, 400);
			_fileName = imagePath;
			imageDetails.setImageBitmap(bitmap);
			imageDetails.setAnimation(null);
		}
	}
	
	public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight)
	{

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// Calculate inSampleSize
		options.inSampleSize = 2; //calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	@Override
	public void onBackPressed() {
		
		imageDetails.setImageResource(0);//Cleaer image to free memory
		super.onBackPressed(); 
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate your menu.
	        getSupportMenuInflater().inflate(R.layout.share_action_provider, menu);

	        // Set file with share history to the provider and set the share intent.
	        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
	        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
	        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
	        // Note that you can set/change the intent any time,
	        // say when the user has selected an image.
	        actionProvider.setShareIntent(createShareIntent());

	        //XXX: For now, ShareActionProviders must be displayed on the action bar
	        // Set file with share history to the provider and set the share intent.
	        //MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
	        //ShareActionProvider overflowProvider =
	        //    (ShareActionProvider) overflowItem.getActionProvider();
	        //overflowProvider.setShareHistoryFileName(
	        //    ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
	        // Note that you can set/change the intent any time,
	        // say when the user has selected an image.
	        //overflowProvider.setShareIntent(createShareIntent());

	        return true;
	    }

	    /**
	     * Creates a sharing {@link Intent}.
	     *
	     * @return The sharing intent.
	     */
	    private Intent createShareIntent() {
	        Intent shareIntent = new Intent(Intent.ACTION_SEND);
	        shareIntent.setType("image/*");
	        
	        Uri picUri = Uri.fromFile(new File(_fileName));
	        shareIntent.putExtra(Intent.EXTRA_STREAM, picUri);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("Picaround has a picture to share with you, http://picaround.azurewebsites.net/Gallery/GalleryByEventId?eventId=%s",cs.SelectedEventID));
	        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PicAround has something to share with you.");
	        
	        return shareIntent;
	    }
	    
	    
}
