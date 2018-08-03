package WebHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

import Services.Utils;
import android.R.drawable;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class ProfileImageLoader
{
	Resources _resources;
	ImageArrivedHandler _imageArrivedHandler;
	public Drawable GetGoogleImageProfile(String id, Context context, Resources resources, ImageArrivedHandler imageArrivedHandler) throws IOException
	{
		_imageArrivedHandler = imageArrivedHandler;
		URL img_value = null;
		img_value = new URL("https://www.google.com/s2/photos/profile/" + id);
		_resources = resources;
		new RetreiveImageTask().execute(img_value);
	
		return null;

	}
	
	public Drawable GetImageProfile(String id, Context context, Resources resources, ImageArrivedHandler imageArrivedHandler) throws IOException
	{
		_imageArrivedHandler = imageArrivedHandler;
		URL img_value = null;
		img_value = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
		_resources = resources;
		new RetreiveImageTask().execute(img_value);
		// try
		// {
		// File f = new File(FileCache(context),"progileImage.png");
		// Bitmap bitmap = null;
		// URL imageUrl = new URL(url);
		// HttpURLConnection conn = (HttpURLConnection)
		// imageUrl.openConnection();
		// conn.setConnectTimeout(30000);
		// conn.setReadTimeout(30000);
		// conn.setInstanceFollowRedirects(true);
		// InputStream is = conn.getInputStream();
		// OutputStream os = new FileOutputStream(f);
		// Utils.CopyStream(is, os);
		// os.close();
		// bitmap = decodeFile(f);
		// Drawable drawable = new BitmapDrawable(resources, bitmap);
		// return drawable;
		// }
		// catch (Exception ex)
		// {
		// Tracker myTracker = EasyTracker.getTracker();
		// myTracker.sendException(ex.getMessage(), false);
		// ex.printStackTrace();
		// }
		// return null;
		return null;

	}

	class RetreiveImageTask extends AsyncTask<URL, Void, Bitmap>
	{
		protected Bitmap doInBackground(URL... urls)
		{

			URL url = urls[0];
			try
			{
				return BitmapFactory.decodeStream(url.openConnection().getInputStream());
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Bitmap bitmap)
		{
			if (_imageArrivedHandler != null)
			{

				_imageArrivedHandler.OnImageArrive(bitmap);
			}
		}
	}

	private File FileCache(Context context)
	{
		File cacheDir;
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists()) cacheDir.mkdirs();
		return cacheDir;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f)
	{
		try
		{
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true)
			{
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		}
		catch (FileNotFoundException e)
		{
		}
		return null;
	}
}
