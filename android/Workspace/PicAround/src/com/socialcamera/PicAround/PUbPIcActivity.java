package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.nio.charset.Charset;
//import java.sql.Date;
//import java.util.ArrayList;
//import java.text.SimpleDateFormat;
//
//import org.apache.http.HttpResponse;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
////import com.andrkoid.PubPIc.R;
//import com.android.PicAround.R;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
//import com.facebook.android.Util;
//
//import Services.FacebookService;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.IntentService;
//import android.app.ListActivity;
//import android.app.NotificationManager;
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.hardware.Camera;
//import android.hardware.Camera.PictureCallback;
//import android.hardware.Camera.ShutterCallback;
//import android.hardware.SensorManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.media.ExifInterface;
//import android.net.MailTo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Messenger;
//import android.provider.MediaStore;
//import android.provider.MediaStore.Audio.Media;
//import android.provider.MediaStore.Images;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.util.Xml.Encoding;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.OrientationEventListener;
//import android.view.Surface;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.view.animation.LayoutAnimationController;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class PUbPIcActivity extends Activity
//{
//	private static final String TAG = "PUbPIcActivity";
//	//Preview preview;
//	Button buttonClick;
//	
//	protected Button retrieveLocationButton;
//	public ImageView imageView;
//	public double latitude;
//	public double longitude;
//	public boolean duringCameraShot = false;
//	public Location Location;
//	protected ProgressDialog dialog;
//	private FrameLayout frameLayout;
//	private LinearLayout mainLayout;
//	
//	private OrientationEventListener mOrientationEventListener;
//	private int mOrientation = -1;
//	//private int _notificationID = 12358;
//
//	private SharedPreferences mPrefs;
//	private static final int ORIENTATION_PORTRAIT_NORMAL = 1;
//	private static final int ORIENTATION_PORTRAIT_INVERTED = 2;
//	private static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
//	private static final int ORIENTATION_LANDSCAPE_INVERTED = 4;
//	// LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
//	ArrayList<String> listItems = new ArrayList<String>();
//
//	// DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
//	ArrayAdapter<String> adapter;
//
//	private Handler handler = new Handler() {
//		public void handleMessage(Message message)
//		{
//			Object path = message.obj;
//			if (message.arg1 == RESULT_OK)
//			{
//				Toast.makeText(PUbPIcActivity.this, "Uploaded OK", Toast.LENGTH_LONG).show();
//			}
//			else
//			{
//				Toast.makeText(PUbPIcActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
//			}
//
//		};
//	};
//	private ListView placesList;
//	private JSONArray jsonArray;
//
//	@Override
//	protected void onDestroy()
//	{
//		super.onDestroy();
//		try
//		{
//			releaseCamera();
//		}
//		catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			ShowDialog("Fail to release camera");
//		}
//	};
//
//	
//
//	protected void showCurrentLocation() throws MalformedURLException, IOException, JSONException
//	{
//		String txt = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", latitude, longitude);
//
//		if (Location != null)
//		{
//			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", Location.getLongitude(), Location.getLatitude());
//			Toast.makeText(PUbPIcActivity.this, message, Toast.LENGTH_LONG).show();
//			
//		}
//		else
//		{
//			String message = String.format("not available");
//			Toast.makeText(PUbPIcActivity.this, message, Toast.LENGTH_LONG).show();
//		}
//	}
//
//	public void TakeCameraShot()
//	{
//		if (preview.camera != null && !duringCameraShot)
//		{
//			duringCameraShot = true;
//			Camera.Parameters params = preview.camera.getParameters();
//			if (Location != null)
//			{
//				double lat = Location.getLatitude();
//				double lon = Location.getLongitude();
//				boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);
//
//				if (hasLatLon)
//				{
//					params.setGpsLatitude(lat);
//					params.setGpsLongitude(lon);
//					// params.setGpsProcessingMethod(Location.getProvider().toUpperCase());
//					if (Location.hasAltitude())
//					{
//						params.setGpsAltitude(Location.getAltitude());
//					}
//					else
//					{
//						// for NETWORK_PROVIDER location provider, we may have
//						// no altitude information, but the driver needs it, so
//						// we fake one.
//						params.setGpsAltitude(0);
//					}
//					if (Location.getTime() != 0)
//					{
//						// Location.getTime() is UTC in milliseconds.
//						// gps-timestamp is UTC in seconds.
//						long utcTimeSeconds = Location.getTime() / 1000;
//						params.setGpsTimestamp(utcTimeSeconds);
//					}
//				}
//			}
//
//			mOrientationEventListener.disable();
//			if (_orientation >= 315 || _orientation < 45)
//			{
//				params.set("rotation", 90);
//				params.setRotation(90);
//				Toast.makeText(this.getBaseContext(), "90", Toast.LENGTH_SHORT).show();
//			}
//			if (_orientation >= 45 && _orientation < 135)
//			{
//				params.set("rotation", 180);
//				params.setRotation(180);
//				Toast.makeText(this.getBaseContext(), "180", Toast.LENGTH_SHORT).show();
//			}
//			if (_orientation >= 135 && _orientation < 225)
//			{
//				params.set("rotation", 270);
//				params.setRotation(270);
//				Toast.makeText(this.getBaseContext(), "270", Toast.LENGTH_SHORT).show();
//			}
//			if (_orientation >= 225 && _orientation <= 315)
//			{
//				params.set("rotation", 0);
//				params.setRotation(0);
//				Toast.makeText(this.getBaseContext(), "0", Toast.LENGTH_SHORT).show();
//			}
//
//			// params.set("rotation", 0);
//			// params.set("orientation", "portrait");
//			// Toast.makeText(this.getBaseContext(), "#####0",
//			// Toast.LENGTH_SHORT).show();
//			// params.setRotation(90);
//			preview.camera.stopPreview();
//			preview.camera.setPreviewCallback(null);
//			Log.v("Picup - Activity", params.toString());
//			preview.camera.setParameters(params);
//			preview.camera.takePicture(null, null, jpegCallback);
//
//		}
//	}
//
//	private int _orientation = 0;
//	private Button OpenGallery;
//	private ListView lv;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//
//		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//		//ListView lv = getListView();
//		 lv = (ListView)findViewById(R.id.lista);
//		lv.setTextFilterEnabled(true);
//
////		lv.setOnItemClickListener(new OnItemClickListener() {
////			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
////			{
////				CharSequence place = ((TextView) view).getText();
////				SelectedPlace = place.toString();
////				// When clicked, show a toast with the TextView text
////				Toast.makeText(getApplicationContext(), place, Toast.LENGTH_SHORT).show();
////			}
////		});
//
//		preview = new Preview(this);
//		((FrameLayout) findViewById(R.id.preview)).addView(preview);
////		LocationFinder locationFinder = new LocationFinder();
////		locationFinder.acti = this;
////		locationFinder.startRecording();
//		OpenGallery = (Button) findViewById(R.id.viewImages);
//
//		OpenGallery.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				Intent i = new Intent(PUbPIcActivity.this, WebGallery.class);
//				startActivity(i);
//			}
//		});
//
//		buttonClick = (Button) findViewById(R.id.buttonClick);
//
//		buttonClick.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				TakeCameraShot();
//			}
//		});
//		Button buttonCam = (Button) findViewById(R.id.camClick);
//		buttonCam.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
////				Intent i = new Intent(PUbPIcActivity.this, CameraIntent.class);
////
////				String lats = null;
////				String lons = null;
////				String alts = null;
////				if (Location != null)
////				{
////					lats = Double.toString(Location.getLatitude());
////					lons = Double.toString(Location.getLongitude());
////					alts = Double.toString(Location.getAltitude());
////				}
////				else
////				{
////					lats = "404";
////					lons = "404";
////					alts = "404";
////				}
////				i.putExtra("lat", lats);
////				i.putExtra("lon", lons);
////				i.putExtra("alt", alts);
////				if (SelectedPlace == null || SelectedPlace == "")
////				{
////					SelectedPlace = "NOT SET";
////				}
////				i.putExtra("place", SelectedPlace);
////				i.putExtra("eventLabel", SelectedPlace);
////				
////				// intent.putExtra("uri", Uri);
////				// intent.setData(uri);
////
////				String facebookid = "1";
////				if (fpp != null && fpp.FacebookUserID != null)
////				{
////					i.putExtra("userid", fpp.FacebookUserID);
////				}
////				startActivity(i);
//			}
//		});
//		retrieveLocationButton = (Button) this.findViewById(R.id.getLocationButton);
//		imageView = (ImageView) this.findViewById(R.id.imageView1);
//		frameLayout = (FrameLayout) this.findViewById(R.id.preview);
//		mainLayout = (LinearLayout) this.findViewById(R.id.layout);
//
//		retrieveLocationButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				try
//				{
//					showCurrentLocation();
//				}
//				catch (MalformedURLException e)
//				{
//					e.printStackTrace();
//				}
//				catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//				catch (JSONException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});
//
//		imageView.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				TakeCameraShot();
//			}
//		});
//
////		/*
////		 * Get existing access_token if any
////		 */
////		mPrefs = getPreferences(MODE_PRIVATE);
////		String access_token = mPrefs.getString("access_token", null);
////		long expires = mPrefs.getLong("access_expires", 0);
////		if (access_token != null)
////		{
////			facebook.setAccessToken(access_token);
////		}
////		if (expires != 0)
////		{
////			facebook.setAccessExpires(expires);
////		}
////
////		/*
////		 * Only call authorize if the access_token has expired.
////		 */
////		if (!facebook.isSessionValid())
////		{
////
////			facebook.authorize(this, new String[]
////			{
////				"publish_checkins"
////			},Facebook.FORCE_DIALOG_AUTH,new DialogListener() {
////
////				@Override
////				public void onFacebookError(FacebookError e)
////				{
////					// TODO Auto-generated method stub
////
////				}
////
////				@Override
////				public void onError(DialogError e)
////				{
////					// TODO Auto-generated method stub
////
////				}
////
////				@Override
////				public void onComplete(Bundle values)
////				{
////					SharedPreferences.Editor editor = mPrefs.edit();
////					editor.putString("access_token", facebook.getAccessToken());
////					editor.putLong("access_expires", facebook.getAccessExpires());
////					editor.commit();
////				}
////
////				@Override
////				public void onCancel()
////				{
////					// TODO Auto-generated method stub
////
////				}
////			});
////		}
//
////		NotificationManager _notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////		_notificationManager.cancel(_notificationID);
//	}
//
//	@Override
//	protected void onResume()
//	{
//		super.onResume();
//		
//		// Open the default i.e. the first rear facing camera.
//		try
//		{
//			preview.ReconnectToCamera();
//			if (preview.camera != null)
//			{
//				preview.camera.startPreview();
//			}
//		}
//		catch (IOException e)
//		{
//
//			ShowDialog(e.toString());
//		}
//
//		if (mOrientationEventListener == null)
//		{
//			mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL)
//			{
//
//				public void onOrientationChanged(int orientation)
//				{
//
//					_orientation = orientation;
//					// determine our orientation based on sensor response
//					int lastOrientation = mOrientation;
//
//					Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//
//					if (display.getOrientation() == Surface.ROTATION_0)
//					{ // landscape oriented devices
//						if (orientation >= 315 || orientation < 45)
//						{
//							if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL)
//							{
//								mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
//							}
//						}
//						else if (orientation < 315 && orientation >= 225)
//						{
//							if (mOrientation != ORIENTATION_PORTRAIT_INVERTED)
//							{
//								mOrientation = ORIENTATION_PORTRAIT_INVERTED;
//							}
//						}
//						else if (orientation < 225 && orientation >= 135)
//						{
//							if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED)
//							{
//								mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
//							}
//						}
//						else if (orientation < 135 && orientation > 45)
//						{
//							if (mOrientation != ORIENTATION_PORTRAIT_NORMAL)
//							{
//								mOrientation = ORIENTATION_PORTRAIT_NORMAL;
//							}
//						}
//					}
//					else
//					{ // portrait oriented devices
//						if (orientation >= 315 || orientation < 45)
//						{
//							if (mOrientation != ORIENTATION_PORTRAIT_NORMAL)
//							{
//								mOrientation = ORIENTATION_PORTRAIT_NORMAL;
//							}
//						}
//						else if (orientation < 315 && orientation >= 225)
//						{
//							if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL)
//							{
//								mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
//							}
//						}
//						else if (orientation < 225 && orientation >= 135)
//						{
//							if (mOrientation != ORIENTATION_PORTRAIT_INVERTED)
//							{
//								mOrientation = ORIENTATION_PORTRAIT_INVERTED;
//							}
//						}
//						else if (orientation < 135 && orientation > 45)
//						{
//							if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED)
//							{
//								mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
//							}
//						}
//					}
//
//					if (lastOrientation != mOrientation)
//					{
//						changeRotation(mOrientation, lastOrientation);
//					}
//				}
//			};
//		}
//		if (mOrientationEventListener.canDetectOrientation())
//		{
//			mOrientationEventListener.enable();
//		}
//
//		// cameraCurrentlyLocked = defaultCameraId;
//		// preview.setCamera(preview.camera);
//	}
//
//	@Override
//	protected void onStop()
//	{
//		super.onStop();
//		try
//		{
//			releaseCamera();
//		}
//		catch (InterruptedException e)
//		{
//			ShowDialog("Fail to release camera");
//		}
//	};
//
//	@Override
//	protected void onPause()
//	{
//		super.onPause();
//
//		// Because the Camera object is a shared resource, it's very
//		// important to release it when the activity is paused.
//		if (preview.camera != null)
//		{
//			// preview.setCamera(null);
//			preview.camera.stopPreview();
//			preview.camera.unlock();
//			// preview.camera.release();
//			// preview.camera = null;
//		}
//		mOrientationEventListener.disable();
//	}
//
//	/**
//	 * Performs required action to accommodate new orientation
//	 * 
//	 * @param orientation
//	 * @param lastOrientation
//	 */
//	private void changeRotation(int orientation, int lastOrientation)
//	{
//		if (preview.camera != null)
//		{
//			Camera.Parameters params = preview.camera.getParameters();
//			mOrientationEventListener.disable();
//			if (_orientation >= 315 || _orientation < 45)
//			{
//				// params.set("rotation", 90);
//				// params.setRotation(90);
//				Toast.makeText(this.getBaseContext(), "90", Toast.LENGTH_SHORT).show();
//
//			}
//			if (_orientation >= 45 && _orientation < 135)
//			{
//				// params.set("rotation", 180);
//				// params.setRotation(180);
//				Toast.makeText(this.getBaseContext(), "180", Toast.LENGTH_SHORT).show();
//
//			}
//			if (_orientation >= 135 && _orientation < 225)
//			{
//				// params.set("rotation", 270);
//				// params.setRotation(270);
//				Toast.makeText(this.getBaseContext(), "270", Toast.LENGTH_SHORT).show();
//
//			}
//			if (_orientation >= 225 && _orientation <= 315)
//			{
//				// params.set("rotation", 0);
//				// params.setRotation(0);
//				Toast.makeText(this.getBaseContext(), "0", Toast.LENGTH_SHORT).show();
//
//			}
//			mOrientationEventListener.enable();
//			// params.set("rotation", 90);
//			// params.setRotation(90);
//			// preview.camera.stopPreview();
//			// preview.camera.setPreviewCallback(null);
//			// preview.camera.setParameters(params);
//			// preview.camera.startPreview();
//
//			// Animation rotateAnim = AnimationUtils.loadAnimation(this,
//			// R.anim.rotation);
//			// LayoutAnimationController animController = new
//			// LayoutAnimationController(rotateAnim, 0);
//			switch (orientation)
//			{
//				case ORIENTATION_PORTRAIT_NORMAL:
//					// imageView.setImageDrawable(getRotatedImage(R.drawable.ic_camera,
//					// 270));
//					// mainLayout.setLayoutAnimation(animController);
//					Log.v("picup", "Orientation = 90");
//					break;
//				case ORIENTATION_LANDSCAPE_NORMAL:
//					// imageView.setImageResource(R.drawable.ic_camera);
//					Log.v("picup", "Orientation = 0");
//					// mainLayout.setLayoutAnimation(animController);
//					break;
//				case ORIENTATION_PORTRAIT_INVERTED:
//					// imageView.setImageDrawable(getRotatedImage(R.drawable.ic_camera,
//					// 90));
//					Log.v("picup", "Orientation = 270");
//					// mainLayout.setLayoutAnimation(animController);
//					break;
//				case ORIENTATION_LANDSCAPE_INVERTED:
//					// imageView.setImageDrawable(getRotatedImage(R.drawable.ic_camera,
//					// 180));
//					Log.v("picup", "Orientation = 180");
//					// mainLayout.setLayoutAnimation(animController);
//					break;
//			}
//		}
//	}
//
//	/**
//	 * Rotates given Drawable
//	 * 
//	 * @param drawableId
//	 *            Drawable Id to rotate
//	 * @param degrees
//	 *            Rotate drawable by Degrees
//	 * @return Rotated Drawable
//	 */
//	private Drawable getRotatedImage(int drawableId, int degrees)
//	{
//		Bitmap original = BitmapFactory.decodeResource(getResources(), drawableId);
//		Matrix matrix = new Matrix();
//		matrix.postRotate(degrees);
//
//		Bitmap rotated = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
//		return new BitmapDrawable(rotated);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		try
//		{
//			FacebookService.getInstance().AuthorizeCallback(requestCode, resultCode, data);
//		}
//		catch (Exception e)
//		{
//			Log.v("picup", e.toString());
//			Log.v("picup", e.getStackTrace().toString());
//		}
//		
//
//	};
//
//	private void releaseCamera() throws InterruptedException
//	{
//		if (preview.camera != null)
//		{
//			preview.camera.stopPreview();
//			// preview.camera.setPreviewCallback(null);
//			// preview.camera.unlock();
//			Thread.sleep(10, 10);
//			preview.camera.release();
//			Thread.sleep(10, 10);
//			preview.camera = null;
//		}
//	}
//
//	// ShutterCallback shutterCallback = new ShutterCallback() {
//	// public void onShutter()
//	// {
//	//
//	// }
//	// };
//	//
//	// /** Handles data for raw picture */
//	// PictureCallback rawCallback = new PictureCallback() {
//	// public void onPictureTaken(byte[] data, Camera camera)
//	// {
//	// }
//	// };
//
//	public void ShowDialog(String text)
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(PUbPIcActivity.this);
//		builder.setMessage(text).setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int id)
//			{
//
//			}
//		});
//		AlertDialog alert = builder.create();
//		alert.show();
//	}
//
//	/** Handles data for jpeg picture */
//	PictureCallback jpegCallback = new PictureCallback() {
//
//		public void onPictureTaken(byte[] data, Camera camera)
//		{
//			FileOutputStream outStream = null;
//			try
//			{
//				if (preview.camera != null)
//				{
//					preview.camera.stopPreview();
//
//					// Populate image metadata
//					String fileName = String.format("%d.jpg", System.currentTimeMillis());
//
//					// write to local sandbox file system
//					// outStream =
//					// CameraDemo.this.openFileOutput(String.format("%d.jpg",
//					// System.currentTimeMillis()), 0);
//					// Or write to sdcard
//					File rootsd = Environment.getExternalStorageDirectory();
//					File root = new File(rootsd.getAbsolutePath() + "/DCIM");
//					// File root =
//					// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//					File dir;
//					File picFile;
//					if (!root.exists())
//					{
//						dir = new File(root.getAbsolutePath());
//						dir.mkdir();
//					}
//					else
//					{
//						dir = root;
//					}
//
//					picFile = new File(dir, fileName);
//					// File root2 =
//					// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//					// ShowDialog(root.getAbsolutePath());
//					Thread.sleep(200);
//
//					outStream = new FileOutputStream(picFile);
//					outStream.write(data);
//					outStream.close();
//
//					// Thread.sleep(200);
//
//					// ExifInterface exif = new
//					// ExifInterface(root.getAbsoluteFile()
//					// + "/" + fileName);
//					// exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
//					// Double.toString(latitude));
//					// exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
//					// Double.toString(longitude));
//					// exif.setAttribute(ExifInterface.TAG_ORIENTATION,
//					// ""+ExifInterface.ORIENTATION_ROTATE_90);
//					// exif.saveAttributes();
//
//					// ExifInterface exif = new
//					// ExifInterface(root.getAbsoluteFile()
//					// + "/" + fileName); // Since
//					// API
//					// Level
//					// 5
//					// int rotation = (int)
//					// exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//					// ExifInterface.ORIENTATION_NORMAL));
//
//					// //////////////////////////
//					// Call Upload
//					// ////////////////////////////////////////////////////
//
//					// ////////////
//					// BMP
//					// ////////////
//
//					// Uri uri =
//					// saveMediaEntry(fileName,"dsds","asdasd",System.currentTimeMillis());
//					// Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
//					// data.length);
//					// OutputStream out =
//					// getContentResolver().openOutputStream(uri);
//					//
//					// boolean success =
//					// bitmap.compress(Bitmap.CompressFormat.JPEG,
//					// 75, out);
//					// out.close();
//
//					// exif.setAttribute(ExifInterface.TAG_ORIENTATION,
//					// ""+ExifInterface.ORIENTATION_FLIP_VERTICAL);
//					// exif.saveAttributes();
//					// releaseCamera();
////					Intent intent = new Intent();
////					intent.setClass(getApplicationContext(), UploadService.class);
////					// Create a new Messenger for the communication back
////					Messenger messenger = new Messenger(handler);
////					String lats = null;
////					String lons = null;
////					String alts = null;
////					if (Location != null)
////					{
////						lats = Double.toString(Location.getLatitude());
////						lons = Double.toString(Location.getLongitude());
////						alts = Double.toString(Location.getAltitude());
////					}
////					else
////					{
////						lats = "404";
////						lons = "404";
////						alts = "404";
////					}
////					intent.putExtra("lat", lats);
////					intent.putExtra("MESSENGER", messenger);
////					intent.putExtra("lon", lons);
////					intent.putExtra("alt", alts);
////					if (SelectedPlace == null || SelectedPlace == "")
////					{
////						SelectedPlace = "NOT SET";
////					}
////					intent.putExtra("place", SelectedPlace);
////					intent.putExtra("eventLabel", SelectedPlace);
////					intent.putExtra("filePath", picFile.getAbsolutePath());
////					// intent.putExtra("uri", Uri);
////					// intent.setData(uri);
////
////					String facebookid = "1";
////					if (fpp != null && fpp.FacebookUserID != null)
////					{
////						intent.putExtra("userid", fpp.FacebookUserID);
////					}
////
////					startService(intent);
//					// ////////////////////////////////////////////////////////////////////////////
//
//					preview.camera.startPreview();
//					duringCameraShot = false;
//					mOrientationEventListener.enable();
//					// Thread.sleep(200);
//				}
//			}
//			catch (FileNotFoundException e)
//			{
//				ShowDialog(e.toString());
//			}
//			catch (IOException e)
//			{
//				ShowDialog(e.toString());
//			}
//			catch (InterruptedException e)
//			{
//				ShowDialog(e.toString());
//			}
//			finally
//			{
//				duringCameraShot = false;
//			}
//
//		}
//
//		public Uri saveMediaEntry(String fileName, String title, String description, long dateTaken)
//		{
//			ContentValues image = new ContentValues();
//
//			image.put(Images.Media.TITLE, "ImageTitle");
//			image.put(Images.Media.DISPLAY_NAME, "Heart");
//			image.put(Images.Media.DATE_TAKEN, dateTaken);
//			// image.put(Images.Media.MIME_TYPE, "image/jpeg");
//			image.put(Images.Media.ORIENTATION, 180);
//			// File imageFile = new File(imagePath) ;
//			// File parent = imageFile.getParentFile();
//			// String path = parent.toString().toLowerCase();
//			// String name = parent.getName().toLowerCase();
//			// image.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
//			// image.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
//			// image.put(Images.Media.SIZE, imageFile.length());
//			// image.put("_data", imageFile.getAbsolutePath());
//			return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
//		}
//
//		private float exifOrientationToDegrees(int exifOrientation)
//		{
//			if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
//			{
//				return 90;
//			}
//			else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
//			{
//				return 180;
//			}
//			else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
//			{
//				return 270;
//			}
//			return 0;
//		}
//
//		
//		public void ShowDialog(String text)
//		{
//			AlertDialog.Builder builder = new AlertDialog.Builder(PUbPIcActivity.this);
//			builder.setMessage(text).setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener()
//			{
//				public void onClick(DialogInterface dialog, int id)
//				{
//
//				}
//			});
//			AlertDialog alert = builder.create();
//			alert.show();
//		}
//	};
//}
//
//// @Override
//// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//// {
//// // TODO Auto-generated method stub
////
//// }
//
//// matyyy
//// String[] COUNTRIES = new String[] {
//// "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
//// "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
//// "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
//// "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
//// "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia"};
//// setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,COUNTRIES));
