package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.io.File;
//
//import com.android.PicAround.R;
//
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
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//public class CameraIntent extends Activity
//{
//	private ImageView imageView;
//	private Button cameraButton;
//	private ImageView uploadButton;
//	private String TAG = "Picup";
//	private static final int TAKE_PHOTO_CODE = 1;
//
//	public void onCreate(Bundle v)
//	{
//		super.onCreate(v);
//		// myWebView.loadUrl("http://picaround.azurewebsites.net/Home/Images");
//		setContentView(R.layout.cameraintentview);
//		imageView = (ImageView) findViewById(R.id.cameraintentview);
//
//		cameraButton = (Button) findViewById(R.id.cameraIntentButton);
//
//		cameraButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v)
//			{
//				takePhoto(imageView);
//			}
//		});
//
//		uploadButton = (ImageView) findViewById(R.id.uploadButton);
//		Intent intent = getIntent();
//
//		userid = intent.getStringExtra("userid");
//		lats = intent.getStringExtra("lat");
//		lons = intent.getStringExtra("lon");
//		alts = intent.getStringExtra("alt");
//		selectedPlace = intent.getStringExtra("place");
//		selectedPlace = intent.getStringExtra("eventLabel");
//
//		if (userid == null)
//		{
//			userid = "0";
//		}
//	}
//
//	String lats = null;
//	String lons = null;
//	String alts = null;
//	String selectedPlace = null;
//	String userid = null;
//	private Uri imageUri;
//	File photo;
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
//			if (message.arg1 == RESULT_OK)
//			{
//				Toast.makeText(CameraIntent.this, "Uploaded OK", Toast.LENGTH_LONG).show();
//			}
//			else
//			{
//				Toast.makeText(CameraIntent.this, "Upload failed", Toast.LENGTH_LONG).show();
//			}
//
//		};
//	};
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode)
//		{
//			case TAKE_PHOTO_CODE:
//				if (resultCode == Activity.RESULT_OK)
//				{
//					Uri selectedImage = imageUri;
//					getContentResolver().notifyChange(selectedImage, null);
//
//					ContentResolver cr = getContentResolver();
//					Bitmap bitmap;
//					try
//					{
//						bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//
//						imageView.setImageBitmap(bitmap);
//						Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
//					}
//					catch (Exception e)
//					{
//						Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
//						Log.i(TAG ,"Camera" + e.toString());
//					}
//					// uploadButton.setOnClickListener(new OnClickListener() {
//					// public void onClick(View v)
//					// {
//					Intent intent = new Intent();
//					intent.setClass(getApplicationContext(), UploadService.class);
//					// Create a new Messenger for the communication back
//
//					if (lats == null)
//					{
//						lats = "404";
//					}
//					if (lons == null)
//					{
//						lons = "404";
//					}
//					if (alts == null)
//					{
//						alts = "404";
//					}
//					Messenger messenger = new Messenger(handler);
//
//					intent.putExtra("lat", lats);
//					intent.putExtra("MESSENGER", messenger);
//					intent.putExtra("lon", lons);
//					intent.putExtra("alt", alts);
//					if (selectedPlace == null || selectedPlace == "")
//					{
//						selectedPlace = "NOT SET";
//					}
//					intent.putExtra("place", selectedPlace);
//					intent.putExtra("eventLabel", selectedPlace);
//
//					intent.putExtra("userid", userid);
//
//					intent.putExtra("filePath", photo.getAbsolutePath());
//					startService(intent);
//					takePhoto(imageView);
//					// }
//					// });
//				}
//		}
//	}
//}
