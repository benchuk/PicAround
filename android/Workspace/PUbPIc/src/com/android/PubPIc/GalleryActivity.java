package com.android.PubPIc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;

import Services.AsyncGetFacebookIDOperation;
import Services.ContainerService;
import Services.FacebookService;
import Services.LocationService;
import WebHelpers.AlbumImagesResultHandler;
import WebHelpers.AlbumImagesWebRequestHandlerParams;
import WebHelpers.AlbumImagesWebResult;
import WebHelpers.AlbumParams;
import WebHelpers.DownloadImageTask;
import WebHelpers.EventsResultHandler;
import WebHelpers.EventsWebRequestHandlerParams;
import WebHelpers.HttpRequestParameters;
import WebHelpers.ProcessEventsWebResult;
import WebHelpers.WebRequest;
import WebHelpers.WebRequestResultHandler;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

public class GalleryActivity extends SherlockActivity
{
	private LocationService ls;
	private ContainerService cs;
	private FacebookService fs;
	private WebView webview;
	ActionBar bar;
	private HttpResponse response;
	File photo;
	private Uri imageUri;
	private static final int TAKE_PHOTO_CODE = 1;
	private String TAG = "Picup";
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		this.getSherlock().setProgressBarIndeterminate(false);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		cs = ContainerService.getInstance();
		setTheme(R.style.Theme_Sherlock);
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
		// bar.setTitle("Activity Title");

		bar.setIcon(R.drawable.abs__spinner_48_inner_holo);

		ls = LocationService.getInstance();
		cs = ContainerService.getInstance();
		fs = FacebookService.getInstance();

		fs.getInstance().asyncGetFacebookIDOperation = new AsyncGetFacebookIDOperation()
		{
			@Override
			public void GetIdCompleted(String facebookID)
			{
				bar.setIcon(ContainerService.getInstance().FacebookUserImage);
				// isThereAreEventsNearToYou();
			}
		};
		fs.GetFacebookIdAsync();

		bar.setDisplayUseLogoEnabled(false);

		cs.ActionBar = bar;
		cs.ActionBar.setTitle(cs.SelectedEventName);
		cs.ActionBar.setSubtitle(cs.SelectedPlace);

		_gridview = (GridView) findViewById(R.id.gridview);

