package Services;

import java.util.ArrayList;

import com.facebook.model.GraphPlace;
import com.google.android.gms.plus.PlusClient;
import com.socialcamera.PicAround.GridviewPaImage;

import Interfaces.IAsyncLocationProviderOperation;
import WebHelpers.AlbumParams;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.ImageView;

public class ContainerService
{
	private static ContainerService instance;
	public String FacebookId;
	public Location Location;
	public String[] places;
	public String[] events;
	public com.socialcamera.PicAround.Main Main;
	public String SelectedPlace;
	public String SelectedEventName;
	public Drawable FacebookUserImage;
	//private LocationManager locationManager;
	public String SelectedEventID;
	public String SelectedImageLink;
	public boolean UseImageView;
	public float Heading;
	public String SelectedImageSDPath;
	public String SelectedImageEventName;
	public String SelectedImageUserId;
	public boolean UserDisabledLocation = false;
	public boolean UserAddedNewEvent = false;
	public AlbumParams[] FetchedAlbums;
	public ArrayList<GridviewPaImage> AddedCells;
	public int Position;
	public String UserName;
	public String SelectedFBPlace;
	public ImageView ProfileImageView;
	public boolean GooglepluseLogin;
	public PlusClient PlusClient;
	public boolean MainActivity;
	public ArrayList<PlacesParams> GooglePlaces;
	public Resources Resources;
	
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
