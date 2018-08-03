package WebHelpers;

public class AlbumParams
{
	public String UserId;
	public String Link;
	public String MidQLink;
	public String ThumbLink;
	public String EventName;
	public String LocationName;
	public String Description;
		
	public AlbumParams(String userId,String link,String midQLink,String thumbLink, String eventName,String locationName,String description)
	{
		UserId = userId;
		Link = "http://picaround.blob.core.windows.net/" + link;
		MidQLink = "http://picaround.blob.core.windows.net/" + midQLink;
		ThumbLink = "http://picaround.blob.core.windows.net/" + thumbLink;
		EventName = eventName;
		LocationName = locationName;
		Description = description;
	}
}
