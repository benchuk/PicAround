package Services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import Interfaces.IAsyncGetFacebookIdOperation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class FacebookGetIdAsync extends AsyncTask<FacebookGetIdParams, Integer, String>
{
	IAsyncGetFacebookIdOperation _getIdCompletedDelegate;
	FacebookGetIdParams _params;

	public FacebookGetIdAsync(IAsyncGetFacebookIdOperation getIdCompletedDelegate) {
		_getIdCompletedDelegate = getIdCompletedDelegate;
	}

	protected String doInBackground(FacebookGetIdParams... fParams)
	{
		String userId = null;
		for (FacebookGetIdParams facebookParams : fParams)
		{
			Bundle params = new Bundle();
			
			params.putString("access_token", facebookParams.access_token);
			
			// String response;
			String response = null;
			try
			{
				response = facebookParams.facebook.request("search", params);
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			JSONObject json = null;
			try
			{
				json = Util.parseJson(facebookParams.facebook.request("me"));
			}
			catch (FacebookError e)
			{
				Log.v("Picup", e.toString());
				e.printStackTrace();
				Log.v("Picup", e.getStackTrace().toString());
				return"";
			}
			catch (MalformedURLException e)
			{
				Log.v("Picup", e.toString());
				e.printStackTrace();
				Log.v("Picup", e.getStackTrace().toString());
				return"";
			}
			catch (JSONException e)
			{
				Log.v("Picup", e.toString());
				e.printStackTrace();
				Log.v("Picup", e.getStackTrace().toString());
				return"";
			}
			catch (IOException e)
			{
				Log.v("Picup", e.toString());
				e.printStackTrace();
				Log.v("Picup", e.getStackTrace().toString());
				return"";
			}
		
			try
			{
				userId = json.getString("id");
				getUserPicture(userId);
                //ContainerService.getInstance().FacebookId = userId;
				ContainerService.getInstance().FacebookId = userId;
               
            	return userId;
			}
			catch (JSONException e)
			{
				Log.v("Picup", e.toString());
				e.printStackTrace();
				Log.v("Picup", e.getStackTrace().toString());
				return"";
			}
			//Toast.makeText(_view, userId, Toast.LENGTH_LONG).show();
		
		}
			return "";
	}

	private String getStringValue(JSONObject jsonObject, String fieldName)
	{
		if (jsonObject.has(fieldName))
		{
			try
			{
				return jsonObject.getString(fieldName);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	private void getUserPicture(String facebookId)
	{
		Bitmap mIcon1 = null;
		Bitmap sacledFacebookProfileImage = null;
		URL img_value = null;
		try
		{
			img_value = new URL("http://graph.facebook.com/" + facebookId + "/picture?type=large");
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			//sacledFacebookProfileImage = Bitmap.createScaledBitmap(mIcon1,150, 150, true);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable FacebookImage = new BitmapDrawable(mIcon1);
		ContainerService.getInstance().FacebookUserImage = FacebookImage;

	}
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		// _dialog = ProgressDialog.show(_view, "",
		// "Searching places nearby...", true);
	}

	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		// setProgressPercent(progress[0]);

	}

	@Override
	protected void onPostExecute(String facebookId)
	{
		super.onPostExecute(facebookId);
		// _dialog.dismiss();

		_getIdCompletedDelegate.GetIdCompleted(facebookId);

	}

	

}
