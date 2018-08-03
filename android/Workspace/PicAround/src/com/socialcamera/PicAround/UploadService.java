package com.socialcamera.PicAround;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class UploadService extends IntentService
{
	private String TAG = "Picup";
	private NotificationManager _notificationManager;

	public UploadService() {
		super("UploadService");

	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent)
	{
		Bitmap original = null;
		try
		{
			// String filePath = intent.getData();
			String fileName = intent.getStringExtra("filePath");
			// Uri uri = intent.getData();
			String userID = intent.getStringExtra("userid");
			String lat = intent.getStringExtra("lat");
			String lon = intent.getStringExtra("lon");
			String alt = intent.getStringExtra("alt");
			String place = intent.getStringExtra("place");
			String eventLabel = intent.getStringExtra("SelectedEventName");
			String eventID = intent.getStringExtra("SelectedEventID");
			String heading = intent.getStringExtra("heading");

			if (userID == null)
			{
				userID = "0";
			}
			// String fileName = data.getLastPathSegment();
			// upload.Upload(dir.getAbsolutePath() +"/"+ fileName,
			// PUbPIcActivity.this);

			// InputStream stream = getContentResolver().openInputStream(uri);

			// read this file into InputStream
			File rootsd = Environment.getExternalStorageDirectory();
			// File file = new File(rootsd.getAbsolutePath() + "/DCIM" + "/" +
			// fileName);
			//Uri selectedImage = Uri.fromFile(new File(fileName));
			//ContentResolver cr = UploadService.this.getApplication().getContentResolver();
			//Bitmap bitmap = null;
			//original = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
			original = BitmapFactory.decodeStream(new FileInputStream(fileName));
			FileOutputStream stream = new FileOutputStream(fileName);
			/* Write bitmap to file using JPEG and 80% quality hint for JPEG. */
			original.compress(CompressFormat.JPEG, 70, stream);
			stream.flush();
			stream.close();
			original.recycle();
			original = null;
			File file = new File(fileName);
			// File root =
			// Environment.getExternalStoragePublicDirectory(Environment.);
			// File dir;
			// File picFile;
			// if (!root.exists())
			// {
			// dir = new File(root.getAbsolutePath());
			// dir.mkdir();
			// }
			// else
			// {
			// dir = root;
			// }
			// String fileName = String.format("%d.jpg",
			// System.currentTimeMillis());

			// picFile = new File(dir, fileName);
			// // write the inputStream to a FileOutputStream
			// File image = new File(picFile.getAbsolutePath());
			// OutputStream out = new FileOutputStream(image);
			//
			// int read = 0;
			// byte[] bytes = new byte[1024];
			//
			// while ((read = stream.read(bytes)) != -1) {
			// out.write(bytes, 0, read);
			// }
			//
			// stream.close();
			// out.flush();
			// out.close();

			// //ExifInterface exif = new ExifInterface(root.getAbsoluteFile() +
			// "/" + image);
			// ExifInterface exif = new ExifInterface(root.getAbsoluteFile() +
			// "/" + fileName);
			// // exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,lat);
			// // exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,lon);
			//
			// exif.setAttribute(ExifInterface.TAG_ORIENTATION,
			// ""+ExifInterface.ORIENTATION_FLIP_HORIZONTAL);
			// exif.saveAttributes();
			//
			Bundle extras = intent.getExtras();
			Messenger messenger = (Messenger) extras.get("MESSENGER");

			UploadingFiles upload = new UploadingFiles();

			_notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			UploadedEventsHandler events = new UploadedEventsHandler(this, _notificationManager, messenger);
			UploadFileParams ufp = new UploadFileParams(userID, lat, lon, alt, heading, place, eventLabel, eventID);
			int res = 0;
			try
			{
				res = upload.uploadFile(file, events, ufp);
			}
			catch (Exception e)
			{
				Tracker myTracker = EasyTracker.getTracker();
				myTracker.sendException(e.getMessage(), false); 
				Log.i(TAG, "Picup :Upload failed " + e.getStackTrace().toString());
				Log.i(TAG, "Picup :Upload failed " + e.toString());
			}

			if (extras != null)
			{
				//Message msg = Message.obtain();
				// Sucessful finished == Activity.RESULT_OK;
				// msg.arg1 = res;
				// try
				// {
				// messenger.send(msg);
				// }
				// catch (android.os.RemoteException e1)
				// {
				// Log.i(TAG, "Picup" + e1.toString());
				// Log.i(TAG, "Picup Exception sending message" +
				// e1.getStackTrace().toString());
				// }
				upload = null;
			}

		}

		catch (Exception e)
		{
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false); 
			Log.i(TAG, "Picup" + e.toString());
			Log.i(TAG, "Picup " + e.getStackTrace().toString());
			e.printStackTrace();
		}
		finally
		{
			
		}

	}
}
