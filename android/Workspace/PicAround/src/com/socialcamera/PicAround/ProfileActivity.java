package com.socialcamera.PicAround;

import java.io.IOException;

import Services.ContainerService;
import Services.FacebookService;
import Services.LocationService;
import WebHelpers.ImageArrivedHandler;
import WebHelpers.ProfileImageLoader;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class ProfileActivity extends SherlockActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener
{

	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;

	ActionBar bar;
	private ContainerService cs;
	private UiLifecycleHelper uiHelper;

	ProfilePictureView facebookProfileImage;
	private LoginButton facebookSingOutbutton;

	ImageView googleProfileImage;
	Button googleSingOutbutton;

	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.sign_out_button)
		{
			if (mPlusClient.isConnected())
			{
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
				cs.GooglepluseLogin = false;
				cs.UserName = "";
				cs.FacebookId = "";
				cs.FacebookUserImage = null;
				mGaTracker.sendEvent("ui_action", "button_press", "Google+ LOGOUT Android", (long) 1);
				Intent mainActivity = new Intent(getApplicationContext(), Main.class);

				startActivity(mainActivity);
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("com.package.ACTION_LOGOUT");
				sendBroadcast(broadcastIntent);

				finish();
			}
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception)
		{
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		if (!cs.GooglepluseLogin)
		{
			if (state.isOpened())
			{

			}
			else if (state.isClosed())
			{
				cs.UserName = "";
				cs.FacebookId = "";
				cs.FacebookUserImage = null;
				mGaTracker.sendEvent("ui_action", "button_press", "Facebook LOGOUT Android", (long) 1);
				Intent mainActivity = new Intent(getApplicationContext(), Main.class);

				startActivity(mainActivity);
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("com.package.ACTION_LOGOUT");
				sendBroadcast(broadcastIntent);

				finish();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		
		cs = ContainerService.getInstance();
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");

		facebookProfileImage = (ProfilePictureView) findViewById(R.id.userImage);
		facebookSingOutbutton = (LoginButton) findViewById(R.id.authButton);
		googleProfileImage = (ImageView) findViewById(R.id.google_profile_user_image);
		googleSingOutbutton = (Button) findViewById(R.id.sign_out_button);
		googleSingOutbutton.setOnClickListener(this);

		TextView userNameView = (TextView) findViewById(R.id.userName);
		userNameView.setText(cs.UserName);

		mPlusClient = new PlusClient.Builder(this, this, this).setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();
		// Progress bar to be displayed if the connection failure is not
		// resolved.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");

		uiHelper = new UiLifecycleHelper(this, callback);

		

		bar = getSupportActionBar();
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("Profile Settings");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);

		bar.setIcon(cs.FacebookUserImage);

		if (cs.GooglepluseLogin)
		{
			facebookProfileImage.setVisibility(View.INVISIBLE);
			facebookSingOutbutton.setVisibility(View.INVISIBLE);

			// googleSingOutbutton.setAlpha((float) 0.0);
			// button.setBackgroundColor(Color.RED);
			ProfileImageLoader getimage = new ProfileImageLoader();
			try
			{
				getimage.GetGoogleImageProfile(cs.FacebookId, getApplicationContext(), getResources(), new ImageArrivedHandler()
				{
					@Override
					public void OnImageArrive(final Bitmap bitmap)
					{
						new Handler().post(new Runnable() {
							@Override
							public void run()
							{
								// Drawable drawable = new
								// BitmapDrawable(getResources(), bitmap);
								// cs.FacebookUserImage = drawable;
								// bar.setIcon(cs.FacebookUserImage);
								googleProfileImage.setImageBitmap(bitmap);
							}
						});

					};

				});
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else //Facebook Login
		{
			googleProfileImage.setVisibility(View.INVISIBLE);
			googleSingOutbutton.setVisibility(View.INVISIBLE);
			facebookProfileImage.setProfileId(ContainerService.getInstance().FacebookId);
		}
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 0)
		{
			return false;
		}
		
		switch (item.getItemId())
		{
			case 0:
				return super.onOptionsItemSelected(item);
			case 1:
				mGaTracker.sendEvent("auto_action_android", "menu_press", "Send feedback", (long) 1);
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				/* Fill it with Data */
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"wwwpicaround@gmail.com"});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback for PicAround Android Version 1.3");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Android Version 1.3 Feedback:");			
				startActivity(emailIntent);
				
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	ShareActionProvider actionProvider;
	Menu _menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		menu.add(0, 1, 0, "Feedback").setIcon(R.drawable.ic_action_send).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	
		return true;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/ProfileSettings(start)");
		if (cs.GooglepluseLogin)
		{
			cs.MainActivity = false;
			mPlusClient.connect();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		if (cs.GooglepluseLogin)
		{
			mPlusClient.disconnect();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
		mGaTracker.sendView("/ProfileSettings(end)");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (!cs.GooglepluseLogin)
		{
			super.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}

		if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK)
		{
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()))
		{
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result)
	{
		if (mConnectionProgressDialog.isShowing())
		{
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			if (result.hasResolution())
			{
				try
				{
					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				}
				catch (SendIntentException e)
				{
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = result;
	}

	@Override
	public void onDisconnected()
	{
		// Log.d(TAG, "disconnected");
	}

	@Override
	public void onConnected(Bundle connectionHint)
	{
		cs.GooglepluseLogin = true;
		// We've resolved any connection errors.
		mConnectionProgressDialog.dismiss();
		String accountName = mPlusClient.getAccountName();
		String name = mPlusClient.getCurrentPerson().getDisplayName();
		// Toast.makeText(this, accountName + " is connected.",
		// Toast.LENGTH_LONG).show();
		// Toast.makeText(this, name + " is connected.",
		// Toast.LENGTH_LONG).show();

		cs.UserName = name;
		cs.FacebookId = mPlusClient.getCurrentPerson().getId();
		cs.PlusClient = mPlusClient;

	}
}
