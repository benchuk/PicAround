package com.socialcamera.PicAround;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.socialcamera.PicAround.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import Interfaces.IPlacesResult;
import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.ImageLoader;
import Services.LocationService;
import WebHelpers.AlbumImagesResultHandler;
import WebHelpers.AlbumImagesWebRequestHandlerParams;
import WebHelpers.AlbumImagesWebResult;
import WebHelpers.AlbumParams;
import WebHelpers.DownloadImageTask;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ProfileImageLoader;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.text.format.Time;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GalleryActivity extends SherlockActivity
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	ActionBar bar;
	File photo;
	private Uri imageUri;
	private final int TAKE_PHOTO_CODE = 1;
	private String TAG = "PicAround";
	String lats = null;
	String lons = null;
	String alts = null;
	String _selectedPlace = null;
	String _selectedEventName = null;
	String _selectedEventID = null;
	String userid = null;
	AlbumParams[] _fetchedAlbums;
	GridView _gridview;
	ArrayList<GridviewPaImage> _addedCells = new ArrayList<GridviewPaImage>();
	private ImageAdapter _adapter;
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
	private boolean _autoCameraOn = false;
	SubMenu sub;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			mGaTracker.sendEvent("ui_action_android", "button_press", "close gallery view", (long) 1);
			if (cs != null)
			{
				cs.FetchedAlbums = null;
				cs.AddedCells = null;
				cs.SelectedImageEventName = null;
				cs.SelectedImageLink = null;
				cs.SelectedImageUserId = null;
				// ls = null;
				cs = null;
			}
			fs = null;
			bar = null;
			photo = null;
			imageUri = null;
			lats = null;
			lons = null;
			alts = null;
			_selectedPlace = null;
			_selectedEventName = null;
			_selectedEventID = null;
			userid = null;
			_fetchedAlbums = null;
			_gridview = null;
			_addedCells = null;
			_adapter = null;
			mGaInstance = null;
			mGaTracker = null;
			
			finish();
			System.gc();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN && _menu != null)
			{
				mGaTracker.sendEvent("ui_action_android", "button_press", "Show menu on gallery view", (long) 1);
				_menu.performIdentifierAction(0, 0);
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		ls = LocationService.getInstance();
		ls.StartRecordingLocation();
		if (actionProvider != null)
		{
			actionProvider.setShareIntent(createShareIntent());
		}
	};

	@Override
	protected void onPause()
	{
		super.onPause();
		ls.StopRecordingLocation();
		ls = null;
	};

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
		mGaTracker.sendView("/Gallery");
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getSherlock().setProgressBarIndeterminate(false);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		cs = ContainerService.getInstance();
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-40637183-2");
		// setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setContentView(R.layout.webgallery);
		bar = getSupportActionBar();
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(false);
		// bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("");
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayUseLogoEnabled(false);

		bar.setIcon(R.drawable.picaroundicon);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		bar.setIcon(cs.FacebookUserImage);
		// if (cs.FacebookId == null)
		// {
		// fs.asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation() {
		// @Override
		// public void GetIdCompleted(String facebookID)
		// {
		// if(bar!=null)
		// {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }
		// // isThereAreEventsNearToYou();
		// }
		// };
		// fs.GetFacebookIdAsync();
		// }
		// else
		// {
		// bar.setIcon(ContainerService.getInstance().FacebookUserImage);
		// }

		bar.setDisplayUseLogoEnabled(false);
		bar.setTitle(cs.SelectedEventName);
		bar.setSubtitle(cs.SelectedPlace);

		File direct = new File(Environment.getExternalStorageDirectory() + "/PicAroundCache");

		if (!direct.exists())
		{
			if (direct.mkdir())
			{
				// directory is created;
			}

		}

		_gridview = (GridView) findViewById(R.id.gridview);
		_gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				mGaTracker.sendEvent("ui_action_android", "button_press", "User opens a photo", (long) 1);
				
				cs.FetchedAlbums = _fetchedAlbums;
				cs.AddedCells = _addedCells;
				cs.Position = position;
				if (_fetchedAlbums.length - 1 >= position)
				{
					cs.SelectedImageLink = _fetchedAlbums[position].MidQLink;
					cs.SelectedImageEventName = _fetchedAlbums[position].EventName;
					cs.SelectedImageUserId = _fetchedAlbums[position].UserId;
					cs.UseImageView = false;
					Intent imageDetails = new Intent(GalleryActivity.this, ImageDetailsPagerActivity.class);
					GalleryActivity.this.startActivity(imageDetails);
				}
				else
				{
					cs.SelectedImageEventName = cs.SelectedEventName;
					cs.SelectedImageUserId = cs.FacebookId;
					cs.SelectedImageSDPath = _addedCells.get(position - _fetchedAlbums.length).GetImageSdPath();
					cs.UseImageView = true;
					Intent imageDetails = new Intent(GalleryActivity.this, ImageDetailsPagerActivity.class);
					GalleryActivity.this.startActivity(imageDetails);
				}
			}
		});
		
		LoadEventPictures();

		// // Workaround for facebook places.
		// fs.GetPlacesAsync(cs.Location, new FacebookAsyncPlacesSearch() {
		// @Override
		// public void searchComleted(IPlacesResult places)
		// {
		//
		// }
		// });
	}

	private void LoadEventPictures()
	{
		mGaTracker.sendEvent("auto_action_android", "load_event_pictures", "loading the event pictures", (long) 1);
		
		UploadedEventsHandler.id = 0;

		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);

		_addedCells = new ArrayList<GridviewPaImage>();

		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("id", cs.SelectedEventID));

		String baseUrl = "http://picaround.azurewebsites.net/Home/GalleryByEventId";
		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		parameters.NameValuePairs = NameValuePairs;
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{
				super.OnWebRequestCompleted(httpResponse);
				AlbumImagesWebRequestHandlerParams param = new AlbumImagesWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.albumImagesResultHandler = new AlbumImagesResultHandler()
				{
					@Override
					public void HandleResult(ArrayList<AlbumParams> images)
					{
						if (images != null)
						{
							if (images.size() == 0)
							{
								mGaTracker.sendEvent("auto_action_android", "load_event_pictures", "No images for this album", (long) 1);
								_menu.performIdentifierAction(0, 0);
							}
							_fetchedAlbums = new AlbumParams[images.size()];
							images.toArray(_fetchedAlbums);
							_adapter = new ImageAdapter(GalleryActivity.this, _fetchedAlbums);
							if (_gridview != null)
							{
								mGaTracker.sendEvent("auto_action_android", "load_event_pictures", "Images for album loaded ok", (long) 1);
								_gridview.setAdapter(_adapter);
							}
						}
						GalleryActivity.this.setSupportProgressBarIndeterminateVisibility(false);
						GalleryActivity.this.getSherlock().setProgressBarIndeterminate(false);
					};
				};
				new AlbumImagesWebResult().execute(param);
			}
		};

		new WebRequest().PostData(parameters);
	}

	private Handler handler = new Handler() {
		public void handleMessage(final Message message)
		{
			if (message.arg2 >= 0)
			{
				GalleryActivity.this.runOnUiThread(new Runnable() {
					public void run()
					{
						if (message.arg2 < 100)
						{
							if (_addedCells != null && _addedCells.size() > 0)
							{
								GridviewPaImage cell = _addedCells.get(message.arg1 - 1);
								ProgressBar bar = cell.GetProgressControl();
								bar.setVisibility(View.VISIBLE);
								bar.setProgress(message.arg2);
							}
						}
						else
						{
							if (_addedCells != null && _addedCells.size() > 0)
							{
								GridviewPaImage cell = _addedCells.get(message.arg1 - 1);
								ProgressBar bar = cell.GetProgressControl();
								bar.setVisibility(View.INVISIBLE);
								cell.SetOkVisiable();
							}
						}
					}
				});
			}
		};
	};

	public String getRealPathFromURI(Uri contentUri)
	{
		String[] proj =
		{
			MediaStore.Images.Media.DATA
		};
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor != null)
		{
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		else
		{
			return contentUri.getPath();
		}
	}

	public void takePhoto()
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		String fileName = String.format("PicAround%d.jpg", System.currentTimeMillis());
		// String fileName = String.format("%s.jpg", "PicAroundTemp");
		// photo = new
		// System.gc();
		if (Build.VERSION.SDK_INT > 7)
		{
			mGaTracker.sendEvent("ui_action", "takePhotoSaveToPictures", "SDK_INT > 7", (long) 1);
			photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
		}
		else
		{
			mGaTracker.sendEvent("ui_action", "takePhotoSaveTo/DCIM/Camera/", "SDK_INT < 7", (long) 1);
			File checkDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
			if (!checkDir.exists())
			{
				photo = new File(Environment.getExternalStorageDirectory(), fileName);

			}
			else
			{
				photo = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", fileName);
			}
		}

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		imageUri = Uri.fromFile(photo);
		mGaTracker.sendEvent("ui_action_android", "button_press", "TAKE PHOTO VIEW IS OPENED", (long) 1);
		startActivityForResult(intent, TAKE_PHOTO_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{
			switch (requestCode)
			{
				case TAKE_PHOTO_CODE:
					if (resultCode == Activity.RESULT_OK)
					{
						mGaTracker.sendEvent("ui_action_android", "button_press", "TAKE PHOTO COMPLETED OK", (long) 1);
						Uri rotatedCorectlyBitmapUri = RotateImageIfNeededAndSaveToMeidaFolder(imageUri, true);
						String imagePath = getRealPathFromURI(rotatedCorectlyBitmapUri);
						StartUploadService(imagePath);
						if (_autoCameraOn)
						{
							takePhoto();
						}
					}
					break;
				case 123456:
					if (data != null)
					{
						mGaTracker.sendEvent("ui_action_android", "button_press", "UPLOADED FROM GALLERY COMPLETED OK", (long) 1);
						Uri selectedImageUri = data.getData();
						String imageRealPath = getRealPathFromURI(selectedImageUri);
						Uri u = Uri.parse(imageRealPath);
						Uri rotatedCorectlyBitmapUri = RotateImageIfNeededAndSaveToMeidaFolder(u, false);
						String path = getRealPathFromURI(rotatedCorectlyBitmapUri);
						StartUploadService(path);
					}
					break;
			}
		}
		catch (Exception e)
		{
			mGaTracker.sendEvent("ui_action_android", "button_press", "FAILED TO TAKE PHOTO OR UPLOADED FROM GALLERY", (long) 1);
			mGaTracker.sendEvent("ui_action_android", "button_press", e.toString(), (long) 1);
			Tracker myTracker = EasyTracker.getTracker();
			myTracker.sendException(e.getMessage(), false);
			Log.i(TAG, "PicAround -> ERROR -> Failed to take photo" + e.toString());
			Log.i(TAG, "PicAround -> ERROR -> Failed to take photo" + e.getStackTrace().toString());
			Toast.makeText(getApplicationContext(), "Failed to upload image!", Toast.LENGTH_LONG).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Bitmap decodeSampledBitmapFromResource(Uri imageUriRef)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 2; // calculateInSampleSize(options, reqWidth,
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imageUriRef.getPath(), options);
	}

	private Uri RotateImageIfNeededAndSaveToMeidaFolder(Uri imageUriRef, boolean forceSave) throws IOException
	{
		Bitmap bm = decodeSampledBitmapFromResource(imageUriRef);
		ExifInterface exif = new ExifInterface(imageUriRef.getPath());
		String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
		int rotationAngle = 0;
		if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
		if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
		if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
		boolean saveRotated = rotationAngle != orientation;
		Bitmap rotatedBitmap = null;
		if (saveRotated)
		{
			try
			{
				Matrix matrix = new Matrix();
				matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
				rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
			}
			catch (Exception e)
			{
				Tracker myTracker = EasyTracker.getTracker();
				myTracker.sendException(e.getMessage(), false);
				Log.i(TAG, "picup -> ERROR -> Failed to rotate photo" + e.getStackTrace().toString());
			}
		}
		String imagePath = null;
		if (forceSave)// user took picture from camera
		{
			// user took picture with device camera
			if (rotatedBitmap != null)
			{
				FileOutputStream out = new FileOutputStream(imageUriRef.getPath());
				rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				imagePath = imageUriRef.getPath();
			}
			else
			{
				imagePath = imageUriRef.getPath();
			}
			Uri u = Uri.parse(imagePath);
			RefreshGallery(u);
		}
		else
		// user is uploading from gallery.
		{
			if (rotatedBitmap != null)
			{
				mGaTracker.sendEvent("auto_action_android", "rotateImage", "AutoRotateImage", (long) 1);
				// User uploaded from gallery image with wrong orientation
				// fixing and saving only to a temp file.
				String fileName = String.format("%s.jpg", "PicAroundRotatedTemp");
				// photo = new
				// File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
				File rotatedTempImage = new File(Environment.getExternalStorageDirectory() + "/PicAroundCache", fileName);
				FileOutputStream out = new FileOutputStream(rotatedTempImage);
				rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				imagePath = rotatedTempImage.getAbsolutePath();
			}
			else
			{
				// User uploaded from gallery image with the correct orientation
				imagePath = imageUriRef.getPath();
			}
		}
		if (bm != null)
		{
			bm.recycle();
		}
		if (rotatedBitmap != null)
		{
			rotatedBitmap.recycle();
		}

		Uri u = Uri.parse(imagePath);
		return u;

	}

	private void GalleryAddPic(String imagePath)
	{
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(imagePath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void RefreshGallery(Uri newImageUri)
	{
		ContentValues image = new ContentValues();
		Time now = new Time();
		now.setToNow();
		image.put(Images.Media.TITLE, "PicAround");
		image.put(Images.Media.DISPLAY_NAME, cs.SelectedEventName);
		image.put(Images.Media.DESCRIPTION, "PicAround");
		image.put(Images.Media.DATE_ADDED, now.toString());
		image.put(Images.Media.DATE_TAKEN, now.toString());
		image.put(Images.Media.DATE_MODIFIED, now.toString());
		image.put(Images.Media.MIME_TYPE, "image/png");
		image.put(Images.Media.ORIENTATION, 0);
		File imageFile = new File(newImageUri.getPath());
		File parent = imageFile.getParentFile();
		String fpath = parent.toString().toLowerCase();
		String name = parent.getName().toLowerCase();
		image.put(Images.ImageColumns.BUCKET_ID, fpath.hashCode());
		image.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		image.put(Images.Media.SIZE, imageFile.length());

		image.put(Images.Media.DATA, imageFile.getAbsolutePath());

		getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
	}

	public void StartUploadService(String imagePath)
	{
		mGaTracker.sendEvent("auto_action_android", "StartUpload", "User is uploading photo to server", (long) 1);
		
		_addedCells.add(_adapter.AddImage(imagePath));
		_adapter.notifyDataSetChanged();

		Intent intent = new Intent();
		intent.setClass(GalleryActivity.this.getApplication().getApplicationContext(), UploadService.class);
		// Create a new Messenger for the communication back
		if (cs != null && cs.Location != null)
		{
			lons = Double.toString(cs.Location.getLongitude());
			lats = Double.toString(cs.Location.getLatitude());
			alts = Double.toString(cs.Location.getAltitude());
		}

		if (lats == null)
		{
			lats = "404";
		}
		if (lons == null)
		{
			lons = "404";
		}
		if (alts == null)
		{
			alts = "404";
		}
		Messenger messenger = new Messenger(handler);

		intent.putExtra("lat", lats);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("lon", lons);
		intent.putExtra("alt", alts);
		_selectedPlace = cs.SelectedPlace;
		_selectedEventName = cs.SelectedEventName;
		_selectedEventID = cs.SelectedEventID;
		if (_selectedPlace == null || _selectedPlace == "")
		{
			_selectedPlace = "A place was not selected";
		}
		if (_selectedEventName == null || _selectedEventName == "")
		{
			_selectedEventName = "An event Was not selected";
		}

		if (_selectedEventID == null || _selectedEventID == "")
		{
			_selectedEventID = "An event Was not selected";
		}
		intent.putExtra("place", _selectedPlace);
		intent.putExtra("SelectedEventName", _selectedEventName);

		intent.putExtra("SelectedEventID", _selectedEventID);
		userid = cs.FacebookId;
		if (userid == null || userid == "0")
		{
			Toast.makeText(getApplicationContext(), "User ID Is Missing!", Toast.LENGTH_SHORT).show();
			mGaTracker.sendEvent("auto_action_android", "Error", "User is missing", (long) 1);
		}
		intent.putExtra("userid", userid);

		intent.putExtra("filePath", imagePath);
		intent.putExtra("heading", Double.toString(cs.Heading));

		GalleryActivity.this.getApplication().startService(intent);
		// _gridview.smoothScrollToPosition(_fetchedAlbums.length +
		// _addedCells.size());
		// takePhoto(imageView);
		// }
		// });
	}

	public void openGallery(int req_code)
	{
		mGaTracker.sendEvent("ui_action_android", "button_press", "openning gallery activity", (long) 1);
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 0)
		{
			return false;
		}
		
		switch (item.getItemId())
		{
			case 0:
				return super.onOptionsItemSelected(item);
			case 1:
				mGaTracker.sendEvent("auto_action_android", "menu_press", "takePhoto (Inside Menu)", (long) 1);
				takePhoto();
				return super.onOptionsItemSelected(item);
			case 2:
				mGaTracker.sendEvent("auto_action_android", "menu_press", "UploadFromGallery", (long) 1);
				openGallery(123456);
				return super.onOptionsItemSelected(item);
			case 3:
				mGaTracker.sendEvent("auto_action_android", "menu_press", "takePhoto (Main outer button)", (long) 1);
				takePhoto();
				return super.onOptionsItemSelected(item);
			case 4:
				if (item.getTitle().equals("Check for new pictures"))
				{
					mGaTracker.sendEvent("auto_action_android", "menu_press", "RefreshGallery", (long) 1);
					LoadEventPictures();
				}
				return super.onOptionsItemSelected(item);
			case 5:

				_autoCameraOn = !_autoCameraOn;
				if (_autoCameraOn)
				{
					mGaTracker.sendEvent("auto_action_android", "menu_press", "auto camera on", (long) 1);
					sub.getItem(4).setIcon(R.drawable.ic_repeat).setTitle("Auto Return To Camera [on]");
				}
				else
				{
					mGaTracker.sendEvent("auto_action_android", "menu_press", "auto camera off", (long) 1);
					sub.getItem(4).setIcon(R.drawable.ic_pause).setTitle("Auto Return To Camera [off]");
				}
				return super.onOptionsItemSelected(item);
			default:
				mGaTracker.sendEvent("ui_action_android", "menu_press", "User is sharing the album", (long) 1);
				return super.onOptionsItemSelected(item);
		}
	}

	private Intent createShareIntent()
	{
		//mGaTracker.sendEvent("ui_action", "share", "UserIsSharingAlbum", (long) 13);

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("Picaround has an album to share with you, http://picaround.com/Gallery/GalleryByEventId?eventId=%s", cs.SelectedEventID));
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PicAround has something to share with you.");

		return shareIntent;
	}

	ShareActionProvider actionProvider;
	Menu _menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		_menu = menu;
		menu.add(0, 3, 0, "Camera").setIcon(R.drawable.camera_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		sub = menu.addSubMenu("Options");
		sub.add(1, 1, 1, "Take Photo").setIcon(R.drawable.camera_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub.add(1, 2, 2, "Upload from gallery").setIcon(R.drawable.gallery_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.add(1, 4, 0, "Check for new pictures").setIcon(R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		sub.add(1, 5, 3, "Auto Return To Camera [off]").setIcon(R.drawable.ic_pause).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		getSupportMenuInflater().inflate(R.layout.share_action_provider, sub);
		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		sub.getItem(1).setIcon(R.drawable.ic_action_share).setTitle("Share This Album");
		actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		if (actionProvider != null)
		{
			actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
			actionProvider.setShareIntent(createShareIntent());
		}
		return true;
	}

	public class ImageAdapter extends BaseAdapter
	{
		private Context _context;
		private AlbumParams[] _albums;
		private ArrayList<GridviewPaImage> _images;
		private ArrayList<GridviewPaImage> _ADDimages;
		private int _size;
		RotateAnimation _anim;
		boolean _killed = false;
		private int _maxThreadsCount;
		private BlockingQueue<DownloadThumb> queue = new LinkedBlockingQueue<DownloadThumb>();

		class DownloadImageTaskRunnable implements Runnable
		{
			DownloadThumb _d;

			DownloadImageTaskRunnable(DownloadThumb d) {
				_d = d;
			}

			public void run()
			{
				donwloadAsync(_d);
			}
		}

		private void donwloadAsync(DownloadThumb d)
		{
			d.downloadThumb();
		}

		public ImageAdapter(Context c, AlbumParams[] albums) {
			_context = c;
			_albums = albums;
			_size = albums.length;
			_anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			_anim.setInterpolator(new LinearInterpolator());
			_anim.setRepeatCount(Animation.INFINITE);
			_anim.setDuration(2000);
			_maxThreadsCount = albums.length - 1;
			_images = new ArrayList<GridviewPaImage>();
			_ADDimages = new ArrayList<GridviewPaImage>();

			Runnable queueController = new Runnable() {
				public void run()
				{
					while (true)
					{
						try
						{
							if (_killed && (queue.size() == 0))
							{
								break;
							}
							DownloadThumb d = queue.take();
							new Thread(new DownloadImageTaskRunnable(d)).start();
							Log.e("QueueInfo", "queue size: " + queue.size());

						}
						catch (InterruptedException e)
						{
							break;
						}
					}
					Log.e("queueController", "queueController has finished processing");
					Log.e("QueueInfo", "queue size: " + queue.toString());
				}
			};

			takList = new Thread(queueController);
			takList.setName("Downloader");
			takList.start();
		}

		Thread takList;

		public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
		{
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth)
			{

				// Calculate ratios of height and width to requested height and
				// width
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);

				// Choose the smallest ratio as inSampleSize value, this will
				// guarantee
				// a final image with both dimensions larger than or equal to
				// the
				// requested height and width.
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}

			return inSampleSize;
		}

		public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inSampleSize = 16; // calculateInSampleSize(options,
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}

		public GridviewPaImage AddImage(String imagePath)
		{
			Bitmap scaledBitmap = decodeSampledBitmapFromResource(imagePath, 25, 25);
			GridviewPaImage gvpaImage = new GridviewPaImage(_context, imagePath);
			ImageView imageView = gvpaImage.GetImageControl();
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageBitmap(scaledBitmap);
			_ADDimages.add(gvpaImage);
			_size++;
			return gvpaImage;
		}

		public int getCount()
		{
			return _size;
		}

		public Object getItem(int position)
		{
			return null;
		}

		public long getItemId(int position)
		{
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;
			GridviewPaImage control;

			if (position >= _images.size() || _images.get(position) == null)
			{
				if (_albums.length > position)
				{
					GridviewPaImage gvpaImage = new GridviewPaImage(_context, "null");
					imageView = gvpaImage.GetImageControl();
					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					imageView.startAnimation(_anim);

					// new
					// DownloadImageTask(imageView).execute(_albums[position].ThumbLink);
					DownloadThumb d = new DownloadThumb();
					d.setThumbLink(_albums[position].ThumbLink);
					d.setImageView(imageView);
					queue.add(d);
					_images.add(position, gvpaImage);
				}
			}
			if (position < _images.size())
			{
				control = _images.get(position);
			}
			else
			{
				control = _ADDimages.get(position - _images.size());
			}
			if (position == _maxThreadsCount)
			{
				_killed = true;
				// takList.interrupt();
			}
			return control;
		}

		class DownloadThumb
		{
			String _thumbLink;
			ImageView _imageView;

			private final Handler uiHandler = new Handler();

			public void setThumbLink(String thumbLink)
			{
				_thumbLink = thumbLink;
			}

			public void setImageView(ImageView imageView)
			{
				_imageView = imageView;
			}

			public Bitmap decodeSampledBitmapFromResource(InputStream in)
			{
				Bitmap res = null;
				try
				{
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					options.inSampleSize = 2; // calculateInSampleSize(options,
					Rect paddingRect = new Rect(-1, -1, -1, -1);
					options.inJustDecodeBounds = false;
					res = BitmapFactory.decodeStream(in, paddingRect, options);
				}
				catch (Exception e)
				{
					Tracker myTracker = EasyTracker.getTracker();
					myTracker.sendException(e.getMessage(), false);
				}
				return res;
			}

			void downloadThumb()
			{
				String urldisplay = _thumbLink;
				Bitmap downloadedThumb = null;
				try
				{
					InputStream in = new java.net.URL(urldisplay).openStream();
					downloadedThumb = BitmapFactory.decodeStream(in);
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					downloadedThumb.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
				}
				catch (Exception e)
				{
					Tracker myTracker = EasyTracker.getTracker();
					myTracker.sendException(e.getMessage(), false);
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				final Bitmap downloadedThumbFinal = downloadedThumb;
				final Runnable updateUIRunnable = new Runnable() {
					public void run()
					{
						_imageView.setAnimation(null);
						_imageView.setImageBitmap(downloadedThumbFinal);
						// downloadedThumbFinal.recycle();
						_imageView = null;
						_thumbLink = null;
					}
				};
				uiHandler.post(updateUIRunnable);
			}
		}
	}
}