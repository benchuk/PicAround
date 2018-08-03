package com.android.PubPIc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.IAsyncSearchOperation;
import Interfaces.IPlacesResult;
import Services.ContainerService;
import Services.FacebookService;
import Services.PlacesParams;
import android.app.ListActivity;

import com.android.PubPIc.R.drawable;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class FacebookPlaces extends AsyncTask<FacebookPlaceParams, Integer, Integer>
{
	private String TAG = "Picup";
	ProgressDialog _dialog;
	Activity _view;
	ArrayList<PlacesParams> PlacesResults;
	IAsyncSearchOperation _searchnCompletedDelegate;
	private ContainerService cs;

	public FacebookPlaces(Activity view, IAsyncSearchOperation searchnCompletedDelegate) {
		_view = view;
		_searchnCompletedDelegate = searchnCompletedDelegate;
	}

	protected Integer doInBackground(FacebookPlaceParams... fParams)
	{
		for (FacebookPlaceParams facebookParams : fParams)
		{
 			Bundle params = new Bundle();
			params.putString("type", "place");
			params.putString("center", facebookParams.Location.getLatitude() + "," + facebookParams.Location.getLongitude());
			params.putString("distance", "1000");
			params.putString("access_token", facebookParams.AccessToken);

			// String response;
			String response = null;
			try
			{
				response = facebookParams.Facebook.request("search", params);
			}
			catch (MalformedURLException e)
			{
				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				return 0;
			}
			catch (IOException e)
			{
				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				return 0;
			}
			JSONObject json = null;
			try
			{
				json = Util.parseJson(facebookParams.Facebook.request("me"));
			}
			catch (FacebookError e)
			{

				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup " + e.getStackTrace().toString());
				return 0;
			}
			catch (MalformedURLException e)
			{

				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup " + e.getStackTrace().toString());
				return 0;
			}
			catch (JSONException e)
			{

				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup " + e.getStackTrace().toString());
				return 0;
			}
			catch (IOException e)
			{

				Log.i(TAG, "Picup" + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup" + e.getStackTrace().toString());
				return 0;
			}
			String userId = null;
			try
			{
				userId = json.getString("id");
				cs.getInstance();
				if (cs != null)
				{
					cs.FacebookId = userId;
				}
				facebookParams.FacebookUserID = userId;

//				URL img_value = new URL("http://graph.facebook.com/" + userId + "/picture?type=large");
//				Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
//				Drawable FaceBookImage = new BitmapDrawable(mIcon1);
				
				

				ContainerService.getInstance().FacebookId = userId;
				FacebookService.getInstance().facebookUserID = userId;
				Log.i(TAG, "Picup facebook User ID: " + userId);
			}
			catch (JSONException e)
			{

				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup" + e.getStackTrace().toString());
				return 0;
			}
//			catch (MalformedURLException e)
//			{
//				Log.i(TAG, "Picup " +  e.toString());
//				Log.i(TAG, "Picup" + e.getStackTrace().toString());
//				e.printStackTrace();
//			}
//			catch (IOException e)
//			{
//				Log.i(TAG, "Picup " +  e.toString());
//				Log.i(TAG, "Picup " + e.getStackTrace().toString());
//				e.printStackTrace();
//			}
			// Toast.makeText(_view, userId, Toast.LENGTH_LONG).show();
			JSONArray jsonArray = null;
			try
			{
				jsonArray = new JSONObject(response).getJSONArray("data");
			}
			catch (JSONException e)
			{

				Log.i(TAG, "Picup" + e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup" + e.getStackTrace().toString());
				return 0;
			}
			ArrayList<PlacesParams> listArray = new ArrayList<PlacesParams>();
			for (int i = 0; i < jsonArray.length(); ++i)
			{
				JSONObject rec = null;
				try
				{
					rec = jsonArray.getJSONObject(i);
				}
				catch (JSONException e)
				{
					Log.i(TAG, "Picup " + e.toString());
					e.printStackTrace();
					Log.i(TAG, "Picup " + e.getStackTrace().toString());
					return 0;
				}
				String id = null;
				PlacesParams placesParams = new PlacesParams();
				try
				{

					placesParams.Id = rec.getString("id");
					placesParams.Name = getStringValue(rec, "name");
					placesParams.Type = getStringValue(rec, "category");
					placesParams.PicUrl = "http://a2.twimg.com/profile_images/577134561/UnivPlace-BOARDWALK-3_reasonably_small.jpg";
					JSONObject jsonLoca = rec.getJSONObject("location");
					placesParams.City = getStringValue(jsonLoca, "city");
					placesParams.Country = getStringValue(jsonLoca, "country");
					placesParams.Longtitude = getStringValue(jsonLoca, "longitude");
					placesParams.Latitude = getStringValue(jsonLoca, "latitude");

				}
				catch (JSONException e)
				{

					Log.i(TAG, "Picup " + e.toString());
					e.printStackTrace();
					Log.i(TAG, "Picup" + e.getStackTrace().toString());
					return 0;
				}
				listArray.add(placesParams);
			}
			PlacesResults = listArray;
			// listArray.toArray(PlacesResults);

			
		}
	
		return 0;
	}

	// private void ReportError()
	// {
	// Toast.makeText(_view, "Network Error", Toast.LENGTH_LONG).show();
	// }
	//
	// private void ReportError(String message)
	// {
	// Toast.makeText(_view, message, Toast.LENGTH_LONG).show();
	// }

	

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
				Log.i(TAG, "Picup " +  e.toString());
				Log.i(TAG, "Picup" + e.getStackTrace().toString());
				e.printStackTrace();
			}
		}
		return "";
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
	protected void onPostExecute(Integer result)
	{
		super.onPostExecute(result);
		// _dialog.dismiss();
		if (PlacesResults != null)
		{
			_searchnCompletedDelegate.searchComleted(new FacebookPlacesResult(PlacesResults));
		}
	}
}

// startActivity(emailIntent);
// JSONObject jsonObject;
// String location =
// jsonObject.getJSONObject("location").getString("street") + ", "
// + jsonObject.getJSONObject("location").getString("city") + ", "
// + jsonObject.getJSONObject("location").getString("state");

// String loc = rec.getString("loc");
// ...

// jsonArray.toString();
// emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
// aEmailList);
//
// emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
// "My subject");
//
// emailIntent.setType("plain/text");
// emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
// jsonArray.toString());

// facebook.request(" https://graph.facebook.com/search",
// parameters)
// String response =facebook.request("search");
// Bundle parameters = new Bundle();
// parameters.putString("access_token", "131445373646382");
// parameters.putString("type", "places");
// // parameters.putString("place", "131445373646382");
// // parameters.putString("place", placeID);
// // parameters.putString("Message",msg);
// JSONObject coordinates = new JSONObject();
// coordinates.put("latitude", Location.getLatitude());
// coordinates.put("longitude", Location.getLongitude());
// parameters.putString("coordinates",coordinates.toString());
// parameters.putString("distance", "1000");
// // parameters.putString("tags", tags);
// String response = facebook.request("search", parameters);
/*
 * Source tag: fetch_places_tag
 */

// Intent emailIntent = new
// Intent(android.content.Intent.ACTION_SEND);
//
// String aEmailList[] = { "maty21@gmail.com","benchuk@gmail.com" };
// byte[] aa= response.getBytes();
//
// {
// "maty21@gmail.com", "benchuk@gmail.com",
// "meidang@gmail.com", "efi.shtain@gmail.com"
// };
// Charset UTF8_CHARSET = Charset.forName("UTF-8");