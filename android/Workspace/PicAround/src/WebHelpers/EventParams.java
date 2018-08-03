package WebHelpers;

import java.util.Date;

public class EventParams
{
	public String StartDate;
	public String EndDate;
	public String EventName;
	public String LocationId;

	public String EventId;
	public String LocationName;
	public String ThumbLink;
	public Date DateTimeStart;
	public Date DateTimeEnd;

	public EventParams(String eventName, String eventId, String thumbLink, String locationId, String endDate, String locationName, String startDate) {
		StartDate = startDate;
		EndDate = endDate;
		EventName = eventName;
		LocationName = locationName;
		LocationId = locationId;
		if (thumbLink == null || thumbLink=="null" || thumbLink == "")
		{
			ThumbLink = null;
		}
		else
		{
			ThumbLink = "http://picaround.blob.core.windows.net/" + thumbLink;
		}
		EventId = eventId;
	}

}
