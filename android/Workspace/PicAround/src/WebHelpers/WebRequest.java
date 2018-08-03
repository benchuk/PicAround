package WebHelpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import android.os.AsyncTask;
import android.util.Log;

public class WebRequest
{
	private String TAG = "PicAround";

	public void PostData(HttpRequestParameters parameters)
	{
		try
		{
			WebRequestTask task = new WebRequestTask();
			task.execute(parameters);
		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false); 
			Log.i(TAG, "Picup -> PostData" + e.toString());
		}
	}

	public void GetData(HttpRequestParameters parameters)
	{
		if (parameters.NameValuePairs == null)
		{
			ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
			parameters.NameValuePairs = NameValuePairs;
		}
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

					if (params.NameValuePairs != null)
					{
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
				Tracker myTracker = EasyTracker.getTracker();
				myTracker.sendException(e.getMessage(), false); 
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
				Tracker myTracker = EasyTracker.getTracker();
				myTracker.sendException(e.getMessage(), false); 
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
				HttpContext localContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet(params.url); // For example:
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
					HttpResponse response = null;// = httpclient.execute(httpGet,localContext);
					return response;
				}
				catch (Exception e)
				{
					Tracker myTracker = EasyTracker.getTracker();
					myTracker.sendException(e.getMessage(), false); 
					e.printStackTrace();
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
			if (httpRequestParameters != null && httpRequestParameters.webRequestHandler != null)
			{
				httpRequestParameters.webRequestHandler.OnWebRequestCompleted(httpResponse);
			}
		}
	}

}