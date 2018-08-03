package Services;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacesParams
{
	public String Id;
	public String Name;
	public String Type;
	public String Longtitude;
	public String Latitude;
	public String Country;
	public String City;
	public String PicUrl;

	
	public PlacesParams(String id, String name, String type, String longtitude, String latitude, String country, String city, String picUrl) {
		Id = id;
		Name = name;
		Type = type;
		Longtitude = longtitude;
		Latitude = latitude;
		Country = country;
		City = city;
		PicUrl = picUrl;
	}




}
