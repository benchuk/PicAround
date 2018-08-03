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
import com.android.PubPIc.FeedEventWebRequestHandler;

import android.os.AsyncTask;

public class ProcessFeedEventsWebResult extends AsyncTask<FeedEventsWebRequestHandlerParams , Integer, ArrayList>
{

	FeedEventsWebRequestHandlerParams _eventsWebRequestHandlerParams;

	@Override
	protected ArrayList doInBackground(FeedEventsWebRequestHandlerParams... feedEventsWebRequestHandlerParams)
	{

		
		for (FeedEventsWebRequestHandlerParams eventsWebRequestHandlerParam : feedEventsWebRequestHandlerParams)
		{
			_eventsWebRequestHandlerParams = eventsWebRequestHandlerParam;
			FeedEventWebRequestHandler eh = new FeedEventWebRequestHandler();
			
			return eh.GetObjectEventsforplace(_eventsWebRequestHandlerParams.httpResponse); 
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
	protected void onPostExecute(ArrayList feedEventsList)
	{
		super.onPostExecute(feedEventsList);
		if (_eventsWebRequestHandlerParams.feedEventsResultHandler != null)
		{
			_eventsWebRequestHandlerParams.feedEventsResultHandler.HandleEventResult(feedEventsList);
		}
	}

}
