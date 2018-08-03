package Services;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.PubPIc.LocationFinder;

import Interfaces.IAsyncLocationProviderOperation;
import Interfaces.ILocationResult;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.SubMenu;

public class ContainerService
{
	private static ContainerService instance;
	public String FacebookId;
	public Location Location;
	public SherlockFragmentActivity FragmentActivity;
	public String[] places;
	public String[] events;
	public com.android.PubPIc.Main Main;
	public String SelectedPlace;
	public com.android.PubPIc.TabNavigation TabNavigation;
	public ActionBar ActionBar;
	public String SelectedEventName;
	public Drawable FacebookUserImage;
	//private LocationManager locationManager;
	public String SelectedEventID;
	public boolean AlowAllEvents = false;
	public SubMenu subMenu;
	public String SelectedImageLink;
	public boolean UseImageView;
	public float Heading;
	public String SelectedImageSDPath;
	
	public static ContainerService getInstance()
	{
		if (instance == null)
		{
			// Create the instance
			instance = new ContainerService();
		}
		return instance;
	}

	

	private ContainerService()
	{
		// Constructor hidden because this is a singleton
	}

	public void LoadService(Activity activity)
	{
		
	}
	public void RegisterForLocationsChange(IAsyncLocationProviderOperation locationFoundDelegate)
	{
		
	}
	
}
