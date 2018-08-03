package Services;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorService
{
	private static SensorService instance;
	private static SensorManager mySensorManager;
	private Activity _activity;
	private boolean sersorrunning;
	private float _heading;
	
	public float GetHeading()
	{
		return _heading;
	}
	public Activity getApplicationContext()
	{
		return _activity;
	}

	public static SensorService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new SensorService();
		}
		return instance;
	}

	public void LoadService(Activity activity)
	{
		_activity = activity;
		_cs = ContainerService.getInstance();
		mySensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (mySensors.size() > 0)
		{
			mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			sersorrunning = true;
	

		}
		else
		{
	
			sersorrunning = false;
		
		}
	}

	private SensorEventListener mySensorEventListener = new SensorEventListener()
	{

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			// TODO Auto-generated method stub
			_heading = (float) event.values[0];
			_cs.Heading = _heading;
		}

	};
	ContainerService _cs;
	private SensorService() {
		
	}

}
