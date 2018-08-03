package WebHelpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

public class WebRequest
{
	private String TAG = "Picup";

	public void PostData(HttpRequestParameters parameters)
	{
		try
		{
		WebRequestTask task = new WebRequestTask();
		task.execute(parameters);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Picup -> PostData" + e.toString());
		}
	}

	public void GetData(HttpRequestParameters parameters)
	{
		WebRequestTaskGet task = new WebRequestTaskGet();
		task.execute(parameters);
	}
	
	private class WebRequestTask extends AsyncTask<HttpRequestParameters, Integer, HttpResponse>
	{
		HttpRequestParameters httpRequestParameters;

		@Override
		protected HttpResponse doInBackground(HttpRequestParameters... parametersList)
		{
			try
			{
				for (HttpRequestParameters params : parametersList)
				{
					httpRequestParameters = params;
					// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(params.url); // For
																	// example:
					// "http://benchuk.no-ip.info/controller/action

					try
					{
						UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params.NameValuePairs, "UTF-8");
						httpPost.setEntity(urlEncoded);
					}
					catch (UnsupportedEncodingException e1)
					{
						Log.i(TAG, "Picup " + e1.toString());
						e1.printStackTrace();
					}

					try
					{
						// Execute HTTP Post Request
						HttpResponse response = httpclient.execute(httpPost);
						return response;
					}
					catch (ClientProtocolException e)
					{
						Log.i(TAG, "Picup " + e.toString());
					}
					catch (IOException e)
					{
						Log.i(TAG, "Picup " + e.toString());
					}

					return null;
				}
			}
			catch (Exception e)
			{
				Log.i(TAG, "Picup -> PostData" + e.toString());
			}

			return null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			// setProgressPercent(progress[0]);

		}

		@Override
		protected void onPostExecute(HttpResponse httpResponse)
		{
			super.onPostExecute(httpResponse);
			try
			{
				if (httpRequestParameters != null && httpRequestParameters.webRequestHandler != null)
				{
					httpRequestParameters.webRequestHandler.OnWebRequestCompleted(httpResponse);
				}
			}
			catch (Exception e)
			{
				Log.i(TAG, "Picup -> PostData" + e.toString());
			}
		}
	}
	private class WebRequestTaskGet extends AsyncTask<HttpRequestParameters, Integer, HttpResponse>
	{
		HttpRequestParameters httpRequestParameters;
		@Override
		protected HttpResponse doInBackground(HttpRequestParameters... parametersList)
		{

			for (HttpRequestParameters params : parametersList)
			{
				httpRequestParameters = params;
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(); // For example:
														// "http://benchuk.no-ip.info/controller/action
				
			
					try {
						httpGet.setURI(new URI(params.url));
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				
				try
				{
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httpGet);
					return response;
				}
				catch (ClientProtocolException e)
				{
					// TODO Auto-generated catch block
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
				}
				
				return null;
			}
			return null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			// setProgressPercent(progress[0]);

		}

		@Override
		protected void onPostExecute(HttpResponse httpResponse)
		{
			super.onPostExecute(httpResponse);
			if (httpRequestParameters!=null && httpRequestParameters.webRequestHandler!=null)
			{
				httpRequestParameters.webRequestHandler.OnWebRequestCompleted(httpResponse);
			}
		}
	}


}