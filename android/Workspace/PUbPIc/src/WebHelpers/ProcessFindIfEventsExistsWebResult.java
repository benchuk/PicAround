package WebHelpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.PubPIc.EventsWebRequestHandler;
import com.android.PubPIc.FindIfEventsExistsWebRequestHandler;

import android.os.AsyncTask;

public class ProcessFindIfEventsExistsWebResult extends AsyncTask<FindIfEventsExsitsWebRequestHandlerParams, Integer, Boolean>
{

	FindIfEventsExsitsWebRequestHandlerParams _eventsWebRequestHandlerParams;

	@Override
	protected Boolean doInBackground(FindIfEventsExsitsWebRequestHandlerParams... findIfEventsExsitsWebRequestHandlerParams)
	{

		
		for (FindIfEventsExsitsWebRequestHandlerParams eventsWebRequestHandlerParam : findIfEventsExsitsWebRequestHandlerParams)
		{
			_eventsWebRequestHandlerParams = eventsWebRequestHandlerParam;
			FindIfEventsExistsWebRequestHandler eh = new FindIfEventsExistsWebRequestHandler();
			
			return eh.GetifEventExists(_eventsWebRequestHandlerParams.httpResponse); 
		}
		return false;
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
	protected void onPostExecute(Boolean eventsList)
	{
		super.onPostExecute(eventsList);
		if (_eventsWebRequestHandlerParams.eventsResultHandler != null)
		{
			_eventsWebRequestHandlerParams.eventsResultHandler.HandleEventResult(eventsList);
		}
	}

}
