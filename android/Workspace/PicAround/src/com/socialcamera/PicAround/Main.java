package com.socialcamera.PicAround;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
//import com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectionFailedListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.plus.PlusClient;
import com.socialcamera.PicAround.R;
import Services.ContainerService;
import Services.FacebookService;
import Services.ImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;

public class Main extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener
{

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	private final int SPLASH_DISPLAY_LENGTH = 2500;
	private int _notificationID = 12358;
	private FacebookService fs;
	private ContainerService cs;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
	private boolean isFacebookSessionValid;
	private UiLifecycleHelper uiHelper;
	private static final String TAG = "Main";
	private boolean alreadyLoggedIn = false;
	// private MainFragment mainFragment;
	LoginButton button;
	private ProfilePictureView profilePictureView;
	SignInButton _googleButton;
	LinearLayout _signinLayout;
	
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
		//Log.d(TAG, "disconnected");
		//mGaTracker.sendEvent("General", "Android", "GooglePlus Logout Android", (long) 1);
	}

	@Override
	public void onConnected(Bundle connectionHint)
	{
		mGaTracker.sendEvent("General", "Android", "GooglePlus Login Android", (long) 1);
		
		cs.GooglepluseLogin = true;
		_googleButton.setVisibility(View.INVISIBLE);
		button.setVisibility(View.INVISIBLE);
		_signinLayout.setVisibility(View.INVISIBLE);
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

		if (cs.MainActivity)
		{
			ShowMainTabs();
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception)
		{
			onSessionStateChange(session, state, exception);
		}
	};

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		if(exception!=null){
			Toast.makeText(getApplicationContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
		}
		if (state.isOpened())
		{
			button.setVisibility(View.INVISIBLE);
			_googleButton.setVisibility(View.INVISIBLE);
			_signinLayout.setVisibility(View.INVISIBLE);
			// make request to the /me API
			Request.executeMeRequestAsync(session, new Request.GraphUserCallback()
			{
				@Override
				public void onCompleted(GraphUser user, Response response)
				{
					if (user != null && !alreadyLoggedIn)
					{
						alreadyLoggedIn = true;
						// fs.facebookUserID = user.getId();
						cs.FacebookId = user.getId();
						cs.UserName = user.getName();

						// com.facebook.widget.ProfilePictureView p = new
						// ProfilePictureView(getApplicationContext());
						// p.setDrawingCacheEnabled(true);
						// p.setProfileId(user.getId());
						// p.
						// ImageView fbImage = ( ( ImageView)p.getChildAt( 0));
						// Bitmap bitmap = ( ( BitmapDrawable)
						// fbImage.getDrawable()).getBitmap();
						//
						// Drawable drawable = new
						// BitmapDrawable(getResources(), bitmap);
						// cs.FacebookUserImage = drawable;
						mGaTracker.sendEvent("General", "Android", "Facebook Login Android", (long) 1);
						// profilePictureView.setProfileId(user.getId());
						ShowMainTabs();

						// welcome.setText("Hello " + user.getName() +
						// "!");
					}
					else if (!isNetworkAvailable())
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
						builder.setTitle("Facebook connection problem!");
						builder.setIcon(R.drawable.com_facebook_icon);
						builder.setMessage("Please check your internet connection.").setPositiveButton("Close", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								mGaTracker.sendEvent("auto_action", "locationProblem", "AddNewEventUserOpendLocationSettings", (long) 17);
								finish();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}

				}
			});
		}
		else if (state.isClosed())
		{
			//mGaTracker.sendEvent("ui_action", "button_press", "Facebook LOGOUT Android", (long) 1);
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/Main(start)");
		mPlusClient.connect();
		cs.MainActivity = true;
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
		mGaTracker.sendView("/Main(end)");
		mPlusClient.disconnect();
		cs.MainActivity = false;
	}
//	public void printHashKey() {
//
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.socialcamera.PicAround",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("TEMPTAGHASH KEY:",
//                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//
//    }
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// if (savedInstanceState == null)
		// {
		// // Add the fragment on initial activity setup
		// mainFragment = new MainFragment();
		// getSupportFragmentManager().beginTransaction().add(android.R.id.content,
		// mainFragment).commit();
		// }
		// else
		// {
		// // Or set the fragment from restored state info
		// mainFragment = (MainFragment)
		// getSupportFragmentManager().findFragmentById(android.R.id.content);
		//
		//
		// }
		setContentView(R.layout.splash);
		//printHashKey();
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");

		mPlusClient = new PlusClient.Builder(this, this, this).setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();
		// Progress bar to be displayed if the connection failure is not
		// resolved.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
		// Clear old (stacked) notifications from notification bar if any,
		// might
		// happen if we crash.
		NotificationManager _notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		_notificationManager.cancel(_notificationID);
		cs = ContainerService.getInstance();
		cs.Main = this;
		cs.GooglepluseLogin = false;
		if (!isTaskRoot())
		{
			final Intent intent = getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN))
			{

				finish();
				return;
			}
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		button = (LoginButton) findViewById(R.id.authButton);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		_googleButton = (SignInButton) findViewById(R.id.sign_in_button);
		_signinLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
		
		// start Facebook Login
		// Session.openActiveSession(this, true, new Session.StatusCallback() {
		//
		// @Override
		// public void call(Session session, SessionState state, Exception
		// exception)
		// {
		// if (session.isOpened())
		// {
		//
		// // make request to the /me API
		// Request.executeMeRequestAsync(session, new
		// Request.GraphUserCallback()
		// {
		// @Override
		// public void onCompleted(GraphUser user, Response response)
		// {
		// if (user != null)
		// {
		//
		// // welcome.setText("Hello " + user.getName() +
		// // "!");
		// }
		//
		// }
		// });
		// }
		//
		// }
		// });
		// Intent eventsIntent2= new Intent(Main.this, TabNavigation.class);
		// startActivityForResult(eventsIntent2,200);
		//
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
			_signinLayout.setVisibility(View.INVISIBLE);
			mConnectionResult = null;
			mPlusClient.connect();
		}

	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode,
	// android.content.Intent data)
	// {
	// super.onActivityResult(requestCode, resultCode, data);
	// fs.AuthorizeCallback(requestCode, resultCode, data);
	// ShowMainTabs();
	// }
	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
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

	public void ShowMainTabs()
	{
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run()
			{
				// Finish the splash activity so it can't be returned to.
				Main.this.finish();
				// Create an Intent that will start the main activity.
				// Intent tabsIntent = new Intent(Main.this,
				// TabNavigation.class);
				Intent tabsIntent = new Intent(Main.this, TabNavigation.class);
				Main.this.startActivity(tabsIntent);
			}
		}, SPLASH_DISPLAY_LENGTH);
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
	public void onClick(View view)
	{
		mGaTracker.sendEvent("ui_action", "button_press", "GooglePlus Login Android", (long) 1);
		cs.GooglepluseLogin = true;
		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected())
		{
			if (mConnectionResult == null)
			{
				mConnectionProgressDialog.show();
			}
			else
			{
				try
				{
					mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				}
				catch (SendIntentException e)
				{
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}

	// @Override
	// protected void onResume()
	// {
	// super.onResume();
	//
	// // fs = FacebookService.getInstance();
	// //
	// // isFacebookSessionValid = fs.LoadService(this);
	// //
	// // if (isFacebookSessionValid)
	// // {
	// // fs.GetFacebookIdAsync();
	// // fs.ExtendAccessTokenIfNeeded();
	// // ShowMainTabs();
	// // }
	// }
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data)
	// {
	// this.finish();
	// }
}
