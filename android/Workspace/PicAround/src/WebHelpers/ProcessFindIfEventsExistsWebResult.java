package WebHelpers;

import com.socialcamera.PicAround.FindIfEventsExistsWebRequestHandler;

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
