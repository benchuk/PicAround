package WebHelpers;

import java.util.ArrayList;

import com.socialcamera.PicAround.EventsWebRequestHandler;

import android.os.AsyncTask;

public class ProcessEventsWebResult extends AsyncTask<EventsWebRequestHandlerParams, Integer, ArrayList>
{

	EventsWebRequestHandlerParams _eventsWebRequestHandlerParams;

	@Override
	protected ArrayList doInBackground(EventsWebRequestHandlerParams... eventsWebRequestHandlerParams)
	{

		
		for (EventsWebRequestHandlerParams eventsWebRequestHandlerParam : eventsWebRequestHandlerParams)
		{
			_eventsWebRequestHandlerParams = eventsWebRequestHandlerParam;
			EventsWebRequestHandler eh = new EventsWebRequestHandler();
			
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
	protected void onPostExecute(ArrayList eventsList)
	{
		super.onPostExecute(eventsList);
		if (_eventsWebRequestHandlerParams.eventsResultHandler != null)
		{
			_eventsWebRequestHandlerParams.eventsResultHandler.HandleEventResult(eventsList);
		}
	}

}
