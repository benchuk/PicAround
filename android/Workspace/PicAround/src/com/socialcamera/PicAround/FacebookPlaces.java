package com.socialcamera.PicAround;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Interfaces.IAsyncSearchOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.PlacesParams;
import WebHelpers.HttpRequestParameters;

import com.facebook.Response;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FacebookPlaces extends AsyncTask<FacebookPlaceParams, Integer, Integer>
{
	private String TAG = "PicAround";
	ProgressDialog _dialog;
	ArrayList<PlacesParams> PlacesResults;
	IAsyncSearchOperation _searchnCompletedDelegate;
	private ContainerService cs;

	public FacebookPlaces(IAsyncSearchOperation searchnCompletedDelegate) {
		_searchnCompletedDelegate = searchnCompletedDelegate;
	}

	protected Integer doInBackground(FacebookPlaceParams... fParams)
	{
		FacebookPlaceParams facebookParams = fParams[0];

		// Bundle params = new Bundle();
		// params.putString("latlong", facebookParams.Location.getLatitude() +
		// "," + facebookParams.Location.getLongitude());
		// http://picaround.azurewebsites.net/
		// String response;
		// String response = null;
		HttpResponse response = null;
		String baseUrl = "http://picaround.azurewebsites.net/FBProxy/Places";
		baseUrl += "?latlong=" + facebookParams.Location.getLatitude() + "," + facebookParams.Location.getLongitude();
		HttpClient httpclient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(baseUrl); // For example:
												// "http://benchuk.no-ip.info/controller/action

		// try {
		// httpGet.setURI(new URI(params.url));
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try
		{
			// Execute HTTP Post Request
			response = httpclient.execute(httpGet, localContext);

		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false);
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		InputStream is = null;
		try
		{
			is = entity.getContent();
		}
		catch (IllegalStateException e1)
		{
			e1.printStackTrace();
			Log.i(TAG, "Picup " + e1.toString());
			Log.i(TAG, "Picup " + e1.getStackTrace().toString());
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			Log.i(TAG, "Picup " + e1.toString());
			Log.i(TAG, "Picup " + e1.getStackTrace().toString());
		}

		String responseString = convertStreamToString(is);
		// responseString = "{" + '"' + "a" + '"' + ":" + responseString + "}";

		JSONArray jsonArray = null;
		try
		{
			jsonArray = new JSONObject(responseString).getJSONArray("data");
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
			PlacesParams placesParams;
			try
			{
				JSONObject jsonLoca = rec.getJSONObject("location");
				placesParams = new PlacesParams(rec.getString("id"), getStringValue(rec, "name"), getStringValue(rec, "category"), getStringValue(jsonLoca, "longitude"), getStringValue(jsonLoca, "latitude"), getStringValue(jsonLoca, "country"), getStringValue(jsonLoca, "city"), "http://a2.twimg.com/profile_images/577134561/UnivPlace-BOARDWALK-3_reasonably_small.jpg");

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

	private String convertStreamToString(InputStream is)
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append((line + "\n"));
			}
		}
		catch (IOException e)
		{
			Log.i(TAG, "Picup " + e.toString());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				Log.i(TAG, "Picup " + e.toString());
				e.printStackTrace();
			}
		}
		return sb.toString();
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
				Log.i(TAG, "Picup " + e.toString());
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