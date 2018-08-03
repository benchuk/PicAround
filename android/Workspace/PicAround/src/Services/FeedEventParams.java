package Services;

public class FeedEventParams
{
	public String UserId;
	public String Link;
	public String PlaceName;
	public String EventName;

	public String Description;
	public String UserIdImage;

	public FeedEventParams(String userId, String link, String placeName, String eventName, String description) {
		UserId = userId;
		if (userId.length() < 16)
		{
			UserIdImage = "http://graph.facebook.com/" + userId + "/picture?type=large";
		}
		else
		{
			UserIdImage = "https://www.google.com/s2/photos/profile/" + userId;//110081920118355073505
		}
		Link = "http://picaround.blob.core.windows.net/" + link;
		PlaceName = placeName;
		EventName = eventName;
		Description = description;

	}

	public FeedEventParams() {
	}

}
