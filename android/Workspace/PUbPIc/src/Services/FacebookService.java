package Services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;

import Interfaces.IAsyncGetFacebookIdOperation;
import Interfaces.IAsyncSearchOperation;
import WebHelpers.HttpRequestParameters;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

import com.android.PubPIc.FacebookPlaceParams;
import com.android.PubPIc.FacebookPlaces;
import com.android.PubPIc.Main;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookService
{
	private static FacebookService instance;
	private FacebookPlaceParams fpp;
	private Facebook facebook = new Facebook("360663317371353");
	private SharedPreferences mPrefs;
	public IAsyncGetFacebookIdOperation asyncGetFacebookIDOperation;
	public String SelectedPlace;
	public String facebookUserID = null;

	public static FacebookService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new FacebookService();
		}
		return instance;
	}

	public void ExtendAccessTokenIfNeeded(Activity activity)
	{
		
		facebook.extendAccessTokenIfNeeded(activity, null);
	}

	private FacebookService() {
		// Constructor hidden because this is a singleton
	}

	public void GetPlacesAsync(Activity activity, Location location, IAsyncSearchOperation onCompletedDelegate)
	{

		FacebookPlaces fp = new FacebookPlaces(activity, onCompletedDelegate);
		String access_token = mPrefs.getString("access_token", null);
		fpp = new FacebookPlaceParams(location, facebook, access_token);
		fp.execute(fpp);
		
	};

	public void GetFacebookIdAsync()
	{
		if (facebookUserID != null)
		{
			if (asyncGetFacebookIDOperation != null)
			{
				asyncGetFacebookIDOperation.GetIdCompleted(facebookUserID);
			}
		}
		else
		{
		AsyncGetFacebookIDOperation getID = new AsyncGetFacebookIDOperation() {

			@Override
			public void GetIdCompleted(String facebookID)
			{
				facebookUserID = facebookID;
				if (asyncGetFacebookIDOperation != null)
				{
					asyncGetFacebookIDOperation.GetIdCompleted(facebookID);
				}
			}
		};
		FacebookGetIdAsync fid = new FacebookGetIdAsync(getID);
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
			Log.v("picup", e.toString());
			Log.v("picup", e.getStackTrace().toString());
		}
	}

	Activity _activity;
	public boolean LoadService(Activity activity)
	{
		_activity = activity;
		/*
		 * Get existing access_token if any
		 */
		mPrefs = activity.getPreferences(0);
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

				}

				@Override
				public void onError(DialogError e)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_activity);
			 
						// set title
						alertDialogBuilder.setTitle("Error");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Chcek you network connection...")
							.setCancelable(false)
							.setPositiveButton("Close",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									_activity.finish();
								}
							  });
//							.setNegativeButton("No",new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,int id) {
//									// if this button is clicked, just close
//									// the dialog box and do nothing
//									dialog.cancel();
//								}
//							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
						

				}

				@Override
				public void onComplete(Bundle values)
				{
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
					// TODO Auto-generated method stub

				}
			});
		}
		return isfacebookSessionValid;
	}

	public String GetUserNickName( String userId,  final HandleUserNameArrived handleUserNameArrived)
	{
		String baseUrl = "http://graph.facebook.com/"+ userId ;
		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{

				// TODO Auto-generated method stub
				super.OnWebRequestCompleted(httpResponse);
				
				//handleUserNameArrived.OnUserNameArrived(userId);
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

			
	}
		};
		new WebRequest().GetData(parameters);
		return userId;
	}

}
