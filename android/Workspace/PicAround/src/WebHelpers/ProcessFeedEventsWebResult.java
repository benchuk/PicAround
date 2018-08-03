package WebHelpers;

import java.util.ArrayList;

import com.socialcamera.PicAround.FeedEventWebRequestHandler;

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