		_gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				// Toast.makeText(GalleryActivity.this, "" + position,
				// Toast.LENGTH_SHORT).show();
				if (_fetchedAlbums.length - 1 >= position)
				{
					cs.SelectedImageLink = _fetchedAlbums[position].MidQLink;
					cs.UseImageView = false;
					Intent imageDetails = new Intent(GalleryActivity.this, PhotoDetailsActivity.class);
					GalleryActivity.this.startActivity(imageDetails);
				}
				else
				{
					cs.SelectedImageSDPath = _addedCells.get(position - _fetchedAlbums.length).GetImageSdPath();
					cs.UseImageView = true;
					Intent imageDetails = new Intent(GalleryActivity.this, PhotoDetailsActivity.class);
					GalleryActivity.this.startActivity(imageDetails);
				}
			}
		});
		this.setSupportProgressBarIndeterminateVisibility(true);
		this.getSherlock().setProgressBarIndeterminate(true);

		ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("id", cs.SelectedEventID));

		String baseUrl = "http://picaround.azurewebsites.net/Home/GalleryByEventId";
		HttpRequestParameters parameters = new HttpRequestParameters();
		parameters.url = baseUrl;
		parameters.NameValuePairs = NameValuePairs;
		parameters.webRequestHandler = new WebRequestResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnWebRequestCompleted(HttpResponse httpResponse)
			{

				// TODO Auto-generated method stub
				super.OnWebRequestCompleted(httpResponse);
				AlbumImagesWebRequestHandlerParams param = new AlbumImagesWebRequestHandlerParams();
				param.httpResponse = httpResponse;
				response = httpResponse;
				// listViewHandler = new ListViewHandler(httpResponse);
				// listViewHandler.getData();

				param.albumImagesResultHandler = new AlbumImagesResultHandler()
				{
					@Override
					public void HandleResult(ArrayList<AlbumParams> images)
					{
						_fetchedAlbums = new AlbumParams[images.size()];
						images.toArray(_fetchedAlbums);
						_adapter = new ImageAdapter(GalleryActivity.this.getApplicationContext(), _fetchedAlbums);
						_gridview.setAdapter(_adapter);
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
			Object path = message.obj;
			// if (message.arg1 == Activity.RESULT_OK)
			// {
			// Toast.makeText(GalleryActivity.this.getApplication(),
			// "Uploaded OK", Toast.LENGTH_LONG).show();
			// }
			// else
			// {
			// Toast.makeText(GalleryActivity.this.getApplication(),
			// "Upload failed", Toast.LENGTH_LONG).show();
			// }

			if (message.arg2 >= 0)
			{

				GalleryActivity.this.runOnUiThread(new Runnable() {
					public void run()
					{
						if (message.arg2 < 100)
						{
							if (_addedCells.size() > 0)
							{
								GridviewPaImage cell = _addedCells.get(_addedCells.size() - 1);
								ProgressBar bar = cell.GetProgressControl();
								bar.setVisibility(View.VISIBLE);
								bar.setProgress(message.arg2);
							}
						}
						else
						{
							if (_addedCells.size() > 0)
							{
								GridviewPaImage cell = _addedCells.get(_addedCells.size() - 1);
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
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	public void takePhoto()
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		//String fileName = String.format("%d.jpg", System.currentTimeMillis());
		String fileName = String.format("%s.jpg", "PicAroundTemp");
		//photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
		photo = new File(Environment.getExternalStorageDirectory(), fileName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		imageUri = Uri.fromFile(photo);
		startActivityForResult(intent, TAKE_PHOTO_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		try
		{
			switch (requestCode)
			{
				case TAKE_PHOTO_CODE:
					if (resultCode == Activity.RESULT_OK)
					{

						// GalleryActivity.this.getApplication().getContentResolver().notifyChange(selectedImage,
						// null);
						//

						// try
						// {
						// if (selectedImage != null)
						// {
						// Log.i(TAG, "picup -> selectedImage " +
						// selectedImage.toString());
						// }
						// if (cr == null)
						// {
						// Log.i(TAG, "picup -> ContentResolver ERROR");
						// }
						// else
						// {
						// Log.i(TAG, "picup -> ContentResolver " +
						// cr.toString());
						// }
						//

						//
						// if (bitmap == null)
						// {
						// Log.i(TAG, "picup -> bitmap = ERROR ");
						// }
						//
						// // imageView.setImageBitmap(bitmap);
						// //
						// Toast.makeText(GalleryActivity.this.getApplication(),
						// // selectedImage.toString(),
						// // Toast.LENGTH_LONG).show();
						// }
						// catch (Exception e)
						// {
						// Toast.makeText(GalleryActivity.this.getApplication(),
						// "Failed to load", Toast.LENGTH_SHORT).show();
						// Log.i(TAG, "picup -> Failed to load" + e.toString());
						// Log.i(TAG, "picup -> Failed to load" +
						// e.getStackTrace().toString());
						// }

						// Bitmap original = BitmapFactory.decodeStream(new
						// FileInputStream(photo.getAbsolutePath()));
						// FileOutputStream stream = new
						// FileOutputStream(Environment.getExternalStorageDirectory()
						// + "/temp.jpg");
						// /* Write bitmap to file using JPEG and 50% quality
						// hint for JPEG. */
						// original.compress(CompressFormat.JPEG, 50, stream);
						// stream.flush();
						// stream.close();
						// original.recycle();
						// original = null;

						// FileInputStream(Environment.getExternalStorageDirectory()
						// + "/temp.jpg"))));
						// File file = new
						// File(Environment.getExternalStorageDirectory() +
						// "/temp.jpg");
						// file.delete();
						// _adapter.notifyDataSetChanged();
						// uploadButton.setOnClickListener(new OnClickListener()
						// {
						// public void onClick(View v)
						// {
						String imagePath = MediaStore.Images.Media.insertImage(getContentResolver(), imageUri.getPath(), "PicAround" , cs.SelectedEventName);
						Uri u = Uri.parse(imagePath);
						imagePath = getRealPathFromURI(u);
						//Uri selectedImage = imageUri;
						//ContentResolver cr = GalleryActivity.this.getApplication().getContentResolver();
						// Bitmap bitmap = null;
						// bitmap =
						// android.provider.MediaStore.Images.Media.getBitmap(cr,
						// selectedImage);
						//_addedCells.add(_adapter.AddImage(photo.getAbsolutePath()));
						StartUploadService(imagePath);
					}
					break;
				case 123456:
					 Uri selectedImageUri = data.getData();
					 String imageRealPath = getRealPathFromURI(selectedImageUri);
					 StartUploadService(imageRealPath);
					break;
					
			}
		}
		catch (Exception e)
		{
			Log.i(TAG, "picup -> ERROR -> Failed to take photo" + e.toString());
			Log.i(TAG, "picup -> ERROR -> Failed to take photo" + e.getStackTrace().toString());
			// takePhoto(imageView);
		}

	}
	public void StartUploadService(String imagePath)
	{
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
		intent.putExtra("userid", userid);

		intent.putExtra("filePath", imagePath);
		intent.putExtra("heading", Double.toString(cs.Heading));
		
		GalleryActivity.this.getApplication().startService(intent);
		//_gridview.smoothScrollToPosition(_fetchedAlbums.length + _addedCells.size());
		// takePhoto(imageView);
		// }
		// });
	}

	public void openGallery(int req_code){

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);

   }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// if (item.getItemId() == android.R.id.home || item.getItemId() == 0)
		// {
		// return false;
		// }
		// int THEME = item.getItemId();
		// Toast.makeText(this, "\"" + item.getTitle() + "\"",
		// Toast.LENGTH_SHORT).show();
		// Intent result = new Intent(this,AddNewEventFragment.class);
		// result.setClassName("com.android.PubPIc","DialogEventsNearToYou");
		// startActivity(result);
		// return true;
		// ((Menu) item).findItem(R.drawable.icon).setVisible(false);
		// MenuItem switchButton = ((Menu) item).findItem(R.drawable.icon);

		switch (item.getItemId())
		{

			case 0:
				// tabSwap( "EventsFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
				// app icon in action bar clicked; go home
				// Intent intent = new Intent(this, CameraIntent.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				return super.onOptionsItemSelected(item);
				
			case 1:
				takePhoto();
				return super.onOptionsItemSelected(item);
				// takePhoto();
				// tabSwap("EventsFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
			case 2:
				
				openGallery(123456);
				return super.onOptionsItemSelected(item);
				// tabSwap( "PlacesFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
			case 3:
				takePhoto();
				return super.onOptionsItemSelected(item);
			default:
				// tabSwap( "EventsFragment", "NewEventFregment",
				// AddNewEventFragment.class.getName());
				// FragmentTransaction ft =
				// this.getSupportFragmentManager().beginTransaction();
				// Fragment mFragment =
				// this.getSupportFragmentManager().findFragmentByTag("tab2");
				// Fragment mFragment1 =
				// this.getSupportFragmentManager().findFragmentByTag("tab5");
				// ft.hide(mFragment);
				// if (mFragment1 == null)
				// {
				// mFragment = Fragment.instantiate(this,
				// GalleryFragment.class.getName(), null);
				// ft.add(android.R.id.content, mFragment, "tab5");
				// ft.commit();
				// }
				// ft.add(android.R.id.content, mFragment1, "tab5");
				// ft.commit();
				// bar.removeTab(bar.getSelectedTab());
				// bar.addTab(bar.newTab().setIcon(R.drawable.placewhite).setTabListener(new
				// TabListener<PlacesFragment>(this, "tab2",
				// PlacesFragment.class, null)),1,true);

				return super.onOptionsItemSelected(item);

		}
	}

	SubMenu sub;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 3, 0, "Camera").setIcon(R.drawable.camera_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		//menu.add("Upload").setIcon(R.drawable.gallery_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		sub = menu.addSubMenu("Options");
		// cs.subMenu = sub;
		//
		// // Used to put dark icons on light action bar
		// // boolean isLight = SampleList.THEME ==
		// R.style.Theme_Sherlock_Light;
		//
		 sub.add(0, 1, 0,
		 "Take Photo").setIcon(R.drawable.camera_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS |
		 MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		 sub.add(0, 2, 1,
		 "Upload from gallery").setIcon(R.drawable.gallery_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// sub.add("Search").setIcon(true ? R.drawable.ic_search_inverse :
		// R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// sub.add("Refresh").setIcon(true ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Save").setIcon(true ? R.drawable.ic_compose_inverse :
		// R.drawable.ic_compose).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Search").setIcon(true ? R.drawable.ic_search_inverse :
		// R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		// sub.add("Refresh").setIcon(true ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// webview.saveState(outState);
	};

	public class ImageAdapter extends BaseAdapter
	{
		private Context mContext;
		private AlbumParams[] _albums;
		private ArrayList<GridviewPaImage> _images;
		private int _size;
		RotateAnimation _anim;
		public ImageAdapter(Context c, AlbumParams[] albums) {
			mContext = c;
			_albums = albums;
			_size = albums.length;
			_anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			_anim.setInterpolator(new LinearInterpolator());
			_anim.setRepeatCount(Animation.INFINITE);
			_anim.setDuration(2000);
			
			_images = new ArrayList<GridviewPaImage>();
//			for (AlbumParams item : _albums)
//			{
//				GridviewPaImage gvpaImage = new GridviewPaImage(mContext);
//				gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));
//
//				ImageView imageView = gvpaImage.GetImageControl();
//				// imageView = new ImageView(mContext);
//				// imageView.setBackgroundColor(R.layout.stroke);
//				// imageView.setLayoutParams(new GridView.LayoutParams(100,
//				// 100));
//				gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));
//				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//				imageView.setPadding(2, 2, 2, 2);
//
//				RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//				anim.setInterpolator(new LinearInterpolator());
//				anim.setRepeatCount(Animation.INFINITE);
//				anim.setDuration(2000);
//
//				// Start animating the image
//
//				imageView.startAnimation(anim);
//
//				new DownloadImageTask(imageView).execute(item.ThumbLink);
//				_images.add(gvpaImage);
//			}
		}

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

			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			// Calculate inSampleSize
			options.inSampleSize = 16; //calculateInSampleSize(options, reqWidth, reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;

			return BitmapFactory.decodeFile(path, options);
		}

		public GridviewPaImage AddImage(String imagePath)
		{
			_size++;
			// ByteArrayOutputStream blob = new ByteArrayOutputStream();
			// bm.compress(CompressFormat.JPEG, 50, blob);
			// byte[] bitmapdata = blob.toByteArray();
			// Bitmap compressedBitmap =
			// BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

			Bitmap scaledBitmap = decodeSampledBitmapFromResource(imagePath, 25, 25);

			GridviewPaImage gvpaImage = new GridviewPaImage(mContext,imagePath);
			ImageView imageView = gvpaImage.GetImageControl();
			// imageView = new ImageView(mContext);
			// imageView.setBackgroundColor(R.layout.stroke);
			gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));
			// setLayoutParams(ViewGroup.LayoutParams) sets the height
			// and width for the View—this ensures that, no matter the size of
			// the drawable, each image is resized and cropped to fit in these
			// dimensions, as appropriate.
			// imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(2, 2, 2, 2);
			imageView.setImageBitmap(scaledBitmap);

			_images.add(gvpaImage);

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
			// if (convertView == null)
			// { // if it's not recycled, initialize some attributes
			//
			// GridviewPaImage gvpaImage = new GridviewPaImage(mContext);
			// imageView = gvpaImage.GetImageControl();
			// //imageView = new ImageView(mContext);
			// //imageView.setBackgroundColor(R.layout.stroke);
			// gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));
			// //setLayoutParams(ViewGroup.LayoutParams) sets the height
			// //and width for the View—this ensures that, no matter the size of
			// //the drawable, each image is resized and cropped to fit in these
			// dimensions, as appropriate.
			// //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
			// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// imageView.setPadding(2, 2, 2, 2);
			//
			// RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
			// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
			// 0.5f);
			// anim.setInterpolator(new LinearInterpolator());
			// anim.setRepeatCount(Animation.INFINITE);
			// anim.setDuration(2000);
			//
			// // Start animating the image
			//
			// imageView.startAnimation(anim);
			//
			// // Later.. stop the animation
			// //imageView.setAnimation(null);
			//
			// new
			// DownloadImageTask(imageView).execute(_albums[position].ThumbLink);
			//
			// _images[position] = gvpaImage;
			// control = gvpaImage;
			// }
			// else
			// {

			if (position >= _images.size() || _images.get(position) == null)
			{
				GridviewPaImage gvpaImage = new GridviewPaImage(mContext, "null");
				gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));

				imageView = gvpaImage.GetImageControl();
				// imageView = new ImageView(mContext);
				// imageView.setBackgroundColor(R.layout.stroke);
				// imageView.setLayoutParams(new GridView.LayoutParams(100,
				// 100));
				gvpaImage.setLayoutParams(new GridView.LayoutParams(100, 100));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(2, 2, 2, 2);

				

				// Start animating the image

				imageView.startAnimation(_anim);

				new DownloadImageTask(imageView).execute(_albums[position].ThumbLink);
				_images.add(position, gvpaImage);

			}
			control = _images.get(position);

			return control;
		}

	}

}