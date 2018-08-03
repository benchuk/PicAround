package com.android.PubPIc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import WebHelpers.EventParams;
import android.R.bool;
import android.util.Log;

public class FindIfEventsExistsWebRequestHandler
{
	private String TAG = "Picup";
	public ArrayList GetEventsforplace(HttpResponse httpResponse)
	{
		
		JSONArray jsonArray;

		// httpResponse = client.execute(request);
		HttpEntity entity = httpResponse.getEntity();
		InputStream is = null;
		try
		{
			is = entity.getContent();
		}
		catch (IllegalStateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		String response = convertStreamToString(is);
		response = "{" + '"' + "a" + '"' + ":" + response + "}";
		try
		{
			jsonArray = new JSONObject(response).getJSONArray("a");
		}
		catch (JSONException e)
		{
			Log.i(TAG, "Picup " +  e.toString());
			e.printStackTrace();
			Log.i(TAG, "Picup " +  e.getStackTrace().toString());
			return null;
		}
		ArrayList listArray = new ArrayList();
		for (int i = 0; i < jsonArray.length(); ++i)
		{
			JSONObject rec = null;
			try
			{
				rec = jsonArray.getJSONObject(i);
			}
			catch (JSONException e)
			{
				Log.i(TAG, "Picup " +  e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup " + e.getStackTrace().toString());
				return null;
			}
			String id = null;
			try
			{
				id = rec.getString("Name");
			}
			catch (JSONException e)
			{
				Log.i(TAG, "Picup" +  e.toString());
				e.printStackTrace();
				Log.i(TAG,"Picup" + e.getStackTrace().toString());
				return null;
			}
			listArray.add(id);
		}
		return listArray;
	}

	public boolean GetifEventExists(HttpResponse httpResponse)
	{
		
		JSONArray jsonArray;
		if (httpResponse != null)
		{
			// httpResponse = client.execute(request);
			HttpEntity entity = httpResponse.getEntity();
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
				Log.i(TAG, "Picup " +  e1.toString());
				Log.i(TAG, "Picup " +  e1.getStackTrace().toString());
			}

			String response = convertStreamToString(is);
			//response = "{" + '"' + "a" + '"' + ":" + response + "}";
			try
			{
				String count = (String) new JSONObject(response).get("Count");
				int cnt = Integer.parseInt(count);
		//		rec = jsonArray.getJSONObject(0);
				if( cnt!=0)
				{
						
					return true;
				}
			}
			catch (JSONException e)
			{
				Log.i(TAG, "Picup " +  e.toString());
				e.printStackTrace();
				Log.i(TAG, "Picup " + e.getStackTrace().toString());
				//return null;
			}
			
		
		}
		
		return false;
	
	}

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
			Log.i(TAG, "Picup " +  e.toString());
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
				Log.i(TAG, "Picup " +  e.toString());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
