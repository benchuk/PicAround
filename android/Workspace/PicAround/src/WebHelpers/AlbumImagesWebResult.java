package WebHelpers;

import java.util.ArrayList;

import com.socialcamera.PicAround.AlbumImagesWebRequestHandler;


import android.os.AsyncTask;

public class AlbumImagesWebResult extends AsyncTask<AlbumImagesWebRequestHandlerParams, Integer, ArrayList>
{

	AlbumImagesWebRequestHandlerParams _albumImagesWebRequestHandlerParams;

	@Override
	protected ArrayList doInBackground(AlbumImagesWebRequestHandlerParams... eventsWebRequestHandlerParams)
	{

		
		for (AlbumImagesWebRequestHandlerParams eventsWebRequestHandlerParam : eventsWebRequestHandlerParams)
		{
			_albumImagesWebRequestHandlerParams = eventsWebRequestHandlerParam;
			AlbumImagesWebRequestHandler eh = new AlbumImagesWebRequestHandler();
			
			return eh.GetObjectEventsforplace(_albumImagesWebRequestHandlerParams.httpResponse); 
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
	protected void onPostExecute(ArrayList images)
	{
		super.onPostExecute(images);
		if (_albumImagesWebRequestHandlerParams.albumImagesResultHandler != null)
		{
			_albumImagesWebRequestHandlerParams.albumImagesResultHandler.HandleResult(images);
		}
	}

}
