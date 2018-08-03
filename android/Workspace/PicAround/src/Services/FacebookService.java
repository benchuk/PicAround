package Services;

import org.json.JSONException;
import org.json.JSONObject;
import Interfaces.IAsyncGetFacebookIdOperation;
import Interfaces.IAsyncSearchOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.socialcamera.PicAround.FacebookPlaceParams;
import com.socialcamera.PicAround.FacebookPlaces;

public class FacebookService
{
	private static FacebookService instance;
	private FacebookPlaceParams fpp;
	private Facebook facebook = new Facebook("360663317371353");
	private SharedPreferences mPrefs;
	public IAsyncGetFacebookIdOperation asyncGetFacebookIDOperation;
	public String SelectedPlace;
	// public String facebookUserID = null;
	Activity _mainActivity;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;

	public static FacebookService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new FacebookService();
		}
		return instance;
	}

	public void ExtendAccessTokenIfNeeded()
	{
		facebook.extendAccessTokenIfNeeded(_mainActivity, null);
	}

	private FacebookService() {
		// Constructor hidden because this is a singleton
	}

	public void GetPlacesAsync(Location location, IAsyncSearchOperation onCompletedDelegate)
	{
		FacebookPlaces fp = new FacebookPlaces(onCompletedDelegate);
		fpp = new FacebookPlaceParams(location);
		fp.execute(fpp);

	};

	public void GetFacebookIdAsync()
	{

		if (ContainerService.getInstance().FacebookId != null)
		{
			if (asyncGetFacebookIDOperation != null)
			{
				asyncGetFacebookIDOperation.GetIdCompleted(ContainerService.getInstance().FacebookId);
			}
		}
		else
		{
			AsyncGetFacebookIDOperation getID = new AsyncGetFacebookIDOperation()
			{

				@Override
				public void GetIdCompleted(String facebookID)
				{
					if (facebookID != null)
					{
						ContainerService.getInstance().FacebookId = facebookID;
						if (asyncGetFacebookIDOperation != null)
						{
							asyncGetFacebookIDOperation.GetIdCompleted(facebookID);
						}
					}
				}
			};
			FacebookGetIdAsync fid = new FacebookGetIdAsync(getID);
			if (mPrefs == null)
			{
				if (_mainActivity == null)
				{
					if (asyncGetFacebookIDOperation != null)
					{
						asyncGetFacebookIDOperation.GetIdCompleted(ContainerService.getInstance().FacebookId);
					}
					return;
				}
				mPrefs = _mainActivity.getPreferences(0);
			}
			String access_token = mPrefs.getString("access_token", null);
			FacebookGetIdParams fdip = new FacebookGetIdParams();
			fdip.facebook = facebook;
			fdip.access_token = access_token;
			fid.execute(fdip);
		}
	};

	public void AuthorizeCallback(int requestCode, int resultCode, android.content.Intent data)
	{
		try
		{
			facebook.authorizeCallback(requestCode, resultCode, data);
		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false);
			Log.v("picup", e.toString());
			Log.v("picup", e.getStackTrace().toString());
		}
	}

	public boolean LoadService(Activity activity)
	{
		_mainActivity = activity;
		mPrefs = _mainActivity.getPreferences(0);
		mGaInstance = GoogleAnalytics.getInstance(activity);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");

		/*
		 * Get existing access_token if any
		 */
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null)
		{
			facebook.setAccessToken(access_token);
		}
		if (expires != 0)
		{
			facebook.setAccessExpires(expires);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		boolean isfacebookSessionValid = facebook.isSessionValid();
		if (!isfacebookSessionValid)
		{

			facebook.authorize(activity, new String[]
			{
				"publish_checkins"
			}, Facebook.FORCE_DIALOG_AUTH, new DialogListener() {

				@Override
				public void onFacebookError(FacebookError e)
				{
					// TODO Auto-generated method stub
					mGaTracker.sendEvent("auto_action", "onFacebookError", "onFacebookError", (long) 1);
				}

				@Override
				public void onError(DialogError e)
				{

					mGaTracker.sendEvent("auto_action", "onError(FacebookError)", "onError", (long) 1);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_mainActivity);

					// set title
					alertDialogBuilder.setTitle("Error");

					// set dialog message
					alertDialogBuilder.setMessage("Chcek you network connection...").setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							// if this button is
							// clicked, close
							// current activity
							_mainActivity.finish();
						}
					});
					// .setNegativeButton("No",new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog,int
					// id) {
					// // if this button is clicked, just close
					// // the dialog box and do nothing
					// dialog.cancel();
					// }
					// });

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

				}

				@Override
				public void onComplete(Bundle values)
				{
					mGaTracker.sendEvent("auto_action", "onComplete(Facebook)", "onComplete", (long) 1);
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires", facebook.getAccessExpires());
					editor.commit();
					ContainerService cs = ContainerService.getInstance();
					if (cs != null)
					{
						if (cs.Main != null)
						{
							cs.Main.ShowMainTabs();
						}
					}
				}

				@Override
				public void onCancel()
				{
					mGaTracker.sendEvent("ui_action", "userCanceledLogin(Facebook)", "userCanceledLogin", (long) 1);
				}
			});
		}
		return isfacebookSessionValid;
	}

	public String GetUserNickNameGoogle(String userId, final HandleUserNameArrived handleUserNameArrived)
	{
		String baseUrl = "http://www.picaround.com/gpproxy/GetGooglePlusUserName?userid=" + userId;

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(baseUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response)
			{
//				JSONObject jObj;
//				try
//				{
//					jObj = new JSONObject(response);
//					String aJsonString = jObj.getString("name");
					handleUserNameArrived.OnUserNameArrived(response);
//				}
//				catch (JSONException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

			}

			@Override
			public void onFailure(Throwable arg0)
			{
				// TODO Auto-generated method stub
				super.onFailure(arg0);
			}
		});
		return userId;
	}
	
	public String GetUserNickName(String userId, final HandleUserNameArrived handleUserNameArrived)
	{
		String baseUrl = "http://graph.facebook.com/" + userId;

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(baseUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response)
			{
				JSONObject jObj;
				try
				{
					jObj = new JSONObject(response);
					String aJsonString = jObj.getString("name");
					handleUserNameArrived.OnUserNameArrived(aJsonString);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable arg0)
			{
				// TODO Auto-generated method stub
				super.onFailure(arg0);
			}
		});
		return userId;
	}

}
