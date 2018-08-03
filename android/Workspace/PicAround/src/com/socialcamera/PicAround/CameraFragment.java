package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.io.File;
//import Services.ContainerService;
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Messenger;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.android.PicAround.R;
//
//public class CameraFragment extends SherlockFragment
//{
//
//	private ImageView imageView;
//	private Button cameraButton;
//	private ImageView uploadButton;
//	private ContainerService cs;
//	private static final int TAKE_PHOTO_CODE = 1;
//	private String TAG = "Picup";
//	String lats = null;
//	String lons = null;
//	String alts = null;
//	String _selectedPlace = null;
//	String _selectedEventName = null;
//	String _selectedEventID = null;
//	String userid = null;
//	private Uri imageUri;
//	File photo;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		cs = ContainerService.getInstance();
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState)
//	{
//		super.onActivityCreated(savedInstanceState);
//
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		View view = inflater.inflate(R.layout.cameraintentview, container, false);
//
//		imageView = (ImageView) view.findViewById(R.id.cameraintentview);
//
//		cameraButton = (Button) view.findViewById(R.id.cameraIntentButton);
//
//		cameraButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				takePhoto(imageView);
//			}
//		});
//
//		uploadButton = (ImageView) view.findViewById(R.id.uploadButton);
//		Intent intent = new Intent();
//
//		userid = intent.getStringExtra("userid");
//		lats = intent.getStringExtra("lat");
//		lons = intent.getStringExtra("lon");
//		alts = intent.getStringExtra("alt");
//		_selectedPlace = intent.getStringExtra("place");
//		_selectedEventName = intent.getStringExtra("eventLabel");
//
//		if (userid == null)
//		{
//			userid = "0";
//		}
//
//		takePhoto(imageView);
//		return view;
//	}
//
//	public void setText(String item)
//	{
//
//	}
//
//	public void takePhoto(View view)
//	{
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		String fileName = String.format("%d.jpg", System.currentTimeMillis());
//		photo = new File(Environment.getExternalStorageDirectory(), fileName);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//		imageUri = Uri.fromFile(photo);
//		startActivityForResult(intent, TAKE_PHOTO_CODE);
//	}
//
//	private Handler handler = new Handler() {
//		public void handleMessage(Message message)
//		{
//			Object path = message.obj;
//			if (message.arg1 == Activity.RESULT_OK)
//			{
//				Toast.makeText(getActivity(), "Uploaded OK", Toast.LENGTH_LONG).show();
//			}
//			else
//			{
//				Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
//			}
//
//		};
//	};
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		try
//		{
//			switch (requestCode)
//			{
//				case TAKE_PHOTO_CODE:
//					if (resultCode == Activity.RESULT_OK)
//					{
//
//						Uri selectedImage = imageUri;
//						getActivity().getContentResolver().notifyChange(selectedImage, null);
//
//						ContentResolver cr = getActivity().getContentResolver();
//						Bitmap bitmap;
//						try
//						{
//							if (selectedImage != null)
//							{
//								Log.i(TAG, "picup -> selectedImage " + selectedImage.toString());
//							}
//							if (cr == null)
//							{
//								Log.i(TAG, "picup -> ContentResolver ERROR");
//							}
//							else
//							{
//								Log.i(TAG, "picup -> ContentResolver " + cr.toString());
//							}
//
//							bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//
//							if (bitmap == null)
//							{
//								Log.i(TAG, "picup -> bitmap = ERROR ");
//							}
//
//							//imageView.setImageBitmap(bitmap);
//							Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
//						}
//						catch (Exception e)
//						{
//							Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
//							Log.i(TAG, "picup -> Failed to load" + e.toString());
//							Log.i(TAG, "picup -> Failed to load" + e.getStackTrace().toString());
//						}
//						// uploadButton.setOnClickListener(new OnClickListener()
//						// {
//						// public void onClick(View v)
//						// {
//						Intent intent = new Intent();
//						intent.setClass(getActivity().getApplicationContext(), UploadService.class);
//						// Create a new Messenger for the communication back
//						if (cs != null && cs.Location != null)
//						{
//							lons = Double.toString(cs.Location.getLongitude());
//							lats = Double.toString(cs.Location.getLatitude());
//							alts = Double.toString(cs.Location.getAltitude());
//						}
//
//						if (lats == null)
//						{
//							lats = "404";
//						}
//						if (lons == null)
//						{
//							lons = "404";
//						}
//						if (alts == null)
//						{
//							alts = "404";
//						}
//						Messenger messenger = new Messenger(handler);
//
//						intent.putExtra("lat", lats);
//						intent.putExtra("MESSENGER", messenger);
//						intent.putExtra("lon", lons);
//						intent.putExtra("alt", alts);
//						_selectedPlace = cs.SelectedPlace;
//						_selectedEventName = cs.SelectedEventName;
//						_selectedEventID = cs.SelectedEventID;
//						if (_selectedPlace == null || _selectedPlace == "")
//						{
//							_selectedPlace = "A place was not selected";
//						}
//						if (_selectedEventName == null || _selectedEventName == "")
//						{
//							_selectedEventName = "An event Was not selected";
//						}
//
//						if (_selectedEventID == null || _selectedEventID == "")
//						{
//							_selectedEventID = "An event Was not selected";
//						}
//						intent.putExtra("place", _selectedPlace);
//						intent.putExtra("SelectedEventName", _selectedEventName);
//
//						intent.putExtra("SelectedEventID", _selectedEventID);
//
//						userid = cs.FacebookId;
//						intent.putExtra("userid", userid);
//
//						intent.putExtra("filePath", photo.getAbsolutePath());
//						getActivity().startService(intent);
//						takePhoto(imageView);
//						// }
//						// });
//					}
//			}
//		}
//		catch (Exception e)
//		{
//			Log.i(TAG, "picup -> ERROR -> Failed to take photo" + e.toString());
//			Log.i(TAG, "picup -> ERROR -> Failed to take photo" + e.getStackTrace().toString());
//			takePhoto(imageView);
//		}
//
//	}
//}