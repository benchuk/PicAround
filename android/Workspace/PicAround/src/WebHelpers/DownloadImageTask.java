package WebHelpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
	ImageView bmImage;
	DoneHandler _doneHandler;
	int _imageNumberIndex;

	public DownloadImageTask(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	public DownloadImageTask(ImageView bmImage, int imageNumberIndex, DoneHandler doneHandler) {
		this.bmImage = bmImage;
		_doneHandler = doneHandler;
		_imageNumberIndex = imageNumberIndex;
	}

	protected Bitmap doInBackground(String... urls)
	{
		System.gc();
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try
		{
			InputStream in = new java.net.URL(urldisplay).openStream();
			Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			mIcon11 = BitmapFactory.decodeStream(in,null, options);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			mIcon11.compress(Bitmap.CompressFormat.JPEG, 30, bytes);

			// you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(Environment.getExternalStorageDirectory() + "/PicAroundCache" + File.separator + "PicAround" + _imageNumberIndex + ".jpg");
			f.createNewFile();
			// write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());

			// remember close de FileOutput
			fo.close();
		}
		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false); 
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result)
	{
		bmImage.setImageBitmap(result);
		bmImage.setAnimation(null);
		FrameLayout.LayoutParams imageViewParams2 = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,Gravity.CENTER);
		bmImage.setLayoutParams(imageViewParams2);
		if (_doneHandler != null)
		{
			_doneHandler.Done();
		}
	}
}