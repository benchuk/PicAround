package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.List;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.res.Configuration;
//import android.hardware.Camera;
//import android.hardware.Camera.PreviewCallback;
//import android.hardware.Camera.Size;
//import android.location.Location;
//import android.os.Build;
//import android.util.Log;
//import android.view.Display;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//class Preview extends SurfaceView implements SurfaceHolder.Callback
//{
//	private static final String TAG = "Picup";
//	public SurfaceHolder mHolder;
//	public Camera camera;
//	private Context _context;
//
//	Preview(Context context) {
//		super(context);
//		_context = context;
//		// Install a SurfaceHolder.Callback so we get notified when the
//		// underlying surface is created and destroyed.
//		mHolder = getHolder();
//		mHolder.addCallback(this);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//	}
//
//	public void surfaceCreated(SurfaceHolder holder)
//	{
//		// The Surface has been created, acquire the camera and tell it where
//		// to draw.
//		if (camera == null)
//		{
//			camera = getCameraInstance();
//		}
//		try
//		{
//			if (camera!=null)
//			{
//			camera.setPreviewDisplay(holder);
//		
//
//			// camera.setPreviewCallback(new PreviewCallback() {
//			//
//			// public void onPreviewFrame(byte[] data, Camera arg1)
//			// {
//			// // FileOutputStream outStream = null;
//			// // try
//			// // {
//			// // outStream = new
//			// FileOutputStream(String.format("/sdcard/%d.jpg",
//			// System.currentTimeMillis()));
//			// // outStream.write(data);
//			// // outStream.close();
//			// // Log.d(TAG, "onPreviewFrame - wrote bytes: " + data.length);
//			// // }
//			// // catch (FileNotFoundException e)
//			// // {
//			// // e.printStackTrace();
//			// // }
//			// // catch (IOException e)
//			// // {
//			// // e.printStackTrace();
//			// // }
//			// // finally
//			// // {
//			// // }
//			// Preview.this.invalidate();
//			// }
//			// });
//
//			// camera.startPreview();
//			}
//			else
//			{
//				ShowDialog("FAIL TO CONNECT TO CAMERA");
//			}
//		}
//		catch (IOException e)
//		{
//			ShowDialog(e.toString());
////			 camera.release();
////			 camera = null;
//		}
//	}
//
//	public void Invalidate()
//	{
//		Preview.this.invalidate();
//	}
//
//	public void surfaceDestroyed(SurfaceHolder holder)
//	{
//		// Surface will be destroyed when we return, so stop the preview.
//		// Because the CameraDevice object is not a shared resource, it's very
//		// important to release it when the activity is paused.
//		if (camera != null)
//		{
//			camera.stopPreview();
//			// camera.unlock();
//			// camera.release();
//			// camera = null;
//		}
//	}
//
//	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
//	{
//
//		// Now that the size is known, set up the camera parameters and begin
//		// the preview.
//		if (camera == null)
//		{
//			camera = getCameraInstance();
//		}
//		if (camera == null)
//		{
//			return;
//		}
//		// If your preview can change or rotate, take care of those events here.
//		// Make sure to stop the preview before resizing or reformatting it.
//
//		if (holder == null)
//		{
//			// preview surface does not exist
//			return;
//		}
//
//		// stop preview before making changes
//		try
//		{
//			camera.stopPreview();
//		}
//		catch (Exception e)
//		{
//			// ignore: tried to stop a non-existent preview
//		}
//
//		// set preview size and make any resize, rotate or
//		// reformatting changes here
//
//		// CameraApi is a wrapper to check for backwards compatibility
//
//		// parameters.set("orientation", "portrait");
//		// int rotation = 0;
//		// if (mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
//		// CameraInfo info = CameraHolder.instance().getCameraInfo()[mCameraId];
//		// if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
//		// rotation = (info.orientation - mOrientation + 360) % 360;
//		// } else { // back-facing camera
//		// rotation = (info.orientation + mOrientation) % 360;
//		// }
//		// }
//
//		// parameters.removeGpsData();
//		// parameters.setGpsTimestamp(System.currentTimeMillis() / 1000);
//		// // camera.setDisplayOrientation(90);
//		//parameters.set("rotation", 90);
//		//parameters.setRotation(90);
//		
//		
//		// //parameters.setRotation(90);
//		// if (_location != null)
//		// {
//		// double lat = _location.getLatitude();
//		// double lon = _location.getLongitude();
//		// boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);
//		//
//		// if (hasLatLon)
//		// {
//		// parameters.setGpsLatitude(lat);
//		// parameters.setGpsLongitude(lon);
//		// //parameters.setGpsProcessingMethod(_location.getProvider().toUpperCase());
//		// if (_location.hasAltitude())
//		// {
//		// parameters.setGpsAltitude(_location.getAltitude());
//		// }
//		// else
//		// {
//		// // for NETWORK_PROVIDER location provider, we may have
//		// // no altitude information, but the driver needs it, so
//		// // we fake one.
//		// parameters.setGpsAltitude(0);
//		// }
//		// if (_location.getTime() != 0)
//		// {
//		// // Location.getTime() is UTC in milliseconds.
//		// // gps-timestamp is UTC in seconds.
//		// long utcTimeSeconds = _location.getTime() / 1000;
//		// parameters.setGpsTimestamp(utcTimeSeconds);
//		// }
//		// }
//		// }
//		try
//		{
//			Camera.Parameters parameters = camera.getParameters();
//			
//			 if (Integer.parseInt(Build.VERSION.SDK) >= 8)
//			        setDisplayOrientation(camera, 90);
//			    else
//			    {
//			        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//			        {
//			           parameters.set("orientation", "portrait");
//			           parameters.set("rotation", 90);
//			        }
//			        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//			        {
//			        	parameters.set("orientation", "landscape");
//			        	parameters.set("rotation", 90);
//			        }
//			    }   
//			 
//			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//			int width = w;
//			int height = h;
//			// for(Camera.Size p : previewSizes)
//			// {
//			// height = p.height;
//			// width = p.width;
//			//
//			// }
//
//			// height = previewSizes.get(0).height;
//			// width = previewSizes.get(0).width;
//
//			Size optimalSize = getOptimalPreviewSize(previewSizes, w, h);
//			Log.i(TAG, "Surface size is " + w + "w " + h + "h");
//			Log.i(TAG, "Optimal size is " + optimalSize.width + "w " + optimalSize.height + "h");
//
//
////	        Display display = ((WindowManager)_context.getSystemService("window")).getDefaultDisplay();
////		
////
////	        if(display.getOrientation() == Surface.ROTATION_0)
////	        {
////	            parameters.setPreviewSize(height, width);                           
////	            Toast.makeText(this.getContext(),
////	                    "surfaceChanged: 0", Toast.LENGTH_SHORT).show();
////	            parameters.setRotation(Surface.ROTATION_0);
////	        }
////
////	        if(display.getOrientation() == Surface.ROTATION_90)
////	        {
////	            parameters.setPreviewSize(width, height);
////	            Toast.makeText(this.getContext(),
////	                    "surfaceChanged: 90", Toast.LENGTH_SHORT).show();
////	            parameters.setRotation(Surface.ROTATION_90);
////	        }
////
////	        if(display.getOrientation() == Surface.ROTATION_180)
////	        {
////	            parameters.setPreviewSize(height, width); 
////	            Toast.makeText(this.getContext(),
////	                    "surfaceChanged: 180", Toast.LENGTH_SHORT).show();
////	            parameters.setRotation(Surface.ROTATION_180);
////	        }
////
////	        if(display.getOrientation() == Surface.ROTATION_270)
////	        {
////	            parameters.setPreviewSize(width, height);
////	            Toast.makeText(this.getContext(),
////	                    "surfaceChanged: 270", Toast.LENGTH_SHORT).show();
////	            parameters.setRotation(Surface.ROTATION_270);
////	        }
//	        
//			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
//
//			camera.setPreviewDisplay(holder);
//			camera.setParameters(parameters);
//			camera.startPreview();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.i(TAG, "Picup surfaceChanged" +  e.toString());
//			Log.i(TAG, "picup : surfaceChanged" + e.getStackTrace().toString());
//		}
//		// if (CameraApi.isSetRotationSupported())
//		// {
//		// CameraApi.setRotation(parameters, 90);
//		// }
//
//	}
//
//	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h)
//	{
//		final double ASPECT_TOLERANCE = 0.2;
//		double targetRatio = (double) w / h;
//		if (sizes == null) return null;
//
//		Size optimalSize = null;
//		double minDiff = Double.MAX_VALUE;
//
//		int targetHeight = h;
//
//		// Try to find an size match aspect ratio and size
//		for (Size size : sizes)
//		{
//			Log.i(TAG, "Checking size " + size.width + "w " + size.height + "h");
//			double ratio = (double) size.width / size.height;
//			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//			if (Math.abs(size.height - targetHeight) < minDiff)
//			{
//				optimalSize = size;
//				minDiff = Math.abs(size.height - targetHeight);
//			}
//		}
//
//		// Cannot find the one match the aspect ratio, ignore the
//		// requirement
//		if (optimalSize == null)
//		{
//			minDiff = Double.MAX_VALUE;
//			for (Size size : sizes)
//			{
//				if (Math.abs(size.height - targetHeight) < minDiff)
//				{
//					optimalSize = size;
//					minDiff = Math.abs(size.height - targetHeight);
//				}
//			}
//		}
//		return optimalSize;
//	}
//
//	protected void setDisplayOrientation(Camera camera, int angle)
//	{
//		Method downPolymorphic;
//		try
//		{
//			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]
//			{
//				int.class
//			});
//			if (downPolymorphic != null) downPolymorphic.invoke(camera, new Object[]
//			{
//				angle
//			});
//		}
//		catch (Exception e1)
//		{
//			Log.i(TAG, "Picup " + e1.toString());
//		}
//	}
//
//	Location _location;
//
//	public void SetLocation(Location loc)
//	{
//
//		_location = loc;
//	}
//
//	// @Override
//	// public void draw(Canvas canvas)
//	// {
//	// super.draw(canvas);
//	// Paint p = new Paint(Color.RED);
//	// Log.d(TAG, "draw");
//	// canvas.drawText("PREVIEW", canvas.getWidth() / 2, canvas.getHeight() / 2,
//	// p);
//	// }
//
//	/** A safe way to get an instance of the Camera object. */
//	public static Camera getCameraInstance()
//	{
//		Camera c = null;
//		try
//		{
//			c = Camera.open(); // attempt to get a Camera instance
//		}
//		catch (Exception e)
//		{
//			// Camera is not available (in use or does not exist)
//
//		}
//		return c; // returns null if camera is unavailable
//	}
//
//	public void ReconnectToCamera() throws IOException
//	{
//		if (camera != null)
//		{	
//			//camera.stopPreview();
//			camera.reconnect();
//			camera.lock();
//		}
//		else
//		{
//			camera = getCameraInstance();
//			if (camera!=null)
//			{
//				camera.lock();
//			}
//		}
//		if (camera == null)
//		{
//			camera = getCameraInstance();
//			if (camera!=null)
//			{
//				camera.lock();
//			}
//			if (camera == null)
//			{
//				ShowDialog("Cannot open camera");
//			}
//		}
//	}
//
//	public void ShowDialog(String text)
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
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
//}